document.addEventListener('DOMContentLoaded', () => {
    if (typeof monthlyActivity === 'undefined') {
        console.error("monthlyActivity is not defined. Ensure it's injected correctly from Thymeleaf.");
        return;
    }
    const months = monthlyActivity.map(activity => activity.month);
    const reviewsCounts = monthlyActivity.map(activity => activity.reviewsCount);
    const ctx = document.getElementById('activityChart').getContext('2d');
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: months,
            datasets: [{
                label: 'Reviews per Month',
                data: reviewsCounts,
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            },
            plugins: {
                legend: {
                    display: false
                }
            }
        }
    });
});