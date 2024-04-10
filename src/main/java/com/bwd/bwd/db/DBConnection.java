package com.bwd.bwd.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class DBConnection {

    @PersistenceContext
    EntityManager entityManager;
    
    @Value("${spring.datasource.url}")
    private String uriName;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverName;

    Connection con;

    public void connectDB() {
        try {
            if (con == null) {
                Class.forName(driverName);
                con = DriverManager.getConnection(uriName, username, password);
            }
        } catch (SQLException se) {
            se.printStackTrace(System.err);
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace(System.err);
        }
    }

    public Connection getConnection() {
        connectDB();
        return con;
    }

    public void disconnectDB() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException se) {
            se.printStackTrace(System.err);
        }
    }

    public static void main(String[] args) {
        DBConnection dbc = new DBConnection();
        dbc.connectDB();
    }
}
