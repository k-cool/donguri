<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link href="css/signupSuccess.css" rel="stylesheet">
<div class="success-container">
    <h2>로그인 성공!</h2>

    <p>
        <strong>${sessionScope.user.nickname}</strong> (${sessionScope.user.email})님, <br>
        동구리에 오신 것을 환영합니다!
    </p>

    <div class="button-group">
        <button type="button" onclick="location.href='mypage'">마이페이지</button>
        <button type="button" onclick="location.href='main'" style="background-color: #333;">홈으로 가기</button>
    </div>
</div>