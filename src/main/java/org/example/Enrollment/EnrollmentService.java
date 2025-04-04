package org.example.Enrollment;

import org.example.Course.Course;
import org.example.Student.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;


public class EnrollmentService {

    Connection connection;

    public EnrollmentService(Connection connection) {
        this.connection = connection;
    }

    public void createEnrollmentsTable() throws SQLException {
        String createEnrollmentTableSQL = """
                CREATE TABLE IF NOT EXISTS ENROLLMENTS(
                	ID SERIAL NOT NULL PRIMARY KEY,
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

    public void addEnrollmentToTable(Student student, Course course) throws SQLException {
        String addEnrollmentToTableSQL = """
                INSERT INTO ENROLLMENTS(STUDENT_ID, COURSE_ID) VALUES (?, ?);
                """;

        try(PreparedStatement stmt = connection.prepareStatement(addEnrollmentToTableSQL)){
            stmt.setObject(1, UUID.fromString(student.getId()), java.sql.Types.OTHER);
            stmt.setObject(2, UUID.fromString(course.getId()), java.sql.Types.OTHER);

            stmt.executeUpdate();
            stmt.close();

            System.out.println("Enrollment table added");
        }catch (SQLException e){
            throw new RuntimeException("Couldn't add Enrollment to table", e);
        }
    }
}
