package org.example.Student;

import org.example.Course.Course;

import java.util.ArrayList;

public class Student {
    String uuid;
    String firstName;
    String lastName;
    int age;
    double balance;
    ArrayList<Course> courses = new ArrayList<>();

    Student(String uuid, String firstName, String lastName, int age, double balance) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.balance = balance;
    }

    public Student() {}

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String addCourse(Course course){
        courses.add(course);

        return "Successfully added: " + course.toString();
    }

    public String removeCourse(Course course){
        courses.remove(course);

        return "Successfully remove: " + course.toString();
    }

    @Override
    public String toString() {
        return "Student{" +
                "uuid='" + uuid + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", balance=" + balance +
                '}';
    }
}
