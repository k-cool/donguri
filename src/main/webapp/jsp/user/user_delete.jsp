<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<html>
<head>
    <title>회원 탈퇴</title>
    <link rel="stylesheet" href="css/user_delete.css">
</head>
<body>

<div class="user-delete-wrapper">
    <h2>회원 탈퇴</h2>

    <%-- 에러 메시지 표시 --%>
    <% if (request.getAttribute("errorMessage") != null) { %>
    <div class="error-banner"
         style="color: #dc3545; background-color: #f8d7da; border: 1px solid #f5c6cb; padding: 10px; margin-bottom: 15px; border-radius: 4px;">
        <%= request.getAttribute("errorMessage") %>
    </div>
    <% } %>

    <p class="warning-text">※ 탈퇴 시 동일 이메일로 재가입할 수 없습니다.</p>

    <form id="userDeleteForm" method="post" action="user-delete">
        <div class="form-group">
            <label for="password">비밀번호 확인</label>
            <input type="password" id="password" name="password" required>
            <div class="error-message" id="passwordError"></div>
        </div>

        <div class="btn-group">
            <button type="button" class="btn-cancel" onclick="location.href='mypage'">취소</button>
            <button type="submit" class="btn-delete">회원 탈퇴</button>
        </div>
    </form>
</div>

<script src="js/user_delete.js"></script>

</body>
</html>
