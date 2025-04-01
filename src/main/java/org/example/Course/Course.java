package org.example.Course;

import java.util.UUID;

public class Course {
    String uuid;
    String name;
    String teacher;
    String subject;
    final int maxNumberOfSeats = 50;
    final double cost = 600;

    /*A class must have a name and subject. A class can not have a teacher. */

    public Course(String name, String subject) {
        this.uuid = UUID.randomUUID().toString();
        this.name = name;
        this.subject = subject;
    }

    public Course(String name,String subject, String teacher) {
        this.uuid = UUID.randomUUID().toString();
        this.name = name;
        this.teacher = teacher;
        this.subject = subject;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public double getCost() {
        return cost;
    }

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
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", maxNumberOfSeats=" + maxNumberOfSeats +
                ", cost=" + cost +
                '}';
    }
}
