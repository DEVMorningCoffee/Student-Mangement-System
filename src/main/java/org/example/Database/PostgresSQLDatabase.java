package org.example.Database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class PostgresSQLDatabase {

    private static String URL;
    private static String USER;
    private static String PASSWORD;
    private static String DRIVER;

    static {
        try{
            InputStream dbProperties = PostgresSQLDatabase.class.getClassLoader().getResourceAsStream("database.properties");

            if(dbProperties == null){
                throw new RuntimeException("Unable to load database properties file");
            }
            Properties properties = new Properties();
            properties.load(dbProperties);

            URL = properties.getProperty("db.url");
            USER = properties.getProperty("db.user");
            PASSWORD = properties.getProperty("db.password");
            DRIVER = properties.getProperty("db.driver");

            System.out.println(URL);

            Class.forName(DRIVER);

        }catch(IOException | ClassNotFoundException e){
            throw new RuntimeException("Database initialization failed",e);
        }
    }

    public static Connection createConnection() throws SQLException{
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void close(Connection conn) {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            throw new RuntimeException("Database couldn't close properly.", e);
        }
    }




}
