# Vision Care Clinic

Vision Care Clinic is a comprehensive, full-stack web application designed to streamline the operations of an eye care facility. This application provides a robust platform for patients to explore various eye care services, book appointments, and manage their user profiles. For administrators, it offers tools to manage appointments and user data.

The project is built with a modern technology stack, featuring a Spring Boot backend for reliable API services and a React frontend for a responsive and interactive user experience. Security is paramount, with JWT-based authentication ensuring data integrity and privacy.

---

## Table of Contents

1.  [Features](#features)
2.  [Technology Stack](#technology-stack)
3.  [Project Structure](#project-structure)
4.  [Prerequisites](#prerequisites)
5.  [Installation and Setup](#installation-and-setup)
    *   [Database Setup](#1-database-setup)
    *   [Backend Setup](#2-backend-setup)
    *   [Frontend Setup](#3-frontend-setup)
6.  [Configuration](#configuration)
7.  [Documentation](#documentation)
8.  [Contributing](#contributing)
9.  [License](#license)

---

## Features

### Frontend (Client-Side)
*   **Responsive Design**: Built with Tailwind CSS to ensure a seamless experience across desktops, tablets, and mobile devices.
*   **User Authentication**: Secure login and registration forms using JWT authentication.
*   **Appointment Booking**: Interactive forms for patients to schedule appointments with specific eye care specialists.
*   **Dashboard**: A personalized user dashboard to view upcoming appointments and history.
*   **Service Pages**: dedicated informational pages for various treatments:
    *   **Comprehensive Eye Exams**: General vision health checks.
    *   **Corneal Treatments**: Information on corneal health and procedures.
    *   **IPL Therapy**: Details on Intense Pulsed Light therapy.
    *   **LASIK Surgery**: Information about laser eye surgery options.
    *   **Ophthalmology**: General ophthalmology services.
    *   **Pediatric Eye Care**: Specialized care for children.
*   **Internationalization (i18n)**: Support for multiple languages to serve a diverse patient base.

### Backend (Server-Side)
*   **RESTful API**: A well-structured API architecture following standard HTTP methods.
*   **Secure Authentication**: Implementation of Spring Security with JSON Web Tokens (JWT) for stateless authentication.
*   **User Management**: Endpoints for registering users and managing user sessions.
*   **Appointment Management**: Full CRUD capabilities for appointments, including retrieving user-specific data.
*   **Database Integration**: Robust data persistence using PostgreSQL with Spring Data JPA.
*   **Input Validation**: Server-side validation to ensure data integrity.

---

## Technology Stack

### Backend
*   **Language**: Java 17
*   **Framework**: Spring Boot 3.3.0
*   **Data Access**: Spring Data JPA
*   **Security**: Spring Security, JJWT (JSON Web Token)
*   **Database**: PostgreSQL 14 (Alpine)
*   **Build Tool**: Maven

### Frontend
*   **Library**: React 18
*   **Build Tool**: Vite
*   **Styling**: Tailwind CSS, PostCSS, Autoprefixer
*   **Routing**: React Router DOM 6
*   **State/Data Fetching**: Custom Hooks, Axios (implied)
*   **Internationalization**: i18next, react-i18next
*   **Icons**: Heroicons

### Infrastructure
*   **Containerization**: Docker, Docker Compose

---

## Project Structure

```text
vision-care-clinic/
├── infra/                  # Infrastructure configuration
│   └── docker-compose.yml  # Docker Compose for Database (and potentially full stack)
├── vision_care_clinic/     # Main application source code
│   ├── backend/            # Spring Boot Application
│   │   ├── src/
│   │   │   └── main/java/com/visioncare/backend/
│   │   │       ├── config/       # Security & App Config
│   │   │       ├── controller/   # REST Controllers
│   │   │       ├── dto/          # Data Transfer Objects
│   │   │       ├── model/        # JPA Entities
│   │   │       ├── repository/   # JPA Repositories
│   │   │       └── service/      # Business Logic
│   │   └── pom.xml
│   └── frontend/           # React Application
│       ├── src/
│       │   ├── api/              # API service calls
│       │   ├── components/       # Reusable UI components
│       │   ├── hooks/            # Custom React hooks
│       │   ├── pages/            # Route pages (Landing, Dashboard, etc.)
│       │   ├── styles/           # Global styles
│       │   └── i18n.js           # Internationalization config
│       ├── package.json
│       └── vite.config.js
└── README.md               # Project Documentation
```

---

## Prerequisites

Before you begin, ensure you have the following installed on your local machine:

*   **Java Development Kit (JDK)**: Version 17 or higher.
*   **Node.js**: Version 18 or higher (LTS recommended).
*   **Docker & Docker Compose**: For running the PostgreSQL database.
*   **Git**: For version control.

---

## Installation and Setup

Follow these steps to get the application running locally.

### 1. Database Setup

The easiest way to spin up the database is using Docker Compose located in the `infra` directory.

1.  Navigate to the infrastructure directory:
    ```bash
    cd infra
    ```

2.  Start the PostgreSQL container:
    ```bash
    docker-compose up -d
    ```
    This will start a PostgreSQL database listening on port `5432` with the following credentials (as defined in `docker-compose.yml`):
    *   **Database**: `visioncare`
    *   **User**: `admin`
    *   **Password**: `password`

### 2. Backend Setup

1.  Open a new terminal window and navigate to the backend directory:
    ```bash
    cd vision_care_clinic/backend
    ```

2.  (Optional) Configure Database Connection:
    If you changed the Docker credentials or are using a local PostgreSQL installation, update `src/main/resources/application.properties` with your database details.

3.  Build the project and install dependencies:
    ```bash
    ./mvnw clean install
    ```

4.  Run the Spring Boot application:
    ```bash
    ./mvnw spring-boot:run
    ```
    The backend server will start at `http://localhost:8080`.

### 3. Frontend Setup

1.  Open a third terminal window and navigate to the frontend directory:
    ```bash
    cd vision_care_clinic/frontend
    ```

2.  Install the Node dependencies:
    ```bash
    npm install
    ```

3.  Start the development server:
    ```bash
    npm run dev
    ```
    The frontend application will start at `http://localhost:5173` (or another available port).

---

## Configuration

### Backend Configuration (`application.properties`)

The backend configuration is primarily handled in `src/main/resources/application.properties`. Key properties include:

*   `spring.datasource.url`: JDBC URL for the database.
*   `spring.datasource.username`: Database username.
*   `spring.datasource.password`: Database password.
*   `spring.jpa.hibernate.ddl-auto`: Database initialization strategy (e.g., `update`, `create-drop`).

### Environment Variables

For production environments, it is recommended to pass sensitive information via environment variables or system properties rather than hardcoding them in the properties file.

---

## Documentation

For more detailed information, please refer to the following guides located in the `docs/` directory:

*   **[API Reference](docs/API_REFERENCE.md)**: Detailed documentation of all backend REST API endpoints, including request/response examples.
*   **[Frontend Guide](docs/FRONTEND_GUIDE.md)**: Overview of the React application structure, components, routing, and internationalization.
*   **[Backend Guide](docs/BACKEND_GUIDE.md)**: Architecture details, database schema, security flow, and development guidelines for the Spring Boot backend.

### Quick API Overview

#### Authentication (`/api/auth`)
*   `POST /register`: Register a new user.
*   `POST /login`: Authenticate and receive a JWT.
*   `GET /me`: Get current user profile.

#### Appointments (`/api/appointments`)
*   `POST /book`: Book an appointment.
*   `GET /me`: Get my appointments.

---

## Known Issues

### Frontend Testing Timeouts

There is a persistent issue with the frontend testing environment that causes Vitest to time out. This issue has been observed even when running a single, simple test file. The root cause is suspected to be related to the test runner's configuration or a conflict with the i18next library, but multiple attempts to resolve the issue have been unsuccessful.

As a result, the frontend unit and component tests are currently not operational. This is a known issue, and further investigation is required to resolve it.

---

## Contributing

Contributions are welcome! If you would like to contribute to this project, please follow these steps:

1.  Fork the repository.
2.  Create a new branch for your feature or bugfix (`git checkout -b feature/AmazingFeature`).
3.  Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4.  Push to the branch (`git push origin feature/AmazingFeature`).
5.  Open a Pull Request.

Please ensure your code adheres to the existing style and that you have tested your changes thoroughly.

---

## License

This project is open-source and available for personal and educational use.
