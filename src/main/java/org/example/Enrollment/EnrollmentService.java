package org.example.Enrollment;

import org.example.Course.Course;
import org.example.Course.CourseService;
import org.example.Student.Student;
import org.example.Student.StudentService;

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

    public void removeEnrollmentFromTable(Student student, Course course) throws SQLException {
        String removeEnrollmentFromTableSQL = """
                DELETE FROM ENROLLMENTS WHERE STUDENT_ID = ? AND COURSE_ID = ?;
                """;

        try(PreparedStatement stmt = connection.prepareStatement(removeEnrollmentFromTableSQL)){
            stmt.setObject(1, UUID.fromString(student.getId()), java.sql.Types.OTHER);
            stmt.setObject(2, UUID.fromString(course.getId()), java.sql.Types.OTHER);

            // Remove Course from Student
            StudentService studentService = new StudentService(connection);
            studentService.removeCourseFromStudentTable(course);

            stmt.executeUpdate();
            stmt.close();

            System.out.println("Enrollment table removed");
        }catch (SQLException e){
            throw new RuntimeException("Couldn't remove Enrollment from table", e);
        }
    }
}
