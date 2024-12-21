function handleComment(button) {
    const reviewContainer = button.closest('.review-section');
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

    fetch(`/api/comment/upload/${reviewId}`, {
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
        window.location.reload();
        reviewContainer.querySelector('textarea').value = '';
    })
    .catch((error) => {
        console.error("Error:", error);
        alert("Failed to submit comment. Please try again.");
    });
}