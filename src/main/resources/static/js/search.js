document.addEventListener("DOMContentLoaded", () => {
    const searchInput = document.getElementById("searchInput");
    const searchResults = document.getElementById("searchResults");
    const apiUrl = "/api/movie/search";

    searchInput.addEventListener("input", async () => {
        const query = searchInput.value.trim();

        if (!query) {
            searchResults.innerHTML = "";
            searchResults.classList.remove("show");
            return;
        }

        try {
            const response = await fetch(apiUrl, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ query })
            });

            if (!response.ok) {
                throw new Error("Failed to fetch search results");
            }

            const movies = await response.json();
            renderResults(movies);
        } catch (error) {
            console.error(error);
            alert("An error occurred while fetching search results.");
        }
    });

    function renderResults(movies) {
        searchResults.innerHTML = "";

        if (movies.length === 0) {
            searchResults.classList.remove("show");
            return;
        }

        movies.forEach(movie => {
            const movieItem = document.createElement("div");
            movieItem.className = "movie-item";

            const link = document.createElement("a");
            link.href = `/movie/${movie.id}`;
            link.textContent = movie.title;

            movieItem.appendChild(link);
            searchResults.appendChild(movieItem);
        });

        searchResults.classList.add("show");
    }
});