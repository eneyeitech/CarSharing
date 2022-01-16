package com.eneyeitech;

import java.io.File;
import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static int selectedCar = 0;
    private static CompanyDAO companyDAO;
    private static CarDAO carDAO;
    private static CustomerDAO customerDAO;

    private static List<Company> companies;
    private static List<Car> cars;
    private static List<Customer> customers;

    public static void main(String[] args) {

        String search = "-databaseFileName";
        String dbName = "";
        List<String> arguments = new ArrayList<>(Arrays.asList(args));
        if (arguments.contains(search)) {
            dbName = args[arguments.indexOf(search) + 1];
        } else {
            dbName = "test";
        }
        if (FileManager.createPathDirectory("")){
            System.out.println("carsharing/db created");
        }
        // create the required DAO Factory
        DAOFactory h2DaoFactory = DAOFactory.getDAOFactory(DAOFactory.H2);

        // Create a DAO
        companyDAO = h2DaoFactory.getCompanyDAO();
        carDAO = h2DaoFactory.getCarDAO();
        customerDAO = h2DaoFactory.getCustomerDAO();

        if (true) {

            if(true) {
                System.out.println("carsharing/db created");

                customerDAO.dropTable();
                carDAO.dropTable();
                companyDAO.dropTable();

                companyDAO.createTable();
                carDAO.createTable();
                customerDAO.createTable();
            }
        }

        menuControl();



    }

    public static void selection1(String inp) {
        switch (inp) {
            case "1":
                managerMenuControl();
                break;
            case "2":
                System.out.println("Customer list:");
                if (displayCustomers()) {
                    System.out.println("0. Back");
                } else {
                    return;
                }

                String choice = scanner.nextLine();
                if (!choice.equals("0")) {
                    customerMenuControl(choice);
                }
                break;
            case "3":
                System.out.println("Enter the customer name:");
                String name = scanner.nextLine();
                customerDAO.insertCustomer(name);
                System.out.println("The customer was added!");
                break;
            case "0":
            default:
                break;
        }
    }

    public static void selection2(String inp) {
        switch (inp) {
            case "1":
                System.out.println("Choose the company:");
                if (displayCompanies()) {
                    System.out.println("0. Back");
                } else {
                    return;
                }

                String choice = scanner.nextLine();
                if (!choice.equals("0")) {
                    companyMenuControl(choice);
                }
                break;
            case "2":
                System.out.println("Enter the company name:");
                String name = scanner.nextLine();
                companyDAO.insertCompany(name);
                System.out.println("The company was created!");
                break;
            case "0":
            default:
                break;
        }
    }

    public static void selection3(String inp) {
        switch (inp) {
            case "1":
                System.out.println("Choose the company:");
                if (displayCompanies()) {
                    System.out.println("0. Back");
                } else {
                    return;
                }

                String choice = scanner.nextLine();
                if (!choice.equals("0")) {
                    companyMenuControl(choice);
                }
                break;
            case "2":
                System.out.println("Enter the company name:");
                String name = scanner.nextLine();
                companyDAO.insertCompany(name);
                System.out.println("The company was created!");
                break;
            case "0":
            default:
                break;
        }
    }

    public static void selection4(String inp, Company company) {
        switch (inp) {
            case "1":
                System.out.println("Car list:");
                displayCars(company.getId());
                break;
            case "2":
                System.out.println("Enter the car name:");
                String name = scanner.nextLine();
                carDAO.insertCar(name, company.getId());
                System.out.println("The car was added!");
                break;
            case "0":
            default:
                break;
        }
    }

    public static void selection5(String inp, Customer customer) {
        switch (inp) {
            case "1":

                if (customer.getRentedCarId() == 0) {
                    System.out.println("Choose a company:");
                    if (displayCompanies()) {
                        System.out.println("0. Back");
                    } else {
                        return;
                    }

                    String choice = scanner.nextLine();
                    if (!choice.equals("0")) {
                        Company c = companies.get(Integer.valueOf(choice) - 1);
                        if (displayCars(c.getId())) {
                            System.out.println("0. Back");
                        } else {
                            return;
                        }

                        String selection = scanner.nextLine();
                        if (!choice.equals("0")) {
                            Car a = cars.get(Integer.valueOf(selection) - 1);
                            customer.setRentedCarId(a.getId());
                            if (customerDAO.updateCustomer(customer)) {
                                System.out.printf("You rented '%s'", a.getName());
                            } else {
                                return;
                            }
                        }
                    }
                } else {
                    System.out.println("You've already rented a car!");
                }

                break;
            case "2":
                if (customer.getRentedCarId() == 0) {
                    System.out.println("You didn't rent a car!");
                } else {
                    customer.setRentedCarId(0);
                    customerDAO.updateCustomer(customer);
                    System.out.println("You've returned a rented car!");
                }
                break;
            case "3":
                if (customer.getRentedCarId() == 0) {
                    System.out.println("You didn't rent a car!");
                } else {
                    Car car = carDAO.findCar(customer.getRentedCarId());
                    Company company= companyDAO.findCompany(car.getCompanyId());
                    System.out.printf("Your rented car:\n%s\nCompany:\n%s\n", car.getName(), company.getName());
                }
                break;
            case "0":
            default:
                break;
        }
    }

    public static String mainMenuStr() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("1. Log in as a manager\n");
        stringBuilder.append("2. Log in as a customer\n");
        stringBuilder.append("3. Create a customer\n");
        stringBuilder.append("0. Exit\n");
        return stringBuilder.toString();
    }

    public static String managerMenuStr() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n1. Company list\n");
        stringBuilder.append("2. Create a company\n");
        stringBuilder.append("0. Back\n");
        return stringBuilder.toString();
    }

    public static String customerMenuStr(String name) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n1. Rent a car\n");
        stringBuilder.append("2. Return a rented car\n");
        stringBuilder.append("3. My rented car\n");
        stringBuilder.append("0. Back\n");
        return stringBuilder.toString();
    }

    public static String companyMenu(String name) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%s company\n", name));
        stringBuilder.append("1. Car list\n");
        stringBuilder.append("2. Create a car\n");
        stringBuilder.append("0. Back\n");
        return stringBuilder.toString();
    }

    public static void dispayMainMenu() {
        System.out.println(mainMenuStr());
    }
    public static void dispayManagerMenu() {
        System.out.println(managerMenuStr());
    }

    public static void dispayCustomerMenu(String name) {
        System.out.println(customerMenuStr(name));
    }

    public static void dispayCompanyMenu(String name) {
        System.out.println(companyMenu(name));
    }


    public static boolean  displayCompanies(){
        companies = companyDAO.selectCompaniesTO();
        AtomicInteger cc = new AtomicInteger(1);
        if (companies != null && companies.size() > 0) {
            companies.stream().map(c -> String.format("%s. %s", cc.getAndIncrement(), c.getName()))
                    .forEachOrdered(System.out::println);
            return true;
        } else {
            System.out.println("The company list is empty!");
            return false;
        }
    }

    public static boolean  displayCustomers(){
        customers= customerDAO.selectCustomersTO();
        AtomicInteger cc = new AtomicInteger(1);
        if (customers != null && customers.size() > 0) {
            customers.stream().map(c -> String.format("%s. %s", cc.getAndIncrement(), c.getName()))
                    .forEachOrdered(System.out::println);
            return true;
        } else {
            System.out.println("The customer list is empty!");
            return false;
        }
    }

    public static boolean  displayCars(int search){
        cars= carDAO.selectCarsTO(search);
        AtomicInteger cc = new AtomicInteger(1);
        if(cars != null && cars.size() > 0) {
            cars.stream().map(c -> String.format("%s. %s", cc.getAndIncrement(), c.getName()))
                    .forEachOrdered(System.out::println);
            return true;
        } else {
            System.out.println("The car list is empty!");
            return false;
        }
    }

    public static void menuControl() {
        String input = "";
        do {
            dispayMainMenu();
            input = scanner.nextLine();
            selection1(input);
        } while (!input.equals("0"));
    }

    public static void managerMenuControl() {
        String input = "";
        do {
            dispayManagerMenu();
            input = scanner.nextLine();
            selection2(input);
        } while (!input.equals("0"));
    }

    public static void customerMenuControl(String inp) {
        String input = "";
        do {
            Customer customer = customerDAO.findCustomer(Integer.valueOf(inp));
            if (customer != null) {
                dispayCustomerMenu(customer.getName());
                input = scanner.nextLine();
                selection5(input, customer);
            }
        } while (!input.equals("0"));
    }



    public static void companyMenuControl(String inp) {
        String input = "";
        do {
            Company company = companyDAO.findCompany(Integer.valueOf(inp));
            if (company != null) {
                dispayCompanyMenu(company.getName());
                input = scanner.nextLine();
                selection4(input, company);
            }
        } while (!input.equals("0"));
    }
}

class FileManager {
    public static boolean createPathDirectory(String n) {
        String path = "./src/carsharing/db/";
        File f1 = new File(path);
        boolean created = f1.mkdirs();

        return created;
    }
}

class Car {
    private int id;
    private String name;
    private int companyId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}

class Company {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class Customer {
    private int id;
    private String name;
    private int rentedCarId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRentedCarId() {
        return rentedCarId;
    }

    public void setRentedCarId(int rentedCarId) {
        this.rentedCarId = rentedCarId;
    }
}

interface CarDAO {
    public int insertCar(String name, int id);
    public boolean deleteCar(Car car);
    public Car findCar(int id);
    public boolean updateCar(Car car);
    public ResultSet selectCarsRS();
    public ArrayList<Car> selectCarsTO(int search);
    public void createTable();
    public void dropTable();
}

interface CompanyDAO {
    public int insertCompany(String name);
    public boolean deleteCompany(Company company);
    public Company findCompany(int id);
    public boolean updateCompany(Company company);
    public ResultSet selectCompaniesRS();
    public ArrayList<Company> selectCompaniesTO();
    public void createTable();
    public void dropTable();
}

interface CustomerDAO {
    public int insertCustomer(String name);
    public boolean deleteCustomer(Customer customer);
    public Customer findCustomer(int id);
    public boolean updateCustomer(Customer customer);
    public ResultSet selectCustomersRS();
    public ArrayList<Customer> selectCustomersTO();
    public void createTable();
    public void dropTable();
}

abstract class DAOFactory {

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

class H2CarDAO implements CarDAO{
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
    public ArrayList<Car> selectCarsTO(int search) {
        // implement search cars here using the
        // supplied criteria.
        // Alternatively, implement to return a Collection
        // of Transfer Objects.
        ArrayList<Car> collection = new ArrayList();
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

    public void createTable(){
        try (Statement statement = H2DAOFactory.createConnection().createStatement();){
            statement.execute("create table if not exists CAR(" +
                    "ID INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    " NAME VARCHAR UNIQUE NOT NULL," +
                    " COMPANY_ID INTEGER NOT NULL," +
                    " CONSTRAINT FK_COMPANY FOREIGN KEY (COMPANY_ID)" +
                    " REFERENCES COMPANY(ID)" +
                    ")");
            System.out.println("Car table created.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropTable(){
        try (Statement statement = H2DAOFactory.createConnection().createStatement();){
            statement.execute("drop table CAR if exists");
            System.out.println("CAR table dropped.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class H2CompanyDAO implements CompanyDAO{


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
    public ArrayList<Company> selectCompaniesTO() {
        // implement search companies here using the
        // supplied criteria.
        // Alternatively, implement to return a Collection
        // of Transfer Objects.
        ArrayList<Company> collection = new ArrayList();
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

    public void createTable(){
        try (Statement statement = H2DAOFactory.createConnection().createStatement();){
            statement.execute("create table if not exists COMPANY(" +
                    "ID INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    " NAME VARCHAR UNIQUE NOT NULL" +
                    ")");
            System.out.println("Company table created.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void dropTable(){
        try (Statement statement = H2DAOFactory.createConnection().createStatement();){
            statement.execute("drop table COMPANY if exists");
            System.out.println("Company table dropped.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class H2CustomerDAO implements CustomerDAO{
    public H2CustomerDAO() {
        // initialization
    }

    // The following methods can use
    // H2DAOFactory.createConnection()
    // to get a connection as required

    @Override
    public int insertCustomer(String name) {
        // Implement insert company here.
        // Return newly created company number
        // or a -1 on error

        try (Statement statement = H2DAOFactory.createConnection().createStatement();){
            String query = String.format("INSERT INTO CUSTOMER (NAME) VALUES ('%s')", name);
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return 0;
    }

    @Override
    public boolean deleteCustomer(Customer customer) {
        // Implement delete company here
        // Return true on success, false on failure
        return false;
    }

    @Override
    public Customer findCustomer(int id) {
        // Implement find a company here using supplied
        // argument values as search criteria
        // Return a Transfer Object if found,
        // return null on error or if not found

        try (Statement statement = H2DAOFactory.createConnection().createStatement();){
            String query = String.format("SELECT * FROM CUSTOMER WHERE ID = %d", id);
            ResultSet rs = statement.executeQuery(query);
            Customer customer = null;
            if (!rs.next() ) {

            } else {
                do {
                    customer = new Customer();
                    int i = rs.getInt("ID");
                    int ii = rs.getInt("RENTED_CAR_ID");
                    String name = rs.getString("NAME");
                    customer.setName(name);
                    customer.setId(i);
                    customer.setRentedCarId(ii);
                    return customer;
                } while (rs.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        // implement update record here using data
        // from the companyData Transfer Object
        // Return true on success, false on failure or
        // error

        try (Statement statement = H2DAOFactory.createConnection().createStatement();){
            String query = "";
            if (customer.getRentedCarId() == 0) {
                query = String.format("UPDATE CUSTOMER SET RENTED_CAR_ID = NULL WHERE ID = %d", customer.getId());
            }
            else {
                query = String.format("UPDATE CUSTOMER SET RENTED_CAR_ID = %d WHERE ID = %d", customer.getRentedCarId(), customer.getId());
            }
            statement.execute(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public ResultSet selectCustomersRS() {
        // implement search companies here using the
        // supplied criteria.
        // Return a RowSet
        return null;
    }

    @Override
    public ArrayList<Customer> selectCustomersTO() {
        // implement search companies here using the
        // supplied criteria.
        // Alternatively, implement to return a Collection
        // of Transfer Objects.
        ArrayList<Customer> collection = new ArrayList();
        try (Statement statement = H2DAOFactory.createConnection().createStatement();){
            String query = String.format("SELECT * FROM CUSTOMER ORDER BY ID ASC");
            ResultSet rs = statement.executeQuery(query);
            Customer customer = null;
            if (!rs.next() ) {

            } else {
                do {
                    customer = new Customer();
                    int i = rs.getInt("ID");
                    int ii = rs.getInt("RENTED_CAR_ID");
                    String name = rs.getString("NAME");
                    customer.setName(name);
                    customer.setId(i);
                    customer.setRentedCarId(ii);
                    collection.add(customer);
                } while (rs.next());
                return collection;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createTable(){
        try (Statement statement = H2DAOFactory.createConnection().createStatement();){
            statement.execute("create table if not exists CUSTOMER(" +
                    "ID INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    " NAME VARCHAR UNIQUE NOT NULL," +
                    " RENTED_CAR_ID INTEGER DEFAULT NULL," +
                    " CONSTRAINT FK_CAR FOREIGN KEY (RENTED_CAR_ID)" +
                    " REFERENCES CAR(ID)" +
                    ")");
            System.out.println("Customer table created.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void dropTable(){
        try (Statement statement = H2DAOFactory.createConnection().createStatement();){
            statement.execute("drop table CUSTOMER if exists");
            System.out.println("Customer table dropped.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class H2DAOFactory  extends DAOFactory{
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





