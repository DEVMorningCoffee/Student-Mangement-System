package org.example.Enrollment;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class EnrollmentService {

    Connection connection;

    public EnrollmentService(Connection connection) {
        this.connection = connection;
    }

    public void createEnrollmentsTable() throws SQLException {
        String createEnrollmentTableSQL = """
                CREATE TABLE IF NOT EXISTS ENROLLMENTS(
                	ID UUID NOT NULL PRIMARY KEY,
                	STUDENT_ID UUID NOT NULL,
                	COURSE_ID UUID NOT NULL);
                
                """;

        try(Statement stmt = connection.createStatement()){
            stmt.executeUpdate(createEnrollmentTableSQL);
            System.out.println("Enrollment table created");
        }catch(SQLException e){
            throw new RuntimeException("Couldn't create Enrollment table", e);
        }
    }
}
