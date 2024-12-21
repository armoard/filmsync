package com.example.demo1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.awspring.cloud.autoconfigure.s3.S3AutoConfiguration;

@SpringBootApplication(exclude = {S3AutoConfiguration.class})
public class Demo1Application {
	public static void main(String[] args) {
		SpringApplication.run(Demo1Application.class, args);
	}
}
