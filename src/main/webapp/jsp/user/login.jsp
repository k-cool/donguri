<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<link href="css/login.css" rel="stylesheet">
<div class="login-container">
    <h2>로그인</h2>

    <form action="login" method="post">
        <div class="form-group">
            <label for="email">이메일</label>
            <input type="email" id="email" name="email" required placeholder="example@email.com">
        </div>

        <div class="form-group">
            <label for="password">비밀번호</label>
            <input type="password" id="password" name="password" required>
        </div>

        <div class="button-group">
            <button type="submit" class="btn-login">로그인</button>
            <button type="button" class="btn-signup-link" onclick="location.href='signup-do'">회원가입</button>
        </div>
    </form>
</div>