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

        Student student = new Student("Jeffrey", "Abraham", 21, 600.29,
                UUID.fromString(english_course.getId()));

        try{
            PostgresSQLDatabase.createConnection();

            Connection db = new PostgresSQLDatabase().getConnection();

            CourseService courseService = new CourseService(db);
            StudentService studentService = new StudentService(db);

            // Create the Courses Table
            courseService.createCoursesTable();

            // Create the Students table
            studentService.createStudentsTable();

            PostgresSQLDatabase.close(db);

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