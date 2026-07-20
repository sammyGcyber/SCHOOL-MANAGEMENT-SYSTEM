# Installation guide

1. Install JDK 21, MySQL Server 8, and Maven 3.9 or newer.
2. Start MySQL and create a local database if your MySQL account cannot create one automatically:

   ```sql
   CREATE DATABASE school_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. In `src/main/resources/application.properties`, change `spring.datasource.username` if needed. Set the password securely in the shell before starting:

   ```powershell
   $env:MYSQL_PASSWORD='your-password'
   ```

4. From the project folder, run `mvn spring-boot:run`.
5. Visit `http://localhost:8080` and sign in with a demo account listed in the README.

For IntelliJ IDEA, open `pom.xml` as a project, select JDK 21, set `MYSQL_PASSWORD` in the Run Configuration, and run `SchoolManagementApplication`.
