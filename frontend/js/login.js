class LoginManager {
    constructor() {
        this.loginForm = document.getElementById('loginForm');
        this.togglePasswordBtn = document.querySelector('.toggle-password');
        this.passwordInput = document.getElementById('password');
        this.rememberMeCheckbox = document.getElementById('rememberMe');
        
        this.initializeEventListeners();
        this.loadSavedEmail();
    }

    initializeEventListeners() {
        this.loginForm?.addEventListener('submit', (e) => this.handleLogin(e));
        
        // Toggle password visibility
        this.togglePasswordBtn?.addEventListener('click', () => {
            const type = this.passwordInput.type === 'password' ? 'text' : 'password';
            this.passwordInput.type = type;
            
            // Update icon based on password visibility
            const eyeIcon = this.togglePasswordBtn.querySelector('.eye-icon');
            if (type === 'password') {
                eyeIcon.innerHTML = `<path d="M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zM12 17c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5zm0-8c-1.66 0-3 1.34-3 3s1.34 3 3 3 3-1.34 3-3-1.34-3-3-3z"/>`;
            } else {
                eyeIcon.innerHTML = `<path d="M12 7c2.76 0 5 2.24 5 5 0 .65-.13 1.26-.36 1.83l2.92 2.92c1.51-1.26 2.7-2.89 3.43-4.75-1.73-4.39-6-7.5-11-7.5-1.4 0-2.74.25-3.98.7l2.16 2.16C10.74 7.13 11.35 7 12 7zM2 4.27l2.28 2.28.46.46C3.08 8.3 1.78 10.02 1 12c1.73 4.39 6 7.5 11 7.5 1.55 0 3.03-.3 4.38-.84l.42.42L19.73 22 21 20.73 3.27 3 2 4.27zM7.53 9.8l1.55 1.55c-.05.21-.08.43-.08.65 0 1.66 1.34 3 3 3 .22 0 .44-.03.65-.08l1.55 1.55c-.67.33-1.41.53-2.2.53-2.76 0-5-2.24-5-5 0-.79.2-1.53.53-2.2zm4.31-.78l3.15 3.15.02-.16c0-1.66-1.34-3-3-3l-.17.01z"/>`;
            }
        });
    }

    async handleLogin(e) {
        e.preventDefault();
        
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        // Clear previous error messages
        this.clearErrors();

        try {
            const submitButton = this.loginForm.querySelector('button[type="submit"]');
            submitButton.disabled = true;
            submitButton.classList.add('loading');

            await API.login(email, password);
            
            // Save email if remember me is checked
            if (this.rememberMeCheckbox.checked) {
                localStorage.setItem('savedEmail', email);
            } else {
                localStorage.removeItem('savedEmail');
            }

            window.location.href = '/pages/profile.html';
        } catch (error) {
            this.showError(error.message || 'Login failed. Please try again.');
        } finally {
            const submitButton = this.loginForm.querySelector('button[type="submit"]');
            submitButton.disabled = false;
            submitButton.classList.remove('loading');
        }
    }

    loadSavedEmail() {
        const savedEmail = localStorage.getItem('savedEmail');
        if (savedEmail) {
            document.getElementById('email').value = savedEmail;
            this.rememberMeCheckbox.checked = true;
        }
    }

    showError(message) {
        const errorDiv = document.createElement('div');
        errorDiv.className = 'error-message';
        errorDiv.textContent = message;
        this.loginForm.insertBefore(errorDiv, this.loginForm.firstChild);
    }

    clearErrors() {
        this.loginForm.querySelectorAll('.error-message').forEach(error => error.remove());
    }
}

// Initialize login manager
document.addEventListener('DOMContentLoaded', () => {
    new LoginManager();
}); 