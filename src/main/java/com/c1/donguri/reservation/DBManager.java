package com.c1.donguri.reservation;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBManager {

    public static Connection connect() throws Exception {


        Class.forName("oracle.jdbc.driver.OracleDriver");


        String url = "jdbc:oracle:thin:@10.1.82.127:1521:XE";
        String user = "c##gh1004";
        String password = "gh1004";

        return DriverManager.getConnection(url, user, password);
    }
}