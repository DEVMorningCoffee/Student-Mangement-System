package org.example.Course;

import org.example.Enrollment.EnrollmentService;
import org.example.Student.Student;
import org.example.Student.StudentService;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class CourseService {

    private final Connection connection;
    private StudentService studentService;
    private EnrollmentService enrollmentService;

    public CourseService(Connection connection) {
        this.connection = connection;
    }

    public void setServices(StudentService studentService, EnrollmentService enrollmentService) {
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
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
            System.out.println("Course added");
        }catch(SQLException e){
            throw new RuntimeException("Course adding failed",e);
        }
    }

    public void addCourseToTable(ArrayList<Course> courseArrayList) throws SQLException{
        String addCourseToTableSQL = """
                INSERT INTO Courses (ID, NAME, Teacher, Subject, MaxNumberOfSeats, Cost)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try(PreparedStatement stmt = connection.prepareStatement(addCourseToTableSQL)){

            for(Course course: courseArrayList){
                stmt.setObject(1, course.getId(), java.sql.Types.OTHER);
                stmt.setString(2, course.getName());
                stmt.setString(3, course.getTeacher());
                stmt.setString(4, course.getSubject());
                stmt.setInt(5, course.getMaxNumberOfSeats());
                stmt.setDouble(6, course.getCost());

                stmt.executeUpdate();
            }

            System.out.println("Course added");
        }catch(SQLException e){
            throw new RuntimeException("Course adding failed",e);
        }
    }

    public Course getCourseFromTable(String id) throws SQLException {
        String sql = "SELECT * FROM Courses WHERE ID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, UUID.fromString(id), java.sql.Types.OTHER);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Course course = new Course();
                    course.setId(rs.getString("ID"));
                    course.setName(rs.getString("Name"));
                    course.setTeacher(rs.getString("Teacher"));
                    course.setSubject(rs.getString("Subject"));
                    course.setMaxNumberOfSeats(rs.getInt("MaxNumberOfSeats"));
                    course.setCost(rs.getDouble("Cost"));

                    System.out.println("Retrieved course: " + course.getName());
                    return course;
                } else {
                    throw new RuntimeException("Course with ID " + id + " not found.");
                }
            }
        }
    }

    public ArrayList<Course> getAllCourseFromTable() throws SQLException {
        String sql = "SELECT * FROM Courses";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            ArrayList<Course> courses = new ArrayList<>();

            try (ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                        Course course = new Course();
                        course.setId(rs.getString("ID"));
                        course.setName(rs.getString("Name"));
                        course.setTeacher(rs.getString("Teacher"));
                        course.setSubject(rs.getString("Subject"));
                        course.setMaxNumberOfSeats(rs.getInt("MaxNumberOfSeats"));
                        course.setCost(rs.getDouble("Cost"));

                        courses.add(course);
                }
                return courses;

            }catch (SQLException e){
                    throw new RuntimeException("No courses found");
            }

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
        String sql = "DELETE FROM Courses WHERE ID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, UUID.fromString(course.getId()), java.sql.Types.OTHER);
            stmt.executeUpdate();

            enrollmentService.removeEnrollmentFromTableDueToCourse(course);
            studentService.removeCourseFromStudentTable(course);

            System.out.println("Course deleted: " + course.getName());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete course.", e);
        }
    }

    public ArrayList<Student> getAllStudentFromCourse(Course course) throws SQLException {
        try {
            ArrayList<Student> students = enrollmentService.getAllMatchingCourseFromEnrollmentTable(course);

            System.out.println("Found " + students.size() + " students enrolled in course: " + course.getName());

            return students;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve students for course.", e);
        }
    }
}
