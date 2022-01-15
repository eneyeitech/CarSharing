package com.eneyeitech;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class H2CarDAO implements CarDAO{
    @Override
    public int insertCar(String name, int id) {
        // Implement insert company here.
        // Return newly created company number
        // or a -1 on error

        try (Statement statement = H2DAOFactory.createConnection().createStatement();){
            String query = String.format("INSERT INTO CAR (NAME, COMPANY_ID) VALUES ('%s', %d)", name, id);
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return 0;
    }

    @Override
    public boolean deleteCar(Car car) {
        return false;
    }

    @Override
    public Car findCar(int id) {
        // Implement find a company here using supplied
        // argument values as search criteria
        // Return a Transfer Object if found,
        // return null on error or if not found

        try (Statement statement = H2DAOFactory.createConnection().createStatement();){
            String query = String.format("SELECT * FROM CAR WHERE ID = %d", id);
            ResultSet rs = statement.executeQuery(query);
            Car car = null;
            if (!rs.next() ) {

            } else {
                do {
                    car = new Car();
                    int i = rs.getInt("ID");
                    int ii = rs.getInt("COMPANY_ID");
                    String name = rs.getString("NAME");
                    car.setName(name);
                    car.setId(i);
                    car.setCompanyId(ii);
                    return car;
                } while (rs.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean updateCar(Car car) {
        return false;
    }

    @Override
    public ResultSet selectCarsRS() {
        return null;
    }

    @Override
    public Collection selectCarsTO(int search) {
        // implement search cars here using the
        // supplied criteria.
        // Alternatively, implement to return a Collection
        // of Transfer Objects.
        Collection collection = new ArrayList();
        try (Statement statement = H2DAOFactory.createConnection().createStatement();){
            String query = String.format("SELECT * FROM CAR WHERE COMPANY_ID = %d ORDER BY ID ASC", search);
            ResultSet rs = statement.executeQuery(query);
            Car car = null;
            if (!rs.next() ) {

            } else {
                do {
                    car = new Car();
                    int i = rs.getInt("ID");
                    int ii = rs.getInt("COMPANY_ID");
                    String name = rs.getString("NAME");
                    car.setName(name);
                    car.setId(i);
                    car.setCompanyId(ii);
                    collection.add(car);
                } while (rs.next());
                return collection;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
