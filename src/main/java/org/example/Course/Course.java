package org.example.Course;

public class Course {
    String uuid;
    String name;
    final int maxNumberOfSeats = 50;
    final double cost = 600;

    public Course(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
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
