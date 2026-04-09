package com.c1.donguri.omikuji;

import com.c1.donguri.user.UserDTO;
import com.c1.donguri.util.DBManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;

public class OmikujiDAO {
    public static final OmikujiDAO OMIKUJI_DAO = new OmikujiDAO();

    private OmikujiDAO() {
    }


    public boolean getIsOmikujiAvailable(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT OMIKUJI_AT FROM USERS WHERE USER_ID = ?";

        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

        try {
            con = DBManager.DB_MANAGER.getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, user.getUserId());

            rs = pstmt.executeQuery();

            Timestamp omikujiAt = null;

            while (rs.next()) {
                omikujiAt = rs.getTimestamp("omikuji_at");
            }

            if (omikujiAt == null) return true;

            LocalDate scheduledDate = omikujiAt.toLocalDateTime().toLocalDate();
            LocalDate today = LocalDate.now();

            if (scheduledDate.isEqual(today)) return false;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, rs);
        }

        return true;

    }

    public OmikujiDTO drawOmikuji(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String selectSql = "SELECT *\n" +
                "FROM (\n" +
                "    SELECT LUCK, MESSAGE\n" +
                "    FROM omikuji\n" +
                "    ORDER BY DBMS_RANDOM.VALUE\n" +
                ")\n" +
                "WHERE ROWNUM = 1\n";

        String updateSql = "UPDATE USERS SET OMIKUJI_AT = ? WHERE USER_ID = ?";

        try {
            con = DBManager.DB_MANAGER.getConnection();
            pstmt = con.prepareStatement(selectSql);
            rs = pstmt.executeQuery();

            OmikujiDTO omikuji = new OmikujiDTO();

            // 뽑기
            while (rs.next()) {
                omikuji.setLuck(rs.getString("luck"));
                omikuji.setMessage(rs.getString("message"));
            }

            // 완료기록
            pstmt = con.prepareStatement(updateSql);

            pstmt.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
            pstmt.setString(2, user.getUserId());

            int row = pstmt.executeUpdate();

            if (row > 0) {
                System.out.println("UPDATE SUCCESS: USERS");
            }

            return omikuji;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, rs);
        }

        return null;

    }
}
