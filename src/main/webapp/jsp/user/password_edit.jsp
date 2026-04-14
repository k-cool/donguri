<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<html>
<head>
    <title>비밀번호 재설정</title>
    <link rel="stylesheet" href="css/password_edit.css">
</head>
<body>

<div class="password-edit-wrapper">
    <h2>비밀번호 재설정</h2>

    <%-- 에러 메시지 표시 --%>
    <% if (request.getAttribute("errorMessage") != null) { %>
    <div class="error-banner"
         style="color: #dc3545; background-color: #f8d7da; border: 1px solid #f5c6cb; padding: 10px; margin-bottom: 15px; border-radius: 4px;">
        <%= request.getAttribute("errorMessage") %>
    </div>
    <% } %>

    <form id="passwordEditForm" method="post" action="password-edit">
        <div class="form-group">
            <label for="currentPassword">현재 비밀번호</label>
            <input type="password" id="currentPassword" name="currentPassword" required>
            <div class="error-message" id="currentPasswordError"></div>
        </div>

        <div class="form-group">
            <label for="newPassword">새 비밀번호</label>
            <input type="password" id="newPassword" name="newPassword" required>
            <div class="error-message" id="newPasswordError"></div>
        </div>

        <div class="form-group">
            <label for="confirmPassword">새 비밀번호 확인</label>
            <input type="password" id="confirmPassword" name="confirmPassword" required>
            <div class="error-message" id="confirmPasswordError"></div>
        </div>

        <div class="btn-group">
            <button type="submit" class="btn-submit">비밀번호 변경</button>
            <button type="button" class="btn-cancel" onclick="location.href='mypage'">취소</button>
        </div>
    </form>
</div>

<script src="js/password_edit.js"></script>

</body>
</html>
