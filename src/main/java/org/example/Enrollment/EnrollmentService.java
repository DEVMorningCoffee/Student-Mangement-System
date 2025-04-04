package org.example.Enrollment;

import org.example.Course.Course;
import org.example.Course.CourseService;
import org.example.Student.Student;
import org.example.Student.StudentService;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
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
                	STUDENTID UUID NOT NULL,
                	COURSEID UUID NOT NULL);
                
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
                INSERT INTO ENROLLMENTS(STUDENTID, COURSEID) VALUES (?, ?);
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
                DELETE FROM ENROLLMENTS WHERE STUDENTID = ? AND COURSEID = ?;
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

    // Remove if student delete their account
    public void removeEnrollmentFromTableDueToStudent(Student student) throws SQLException {
        String removeEnrollmentFromTableSQL = """
                DELETE FROM ENROLLMENTS WHERE STUDENTID = ?;
                """;

        try(PreparedStatement stmt = connection.prepareStatement(removeEnrollmentFromTableSQL)){
            stmt.setObject(1, UUID.fromString(student.getId()), java.sql.Types.OTHER);

            stmt.executeUpdate();
            stmt.close();

            System.out.println("Enrollment table removed");
        }catch (SQLException e){
            throw new RuntimeException("Couldn't remove Enrollment from table", e);
        }
    }

    // Remove if course is deleted
    public void removeEnrollmentFromTableDueToCourse(Course course) throws SQLException {
        String removeEnrollmentFromTableSQL = """
                DELETE FROM ENROLLMENTS WHERE COURSEID = ?;
                """;

        try(PreparedStatement stmt = connection.prepareStatement(removeEnrollmentFromTableSQL)){
            stmt.setObject(1, UUID.fromString(course.getId()), java.sql.Types.OTHER);

            stmt.executeUpdate();
            stmt.close();

            System.out.println("Enrollment table removed");
        }catch (SQLException e){
            throw new RuntimeException("Couldn't remove Enrollment from table", e);
        }
    }

    public ArrayList<Student> getAllMatchingCourseFromEnrollmentTable(Course course) throws SQLException {
        String getAllMatchingCourseFromEnrollmentTableSQl = """
                SELECT * FROM ENROLLMENTS WHERE COURSEID = ?;
                """;
        try(PreparedStatement stmt = connection.prepareStatement(getAllMatchingCourseFromEnrollmentTableSQl)){
            stmt.setObject(1, UUID.fromString(course.getId()), java.sql.Types.OTHER);

            ResultSet rs = stmt.executeQuery();

            ArrayList<Student> students = new ArrayList<>();

            StudentService studentService = new StudentService(connection);

            while(rs.next()){
                Student student = studentService.getStudentFromTable(rs.getString("STUDENTID"));

                students.add(student);
            }

            rs.close();
            stmt.close();

            System.out.println("Enrollment table found");

            return students;

        }catch (SQLException e){
            throw new RuntimeException("Couldn't get all matching course", e);
        }
    }
}
