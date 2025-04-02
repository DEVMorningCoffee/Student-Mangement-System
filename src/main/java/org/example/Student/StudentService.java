package org.example.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class StudentService {
    Connection connection;

    public StudentService(Connection connection) {
        this.connection = connection;
    }

    public void createStudentsTable() throws SQLException {
        String createStudentsTableSQL = """
                CREATE TABLE IF NOT EXISTS STUDENTS (
                    ID UUID PRIMARY KEY NOT NULL,
                    FIRSTNAME VARCHAR(50) NOT NULL,
                    LASTNAME VARCHAR(50),\s
                    AGE INTEGER NOT NULL,
                    BALANCE DECIMAL(10,2) NOT NULL,
                    COURSES UUID[] DEFAULT '{}'::UUID[]
                );
                """;

        try(Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createStudentsTableSQL);

            System.out.println("Student table created");
        }catch(SQLException e) {
            throw new RuntimeException("Error creating student table", e);
        }
    }

    public void addStudentToTable(Student student) throws SQLException {
        String getStudentFromTableSQL = """
                INSERT INTO STUDENTS(ID,FIRSTNAME,LASTNAME,AGE,BALANCE,COURSES)
                VALUES (?,?,?,?,?,?)
                """;

        try(PreparedStatement stmt = connection.prepareStatement(getStudentFromTableSQL)) {

            stmt.setObject(1, student.getId(), java.sql.Types.OTHER);
            stmt.setObject(2, student.getFirstName(), java.sql.Types.VARCHAR);
            stmt.setObject(3, student.getLastName(), java.sql.Types.VARCHAR);
            stmt.setObject(4, student.getAge(), java.sql.Types.INTEGER);
            stmt.setObject(5, student.getBalance(), java.sql.Types.FLOAT);

            // Convert Java ArrayList to Array
            // SQL cannot cast java.util.ArrayList to Types.Array

            UUID[] coursesArray = student.getCourses().toArray(new UUID[0]);

            stmt.setObject(6, coursesArray, java.sql.Types.ARRAY);

            stmt.executeUpdate();

            System.out.println("Student table added");

        }catch(SQLException e) {
                throw new RuntimeException("Student adding failed", e);
        }
    }

}
