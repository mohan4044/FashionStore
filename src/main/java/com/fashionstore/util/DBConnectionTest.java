package com.fashionstore.util;
import java.sql.Connection;

import com.fashionstore.util.DBConnection;

public class DBConnectionTest {

    public static void main(String[] args) {

        try {
            Connection connection = DBConnection.getConnection();

            if (connection != null && !connection.isClosed()) {
                System.out.println("Database connection successful!");
            }

            connection.close();

        } catch (Exception e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
    }
}