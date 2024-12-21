let rating = 0;

document.addEventListener("DOMContentLoaded", () => {
    const stars = document.querySelectorAll('.star');
    const starsContainer = document.querySelector('.stars');
    const initialRating = parseInt(starsContainer.getAttribute('data-rating')) || 0;
    const reviewContentInput = document.getElementById('user-review');
    const editButton = document.getElementById('edit-button');
    const submitChangesButton = document.getElementById('submit-changes-button');
    const submitButton = document.getElementById('submit-button');



    if (initialRating > 0) {
        rating = initialRating;
        highlightStars(initialRating);
    }

    stars.forEach((star, index) => {
        star.addEventListener('mouseover', () => {
            highlightStars(index + 1);
        });

        star.addEventListener('mouseout', () => {
            highlightStars(rating);
        });

        star.addEventListener('click', () => {
            rating = index + 1;
            starsContainer.setAttribute('data-rating', rating);
            highlightStars(rating);
        });
    });

    function highlightStars(count) {
        stars.forEach((star, index) => {
            if (index < count) {
                star.classList.add('highlight');
                star.classList.remove('empty');
            } else {
                star.classList.remove('highlight');
                star.classList.add('empty');
            }
        });
    }
    const favoriteContainer = document.getElementById("favorite-container");

    if (favoriteContainer) {
        const isFavorited = favoriteContainer.classList.contains("favorited");
        favoriteContainer.addEventListener("click", () => {
            if (isFavorited) {
                removeFromFavorite();
            } else {
                addToFavorite();
            }
        });
    }
    if (reviewContentInput.disabled) {
        editButton.style.display = "inline";
        submitChangesButton.style.display = "none";
        submitButton.style.display = "none";
    } else {
        submitButton.style.display = "inline";
        submitChangesButton.style.display = "none";
        editButton.style.display = "none";
    }
});


function enableEdit() {
    const reviewContentInput = document.getElementById('user-review');
    const editButton = document.getElementById('edit-button');
    const submitChangesButton = document.getElementById('submit-changes-button');
    const submitButton = document.getElementById('submit-button');

    reviewContentInput.disabled = false;
    editButton.style.display = "none";
    submitChangesButton.style.display = "inline";
    submitButton.style.display = "none";
}


function submitReview(event) {
    event.preventDefault();

    if (rating === 0) {
        showError("Please select a rating.");
        return;
    }

    const reviewContent = document.getElementById("user-review").value.trim();
    if (reviewContent === "") {
        showError("Please write a review.");
        return;
    }

    const url = new URL(window.location.href);
    const path = url.pathname;
    const movieId = path.split('/')[2];

    const movieTitle = document.querySelector("h2").textContent.trim();
    const posterUrl = document.querySelector("#movie-poster img").src;
    const posterPath = posterUrl.replace("https://image.tmdb.org/t/p/w500/", "");

    const reviewData = {
        reviewRating: rating,
        reviewContent: reviewContent,
        movieId: movieId,
        poster_path: posterPath,
        movieTitle: movieTitle
    };

    fetch('/api/review/upload', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include',
        body: JSON.stringify(reviewData)
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(errorData => {
                    showError(errorData.message);
                    throw new Error(`HTTP error! status: ${response.status}`);
                });
            }
            return response.json();
        })
        .then(data => {
            console.log('Review uploaded successfully:', data);
            window.location.reload();
        })
        .catch(error => {
            console.error('Error uploading review:', error);
        });
}

function submitReviewChanges() {
    const reviewContent = document.getElementById("user-review").value.trim();
    const reviewRating = rating;

    if (reviewContent === "") {
        showError("Please write a review.");
        return;
    }

    if (reviewRating < 1 || reviewRating > 5) {
        showError("Rating must be between 1 and 5.");
        return;
    }

    const url = new URL(window.location.href);
    const path = url.pathname;
    const movieId = path.split('/')[2];

    const editReviewRequest = {
        movieId: movieId,
        reviewContent: reviewContent,
        reviewRating: reviewRating,
    };

    console.log('Submitting review changes with data:', editReviewRequest);

    fetch(`/api/review/edit/${movieId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include',
        body: JSON.stringify(editReviewRequest)
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(errorData => {
                showError(errorData.message);
                throw new Error(`HTTP error! status: ${response.status}`);
            });
        }
        return response.json();
    })
    .then(data => {
        console.log('Review updated successfully:', data);
        window.location.reload(); 
    })
    .catch(error => {
        console.error('Error updating review:', error);
    });
}


function showError(message) {
    const errorContainer = document.getElementById("error-message-container");
    const errorMessage = document.getElementById("error-message");

    errorMessage.textContent = message;

    errorContainer.style.display = "block";

    console.log('Error message displayed:', message);
}

function addToFavorite() {

    const url = new URL(window.location.href);
    const path = url.pathname;
    const movieId = path.split('/')[2];


    const title = document.querySelector("#movie-summary h2").textContent.trim();

    const posterUrl = document.querySelector("#movie-poster img").src;
    const posterPath = posterUrl.replace("https://image.tmdb.org/t/p/w500/", "");

    console.log("Adding to favorites:", { movieId, title, posterPath });


    const payload = {
        movieId: movieId,
        title: title,
        posterPath: posterPath
    };

    fetch(`/api/movie/${movieId}/favorite`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload)
    })
    .then(response => {
        if (response.ok) {
            // Actualiza la clase del contenedor y el texto
            const favoriteContainer = document.getElementById("favorite-container");
            favoriteContainer.classList.add("favorited");
            favoriteContainer.querySelector("span").textContent = "Unmark as Favorite";
        } else {
            throw new Error('Failed to mark as favorite.');
        }
    })
    .catch(error => console.error('Error:', error));
}

function removeFromFavorite() {
    const url = new URL(window.location.href);
    const path = url.pathname;
    const movieId = path.split('/')[2];

    fetch(`/api/movie/${movieId}/unfavorite`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
        }
    })
    .then(response => {
        if (response.ok) {
            const favoriteContainer = document.getElementById("favorite-container");
            favoriteContainer.classList.remove("favorited");
            favoriteContainer.querySelector("span").textContent = "Mark as Favorite";
        } else {
            throw new Error('Failed to remove from favorites.');
        }
    })
    .catch(error => console.error('Error:', error));
}