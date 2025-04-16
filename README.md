Microservices-Based Educational Management System
Project Title: Microservices-Based Educational Management System

Submitted By: Yasin Aydın, Batuhan Kaya, Hüseyin Can Kayım

Project Overview
This project implements an educational management system using a microservices architecture. The system is designed to manage various functionalities such as announcements, course content, user authentication, messaging, and user management. Each functionality is encapsulated in its own microservice, enabling scalability and flexibility for the system.

Key Features
Announcements Management

Authentication and Authorization (OAuth2)

Course Management

Messaging System

User Management

Scalable Architecture

Technologies Used
Backend: Spring Boot (Java)

Frontend: React.js

Database: MySQL (for user and course data storage)

Containerization: Docker

Version Control: Git (GitHub for repository management)

CI/CD: GitHub Actions for deployment automation

Architecture
Microservices
Each service in the system is designed as an independent microservice:

Announcement Service

Authentication Service

Course Management Service

User Management Service

Messaging Service

These services communicate through RESTful APIs, and each service is containerized using Docker for scalability.

Team Roles
Yasin Aydın: Frontend Developer – Focused on building the user interface and implementing the messaging system using React.js.

Batuhan Kaya: Backend Developer – Responsible for developing authentication, user management, and course-related services using Spring Boot.

Hüseyin Can Kayım: DevOps Engineer – Handled Docker integration, deployment processes, and ensured scalability through containerization.

Installation & Setup
1. Clone the Repository
Clone the repository to your local machine:

bash
Copy
Edit
git clone https://github.com/HCanKayim/301Project.git
cd 301Project
2. Backend Setup
To run the backend services locally, follow these steps:

Install Java: Ensure that Java 11 or higher is installed.

Run the services using Docker Compose: All services are containerized using Docker. You can use Docker Compose to build and run the containers.

bash
Copy
Edit
docker-compose up --build
3. Frontend Setup
To run the frontend locally:

Install Node.js and npm (if not already installed).

Navigate to the frontend directory and install dependencies:

bash
Copy
Edit
cd frontend
npm install
Start the frontend server:

bash
Copy
Edit
npm start
4. Access the Application
Once the services are running, you can access the application in your browser:

Frontend: http://localhost:3000

Backend services: Each service is available via different ports as defined in the docker-compose.yml.

Database Design
Each microservice has its own database schema. This separation ensures data integrity and allows services to evolve independently.

User Service: Manages user data and authentication.

Course Service: Manages course data.

Messaging Service: Manages messages between users.

Announcement Service: Manages announcements for courses.

Methodology
Developed each service using Spring Boot to implement RESTful APIs.

Containerized each service using Docker to ensure portability and scalability.

Implemented OAuth2 authentication for secure user login and authorization.

Testing
Unit tests for each service were implemented using JUnit and Mockito.

Integration tests were run to ensure that the microservices work as expected together.

Challenges
Handling communication between multiple microservices, especially in a containerized environment.

Ensuring data consistency across different microservices and databases.

Securing user data and implementing robust authentication with OAuth2.

Future Enhancements
Real-time notification system: Implement WebSockets or similar technologies to send real-time notifications to users.

Kubernetes deployment: Utilize Kubernetes for advanced orchestration and deployment strategies, improving scalability and fault tolerance.

Analytics service: Add a service to track and analyze student performance.

References
Fowler, M. Microservices.

Spring Boot Documentation: https://spring.io/projects/spring-boot

Docker Documentation: https://docs.docker.com/
