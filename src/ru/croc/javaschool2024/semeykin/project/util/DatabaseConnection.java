package ru.croc.javaschool2024.semeykin.project.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:h2:~/project";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";
    private static volatile Connection connection;

    private DatabaseConnection()  {
    }

    public static Connection getConnection(){
        if (connection == null){
            synchronized (DatabaseConnection.class){
                if (connection == null){
                    try {
                        connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
                    } catch (SQLException e) {
                        throw new RuntimeException("Error connecting to the database", e);
                    }
                }
            }
        }
        return connection;
    }
}
