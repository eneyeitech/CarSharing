package com.eneyeitech;

// H2CompanyDAO implementation of the
// CompanyDAO interface. This class can contain all
// H2 specific code and SQL statements.
// The client is thus shielded from knowing
// these implementation details.

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class H2CompanyDAO implements CompanyDAO{


    public H2CompanyDAO() {
        // initialization
    }

    // The following methods can use
    // H2DAOFactory.createConnection()
    // to get a connection as required

    @Override
    public int insertCompany(String name) {
        // Implement insert company here.
        // Return newly created company number
        // or a -1 on error

        try (Statement statement = H2DAOFactory.createConnection().createStatement();){
            String query = String.format("INSERT INTO COMPANY (NAME) VALUES ('%s')", name);
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return 0;
    }

    @Override
    public boolean deleteCompany(Company company) {
        // Implement delete company here
        // Return true on success, false on failure
        return false;
    }

    @Override
    public Company findCompany(int id) {
        // Implement find a company here using supplied
        // argument values as search criteria
        // Return a Transfer Object if found,
        // return null on error or if not found

        try (Statement statement = H2DAOFactory.createConnection().createStatement();){
            String query = String.format("SELECT * FROM COMPANY WHERE ID = %d", id);
            ResultSet rs = statement.executeQuery(query);
            Company company = null;
            if (!rs.next() ) {

            } else {
                do {
                    company = new Company();
                    int i = rs.getInt("ID");
                    String name = rs.getString("NAME");
                    company.setName(name);
                    company.setId(i);
                    return company;
                } while (rs.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean updateCompany(Company company) {
        // implement update record here using data
        // from the companyData Transfer Object
        // Return true on success, false on failure or
        // error
        return false;
    }

    @Override
    public ResultSet selectCompaniesRS() {
        // implement search companies here using the
        // supplied criteria.
        // Return a RowSet
        return null;
    }

    @Override
    public Collection selectCompaniesTO() {
        // implement search companies here using the
        // supplied criteria.
        // Alternatively, implement to return a Collection
        // of Transfer Objects.
        Collection collection = new ArrayList();
        try (Statement statement = H2DAOFactory.createConnection().createStatement();){
            String query = String.format("SELECT * FROM COMPANY ORDER BY ID ASC");
            ResultSet rs = statement.executeQuery(query);
            Company company = null;
            if (!rs.next() ) {

            } else {
                do {
                    company = new Company();
                    int i = rs.getInt("ID");
                    String name = rs.getString("NAME");
                    company.setName(name);
                    company.setId(i);
                    collection.add(company);
                } while (rs.next());
                return collection;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
