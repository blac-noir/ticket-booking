class RegisterManager {
    constructor() {
        this.registerForm = document.getElementById('registerForm');
        this.showLoginBtn = document.getElementById('showLoginBtn');
        
        this.initializeEventListeners();
    }

    initializeEventListeners() {
        this.registerForm?.addEventListener('submit', (e) => this.handleRegister(e));
        this.showLoginBtn?.addEventListener('click', (e) => {
            e.preventDefault();
            document.getElementById('loginBtn').click();
        });
    }

    async handleRegister(e) {
        e.preventDefault();
        
        const formData = {
            fullName: document.getElementById('fullName').value,
            email: document.getElementById('email').value,
            password: document.getElementById('password').value,
            confirmPassword: document.getElementById('confirmPassword').value
        };

        // Clear previous error messages
        this.clearErrors();

        // Validate form
        const errors = this.validateForm(formData);
        if (Object.keys(errors).length > 0) {
            this.showErrors(errors);
            return;
        }

        try {
            const submitButton = this.registerForm.querySelector('button[type="submit"]');
            submitButton.disabled = true;
            submitButton.classList.add('loading');

            await API.register(formData);
            window.location.href = '/pages/profile.html';
        } catch (error) {
            this.showErrors({
                general: error.message || 'Registration failed. Please try again.'
            });
        } finally {
            const submitButton = this.registerForm.querySelector('button[type="submit"]');
            submitButton.disabled = false;
            submitButton.classList.remove('loading');
        }
    }

    validateForm(formData) {
        const errors = {};
        
        if (!formData.fullName.trim()) {
            errors.fullName = 'Full name is required';
        }
        
        const emailError = Validator.validateEmail(formData.email);
        if (emailError) errors.email = emailError;
        
        const passwordError = Validator.validatePassword(formData.password);
        if (passwordError) errors.password = passwordError;
        
        if (formData.password !== formData.confirmPassword) {
            errors.confirmPassword = 'Passwords do not match';
        }
        
        return errors;
    }

    isValidEmail(email) {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
    }

    showErrors(errors) {
        Object.keys(errors).forEach(field => {
            const input = document.getElementById(field);
            if (input) {
                input.classList.add('error');
                const errorDiv = document.createElement('div');
                errorDiv.className = 'error-message';
                errorDiv.textContent = errors[field];
                input.parentElement.appendChild(errorDiv);
            } else if (field === 'general') {
                const errorDiv = document.createElement('div');
                errorDiv.className = 'error-message';
                errorDiv.textContent = errors[field];
                this.registerForm.insertBefore(errorDiv, this.registerForm.firstChild);
            }
        });
    }

    clearErrors() {
        this.registerForm.querySelectorAll('.error-message').forEach(error => error.remove());
        this.registerForm.querySelectorAll('.error').forEach(field => field.classList.remove('error'));
    }
}

// Initialize registration manager
document.addEventListener('DOMContentLoaded', () => {
    new RegisterManager();
}); 