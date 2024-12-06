class EventsManager {
    constructor() {
        this.events = [];
        this.currentFilters = {
            search: '',
            date: '',
            priceRange: ''
        };
        this.init();
    }

    async init() {
        this.bindEventListeners();
        await this.loadEvents();
    }

    bindEventListeners() {
        // Filter listeners
        document.getElementById('searchInput').addEventListener('input', (e) => {
            this.currentFilters.search = e.target.value;
            this.applyFilters();
        });

        document.getElementById('dateFilter').addEventListener('change', (e) => {
            this.currentFilters.date = e.target.value;
            this.applyFilters();
        });

        document.getElementById('priceFilter').addEventListener('change', (e) => {
            this.currentFilters.priceRange = e.target.value;
            this.applyFilters();
        });

        // Booking listeners
        document.getElementById('eventsList').addEventListener('click', (e) => {
            if (e.target.classList.contains('btn-book')) {
                this.handleBookingClick(e.target.dataset.eventId);
            }
        });

        document.getElementById('bookingForm').addEventListener('submit', (e) => {
            e.preventDefault();
            this.handleBookingSubmit();
        });
    }

    async loadEvents() {
        const spinner = document.getElementById('loadingSpinner');
        spinner.classList.remove('hidden');
        
        try {
            this.events = await API.getEvents();
            this.renderEvents();
        } catch (error) {
            UI.showError('Failed to load events', document.querySelector('.events-container'));
        } finally {
            spinner.classList.add('hidden');
        }
    }

    applyFilters() {
        const filteredEvents = this.events.filter(event => {
            const matchesSearch = event.name.toLowerCase().includes(this.currentFilters.search.toLowerCase()) ||
                                event.description.toLowerCase().includes(this.currentFilters.search.toLowerCase());
            
            const matchesDate = !this.currentFilters.date || 
                               event.eventDate.startsWith(this.currentFilters.date);
            
            const matchesPrice = this.checkPriceRange(event.ticketPrice);
            
            return matchesSearch && matchesDate && matchesPrice;
        });

        this.renderEvents(filteredEvents);
    }

    checkPriceRange(price) {
        if (!this.currentFilters.priceRange) return true;
        
        const [min, max] = this.currentFilters.priceRange.split('-').map(Number);
        if (this.currentFilters.priceRange === '101+') {
            return price >= 101;
        }
        return price >= min && price <= max;
    }

    async handleBookingClick(eventId) {
        if (!localStorage.getItem(API.TOKEN_KEY)) {
            UI.showError('Please login to book tickets', document.querySelector('.events-container'));
            document.getElementById('loginBtn').click();
            return;
        }

        const event = this.events.find(e => e.id === Number(eventId));
        if (!event) return;

        const modal = document.getElementById('bookingModal');
        document.getElementById('eventName').textContent = event.name;
        document.getElementById('ticketPrice').textContent = event.ticketPrice;
        
        // Populate seat numbers
        const seatSelect = document.getElementById('seatNumber');
        seatSelect.innerHTML = Array.from({length: event.totalSeats}, (_, i) => {
            return `<option value="A${i + 1}">Seat A${i + 1}</option>`;
        }).join('');

        modal.classList.remove('hidden');
    }

    async handleBookingSubmit() {
        const form = document.getElementById('bookingForm');
        const eventId = document.querySelector('.btn-book').dataset.eventId;
        const seatNumber = document.getElementById('seatNumber').value;

        UI.showLoading(form.querySelector('button'));

        try {
            await API.bookTicket(eventId, seatNumber);
            document.getElementById('bookingModal').classList.add('hidden');
            UI.showError('Booking successful!', document.querySelector('.events-container'));
            await this.loadEvents(); // Refresh events list
        } catch (error) {
            UI.showError('Failed to book ticket', form);
        } finally {
            UI.hideLoading(form.querySelector('button'));
        }
    }

    showLoading() {
        const spinner = document.getElementById('loadingSpinner');
        spinner.classList.remove('hidden');
    }

    hideLoading() {
        const spinner = document.getElementById('loadingSpinner');
        spinner.classList.add('hidden');
    }
}

// Initialize events manager when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    new EventsManager();
}); 