class ProfileManager {
    constructor() {
        this.profileForm = document.getElementById('profileForm');
        this.bookingsList = document.querySelector('.bookings-list');
        this.init();
    }

    async init() {
        this.bindEventListeners();
        await this.loadUserProfile();
        await this.loadBookings();
    }

    bindEventListeners() {
        this.profileForm.addEventListener('submit', (e) => {
            e.preventDefault();
            this.handleProfileUpdate();
        });

        this.bookingsList.addEventListener('click', (e) => {
            if (e.target.classList.contains('btn-cancel')) {
                this.handleCancelBooking(e.target.dataset.ticketId);
            }
        });
    }

    async loadUserProfile() {
        try {
            const user = await API.getUserProfile();
            document.getElementById('fullName').value = user.fullName;
            document.getElementById('email').value = user.email;
        } catch (error) {
            UI.showError('Failed to load profile', this.profileForm);
        }
    }

    async loadBookings() {
        try {
            const bookings = await API.getUserBookings();
            this.renderBookings(bookings);
        } catch (error) {
            UI.showError('Failed to load bookings', this.bookingsList);
        }
    }

    renderBookings(bookings) {
        this.bookingsList.innerHTML = bookings.map(booking => `
            <div class="booking-card">
                <div class="booking-details">
                    <h3>${booking.event.name}</h3>
                    <p>Date: ${new Date(booking.event.eventDate).toLocaleDateString()}</p>
                    <p>Seat: ${booking.seatNumber}</p>
                    <p>Booking Date: ${new Date(booking.bookingDate).toLocaleDateString()}</p>
                </div>
                <div class="booking-actions">
                    <span class="booking-status status-${booking.status.toLowerCase()}">
                        ${booking.status}
                    </span>
                    ${booking.status === 'ACTIVE' ? `
                        <button class="btn-cancel" data-ticket-id="${booking.id}">
                            Cancel Booking
                        </button>
                    ` : ''}
                </div>
            </div>
        `).join('');
    }

    async handleProfileUpdate() {
        const formData = {
            fullName: document.getElementById('fullName').value
        };

        UI.showLoading(this.profileForm.querySelector('button'));

        try {
            await API.updateProfile(formData);
            UI.showError('Profile updated successfully', this.profileForm);
        } catch (error) {
            UI.showError('Failed to update profile', this.profileForm);
        } finally {
            UI.hideLoading(this.profileForm.querySelector('button'));
        }
    }

    async handleCancelBooking(ticketId) {
        if (!confirm('Are you sure you want to cancel this booking?')) {
            return;
        }

        try {
            await API.cancelTicket(ticketId);
            await this.loadBookings(); // Refresh bookings list
            UI.showError('Booking cancelled successfully', this.bookingsList);
        } catch (error) {
            UI.showError('Failed to cancel booking', this.bookingsList);
        }
    }
}

// Initialize profile manager
document.addEventListener('DOMContentLoaded', () => {
    new ProfileManager();
}); 