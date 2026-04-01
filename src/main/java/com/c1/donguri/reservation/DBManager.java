package com.c1.donguri.reservation;

public class DBManager {
    public static Connection connect() throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        return DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:XE",
                "계정", "비번"
        );
    }
}