package com.c1.donguri.util;

import java.sql.*;
import java.util.Map;

/*
    DBManger
    - 싱글톤으로 구현
    - 프로젝트 최상단에 .env 파일 생성해야 동작
    - DB_URL, DB_USER, DB_PASSWORD 개별적으로 기입하기
*/

public class DBManager {
    public static final DBManager DB_MANAGER = new DBManager();
    public static Map<String, String> envMap;


    private DBManager() {
        this.envMap = EnvLoader.loadEnv(".env");
    }


    public Connection getConnection() throws ClassNotFoundException, SQLException {
        String driver = "com.p6spy.engine.spy.P6SpyDriver";

        // 일반적인 연결방식
//        String url = "jdbc:p6spy:oracle:thin:@10.1.82.127:1521:XE";
//        String user = "c##sw1004";
//        String password = "sw1004";

        System.out.println("WOWOWOWOWO: " + envMap.get("DB_URL"));

        String url = envMap.get("DB_URL");
        String user = envMap.get("DB_USER");
        String password = envMap.get("DB_PASSWORD");
        Class.forName(driver);

        return DriverManager.getConnection(url, user, password);
    }

    public void close(Connection con, PreparedStatement ps, ResultSet rs) {
        try {

            if (rs != null) {
                rs.close();
            }

            if (ps != null) {
                ps.close();
            }

            // connection을 재사용 하므로 닫으면 안된다.
            if (con != null) {
                con.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
