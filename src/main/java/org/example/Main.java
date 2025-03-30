package org.example;

import org.example.Student.Student;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Student student;

        ArrayList<Student> students = new ArrayList<>();

        Optional<Student> findStudent;
        String uuid;

        while(true){
            String[] choices = {"1: Add Student","2: Get Student", "3: Update Student"};
            for(String choice: choices){
                System.out.println(choice);
            }


            // Get User input
            Scanner scanner = new Scanner(System.in);
            System.out.print("Please enter your choice: ");

            int userChoice = Integer.parseInt(scanner.nextLine());

            switch(userChoice){
                case 1:
                    String firstName = userInput("What's the firstname: ");
                    String lastName = userInput("What's the lastname: ");
                    double balance = Double.parseDouble(userInput("What's the balance: "));
                    int age = Integer.parseInt(userInput("What's the age: "));

                    student = new Student(firstName, lastName, age, balance);

                    students.add(student);

                    System.out.println(student);

                    break;
                case 2:
                    uuid = userInput("What's the uuid: ");

                    findStudent = findUserChoice(uuid, students);

                    if(findStudent.isPresent()){
                        System.out.println(findStudent);
                    }

                    break;
                case 3:
                    uuid = userInput("What's the uuid: ");
                    findStudent = findUserChoice(uuid, students);

                    if(findStudent.isEmpty()){
                        break;
                    }

                    String updateFirstName = userInput("What's the firstname: ");
                    String UpdateLastName = userInput("What's the lastname: ");
                    double updateBalance = Double.parseDouble(userInput("What's the balance: "));
                    int updateAge = Integer.parseInt(userInput("What's the age: "));

                    findStudent.get().setFirstName(updateFirstName);
                    findStudent.get().setLastName(UpdateLastName);
                    findStudent.get().setBalance(updateBalance);
                    findStudent.get().setAge(updateAge);

                    break;
            }

        }

    }

    public static Optional<Student> findUserChoice(String uuid, ArrayList<Student> students){
        Optional<Student> optionalStudent = students.stream().filter(s -> s.getUuid().equals(uuid)).findFirst();

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