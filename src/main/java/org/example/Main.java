package org.example;

import org.example.Student.Student;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Student student;

        while(true){
            String[] choices = {"1: Add Student","2: Get Student"};
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

                    System.out.println(student.toString());

                    break;
                case 2:

            }

        }

    }

    public static String userInput(String question){
        Scanner scanner = new Scanner(System.in);
        System.out.print(question);
        return scanner.nextLine();
    }
}