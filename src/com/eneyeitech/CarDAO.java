package com.eneyeitech;

import java.sql.ResultSet;
import java.util.Collection;

public interface CarDAO {
    public int insertCar(String name, int id);
    public boolean deleteCar(Car car);
    public Car findCar(int id);
    public boolean updateCar(Car car);
    public ResultSet selectCarsRS();
    public Collection selectCarsTO(int search);
}
