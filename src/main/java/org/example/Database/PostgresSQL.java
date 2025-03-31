package org.example.Database;

import org.example.Course.Course;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgresSQL {

    private Connection conn;
    private Statement stmt;

    public void createConnection(){
        try{

            String url = "jdbc:postgresql://localhost:5432/Student Management System";
            String user = "postgres";
            String password = "2001";

             conn = DriverManager.getConnection(url, user, password);

             stmt = conn.createStatement();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void createCourseTable() {
        try{
            String createCourseTableSQL = "CREATE TABLE IF NOT EXISTS Courses(UUID UUID NOT NULL  PRIMARY KEY, Name VARCHAR(255) NOT NULL, " +
                    "Teacher VARCHAR(255), Subject VARCHAR(255) NOT NULL, MaxNumberOfSeats INT NOT NULL, Cost FLOAT NOT NULL)";

            stmt.execute(createCourseTableSQL);

            System.out.println("Course table created");
        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    public void closeConnection(){
        try{
            conn.close();
            stmt.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
