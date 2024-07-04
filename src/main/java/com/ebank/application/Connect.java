package com.ebank.application;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connect {

    @SuppressWarnings("exports")
    public static Connection connectDB() {
        try {
            Class.forName("org.h2.Driver");
            return DriverManager.getConnection("jdbc:h2:~/bank", "test", "test");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}