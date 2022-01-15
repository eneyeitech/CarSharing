package com.eneyeitech;

import java.io.File;
import java.sql.*;
import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static int selectedCar = 0;
    private static CompanyDAO companyDAO;
    private static CarDAO carDAO;

    public static void main(String[] args) {

        String search = "-databaseFileName";
        String dbName = "";
        List<String> arguments = new ArrayList<>(Arrays.asList(args));
        if (arguments.contains(search)) {
            dbName = args[arguments.indexOf(search) + 1];
        } else {
            dbName = "test";
        }

        if (false) {
            DatabaseManager.createDatabase(dbName);
        }
        //DatabaseManager.createPathDirectory();

        // create the required DAO Factory
        DAOFactory h2DaoFactory = DAOFactory.getDAOFactory(DAOFactory.H2);

        // Create a DAO
        companyDAO = h2DaoFactory.getCompanyDAO();
        carDAO = h2DaoFactory.getCarDAO();


        menuControl();



    }

    public static void selection1(String inp) {
        switch (inp) {
            case "1":
                subMenuControl();
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
                displayCompanies();
                System.out.println("0. Back");
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

                break;
            default:
                break;
        }
    }

    public static void selection3(String inp) {
        Company company = companyDAO.findCompany(Integer.valueOf(inp));
        if (company != null) {
            dispayCompanyMenu(company.getName());
            String choice = scanner.nextLine();
            selection4(choice, company);
        }
    }

    public static void selection4(String inp, Company company) {
        switch (inp) {
            case "1":
                System.out.println("Car list:");
                displayCars(company.getId());
                //String choice = scanner.nextLine();
                //selection3(choice);
                break;
            case "2":
                System.out.println("Enter the car name:");
                String name = scanner.nextLine();
                carDAO.insertCar(name, company.getId());
                System.out.println("The car was added!");
                break;
            case "0":

                break;
            default:
                break;
        }
    }

    public static String mainMenuStr() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("1. Log in as a manager\n");
        stringBuilder.append("0. Exit\n");
        return stringBuilder.toString();
    }

    public static String subMenuStr() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n1. Company list\n");
        stringBuilder.append("2. Create a company\n");
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
    public static void dispaySubMenu() {
        System.out.println(subMenuStr());
    }

    public static void dispayCompanyMenu(String name) {
        System.out.println(companyMenu(name));
    }


    public static void  displayCompanies(){
        Collection<Company> companies= companyDAO.selectCompaniesTO();
        if (companies != null && companies.size() > 0) {
            companies.stream().map(c -> String.format("%s. %s", c.getId(), c.getName()))
                    .forEachOrdered(System.out::println);
        } else {
            System.out.println("The company list is empty!");
        }
    }

    public static void  displayCars(int search){
        Collection<Car> cars= carDAO.selectCarsTO(search);
        if(cars != null && cars.size() > 0) {
            cars.stream().map(c -> String.format("%s. %s", c.getId(), c.getName()))
                    .forEachOrdered(System.out::println);
        } else {
            System.out.println("The car list is empty!");
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

    public static void subMenuControl() {
        String input = "";
        do {
            dispaySubMenu();
            input = scanner.nextLine();
            selection2(input);
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

class DatabaseManager{
    private static String connectionPath = "jdbc:h2:file:./src/carsharing/db/carsharing";
    private static Connection connection = null;
    private static Statement statement = null;
    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(connectionPath);
            statement = connection.createStatement();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void createDatabase(String dbName) {
        String path = "./src/carsharing/db/";
        File f1 = new File(path);
        boolean created = f1.mkdirs();

        String connPath = String.format("jdbc:h2:file:%s%s", path, dbName);

        System.out.println("connPath::" + connPath);

        if (created) {
            System.out.println("created!");
        } else {
            System.out.println("not created!");
        }

        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try (Connection conn = DriverManager.getConnection(connPath);
             Statement stat = conn.createStatement()) {
            stat.execute("create table COMPANY(" +
                    "ID INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    " NAME VARCHAR UNIQUE NOT NULL" +
                    ")");

            stat.execute("create table CAR(" +
                    "ID INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    " NAME VARCHAR UNIQUE NOT NULL," +
                    " COMPANY_ID INTEGER NOT NULL," +
                    " CONSTRAINT FK_COMPANY FOREIGN KEY (COMPANY_ID)" +
                    " REFERENCES COMPANY(ID)" +
                    ")");

            conn.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean createPathDirectory() {
        String path = "./src/carsharing/db/";
        File f1 = new File(path);
        boolean created = f1.mkdirs();

        return created;
    }

}