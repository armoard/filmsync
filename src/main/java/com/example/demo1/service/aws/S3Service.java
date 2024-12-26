package com.example.demo1.service.aws;

import com.example.demo1.entity.UserProfile;
import com.example.demo1.exceptions.Profile.InvalidFileException;
import com.example.demo1.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.InputStream;
import java.net.URI;

@Service
public class S3Service {
    private final SecurityService securityService;
    private final S3Client s3Client;
    @Value("${custom.profile.default-url}")
    private String defaultProfileUrl;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    public S3Service(SecurityService securityService, S3Client s3Client) {
        this.securityService = securityService;
        this.s3Client = s3Client;
    }

    public String uploadProfile(MultipartFile file, String key) {
        validateFile(file);
        UserProfile profile = securityService.getAuthenticatedUserProfile();
        String currentProfileUrl = profile.getProfilePictureUrl();

        // delete current profile picture from bucket
        if (currentProfileUrl != null && !currentProfileUrl.equals(defaultProfileUrl)) {
            try {
                URI uri = new URI(currentProfileUrl);
                String currentKey = uri.getPath().substring(1);
                s3Client.deleteObject(DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(currentKey)
                        .build());
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete the existing profile picture: " + e.getMessage(), e);
            }
        }
        try (InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build(),
                    RequestBody.fromInputStream(inputStream, file.getSize()));

            return getProfileUrl(key);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to S3: " + e.getMessage(), e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileException("File is empty.");
        }

        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.equals("image/jpeg") &&
                        !contentType.equals("image/png") &&
                        !contentType.equals("image/gif") &&
                        !contentType.equals("image/webp"))) {
            throw new InvalidFileException("Invalid file type. Only JPEG, PNG, GIF, and WEBP are allowed.");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new InvalidFileException("File size exceeds the maximum limit of 5MB.");
        }
    }

    private String getProfileUrl(String key) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);
    }


}