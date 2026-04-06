package com.c1.donguri.util;

import java.net.URISyntaxException;
import java.nio.file.Paths;
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
        envMap = EnvLoader.loadEnv(".env");
    }


    public Connection getConnection() throws ClassNotFoundException, SQLException, URISyntaxException {
        System.out.println("사용할 DB_URL: " + envMap.get("DB_URL"));

        /*
           추후에 Oracle Cloud와 연동하여 사용할 경우 활성화 시키기
           - resources/Wallet 경로로 다운받은 월렛 파일 추가 필요
        */
//        String walletPath = Paths.get(
//                getClass()
//                        .getClassLoader()
//                        .getResource("Wallet")
//                        .toURI()
//        ).toString().replace("\\", "/");

//        String url = envMap.get("DB_URL") + "?TNS_ADMIN=" + walletPath;

//        System.out.println(url);

        String url = envMap.get("DB_URL");
        String user = envMap.get("DB_USER");
        String password = envMap.get("DB_PASSWORD");

        // 1. Oracle 드라이버 먼저 강제 로딩
        Class.forName("oracle.jdbc.OracleDriver");

        // 2. 그 다음 P6Spy 로딩
        Class.forName("com.p6spy.engine.spy.P6SpyDriver");

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

            if (con != null) {
                con.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
