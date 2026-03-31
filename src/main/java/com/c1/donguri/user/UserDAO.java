package com.c1.donguri.user;

import com.c1.donguri.util.DBManager;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class UserDAO {
    public static final UserDAO USER_DAO = new UserDAO();

    private UserDAO() {
    }

    public void getUserList(HttpServletRequest request) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM USER_TEST";


        try {

            con = DBManager.DB_MANAGER.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            ArrayList<UserDTO> userList = new ArrayList<>();

            while (rs.next()) {
                userList.add(
                        new UserDTO(
                                rs.getString("user_id"),
                                rs.getString("name"),
                                rs.getString("email"),
                                rs.getString("password")
                        )
                );
            }

            System.out.println(userList);

            request.setAttribute("userList", userList);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, rs);
        }

    }


}
