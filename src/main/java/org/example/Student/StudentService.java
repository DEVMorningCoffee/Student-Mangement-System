package org.example.Student;

import org.example.Course.Course;
import org.example.Course.CourseService;
import org.example.Enrollment.EnrollmentService;

import java.sql.*;
import java.util.*;

public class StudentService {
    private final Connection connection;
    private EnrollmentService enrollmentService;
    private CourseService courseService;

    public StudentService(Connection connection) {
        this.connection = connection;
    }

    public void setServices(EnrollmentService enrollmentService,CourseService courseService) {
        this.enrollmentService = enrollmentService;
        this.courseService = courseService;
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

            stmt.close();

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

            for(UUID course : coursesArray) {
                enrollmentService.addEnrollmentToTable(student, courseService.getCourseFromTable(course.toString()));
            }

            stmt.executeUpdate();

            stmt.close();

            System.out.println("Student table added");

        }catch(SQLException e) {
                throw new RuntimeException("Student adding failed", e);
        }
    }

    public Student getStudentFromTable(String studentID) throws SQLException {
        String getStudentFromTableSQL = """
                SELECT * FROM STUDENTS\s
                WHERE ID = ?
               \s""";

        try(PreparedStatement stmt = connection.prepareStatement(getStudentFromTableSQL)) {
            stmt.setObject(1, UUID.fromString(studentID), java.sql.Types.OTHER);

            ResultSet rs = stmt.executeQuery();

            Student student = new Student();

            while(rs.next()) {
                student.setId(rs.getString(1));
                student.setFirstName(rs.getString(2));
                student.setLastName(rs.getString(3));
                student.setAge(rs.getInt(4));
                student.setBalance(rs.getFloat(5));

                Array coursesIDsArray = rs.getArray(6);
                UUID[] courseIDs = (UUID[]) coursesIDsArray.getArray();
                ArrayList<UUID> courses = new ArrayList<>(Arrays.asList(courseIDs));
                student.setCourses(courses);

            }

            rs.close();
            stmt.close();

            System.out.println("Student Retrieve");

            return student;


        }catch (SQLException e) {
            throw new RuntimeException("Error getting student from table", e);
        }
    }

    public void updateStudentFromTable(Student student) throws SQLException {
        String updateStudentFromTableSQL = """
                UPDATE students
                SET firstname = ?,
                    lastname = ?,
                    age = ?,
                    balance = ?,
                    courses = ?
                WHERE id = ?;
                """;

        try(PreparedStatement stmt = connection.prepareStatement(updateStudentFromTableSQL)) {
            // Get ID of what student to update
            stmt.setObject(6, UUID.fromString(student.getId()), Types.OTHER);

            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setInt(3, student.getAge());
            stmt.setDouble(4, student.getBalance());

            // Convert Java ArrayList to Array
            // SQL cannot cast java.util.ArrayList to Types.Array
            UUID[] coursesArray = student.getCourses().toArray(new UUID[0]);
            stmt.setObject(5, coursesArray, Types.ARRAY);

            stmt.executeUpdate();
            stmt.close();

            System.out.println("Student table updated");

        }catch (SQLException e){
            throw new RuntimeException("Updating student failed", e);
        }
    }

    public Student removeStudentFromTable(Student student) throws SQLException {
        String removeStudentFromTableSQL = """
                    DELETE FROM STUDENTS WHERE ID = ?;
                """;

        try(PreparedStatement stmt = connection.prepareStatement(removeStudentFromTableSQL)) {


            stmt.setObject(1, UUID.fromString(student.getId()), java.sql.Types.OTHER);

            stmt.executeUpdate();
            stmt.close();

            // Remove many to many connection
            enrollmentService.removeEnrollmentFromTableDueToStudent(student);

            System.out.println("Student table removed");

            return student;

        }catch(SQLException e){
            throw new RuntimeException("Removing student failed", e);
        }
    }

    public void removeCourseFromStudentTable(Course course) throws SQLException {
        String removeCourseFromStudentTableSQL = """
            UPDATE students
            SET courses = array_remove(courses, ?)
            WHERE courses @> ?::UUID[];
            """;

        try(PreparedStatement stmt = connection.prepareStatement(removeCourseFromStudentTableSQL)){

            // First, update students to remove the course from their courses array
            stmt.setObject(1, course.getId(), java.sql.Types.OTHER);

            // Create a SQL Array for UUID[]
            Array uuidArray = connection.createArrayOf("UUID", new UUID[]{course.getIDFromUUID()});
            stmt.setArray(2, uuidArray);

            stmt.executeUpdate();

            System.out.println("Course table removed");

        }catch (SQLException e){
            throw new RuntimeException("Course table failed to removed", e);
        }
    }

    public void addCourseToStudentTable(Course course, Student student) throws SQLException {
        String addCourseStudentTable = """
                SELECT COUNT(*) FROM ENROLLMENTS WHERE COURSEID = ?;
                """;

        try(PreparedStatement stmt = connection.prepareStatement(addCourseStudentTable)) {
            stmt.setObject(1, course.getId(), java.sql.Types.OTHER);

            int coursesCount = stmt.executeUpdate();

            if(coursesCount < course.getMaxNumberOfSeats()) {
                throw new RuntimeException("Course is full");
            }

            if(course.getCost() > student.getBalance()) {
                throw new RuntimeException("Student can't afford course");
            }

            student.addCourseToStudent(UUID.fromString(course.getId()));

            updateStudentFromTable(student);
            enrollmentService.addEnrollmentToTable(student, course);

        }catch (SQLException e){
            throw new RuntimeException("Adding course failed", e);
        }
    }
}
