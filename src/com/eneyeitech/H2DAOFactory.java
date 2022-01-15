package com.eneyeitech;

// H2 concrete DAO Factory implementation

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2DAOFactory  extends DAOFactory{
    public static final String DRIVER = "org.h2.Driver";
    public static final String DBURL = "jdbc:h2:file:./src/carsharing/db/carsharing";

    // method to create H2 connections
    public static Connection createConnection() {
        // Use DRIVER and DBURL to create a connection
        // Recommend connection pool implementation/usage

        Connection connection = null;
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(DBURL);
            connection.setAutoCommit(true);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public CompanyDAO getCompanyDAO() {
        // H2CompanyDAO implements CompanyDAO
        return new H2CompanyDAO();
    }

    // H2CarDAO implements CarDAO
    @Override
    public CarDAO getCarDAO() {
        return new H2CarDAO();
    }

    // H2CustomerDAO implements CarDAO
    @Override
    public CustomerDAO getCustomerDAO() {
        return new H2CustomerDAO();
    }
}
