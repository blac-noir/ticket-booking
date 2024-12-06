class API {
    static BASE_URL = 'http://localhost:8080/api';
    static TOKEN_KEY = 'auth_token';

    static async request(endpoint, options = {}) {
        const token = localStorage.getItem(this.TOKEN_KEY);
        
        const defaultHeaders = {
            'Content-Type': 'application/json',
            ...(token && { 'Authorization': `Bearer ${token}` })
        };

        try {
            const response = await fetch(`${this.BASE_URL}${endpoint}`, {
                ...options,
                headers: {
                    ...defaultHeaders,
                    ...options.headers
                },
                credentials: 'include'
            });

            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.error || 'Request failed');
            }

            return await response.json();
        } catch (error) {
            console.error('API request failed:', error);
            throw error;
        }
    }

    static async login(email, password) {
        const response = await this.request('/auth/login', {
            method: 'POST',
            body: JSON.stringify({ email, password })
        });
        
        if (response.token) {
            localStorage.setItem(this.TOKEN_KEY, response.token);
            localStorage.setItem('user', JSON.stringify(response.user));
        }
        
        return response;
    }

    static async register(userData) {
        const response = await this.request('/auth/register', {
            method: 'POST',
            body: JSON.stringify(userData)
        });

        if (response.token) {
            localStorage.setItem(this.TOKEN_KEY, response.token);
            localStorage.setItem('user', JSON.stringify(response.user));
        }

        return response;
    }

    static async getUserProfile() {
        return await this.request('/users/profile');
    }

    static async updateProfile(profileData) {
        return await this.request('/users/profile', {
            method: 'PUT',
            body: JSON.stringify(profileData)
        });
    }

    static async getUserBookings() {
        return await this.request('/users/bookings');
    }

    static async getEvents() {
        return await this.request('/events');
    }

    static async bookTicket(eventId, seatNumber) {
        return await this.request('/tickets/book', {
            method: 'POST',
            body: JSON.stringify({ eventId, seatNumber })
        });
    }

    static async cancelTicket(ticketId) {
        return await this.request(`/tickets/${ticketId}/cancel`, {
            method: 'POST'
        });
    }

    static isAuthenticated() {
        return !!localStorage.getItem(this.TOKEN_KEY);
    }

    static logout() {
        localStorage.removeItem(this.TOKEN_KEY);
        localStorage.removeItem('user');
        window.location.href = '/index.html';
    }
} 