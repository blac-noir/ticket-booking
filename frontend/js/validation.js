class Validator {
    static EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    static PASSWORD_REGEX = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;

    static validateEmail(email) {
        if (!email) return 'Email is required';
        if (!this.EMAIL_REGEX.test(email)) return 'Invalid email format';
        return null;
    }

    static validatePassword(password) {
        if (!password) return 'Password is required';
        if (!this.PASSWORD_REGEX.test(password)) {
            return 'Password must be at least 8 characters long and contain both letters and numbers';
        }
        return null;
    }

    static validateForm(formData) {
        const errors = {};
        
        for (const [key, value] of Object.entries(formData)) {
            switch(key) {
                case 'email':
                    const emailError = this.validateEmail(value);
                    if (emailError) errors.email = emailError;
                    break;
                case 'password':
                    const passwordError = this.validatePassword(value);
                    if (passwordError) errors.password = passwordError;
                    break;
                // Add more validation cases as needed
            }
        }

        return {
            isValid: Object.keys(errors).length === 0,
            errors
        };
    }
} 