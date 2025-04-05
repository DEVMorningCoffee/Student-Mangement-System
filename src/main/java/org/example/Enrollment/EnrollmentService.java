package org.example.Enrollment;

import org.example.Course.Course;
import org.example.Student.Student;
import org.example.Student.StudentService;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;


public class EnrollmentService {

    private final Connection connection;
    private StudentService studentService;

    public EnrollmentService(Connection connection) {
        this.connection = connection;
    }

    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    public void createEnrollmentsTable() throws SQLException {
        String createEnrollmentTableSQL = """
                CREATE TABLE IF NOT EXISTS ENROLLMENTS(
                	ID SERIAL NOT NULL PRIMARY KEY,
                	STUDENTID UUID NOT NULL,
                	COURSEID UUID NOT NULL,
                    CONSTRAINT fk_student FOREIGN KEY (STUDENTID) REFERENCES STUDENTS(ID) ON DELETE CASCADE,
                    CONSTRAINT fk_course FOREIGN KEY (COURSEID) REFERENCES COURSES(ID) ON DELETE CASCADE,
                    UNIQUE (STUDENTID, COURSEID));
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
                INSERT INTO ENROLLMENTS(STUDENTID, COURSEID) VALUES (?, ?) 
                                        ON CONFLICT (STUDENTID, COURSEID) DO NOTHING;;
                """;

        try(PreparedStatement stmt = connection.prepareStatement(addEnrollmentToTableSQL)){
            stmt.setObject(1, UUID.fromString(student.getId()), java.sql.Types.OTHER);
            stmt.setObject(2, UUID.fromString(course.getId()), java.sql.Types.OTHER);

            int rowAffected = stmt.executeUpdate();

            if(rowAffected < 0){
                throw new RuntimeException("Student already is already enrolled");
            }

            System.out.println("Enrollment table added");
        }catch (SQLException e){
            throw new RuntimeException("Couldn't add Enrollment to table", e);
        }
    }


    // Remove if student delete their account
    public void removeEnrollmentFromTableDueToStudent(Student student) throws SQLException {
        String removeEnrollmentFromTableSQL = """
                DELETE FROM ENROLLMENTS WHERE STUDENTID = ?;
                """;

        try(PreparedStatement stmt = connection.prepareStatement(removeEnrollmentFromTableSQL)){
            stmt.setObject(1, UUID.fromString(student.getId()), java.sql.Types.OTHER);

            int rowAffected = stmt.executeUpdate();
            System.out.println(rowAffected + " Enrollment table removed");
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

            int rowAffected = stmt.executeUpdate();
            System.out.println(rowAffected + " Enrollment table removed");
        }catch (SQLException e){
            throw new RuntimeException("Couldn't remove Enrollment from table", e);
        }

    }

    public ArrayList<Student> getAllMatchingCourseFromEnrollmentTable(Course course) throws SQLException {
        String getAllMatchingCourseSQL = """
                SELECT STUDENTID FROM ENROLLMENTS WHERE COURSEID = ?;
                """;

        ArrayList<Student> students = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(getAllMatchingCourseSQL)) {
            stmt.setObject(1, UUID.fromString(course.getId()), java.sql.Types.OTHER);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Student student = studentService.getStudentFromTable(rs.getString("STUDENTID"));
                    students.add(student);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't get all matching students for the course", e);
        }

        System.out.println("Found " + students.size() + " students enrolled in the course.");
        return students;
    }
}
