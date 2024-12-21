let debounceTimeout;
let initialHTML;


document.addEventListener("DOMContentLoaded", () => {
    initialHTML = document.getElementById("userList").innerHTML;
});


function searchUsersInline() {
    const query = document.getElementById("searchInput").value.trim();

    clearTimeout(debounceTimeout);

    debounceTimeout = setTimeout(() => {
        if (!query) {
            restoreInitialProfiles();
            return;
        }
        fetch('/api/social/search', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ query }),
        })
            .then(response => response.json())
            .then(users => {
                renderProfiles(users);
            })
            .catch(error => {
                console.error('Error fetching users:', error);
            });
    }, 300);
}

function restoreInitialProfiles() {
    const profilesGrid = document.getElementById("userList");
    profilesGrid.innerHTML = initialHTML;
}

function renderProfiles(users) {
    const profilesGrid = document.getElementById("userList");

    profilesGrid.innerHTML = `
        <ul class="profiles-grid">
        </ul>
    `;

    const profilesUl = profilesGrid.querySelector("ul");


    if (users.length === 0) {
        profilesGrid.innerHTML = "<h3>No users found</h3>";
        return;
    }

    users.forEach(user => {
        const userElement = document.createElement("li");
        userElement.classList.add("profile-container");

        userElement.innerHTML = `
            <div class="profile-container">
                <img src="${user.profileUrl}" alt="Profile Picture" class="profile-picture" />
                <div class="profile-details">
                    <div class="name-button">
                        <span class="profile-username">${user.username}</span>
                        <a href="/user/${user.username}" class="profile-button">Go to Profile</a>
                    </div>
                    <p class="profile-description">${user.description}</p>
                </div>
            </div>
        `;

        profilesUl.appendChild(userElement);
    });
}

document.getElementById("searchInput").addEventListener("input", searchUsersInline);
