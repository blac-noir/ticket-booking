class Auth {
    static init() {
        // Check authentication status on page load
        this.checkAuth();
        
        // Add logout handler
        const logoutBtn = document.getElementById('logoutBtn');
        if (logoutBtn) {
            logoutBtn.addEventListener('click', () => API.logout());
        }

        // Add login form handler
        const loginForm = document.getElementById('loginForm');
        if (loginForm) {
            loginForm.addEventListener('submit', this.handleLogin);
        }
    }

    static checkAuth() {
        const protectedPages = ['profile.html', 'booking.html'];
        const currentPage = window.location.pathname.split('/').pop();

        if (protectedPages.includes(currentPage) && !API.isAuthenticated()) {
            // Redirect to login if trying to access protected pages
            window.location.href = '/index.html';
            return;
        }

        // Update UI based on auth status
        UI.updateAuthUI(API.isAuthenticated());
    }

    static async handleLogin(e) {
        e.preventDefault();
        const email = document.getElementById('loginEmail').value;
        const password = document.getElementById('loginPassword').value;

        try {
            UI.showLoading(e.target.querySelector('button'));
            await API.login(email, password);
            window.location.href = 'pages/events.html';
        } catch (error) {
            UI.showError(error.message || 'Login failed', e.target);
        } finally {
            UI.hideLoading(e.target.querySelector('button'));
        }
    }
}

// Initialize auth handling
document.addEventListener('DOMContentLoaded', () => Auth.init()); 