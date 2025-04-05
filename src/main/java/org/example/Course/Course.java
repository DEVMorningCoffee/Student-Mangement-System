package org.example.Course;

import java.util.UUID;

public class Course {
    String id;
    String name;
    String teacher;
    String subject;
    int maxNumberOfSeats = 50;
    double cost = 600;

    /*A class must have a name and subject. A class can not have a teacher. */

    public Course(String name, String subject) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.subject = subject;
    }

    public Course(String name,String subject, String teacher) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.teacher = teacher;
        this.subject = subject;
    }

    public Course(){}


    public String getId() {
        return id;
    }

    public UUID getIDFromUUID(){
        return UUID.fromString(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxNumberOfSeats() {
        return maxNumberOfSeats;
    }

    public void setMaxNumberOfSeats(int maxNumberOfSeats) {this.maxNumberOfSeats = maxNumberOfSeats;}

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {this.cost = cost;}

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "Course{" +
                "uuid='" + id + '\'' +
                ", name='" + name + '\'' +
                ", maxNumberOfSeats=" + maxNumberOfSeats +
                ", cost=" + cost +
                '}';
    }
}
