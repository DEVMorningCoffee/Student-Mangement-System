package org.example.Course;

import org.example.Database.PostgresSQLDatabase;
import org.example.Enrollment.EnrollmentService;
import org.example.Student.Student;
import org.example.Student.StudentService;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class CourseService {

    Connection connection;

    public CourseService(Connection connection) {
        this.connection = connection;
    }

    public void createCoursesTable() {
        String createCourseTableSQL = """
        CREATE TABLE IF NOT EXISTS Courses (
            ID UUID NOT NULL PRIMARY KEY, 
            Name VARCHAR(255) NOT NULL, 
            Teacher VARCHAR(255), 
            Subject VARCHAR(255) NOT NULL, 
            MaxNumberOfSeats INT NOT NULL, 
            Cost DECIMAL(10,2) NOT NULL
        )
    """;

        try(Statement stmt = connection.createStatement()){

            stmt.execute(createCourseTableSQL);

            stmt.close();

            System.out.println("Course table created successfully");
        }catch(SQLException e){
            throw new RuntimeException("Course table creation failed",e);
        }

    }

    public void addCourseToTable(Course course) throws SQLException{
        String addCourseToTableSQL = """
                INSERT INTO Courses (ID, NAME, Teacher, Subject, MaxNumberOfSeats, Cost)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try(PreparedStatement stmt = connection.prepareStatement(addCourseToTableSQL)){

            stmt.setObject(1, course.getId(), java.sql.Types.OTHER);
            stmt.setString(2, course.getName());
            stmt.setString(3, course.getTeacher());
            stmt.setString(4, course.getSubject());
            stmt.setInt(5, course.getMaxNumberOfSeats());
            stmt.setDouble(6, course.getCost());
            stmt.executeUpdate();

            stmt.close();

            System.out.println("Course added");

        }catch(SQLException e){
            throw new RuntimeException("Course adding failed",e);
        }
    }

    public Course getCourseFromTable(String id) throws SQLException{
        String getCourseFromTableSQL = """
                SELECT * FROM COURSES WHERE ID = ?
                """;

        try(PreparedStatement stmt = connection.prepareStatement(getCourseFromTableSQL)){
            stmt.setObject(1, UUID.fromString(id));

            ResultSet rs = stmt.executeQuery();

            Course course = new Course();

            while(rs.next()){
                course.setId(rs.getString("Id"));
                course.setName(rs.getString("Name"));
                course.setTeacher(rs.getString("Teacher"));
            }

            rs.close();
            stmt.close();

            System.out.println("Course retrieved");

            return course;
        }catch(SQLException e){
            throw new RuntimeException("Error retrieving course from table", e);
        }

    }

    public void updateCourseFromTable(Course course) throws SQLException{
        String updateCourseFromTableSQL = """
                UPDATE COURSES SET NAME=?,
                		TEACHER=?, SUBJECT=?,
                		MAXNUMBEROFSEATS=?,
                		COST=?
                		WHERE ID=?;
                """;

        try(PreparedStatement stmt = connection.prepareStatement(updateCourseFromTableSQL)){
            stmt.setObject(6, UUID.fromString(course.getId()), java.sql.Types.OTHER);

            stmt.setString(1, course.getName());
            stmt.setString(2, course.getTeacher());
            stmt.setString(3, course.getSubject());
            stmt.setInt(4, course.getMaxNumberOfSeats());
            stmt.setDouble(5, course.getCost());

            stmt.executeUpdate();

            stmt.close();

            System.out.println("Course updated");
        }catch(SQLException e){
            throw new RuntimeException("Course update failed", e);
        }
    }

    public void removeCourseFromTable(Course course) throws SQLException {
        String removeCourseFromTableSQL = """
            DELETE FROM COURSES WHERE ID = ?
            """;

        try (PreparedStatement stmt = connection.prepareStatement(removeCourseFromTableSQL)) {

            stmt.setObject(1, course.getId(), java.sql.Types.OTHER);
            stmt.executeUpdate();
            stmt.close();

            // Remove course from student
            StudentService studentService = new StudentService(connection);
            studentService.removeCourseFromStudentTable(course);

            // Remove many to many connection
            EnrollmentService enrollmentService = new EnrollmentService(connection);
            enrollmentService.removeEnrollmentFromTableDueToCourse(course);

            System.out.println("Course deleted");
        } catch (SQLException e) {
            throw new RuntimeException("Course delete failed", e);
        }
    }

    public ArrayList<Student> getAllStudentFromCourse(Course course) throws SQLException{
        try{
            EnrollmentService enrollmentService = new EnrollmentService(connection);

            System.out.println("All students retrieved");

            return enrollmentService.getAllMatchingCourseFromEnrollmentTable(course);
        }catch (SQLException e){
            throw  new RuntimeException("All students retrieved failed", e);
        }
    }
}
