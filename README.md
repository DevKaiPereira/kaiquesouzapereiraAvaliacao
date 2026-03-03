# VEXYN Document Management System

## Project Description
VEXYN is a comprehensive document management system designed to streamline the handling, storage, and retrieval of documents. Built using **Spring Boot 3.5.11**, the application offers a robust and scalable solution for managing documents securely and efficiently.

## Features
- User management with role-based access control
- Document upload, retrieval, and management
- JWT authentication for secure API access
- Docker support for easy deployment
- Extensive unit tests ensuring code reliability
- Complete API documentation included

## API Routes
- **POST** `/api/auth/login` - Authenticate user and obtain JWT
- **POST** `/api/users` - Create a new user
- **GET** `/api/users` - Retrieve all users
- **POST** `/api/documents` - Upload a new document
- **GET** `/api/documents/{id}` - Retrieve document details

## Folder Structure
```
VEXYN/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   ├── resources/
│   │   └── ...
│   └── test/
│       └── java/
└── Dockerfile
```

## Tests
Our project contains extensive unit tests written using JUnit and Mockito to ensure the functionality of each module.

## Technology Stack
- **Spring Boot 3.5.11**
- **MySQL** for database management
- **JWT** for authentication
- **Docker** for containerization

## Configuration
Detailed instructions on configuring the application can be found in the `application.properties` file.

## Setup Instructions
1. Clone the repository: `git clone <repository-url>`
2. Navigate to the project directory: `cd VEXYN`
3. Install dependencies: `mvn install`
4. Run the application: `mvn spring-boot:run`

## Authentication
The application utilizes JWT for authenticating users. Ensure you obtain a token by logging in through the `/api/auth/login` route before accessing protected resources.

## Logging
Basic logging is enabled using SLF4J and Logback for monitoring application behavior.

## Error Handling
Error handling has been implemented to ensure users receive meaningful feedback for issues encountered during API interactions.

## Roadmap
- Implement additional features such as document versioning
- Enhance user roles and permissions management
- Expand API documentation with examples of use cases

---
#### Current Date and Time (UTC): 2026-03-03 05:04:27
#### Created by: DevKaiPereira
