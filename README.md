# Car Rental Web Application

A full-stack e-commerce web app for renting cars. Built with Spring Boot, React, PostgreSQL, and JWT authentication.

## Features

- 🚗 Browse available cars
- 👤 User authentication with JWT
- 📋 Rental management
- ⭐ Car reviews and ratings
- 👨‍💼 Role-based access (Guest, Client, Provider, Admin)
- 📱 Responsive design

## Tech Stack

### Backend
- Spring Boot 3.x
- Spring Data JPA
- Spring Security + JWT
- PostgreSQL
- Maven
- Swagger/OpenAPI

### Frontend
- React 18+
- React Router
- Axios
- Vite
- CSS3

## Project Setup

### Prerequisites
- JDK 17+
- Maven
- Node.js 18+
- PostgreSQL or Neon account

### Backend Setup

1. Navigate to the backend directory:
```bash
cd backend
```

2. Update `src/main/resources/application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:postgresql://your-host:5432/carrental
spring.datasource.username=your_username
spring.datasource.password=your_password
app.jwt.secret=your-secret-key-change-in-production
```

3. Build the project:
```bash
mvn clean install
```

4. Run the application:
```bash
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### Frontend Setup

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm run dev
```

The frontend will start on `http://localhost:3000`

## Database Setup (Neon)

1. Create a free account at [Neon](https://neon.tech)
2. Create a new project and database
3. Copy the connection string
4. Update `backend/src/main/resources/application.properties`

## API Documentation

Once the backend is running, view the Swagger documentation at:
```
http://localhost:8080/swagger-ui.html
```

## Project Structure

```
car-rental/
├── backend/
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   ├── java/com/carrental/
│       │   │   ├── controller/
│       │   │   ├── service/
│       │   │   ├── repository/
│       │   │   ├── model/
│       │   │   ├── dto/
│       │   │   ├── security/
│       │   │   ├── exception/
│       │   │   └── CarRentalApplication.java
│       │   └── resources/
│       │       └── application.properties
│       └── test/
└── frontend/
    ├── package.json
    ├── vite.config.js
    └── src/
        ├── components/
        ├── pages/
        ├── services/
        ├── context/
        ├── styles/
        ├── App.jsx
        └── main.jsx
```

## Next Steps

1. **Build the Backend Models** — Create entity classes (User, Car, Rental, Review)
2. **Create Repository Interfaces** — JPA repositories for database access
3. **Implement Services** — Business logic and authentication
4. **Build REST Controllers** — API endpoints
5. **Develop React Pages** — Complete frontend implementation
6. **Set up Authentication** — JWT token management
7. **Deploy** — Docker + Render or Vercel

## Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [React Documentation](https://react.dev)
- [Neon Documentation](https://neon.tech/docs)
- [Swagger/OpenAPI](https://swagger.io)

---

Built as a CV project for Ausbildung FIAE in Germany.
