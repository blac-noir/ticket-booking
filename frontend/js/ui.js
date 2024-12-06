class UI {
    static showLoading(element) {
        element.classList.add('loading');
        element.disabled = true;
    }

    static hideLoading(element) {
        element.classList.remove('loading');
        element.disabled = false;
    }

    static showError(message, container) {
        const errorDiv = document.createElement('div');
        errorDiv.className = 'error-message';
        errorDiv.textContent = message;
        container.appendChild(errorDiv);
        
        setTimeout(() => {
            errorDiv.remove();
        }, 5000);
    }

    static updateAuthUI(isAuthenticated) {
        const authRequired = document.querySelectorAll('.auth-required');
        const authHide = document.querySelectorAll('.auth-hide');

        authRequired.forEach(element => {
            element.classList.toggle('hidden', !isAuthenticated);
        });

        authHide.forEach(element => {
            element.classList.toggle('hidden', isAuthenticated);
        });
    }

    static renderEvents(events, container) {
        container.innerHTML = events.map(event => `
            <div class="event-card">
                <h3>${event.name}</h3>
                <p>${event.description}</p>
                <div class="event-details">
                    <span>Date: ${new Date(event.eventDate).toLocaleDateString()}</span>
                    <span>Price: $${event.ticketPrice}</span>
                    <span>Available Seats: ${event.totalSeats}</span>
                </div>
                <button class="btn-book" data-event-id="${event.id}">
                    Book Ticket
                </button>
            </div>
        `).join('');
    }
}

// Event Listeners
document.addEventListener('DOMContentLoaded', () => {
    const loginBtn = document.getElementById('loginBtn');
    const loginModal = document.getElementById('loginModal');
    const closeModal = document.querySelector('.close');

    loginBtn?.addEventListener('click', () => {
        loginModal.classList.remove('hidden');
    });

    closeModal?.addEventListener('click', () => {
        loginModal.classList.add('hidden');
    });

    // Close modal when clicking outside
    window.addEventListener('click', (e) => {
        if (e.target === loginModal) {
            loginModal.classList.add('hidden');
        }
    });
}); 