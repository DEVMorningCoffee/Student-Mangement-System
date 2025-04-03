package org.example;

import org.example.Course.Course;
import org.example.Course.CourseService;
import org.example.Database.PostgresSQLDatabase;
import org.example.Student.Student;
import org.example.Student.StudentService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Course math_course = new Course("Calculus 1", "Math", "Kennedy Bravo");
        Course science_course = new Course("Physics 1", "Science", "Michelle Ferguson");
        Course history_course = new Course("Ancient Civilization", "History", "Rowan Zimmerman");
        Course english_course = new Course("Reading & Research", "English", "Layla Stephenson");

        ArrayList<UUID> courses = new ArrayList<>();
        courses.add(math_course.getIDFromUUID());
        courses.add(science_course.getIDFromUUID());
        courses.add(history_course.getIDFromUUID());
        courses.add(english_course.getIDFromUUID());


        Student student = new Student("Jeffrey", "Abraham", 21, 600.29,
                courses);

        try{
            PostgresSQLDatabase.createConnection();

            Connection db = new PostgresSQLDatabase().getConnection();

            CourseService courseService = new CourseService(db);
            StudentService studentService = new StudentService(db);

            // Create the Courses Table
            courseService.createCoursesTable();

            courseService.addCourseToTable(english_course);
            courseService.addCourseToTable(science_course);
            courseService.addCourseToTable(math_course);
            courseService.addCourseToTable(history_course);

            // Create the Students table
            studentService.createStudentsTable();

            // Add Student to Students Table
            studentService.addStudentToTable(student);

            // Retrive Student from Table
            Student getStudent = studentService.getStudentFromTable("e3a1a3f2-70ea-45bf-837b-0699059be7c8");

            // Retrive Courses from Table
            Course getCourse = courseService.getCourseFromTable("2a7829af-cc9d-4fe2-bafc-a13db6dc8c11");

            courseService.removeCourseFromTable("9f59d92e-e4b1-42b3-b00b-35afc35a77e9");

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    public static Optional<Student> findUserChoice(String uuid, ArrayList<Student> students){
        Optional<Student> optionalStudent = students.stream().filter(s -> s.getId().equals(uuid)).findFirst();

        if(optionalStudent.isPresent()){
            return optionalStudent;
        }else{
            System.out.println("Student not found");
        }

        return Optional.empty();
    }

    public static String userInput(String question){
        Scanner scanner = new Scanner(System.in);
        System.out.print(question);
        return scanner.nextLine();
    }
}