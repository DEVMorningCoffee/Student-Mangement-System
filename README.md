# 🎓 Student Course Enrollment Management System

A Java-based backend application using JDBC and PostgreSQL that manages Students, Courses, and their Enrollments. This system supports CRUD operations, enrollment tracking, and many-to-many relationships between students and courses.

---

## 🛠 Features

- Create, Read, Update, Delete (CRUD) for:
  - Students
  - Courses
  - Enrollments
- Many-to-many relationships between Students and Courses
- Automatic enrollment handling
- Referential integrity via PostgreSQL foreign keys
- Course capacity and cost tracking
- UUID-based primary keys for unique identification

---

## 📦 Tech Stack

- **Java 17+**
- **PostgreSQL**
- **JDBC**

---

## 🗃️ Database Schema

### 🧑 Students
| Column     | Type         | Description                   |
|------------|--------------|-------------------------------|
| ID         | UUID         | Primary Key                   |
| FirstName  | VARCHAR(50)  | Required                      |
| LastName   | VARCHAR(50)  | Optional                      |
| Age        | INTEGER      | Required                      |
| Balance    | DECIMAL      | Account balance               |
| Courses    | UUID[]       | Array of Course UUIDs         |

### 📚 Courses
| Column           | Type         | Description                       |
|------------------|--------------|-----------------------------------|
| ID               | UUID         | Primary Key                       |
| Name             | VARCHAR(255) | Required                          |
| Teacher          | VARCHAR(255) | Optional                          |
| Subject          | VARCHAR(255) | Required                          |
| MaxNumberOfSeats | INTEGER      | Course capacity                   |
| Cost             | DECIMAL      | Enrollment cost                   |

### 📋 Enrollments
| Column     | Type | Description |
|------------|------|-------------|
| ID         | SERIAL | Primary Key |
| STUDENTID  | UUID | Foreign Key (Students.ID) |
| COURSEID   | UUID | Foreign Key (Courses.ID) |

> ✅ Unique constraint: `(STUDENTID, COURSEID)` to prevent duplicate enrollments  
> 🔁 Cascading delete to automatically clean up enrollments if a course or student is deleted

---

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/DEVMorningCoffee/Student-Mangement-System.git
cd Student-Mangement-System
```

### 2. Set Up PostgreSQL

- Create a database (e.g. `enrollment_db`)
- Update the connection string in your main application:

```java
String url = "jdbc:postgresql://localhost:5432/enrollment_db";
String user = "your_username";
String password = "your_password";
```

### 3. Run the App

Ensure tables are created via `createStudentsTable()`, `createCoursesTable()`, and `createEnrollmentsTable()` before performing any data operations.

---

## 🧠 Suggested Improvements

- Enforce course seat limits
- Deduct course cost from student balance
- Implement authentication for admin/student roles
- Add CLI or REST API using Spring Boot
- Generate reports or student transcripts

---

## 📁 Project Structure

```
src/
|── Main/
    |── java/
    |── org.example
        ├── Course/
        │   └── Course.java
        │   └── CourseService.java
        ├── Enrollment/
        │   └── EnrollmentService.java
        ├── Student/
        │   └── Student.java
        │   └── StudentService.java
        └── Main.java
```

---

## 🤝 Contributions

Pull requests are welcome! Feel free to open issues or suggest new features.

---

## 📜 License

This project is open source and available under the [MIT License](LICENSE).

```

---

Let me know if you want to include usage examples, a logo/banner, or if you're planning to push it to GitHub—I can tailor it even more!