package com.c1.donguri.inquiry;

import com.c1.donguri.util.DBManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.ArrayList;

public class InquiryDAO {
    public static void selectAllinquiry(HttpServletRequest req, HttpServletResponse resp) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select * from inquiry";


        try {

            conn = DBManager.DB_MANAGER.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            InquiryDTO dto = null;
            ArrayList<InquiryDTO> inquiry = new ArrayList();


            while (rs.next()) {
                dto = new InquiryDTO();
                dto.setInquiryId(rs.getString("inquiry_id"));
                dto.setName(rs.getString("name"));
                dto.setPhone(rs.getString("phone"));
                dto.setEmail(rs.getString("email"));
                dto.setMessage(rs.getString("message"));
                dto.setCreatedAt(rs.getDate("created_at"));
                dto.setUpdatedAt(rs.getDate("updated_at"));
                inquiry.add(dto);
            }

            System.out.println(inquiry);

            req.setAttribute("inquiry", inquiry);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(conn, pstmt, rs);
        }


    }


    public void insert(InquiryDTO inquiry) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "INSERT INTO inquiry (name, phone, email, message) VALUES (?, ?, ?, ?)";


        try {

            conn = DBManager.DB_MANAGER.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, inquiry.getName());
            pstmt.setString(2, inquiry.getPhone());
            pstmt.setString(3, inquiry.getEmail());
            pstmt.setString(4, inquiry.getMessage());

            int result = pstmt.executeUpdate();

            System.out.println("insert 결과: " + result);

            if (result == 1) {
                System.out.println("DB 입력 성공");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(conn, pstmt, null);
        }
    }

}

