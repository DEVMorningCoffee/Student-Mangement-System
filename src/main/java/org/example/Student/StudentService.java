package org.example.Student;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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

}
