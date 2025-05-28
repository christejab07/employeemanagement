# Employee and Department Management System

This is a Spring Boot application designed to manage employees and departments within an organization. It features robust user authentication and role-based authorization using Spring Security, ensuring secure access to data and operations.

---

## Features

- **User Authentication**: Secure login for system users.
- **Role-Based Authorization**:
  - `ADMIN`: Full CRUD access to Departments, Employees, and Users.
  - `NORMAL_USER`: CRUD access to Employees and read-only access to Departments.
- **Department Management**: CRUD operations for organizational departments.
- **Employee Management**: CRUD operations for employees linked to departments.
- **Input Validation**: Using Jakarta Bean Validation.
- **Logging**: SLF4J and Logback for full application flow tracing.
- **Unidirectional Relationship**: Employee → Department.
- **DTO Pattern**: To avoid circular dependencies and clean response/request structure.

---

## Technologies Used

- Java 17  
- Spring Boot 3.2.5  
- Maven  
- MySQL  
- Spring Data JPA  
- Spring Security  
- Lombok  
- Jakarta Bean Validation  
- SLF4J & Logback

---

## Prerequisites

- JDK 17+  
- Maven 3.6.0+  
- MySQL Server (8.0 recommended)  
- HTTP Client (Postman, Insomnia)

---

## Getting Started

### 1. Clone the Repository

```bash
git clone  https://github.com/christejab07/employeemanagement
cd employeemanagement
````

### 2. Database Setup (MySQL)

```sql
CREATE DATABASE IF NOT EXISTS employee_management_db;

CREATE USER 'springuser'@'localhost' IDENTIFIED BY 'strong_app_password';

GRANT ALL PRIVILEGES ON employee_management_db.* TO 'springuser'@'localhost';

FLUSH PRIVILEGES;
```

### 3. Configure `application.properties`

```properties
server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/employee_management_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=springuser
spring.datasource.password=strong_app_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

logging.config=classpath:logback-spring.xml
```

> Replace `springuser` and `strong_app_password` with your actual MySQL credentials.

### 4. Build the Project

```bash
mvn clean install
```

### 5. Run the Application

```bash
mvn spring-boot:run
```

> Application runs at: [http://localhost:8080](http://localhost:8080)

---

## Initial Admin User

Default credentials created on first run:

* **Username**: `admin`
* **Password**: `adminpass`

> ⚠️ **Change this password immediately!**

### To change:

```sql
USE employee_management_db;

UPDATE users
SET password = '$2a$10$YOUR_NEW_HASHED_PASSWORD_HERE'
WHERE username = 'admin';
```

Use a BCrypt hash generated via:

```java
new BCryptPasswordEncoder().encode("your_new_password")
```

---

## API Endpoints

> All endpoints require **HTTP Basic Authentication**.

### Example:

```
Username: admin
Password: adminpass
Authorization: Basic YWRtaW46YWRtaW5wYXNz
```

---

### Authentication

#### Register New User

* **POST** `/api/auth/register`
* **Access**: Public
* **Request Body**:

```json
{
  "username": "normaluser",
  "password": "userpass123",
  "email": "normal.user@example.com"
}
```

---

### Department Management

#### Create

* **POST** `/api/departments`
* **Access**: ADMIN
* **Request**:

```json
{
  "name": "Engineering",
  "location": "Building C"
}
```

#### Get All

* **GET** `/api/departments`
* **Access**: ADMIN, NORMAL\_USER

#### Get by ID

* **GET** `/api/departments/{id}`
* **Access**: ADMIN, NORMAL\_USER

#### Update

* **PUT** `/api/departments/{id}`
* **Access**: ADMIN

#### Delete

* **DELETE** `/api/departments/{id}`
* **Access**: ADMIN

---

### Employee Management

#### Create

* **POST** `/api/employees`
* **Access**: ADMIN, NORMAL\_USER
* **Request**:

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "123-456-7890",
  "hireDate": "2023-01-15",
  "salary": 75000.00,
  "jobRole": "Software Engineer",
  "departmentId": 1
}
```

#### Get All

* **GET** `/api/employees`
* **Access**: ADMIN, NORMAL\_USER

#### Get by ID

* **GET** `/api/employees/{id}`
* **Access**: ADMIN, NORMAL\_USER

#### Get by Department

* **GET** `/api/employees/by-department/{departmentId}`
* **Access**: ADMIN, NORMAL\_USER

#### Update

* **PUT** `/api/employees/{id}`
* **Access**: ADMIN, NORMAL\_USER

#### Delete

* **DELETE** `/api/employees/{id}`
* **Access**: ADMIN, NORMAL\_USER

---

## Authentication Method

This app uses **HTTP Basic Auth**. Include the following header in your requests:

```http
Authorization: Basic <base64(username:password)>
```

Example (admin\:adminpass):

```http
Authorization: Basic YWRtaW46YWRtaW5wYXNz
```

---

## Error Handling

* **200 OK**: Request successful
* **201 Created**: Resource created
* **204 No Content**: Deleted
* **400 Bad Request**: Validation or conflict error
* **401 Unauthorized**: Invalid credentials
* **403 Forbidden**: Insufficient permissions
* **404 Not Found**: Resource missing

---

## Logging

Log configuration is in `logback-spring.xml`. All significant actions are logged using SLF4J.

---

```

Let me know if you'd like this saved into a file or need a downloadable version.
```
