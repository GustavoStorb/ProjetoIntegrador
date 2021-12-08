package com.gustavostorb.projetointegrador1.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Gustavo
 */
public class Database {
    
    private static final String URL = "jdbc:mysql://localhost:3306/faculdade";
    private static final String USER = "root";
    private static final String PASSWORD = "B@guera123";

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (Database.connection == null) {
            Database.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }

        return Database.connection;
    } 
    
}
