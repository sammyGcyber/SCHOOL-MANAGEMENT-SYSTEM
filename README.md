# Campus Portal — School Management System

Campus Portal is a Spring Boot university management application with separate, role-based workspaces for administrators, lecturers, and students. It combines a responsive Thymeleaf interface with Spring Security, JPA persistence, and MySQL.

## Collaboration

This project was developed collaboratively by a team of two contributors.

## Highlights

- Secure login and role-based access for administrators, lecturers, and students
- Student dashboard, profile, course registration, timetable, results, fees, and notifications
- Lecturer dashboard, profile, assigned units, timetable, student lists, attendance, and notifications
- Administrator student registration, search, and removal
- First-time student account creation
- MySQL-backed profiles, enrolments, payments, attendance, and profile-photo storage
- Responsive blue-accent interface for desktop and mobile

## Technology

| Area | Technology |
| --- | --- |
| Backend | Java 21, Spring Boot 3.5 |
| Web | Spring MVC, Thymeleaf, HTML, CSS, JavaScript |
| Security | Spring Security with role-based access control |
| Data | Spring Data JPA, Hibernate, MySQL 8 |
| Build | Maven 3.9+ |

## Requirements

- JDK 21
- Apache Maven 3.9 or later
- MySQL Server 8 running locally

## Run locally

1. Start MySQL Server.
2. Open PowerShell in the project folder.
3. Set the MySQL root password for the current terminal session:

   ```powershell
   $env:MYSQL_PASSWORD='your-mysql-password'
   ```

4. Start the application:

   ```powershell
   mvn spring-boot:run
   ```

5. Open [http://localhost:8080](http://localhost:8080).

The database is created automatically as `school_management`. In development, Hibernate manages the schema through `ddl-auto=update`.

## Demo accounts

| Role | Username | Password |
| --- | --- | --- |
| Administrator | `admin` | `Admin@123` |
| Lecturer | `lecturer` | `Lecturer@123` |
| Student | `student` | `Student@123` |

Change these accounts before deploying the system outside a local development environment.

## Project structure

```text
src/main/java/com/school
├── config        Application security and seed configuration
├── controller    Web endpoints and role-specific portals
├── entity        JPA domain models
├── repository    Persistence interfaces
├── security      Database-backed authentication
└── service       Student business operations

src/main/resources
├── templates     Thymeleaf pages
├── static        CSS and JavaScript assets
└── application.properties
```

## Notes for production

- Use environment variables or a secrets manager for database credentials.
- Replace `ddl-auto=update` with Flyway or Liquibase migrations.
- Change all seeded passwords and enforce a password-reset process.
- Add authorization checks around unit membership before exposing student records in production.

## Architecture diagrams

See [docs/architecture.md](docs/architecture.md) for the ER, use-case, class, and login-sequence diagrams.
