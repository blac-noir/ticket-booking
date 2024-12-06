class RegisterManager {
    constructor() {
        this.form = document.getElementById('registerForm');
        this.init();
    }

    init() {
        this.form.addEventListener('submit', (e) => {
            e.preventDefault();
            this.handleRegister();
        });
    }

    async handleRegister() {
        const formData = {
            fullName: document.getElementById('fullName').value,
            email: document.getElementById('email').value,
            password: document.getElementById('password').value,
            confirmPassword: document.getElementById('confirmPassword').value
        };

        // Validate form data
        const validation = Validator.validateForm(formData);
        if (!validation.isValid) {
            Object.values(validation.errors).forEach(error => {
                UI.showError(error, this.form);
            });
            return;
        }

        if (formData.password !== formData.confirmPassword) {
            UI.showError('Passwords do not match', this.form);
            return;
        }

        UI.showLoading(this.form.querySelector('button'));

        try {
            const response = await API.register(formData);
            if (response.success) {
                window.location.href = 'events.html';
            }
        } catch (error) {
            UI.showError(error.message || 'Registration failed', this.form);
        } finally {
            UI.hideLoading(this.form.querySelector('button'));
        }
    }
}

// Initialize registration manager
document.addEventListener('DOMContentLoaded', () => {
    new RegisterManager();
}); 