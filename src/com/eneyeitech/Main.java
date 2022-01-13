package com.eneyeitech;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String search = "-databaseFileName";
        String dbName = "";
        List<String> arguments = new ArrayList<>(Arrays.asList(args));
        if (arguments.contains(search)) {

            dbName = args[arguments.indexOf(search) + 1];
            System.out.println(dbName);
        } else {
            dbName = "test";
        }
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
}