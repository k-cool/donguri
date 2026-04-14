package com.c1.donguri.user;

import com.c1.donguri.util.DBManager;
import com.c1.donguri.util.S3Uploader;
import com.c1.donguri.util.EmailSend;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Random;

public class UserDAO {
    public static final UserDAO USER_DAO = new UserDAO();

    // 기본 프로필 이미지 URL
    private static final String DEFAULT_PROFILE_IMAGE = "https://donguri-dev.s3.ap-northeast-2.amazonaws.com/profile_img/donguriSample.png";

    private UserDAO() {
    }

    /*
       test 용
    */
    public void getAllUserList(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM users ORDER BY created_at DESC";

        try {
            con = DBManager.DB_MANAGER.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            ArrayList<UserDTO> userList = new ArrayList<>();

            while (rs.next()) {
                UserDTO user = new UserDTO();
                user.setUserId(rs.getString("user_id"));
                user.setNickname(rs.getString("nickname"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setProfileImgUrl(rs.getString("profile_img_url"));
                user.setCreatedAt(rs.getDate("created_at"));
                user.setUpdatedAt(rs.getDate("updated_at"));
                user.setIsDeleted(rs.getString("is_deleted"));

                userList.add(user);
            }

            request.setAttribute("userList", userList);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, rs);
        }
    }


    public void signUp(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement pstmt = null;

//        String savePath = request.getServletContext().getRealPath("image");
//        int maxSize = 1024 * 1024 * 20;

        S3Uploader s3Uploader = new S3Uploader();


        try {
            // 1. 파라미터 추출 (@MultipartConfig가 있으면 getParameter가 동작함)
            String email = request.getParameter("email");
            String nickname = request.getParameter("nickname");
            String password = request.getParameter("password");

            // 2. 비밀번호 유효성 검증
            if (password == null || password.length() < 8) {
                throw new IllegalArgumentException("비밀번호는 최소 8자 이상이어야 합니다.");
            }

            // 특수문자 검증
            if (!password.matches(".*[!@#$%^&*()\\-_=+\\[\\]{}|;:'\",.<>/?].*")) {
                throw new IllegalArgumentException("비밀번호에 특수문자 1개 이상 포함해야 합니다.");
            }

            // 2. 파일 처리 (name="file" 인 input 태그 기준)
            Part filePart = request.getPart("file");
            String imgUrl = DEFAULT_PROFILE_IMAGE; // 기본값으로 기본 이미지 설정

            if (filePart != null && filePart.getSize() > 0) {
                String fileName = "profile_img/" + UUID.randomUUID().toString();
                InputStream inputStream = filePart.getInputStream();
                String contentType = filePart.getContentType();
                long fileSize = filePart.getSize();

                // S3에 바로 업로드 (서버 로컬에 저장되지 않음)
                imgUrl = s3Uploader.upload(inputStream, fileName, contentType, fileSize);
            }

            con = DBManager.DB_MANAGER.getConnection();
            String sql = "INSERT INTO users(email,nickname,password,profile_img_url) VALUES (?, ?, ?, ?)";
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, email);
            pstmt.setString(2, nickname);
            pstmt.setString(3, password);
            pstmt.setString(4, imgUrl);

            if (pstmt.executeUpdate() == 1) {
                System.out.println("sign up success");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("sign up fail");
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, null);
        }
    }

    // 유저가 관리자(ADMIN)인지 확인하는 메서드
    public boolean isAdmin(String email) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;


        String sql = "SELECT roll FROM users WHERE email = ? AND is_deleted = 'N'";

        try {
            con = DBManager.DB_MANAGER.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String roll = rs.getString("roll");
                if ("ADMIN".equals(roll)) {
                    return true; // DB에 ADMIN으로 되어있으면 true 반환
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, rs);
        }
        return false; // 아니면 false 반환
    }

    /**
     * 로그인 처리 - 세션에 사용자 정보 저장
     *
     * @param request HTTP 요청 객체 (email, password 파라미터 필요)
     * @return 로그인 성공 시 true, 실패 시 false
     * <p>
     * [세션 정보]
     * - 속성명: "user"
     * - 저장값: UserDTO 객체 (email, nickname, profileImgUrl)
     * - 만료시간: 60초 (1분)
     * <p>
     * [사용법]
     * UserDAO.USER_DAO.login(request);
     * if (UserDAO.USER_DAO.loginCheck(request)) {
     * // 로그인 성공 처리
     * }
     */
    public boolean login(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBManager.DB_MANAGER.getConnection();

            // [수정] SQL문 끝에 'AND is_deleted = 'N'' 조건을 추가했습니다.
            // 탈퇴하지 않은 유저만 조회되도록 필터링하는 것입니다.

            String sql = "SELECT RAWTOHEX(user_id) AS user_id, email, nickname, profile_img_url " +
                    "FROM users " +
                    "WHERE email = ? AND password = ? AND is_deleted = 'N'";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, request.getParameter("email"));
            pstmt.setString(2, request.getParameter("password"));

            rs = pstmt.executeQuery();

            if (rs.next()) {
                // 2. 로그인 성공 시 DTO 객체 생성
                UserDTO user = new UserDTO();
                user.setUserId(rs.getString("user_id"));
                user.setEmail(rs.getString("email"));
                user.setNickname(rs.getString("nickname"));

                // 3. DB의 스네이크(_) 명칭을 DTO의 카멜케이스 세터에 담기 (핵심!)
                user.setProfileImgUrl(rs.getString("profile_img_url"));

                // 4. 세션에 "user"라는 이름으로 UserDTO 객체 저장
                //    - JSP에서 ${sessionScope.user} 또는 ${user}로 접근 가능
                //    - 다른 컨트롤러에서 session.getAttribute("user")로 가져올 수 있음
                HttpSession session = request.getSession();
                request.getSession().setAttribute("user", user);
                session.setMaxInactiveInterval(60 * 60); //  5분

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, rs);
        }
        return false;
    }

    /**
     * 로그인 상태 확인 - 세션에 사용자 정보가 있는지 검사
     *
     * @param request HTTP 요청 객체
     * @return 로그인되어 있으면 true, 아니면 false
     * <p>
     * [세션 확인 방법]
     * 1. session.getAttribute("user")로 UserDTO 객체 가져오기
     * 2. 객체가 null이 아니면 로그인 상태
     * <p>
     * [사용법]
     * if (UserDAO.USER_DAO.loginCheck(request)) {
     * // 로그인된 사용자만 접근 가능한 기능
     * } else {
     * // 로그인 페이지로 리다이렉트
     * }
     */
    public boolean loginCheck(HttpServletRequest request) {
        // 세션에서 "user" 속성으로 저장된 UserDTO 객체 가져오기
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");

        if (user != null) {
            // 로그인 상태: 헤더에 로그인 성공 페이지 표시
            request.setAttribute("loginPage", "jsp/user/login_ok.jsp");
            return true;
        } else {
            // 비로그인 상태: 헤더에 로그인 페이지 표시
            request.setAttribute("loginPage", "jsp/user/login.jsp");
            return false;
        }
    }

    /**
     * 로그아웃 처리 - 세션 전체 삭제
     *
     * @param request HTTP 요청 객체
     *                <p>
     *                [세션 삭제 방법]
     *                - session.invalidate()로 세션 자체를 완전히 삭제
     *                - 세션에 저장된 모든 데이터(user 등)가 함께 삭제됨
     *                <p>
     *                [사용법]
     *                UserDAO.USER_DAO.logout(request);
     *                response.sendRedirect("main");
     */
    public void logout(HttpServletRequest request) {
        // 기존 세션이 있는지 확인 (없으면 새로 만들지 않고 null 반환)
        HttpSession session = request.getSession(false);

        if (session != null) {
            // 세션 바구니 자체를 제거하여 모든 데이터(user 등)를 한 번에 삭제
            session.invalidate();
            System.out.println("Logout: Session has been invalidated.");
        }
    }

    public boolean checkNickname(String newNickname) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT COUNT(*) FROM users WHERE nickname = ? AND is_deleted = 'N'";

        try {
            con = DBManager.DB_MANAGER.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, newNickname);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // 중복이면 true, 아니면 false
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, rs);
        }

        return false; // 에러 발생 시 false 반환
    }

    public boolean updateNickname(String email, String newNickname) {
        Connection con = null;
        PreparedStatement pstmt = null;

        String sql = "UPDATE users SET nickname = ?, updated_at = CURRENT_TIMESTAMP WHERE email = ?";

        try {
            con = DBManager.DB_MANAGER.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, newNickname);
            pstmt.setString(2, email);

            int result = pstmt.executeUpdate();
            return result > 0; // 성공하면 true, 아니면 false
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, null);
        }

        return false; // 에러 발생 시 false 반환
    }

    public boolean updateProfileImg(String email, String profileImgUrl) {
        Connection con = null;
        PreparedStatement pstmt = null;

        String sql = "UPDATE users SET profile_img_url = ?, updated_at = CURRENT_TIMESTAMP WHERE email = ?";

        try {
            con = DBManager.DB_MANAGER.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, profileImgUrl);
            pstmt.setString(2, email);

            int result = pstmt.executeUpdate();
            return result > 0; // 성공하면 true, 아니면 false
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, null);
        }

        return false; // 에러 발생 시 false 반환
    }

    public boolean updatePassword(HttpServletRequest request, String currentPassword, String newPassword) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // 현재 로그인된 사용자 정보 가져오기
            UserDTO user = (UserDTO) request.getSession().getAttribute("user");
            if (user == null) {
                return false;
            }

            // 현재 비밀번호 확인
            String sql = "SELECT password FROM users WHERE email = ?";
            con = DBManager.DB_MANAGER.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user.getEmail());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String dbPassword = rs.getString("password");
                // 현재 비밀번호가 일치하는지 확인 (실제로는 암호화 필요)
                if (!currentPassword.equals(dbPassword)) {
                    return false;
                }
            } else {
                return false;
            }

            // 비밀번호 업데이트
            String updateSql = "UPDATE users SET password = ?, updated_at = CURRENT_TIMESTAMP WHERE email = ?";
            pstmt = con.prepareStatement(updateSql);
            pstmt.setString(1, newPassword); // 실제로는 암호화 필요
            pstmt.setString(2, user.getEmail());

            int result = pstmt.executeUpdate();
            return result > 0;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, rs);
        }

        return false;
    }

    public boolean deleteUser(HttpServletRequest request, String password) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // 현재 로그인된 사용자 정보 가져오기
            UserDTO user = (UserDTO) request.getSession().getAttribute("user");
            if (user == null) {
                return false;
            }

            // 비밀번호 확인
            String sql = "SELECT password FROM users WHERE email = ?";
            con = DBManager.DB_MANAGER.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user.getEmail());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String dbPassword = rs.getString("password");
                // 비밀번호가 일치하는지 확인 (실제로는 암호화 필요)
                if (!password.equals(dbPassword)) {
                    return false;
                }
            } else {
                return false;
            }
            // [핵심 변경 구간] 3. 회원 탈퇴 처리 (DELETE 대신 UPDATE)
            // DB에 이미 있는 is_deleted 컬럼을 'Y'로 바꿉니다.
            // 트리거 덕분에 updated_at은 자동으로 현재 시간으로 갱신됩니다.
            String deleteSql = "UPDATE users SET is_deleted = 'Y' WHERE email = ?";

            pstmt = con.prepareStatement(deleteSql);
            pstmt.setString(1, user.getEmail());

            int result = pstmt.executeUpdate();

            // 4. 탈퇴 성공 시 세션 무효화 (로그아웃 처리)
            if (result > 0) {
                request.getSession().invalidate(); // 탈퇴했으니 세션도 바로 지워줍니다.
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, rs);
        }

        return false;
    }

    // 이메일 중복 확인 메소드
    public boolean checkEmailExists(String email) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // [수정] AND is_deleted = 'N' 조건을 지워야 합니다.
        // 그래야 탈퇴한 계정(Y)도 검색되어 중복으로 판정됩니다. 오호 그렇군요
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";

        try {
            con = DBManager.DB_MANAGER.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // DB에 존재하면(탈퇴자 포함) 중복(true) 반환
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.DB_MANAGER.close(con, pstmt, rs);
        }
        return false;
    }

    // 6자리 인증 코드 생성
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 100000 ~ 999999
        return String.valueOf(code);
    }

    // 인증 코드 발송 메소드
    public boolean sendVerificationEmail(String email, HttpServletRequest request) {
        try {
            // 6자리 인증 코드 생성
            String verificationCode = generateVerificationCode();

            // 기존 세션 확인
            HttpSession currentSession = request.getSession(false);

            // 기존 세션이 있고 인증 코드가 있으면 재발송만 처리 (새로운 코드 생성 안 함)
            if (currentSession != null && currentSession.getAttribute("verificationCode") != null) {
                // 기존 세션 시간만 갱신
                currentSession.setAttribute("verificationTime", new java.util.Date());
                currentSession.setMaxInactiveInterval(60); // 1분 유효

                // 기존 코드로 이메일 재발송
                String existingCode = (String) currentSession.getAttribute("verificationCode");
                boolean success = EmailSend.EMAIL_SEND.sendVerificationEmail(email, existingCode);

                if (success) {
                    System.out.println("인증 코드 재발송 성공: " + email + " (기존 코드: " + existingCode + ")");
                    return true;
                }
            } else {
                // 새로운 세션에 인증 정보 저장
                HttpSession newSession = request.getSession();
                newSession.setAttribute("verificationEmail", email);
                newSession.setAttribute("verificationCode", verificationCode);
                newSession.setAttribute("verificationTime", new java.util.Date());
                newSession.setMaxInactiveInterval(60); // 1분 유효

                // 이메일 발송
                boolean success = EmailSend.EMAIL_SEND.sendVerificationEmail(email, verificationCode);

                if (success) {
                    System.out.println("인증 코드 발송 성공: " + email + " (새로운 코드: " + verificationCode + ")");
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // 인증 코드 검증 메소드
    public boolean verifyCode(String email, String inputCode, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return false;
        }

        String storedEmail = (String) session.getAttribute("verificationEmail");
        String storedCode = (String) session.getAttribute("verificationCode");
        java.util.Date verificationTime = (java.util.Date) session.getAttribute("verificationTime");

        // 세션 정보 확인
        if (storedEmail == null || storedCode == null || verificationTime == null) {
            return false;
        }

        // 이메일 일치 확인
        if (!email.equals(storedEmail)) {
            return false;
        }

        // 인증 코드 일치 확인
        if (!inputCode.equals(storedCode)) {
            return false;
        }

        // 유효시간 확인 (5분)
        long currentTime = System.currentTimeMillis();
        long storedTime = verificationTime.getTime();
        long timeDiff = (currentTime - storedTime) / 1000; // 초 단위

        if (timeDiff > 300) { // 5분 초과
            session.invalidate(); // 세션 만료
            return false;
        }

        // 인증 성공 처리
        session.setAttribute("isEmailVerified", true);
        session.removeAttribute("verificationCode"); // 사용한 코드는 삭제

        return true;
    }
}