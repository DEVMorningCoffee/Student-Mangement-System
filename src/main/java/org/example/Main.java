package org.example;

import org.example.Course.Course;
import org.example.Course.CourseService;
import org.example.Database.PostgresSQLDatabase;
import org.example.Enrollment.EnrollmentService;
import org.example.Input.InputHelper;
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

        ArrayList<Course> courses = new ArrayList<>();
        courses.add(math_course);
        courses.add(science_course);
        courses.add(history_course);
        courses.add(english_course);

        try{
            Connection db = new PostgresSQLDatabase().getConnection();
            StudentService studentService = new StudentService(db);
            EnrollmentService enrollmentService = new EnrollmentService(db);
            CourseService courseService = new CourseService(db);

            enrollmentService.setStudentService(studentService);
            courseService.setServices(studentService, enrollmentService);
            studentService.setServices(enrollmentService, courseService);

            // Create table
            courseService.createCoursesTable();
            studentService.createStudentsTable();
            enrollmentService.createEnrollmentsTable();

//            courseService.addCourseToTable(courses);

            while(true){
                System.out.println();
                System.out.println();
                System.out.println();

                String[] userChoices = {"Add Student", "Add Course", "Enroll Student", "View Student Info", "Exit"};

                for(int i = 0; i < userChoices.length; i++){
                    System.out.println(i + 1  + ". " + userChoices[i]);
                }

                System.out.println();

                int choice = Integer.parseInt(userInput("What's your choice: "));

                switch(choice){
                    case 1:
                        String studentFirstName = InputHelper.getString("Please enter student first name: ");
                        String studentLastName = InputHelper.getString("Please enter student last name: ");
                        int studentAge = InputHelper.getInt("Please enter student age: ");
                        double studentBalance = InputHelper.getDouble("Please enter student balance: ");

                        Student student = new Student(studentFirstName, studentLastName, studentAge, studentBalance);
                        studentService.addStudentToTable(student);

                        break;


                    case 2:
                        ArrayList<Course> courseFromTable = courseService.getAllCourseFromTable();

                        System.out.println("Please pick a course.");

                        for(int i = 0; i < courseFromTable.size(); i++){
                            System.out.println(i + 1  + ". " + courseFromTable.get(i).getName());
                        }

                        String studentID = userInput("Please enter student ID: ");
                        Student student1 = studentService.getStudentFromTable(studentID);

                        int courseChoice = Integer.parseInt(userInput("What's your choice: "));
                        Course course1 = courseFromTable.get(courseChoice - 1);

                        studentService.addCourseToStudentTable(course1, student1);

                        break;
                }
            }

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