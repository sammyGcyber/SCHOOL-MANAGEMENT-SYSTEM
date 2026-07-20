# Campus Portal — School Management System

A Spring Boot 3.5 university portal demonstrating clean layered architecture, object-oriented design, Spring Security RBAC, JPA relationships, server-side validation, and a responsive Thymeleaf interface.

## Requirements

- JDK 21
- MySQL 8 running locally
- Maven 3.9+

## Run locally

1. Create a MySQL user with permission to create databases, or create `school_management` yourself.
2. Set the password for the configured MySQL user: PowerShell: `$env:MYSQL_PASSWORD='your-password'`.
3. Run `mvn spring-boot:run`.
4. Open `http://localhost:8080`.

The schema is generated from the JPA entity model on startup. For a production deployment, replace `ddl-auto=update` with versioned Flyway migrations.

## Demo access

| Role | Username | Password |
|---|---|---|
| Administrator | `admin` | `Admin@123` |
| Lecturer | `lecturer` | `Lecturer@123` |
| Student | `student` | `Student@123` |

Change all seed passwords before a real deployment.

## Structure

`controller` contains web endpoints; `service` contains use cases; `repository` owns persistence queries; `entity` maps the normalized relational model; `dto` carries validated form data; and `config` / `security` centralize application and access rules.

## Current functional scope

Authentication and role-based redirect, dashboard statistics, announcements, and administrator student registration/search/removal are implemented end-to-end. The entity base provides the academic, programme, semester, enrolment, teaching, and payments foundation for subsequent modules.

## Design diagrams

See [docs/architecture.md](docs/architecture.md) for ER, use-case, class, and login sequence diagrams.
