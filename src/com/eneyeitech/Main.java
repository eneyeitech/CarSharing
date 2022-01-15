package com.eneyeitech;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
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

        //DatabaseManager.createCompanyTable("");

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
                DatabaseManager.listCompany();
                break;
            case "2":
                System.out.println("Enter the company name:");
                String name = scanner.nextLine();
                DatabaseManager.createCompany(name);
                System.out.println("The company was created!");
                break;
            case "0":
                DatabaseManager.closeConnection();
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

    public static void dispayMainMenu() {
        System.out.println(mainMenuStr());
    }
    public static void dispaySubMenu() {
        System.out.println(subMenuStr());
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
            stat.execute("create table COMPANY(ID int, NAME varchar)");

            conn.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void createCompany(String companyName){
        try {
            String query = String.format("INSERT INTO COMPANY (NAME) VALUES ('%s')", companyName);
            //statement.execute("INSERT INTO COMPANY (NAME) VALUES ()");
            statement.execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

        }


    }

    public static void alterCompany(String companyName){
        try {
            statement.execute("ALTER TABLE COMPANY" +
                    " MODIFY COLUMN ID INTEGER NOT NULL");

            statement.execute("ALTER TABLE COMPANY" +
                    " ADD PRIMARY KEY (ID)");

            statement.execute("ALTER TABLE COMPANY" +
                    " ADD AUTO_INCREMENT (ID)");

            statement.execute("ALTER TABLE COMPANY" +
                    " ADD UNIQUE (NAME)");

            statement.execute("ALTER TABLE COMPANY" +
                    " MODIFY COLUMN NAME VARCHAR NOT NULL");
            System.out.println("Table COMPANY altered.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createCompanyTable(String companyName){
        try {
            statement.execute("DROP TABLE COMPANY" +
                    " IF EXISTS ");

            statement.execute("create table COMPANY(" +
                    "ID INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    " NAME VARCHAR UNIQUE NOT NULL" +
                    ")");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean createPathDirectory() {
        String path = "./src/carsharing/db/";
        File f1 = new File(path);
        boolean created = f1.mkdirs();

        return created;
    }

    public static void listCompany(){
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM COMPANY ORDER BY ID ASC");

            if (!resultSet.next() ) {
                System.out.println("The company list is empty!");
            } else {
                System.out.println("company list:");
                do {

                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("NAME");
                    System.out.printf("%s. %s\n", id, name);
                } while (resultSet.next());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}