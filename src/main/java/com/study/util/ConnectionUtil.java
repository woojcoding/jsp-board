package com.study.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionUtil {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ebrainsoft_study";
    private static final String USER = "ebsoft";
    private static final String PASS = "ebsoft";

    public static Connection getConnection() throws Exception {
        Connection connection = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");

            connection = DriverManager.getConnection(DB_URL, USER, PASS);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }
}
