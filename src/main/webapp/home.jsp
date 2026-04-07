<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="css/home.css">
    <script src="js/home.js" defer></script>

</head>
<body>

<c:if test="${empty sessionScope.user}">
    <div class="nav-menu">
        <button type="button" class="btn-signup" onclick="location.href='signup-do'">
            회원가입
        </button>

        <button type="button" class="btn-login" onclick="location.href='login'">
            로그인
        </button>
    </div>
</c:if>

<div>
    <h1>여기 이동 버튼 모아서 사용해주세요!</h1>
    <button onclick="location.href='omikuji'">omikuji</button>
    <button type="button" onclick="location.href='template-list'">template list</button>
    <button type="button" onclick="location.href='template-user'">User's template list</button>
    <button type="button" onclick="location.href='qr-decode'">PC QR Upload!</button>
</div>

<div class="scene" id="scene">
    <div class="acorn" id="acorn">🌰</div>
    <div class="bottom-group" id="bottomGroup">
        <div class="mailbox">📮</div>
        <div class="ground"></div>
    </div>
</div>


</body>
</html>