<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="css/login.css" rel="stylesheet">
<script src="js/login.js" defer></script>
<div class="login-container">
    <h2>로그인</h2>

    <% if ("invalid".equals(request.getAttribute("loginError"))) { %>
    <div class="error-message">
        이메일 또는 비밀번호가 잘못되었습니다.
        이메일과 비밀번호를 정확히 입력해 주세요.
    </div>
    <% } %>

    <form action="login" method="post">
        <input hidden type="text" name="cb" value="${param.cb}"/>

        <div class="form-group">
            <label for="email">이메일</label>
            <input type="email" id="email" name="email" required placeholder="example@email.com"
                   value="<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>">
        </div>

        <div class="form-group">
            <label for="password">비밀번호</label>
            <%-- data-login-error 속성을 추가하여 서버 상태를 JS에 전달합니다 --%>
            <input type="password" id="password" name="password" required
                   data-login-error="<%= "invalid".equals(request.getAttribute("loginError")) %>">
        </div>

        <div class="button-group">
            <button type="submit" class="btn-login">로그인</button>
            <button type="button" class="btn-signup-link" onclick="location.href='signup-do'">회원가입</button>
        </div>
    </form>
</div>