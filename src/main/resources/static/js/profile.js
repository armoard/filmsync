
function previewProfilePicture() {
    const fileInput = document.getElementById('file-input');
    const profilePic = document.getElementById('profile-pic');
    const confirmButton = document.getElementById('confirm-button');
    const uploadMessage = document.getElementById('upload-message');


    if (fileInput.files.length === 0) {
        uploadMessage.textContent = "No file selected.";
        uploadMessage.className = "message error";
        return;
    }

    const file = fileInput.files[0];

    const reader = new FileReader();
    reader.onload = function (e) {
        profilePic.src = e.target.result;
    };
    reader.readAsDataURL(file);

    confirmButton.classList.remove('hidden');
}

function uploadProfilePicture() {
    const fileInput = document.getElementById('file-input');
    const confirmButton = document.getElementById('confirm-button');
    const uploadMessage = document.getElementById('upload-message');

    const file = fileInput.files[0];

    if (!file) {
        uploadMessage.textContent = "No file selected.";
        uploadMessage.className = "message error";
        return;
    }

    const formData = new FormData();
    formData.append("file", file);


    fetch('/api/user/picture', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.url) {

            uploadMessage.textContent = "Profile picture updated successfully!";
            uploadMessage.className = "message success";
            confirmButton.classList.add('hidden');
        } else {
            throw new Error(data.message || "Failed to upload picture.");
        }
    })
    .catch(error => {
        console.error("Error uploading picture:", error);
        uploadMessage.textContent = error.message;
        uploadMessage.className = "message error";
    });
}

function enableEdit() {
    const usernameDisplay = document.getElementById('username-display');
    const usernameInput = document.getElementById('username-input');
    const saveButton = document.getElementById('save-button');

    if (!usernameInput.classList.contains('hidden')) {
        usernameDisplay.classList.remove('hidden');
        usernameInput.classList.add('hidden');
        saveButton.classList.add('hidden');
    } else {
        usernameInput.value = usernameDisplay.textContent.trim();
        usernameDisplay.classList.add('hidden');
        usernameInput.classList.remove('hidden');
        saveButton.classList.remove('hidden');
    }
}

function saveUsername() {
    const usernameInput = document.getElementById('username-input');
    const usernameDisplay = document.getElementById('username-display');
    const messageContainer = document.getElementById('username-message');
    const newUsername = usernameInput.value;

    fetch(`/api/user/edit/username/${newUsername.trim()}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        }
    })
    .then(response => {
        if (response.ok) {
            messageContainer.textContent = "Username updated successfully!";
            messageContainer.classList.remove('error');
            messageContainer.classList.add('success');

            usernameDisplay.textContent = newUsername;
            usernameDisplay.classList.remove('hidden');
            usernameInput.classList.add('hidden');
            document.getElementById('save-button').classList.add('hidden');
        } else {
            return response.json().then(data => {
                throw new Error(data.message || 'Failed to update username.');
            });
        }
    })
    .catch(error => {
        messageContainer.textContent = error.message;
        messageContainer.classList.remove('success');
        messageContainer.classList.add('error');
        console.error("Error while updating username:", error);
    });
}

function enableAboutEdit() {
    const display = document.getElementById('about-me-display');
    const input = document.getElementById('about-me-input');
    const saveButton = document.getElementById('about-save-button');
    const editIcon = document.getElementById('edit-icon');

    if (!input.classList.contains('hidden')) {
        display.classList.remove('hidden');
        input.classList.add('hidden');
        saveButton.classList.add('hidden');
        editIcon.classList.remove('hidden');
    } else {
        input.value = display.textContent.trim();
        display.classList.add('hidden');
        input.classList.remove('hidden');
        saveButton.classList.remove('hidden');
        editIcon.classList.remove('hidden');
    }
}

function saveAboutMe() {
    const input = document.getElementById('about-me-input');
    const display = document.getElementById('about-me-display');
    const messageContainer = document.getElementById('about-me-message');
    const updatedAbout = input.value.trim();

    const username = document.getElementById('username-display').textContent.trim();

    const requestBody = { aboutme: updatedAbout, username: username };
    console.log("Data sent to the server:", requestBody);

    fetch('/api/user/edit/about-me', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestBody),
    })
    .then(response => {
        if (response.ok) {
            messageContainer.textContent = "About me updated successfully!";
            messageContainer.classList.remove('error');
            messageContainer.classList.add('success');

            display.textContent = updatedAbout;
            display.classList.remove('hidden');
            input.classList.add('hidden');
            document.getElementById('about-save-button').classList.add('hidden');
        } else {
            return response.json().then(data => {
                throw new Error(data.message || 'Failed to update about me.');
            });
        }
    })
    .catch(error => {
        messageContainer.textContent = error.message;
        messageContainer.classList.remove('success');
        messageContainer.classList.add('error');
    });
}


function handleComment(button) {
    const reviewContainer = button.closest('.review-container');
    const reviewId = reviewContainer.querySelector('.review-id').value;
    const commentText = reviewContainer.querySelector('textarea').value;
    if (!commentText.trim()) {
        alert("Comment cannot be empty!");
        return;
    }
    const payload = {
        content: commentText,
        reviewId: reviewId
    };
    fetch(`/api/comment/${reviewId}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
    })
        .then((response) => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error("Failed to submit comment");
            }
        })
        .then((data) => {
            alert(data.message);
            reviewContainer.querySelector('textarea').value = '';
        })
        .catch((error) => {
            console.error("Error:", error);
            alert("Failed to submit comment. Please try again.");
        });
}

function toggleFollow() {
    const button = document.getElementById("follow-button");
    const isFollowing = button.textContent === "Unfollow";
    const username = document.getElementById("username-display").textContent.trim();

    const method = isFollowing ? "DELETE" : "PUT";
    const endpoint = isFollowing ? `/api/user/unfollow/${username}` : `/api/user/follow/${username}`;

    fetch(endpoint, {
        method: method,
        headers: {
            "Content-Type": "application/json",
        }
    })
    .then(response => {
        if (response.ok) {
            button.textContent = isFollowing ? "Follow" : "Unfollow";


            const followersElement = document.querySelector(".stats .stat:nth-child(3) span"); // Followers
            const followingElement = document.querySelector(".stats .stat:nth-child(2) span"); // Following

            if (isFollowing) {

                followersElement.textContent = parseInt(followersElement.textContent) - 1;
            } else {

                followersElement.textContent = parseInt(followersElement.textContent) + 1;
            }
        } else {
            console.error("Error toggling follow status");
        }
    })
    .catch(error => console.error("Error:", error));
}