# Event Ticket Booking System

A full-stack ticket booking system that allows users to browse events, book tickets, and manage their bookings. Built with Java (backend) and vanilla JavaScript (frontend).

## Features

### User Features
- User registration and authentication
- Browse available events with pagination
- Advanced search and filtering (date, price, category, venue)
- Real-time seat selection and booking
- E-ticket generation and email confirmation
- View booking history and upcoming events
- Update profile and preferences
- Cancel or modify bookings

### Admin Features
- Event management (CRUD operations)
- User management
- Sales and booking analytics
- Venue capacity management
- Discount code management

### Technical Features
- RESTful API architecture
- JWT-based authentication
- Hibernate ORM with connection pooling
- Responsive frontend design
- Client-side form validation
- Real-time seat availability checking
- Rate limiting and request throttling
- Caching for improved performance

## Technology Stack

### Backend
- Java 17
- Spring Security
- Hibernate 6.2.7
- MySQL 8.0
- Servlet API 4.0
- Jackson (JSON processing)
- Maven
- JUnit 5 for testing
- Log4j2 for logging

### Frontend
- HTML5
- CSS3 with Flexbox/Grid
- Vanilla JavaScript (ES6+)
- Fetch API for HTTP requests
- LocalStorage for client-side caching
- Chart.js for analytics

## Project Structure

```
project/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/ticketbooking/
│   │   │       ├── config/
│   │   │       ├── dao/
│   │   │       ├── model/
│   │   │       ├── service/
│   │   │       ├── facade/
│   │   │       ├── servlet/
│   │   │       ├── exception/
│   │   │       └── util/
│   │   ├── resources/
│   │   └── webapp/
│   └── test/
└── frontend/
    ├── index.html
    ├── css/
    ├── js/
    ├── assets/
    └── pages/
```

## Setup Instructions

### Prerequisites
- JDK 17 or later
- MySQL 8.0 or later
- Maven 3.6 or later
- Node.js 18+ (for development tools)
- Web browser with JavaScript enabled

### Database Setup
1. Create a MySQL database:
```sql
CREATE DATABASE ticketbooking;
```

2. Configure database connection in `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ticketbooking
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Initialize the database:
```bash
mvn flyway:migrate
```

### Backend Setup
1. Clone the repository:
```bash
git clone https://github.com/yourusername/ticket-booking-system.git
cd ticket-booking-system
```

2. Build and run tests:
```bash
mvn clean verify
```

3. Start the application:
```bash
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### Frontend Setup
1. Install development dependencies:
```bash
npm install
```

2. Start the development server:
```bash
npm start
```

The frontend will be available at `http://localhost:3000`

## API Documentation

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - User login
- `POST /api/auth/refresh` - Refresh access token
- `POST /api/auth/logout` - User logout

### Users
- `GET /api/users/profile` - Get user profile
- `PUT /api/users/profile` - Update user profile
- `GET /api/users/bookings` - Get user's bookings
- `GET /api/users/preferences` - Get user preferences

### Events
- `GET /api/events` - List events with pagination
- `GET /api/events/{id}` - Get event details
- `GET /api/events/search` - Search events
- `POST /api/events` - Create event (admin)
- `PUT /api/events/{id}` - Update event (admin)

### Tickets
- `POST /api/tickets/book` - Book tickets
- `GET /api/tickets/{id}` - Get ticket details
- `POST /api/tickets/{id}/cancel` - Cancel booking
- `GET /api/tickets/{id}/download` - Download e-ticket

## Security Features

- JWT-based authentication with refresh tokens
- Password hashing using BCrypt
- Role-based access control (RBAC)
- Rate limiting and request throttling
- CORS configuration
- XSS protection
- CSRF protection
- Input validation and sanitization
- Secure session management

## Development

### Testing
```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify

# Generate test coverage report
mvn jacoco:report
```

### Code Quality
```bash
# Run static code analysis
mvn sonar:sonar

# Check code style
mvn checkstyle:check
```

## Deployment

### Production Build
```bash
# Backend
mvn clean package -P prod

# Frontend
npm run build
```

### Docker Support
```bash
# Build image
docker build -t ticket-booking-system .

# Run container
docker-compose up
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Coding Standards
- Follow Java code conventions
- Write unit tests for new features
- Update documentation as needed
- Use meaningful commit messages

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

For support, email support@ticketbooking.com or join our Slack channel.

## Acknowledgments

- Spring Framework documentation
- Hibernate documentation
- MySQL documentation
- MDN Web Docs
- The awesome open-source community

