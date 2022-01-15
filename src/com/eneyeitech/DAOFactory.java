package com.eneyeitech;

// Abstract class DAO Factory

public abstract class DAOFactory {

    // List of DAO types supported by the factory
    public static final int H2 = 1;

    // There will be a method for each DAO that can be
    // created. The concrete factories will have to
    // implement these methods.
    public abstract CompanyDAO getCompanyDAO();
    public abstract CarDAO getCarDAO();
    public abstract CustomerDAO getCustomerDAO();

    public static DAOFactory getDAOFactory(int whichFactory) {
        switch (whichFactory) {
            case H2:
                return new H2DAOFactory();
            default:
                return null;
        }
    }
}
