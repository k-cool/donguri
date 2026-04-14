<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Donguri - Main</title>
    <link href="css/reset.css" rel="stylesheet">
    <link href="css/main.css" rel="stylesheet">
    <link href="css/header.css" rel="stylesheet">
    <link href="css/home.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.7.1.js"
            integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
    <script src="js/main.js"></script>
    <script src="js/header.js"></script>

</head>

<body>

<header>
    <div class="hd-header-top">

        <div class="hd-header-left">
            <c:if test="${not empty loginPage}">
                <jsp:include page="${loginPage}"/>
            </c:if>
            <c:if test="${empty loginPage and not empty sessionScope.user}">
                <jsp:include page="jsp/user/login_ok.jsp"/>
            </c:if>
        </div>

        <div class="hd-logo-center">
            <a href="main">
                <img src="https://donguri-dev.s3.ap-northeast-2.amazonaws.com/ui_asset/%EC%A0%84%EC%86%A1%EC%84%9C%EB%B9%84%EC%8A%A4/logo.svg"
                     alt="donguri post" height="40">
            </a>
        </div>

        <div class="hd-header-right">
            <button id="hd-menuBtn" class="hd-menu-btn">
                <img src="https://donguri-dev.s3.ap-northeast-2.amazonaws.com/ui_asset/main/profile.svg"
                     alt="menu" width="70" height="70">
            </button>
        </div>

    </div>

    <div class="hd-header-bottom">
        <nav class="hd-main-nav">
            <ul class="hd-nav-menu">
                <li><a href="#">동구리 예약</a></li>
                <li><a href="#">동구리 보관함</a></li>
                <li><a href="#">동구리 뽑기</a></li>
            </ul>
        </nav>
    </div>
</header>

<div class="container">
    <div id="hd-slideMenu">
        <div class="hd-user-info">
            <c:choose>
                <c:when test="${sessionScope.user.profileImgUrl != null}">
                    <img src="${sessionScope.user.profileImgUrl}" alt="Profile" class="hd-profile-pic">
                </c:when>
                <c:otherwise>
                    <img src="https://s3.amazonaws.com/donguri-dev/ui_asset/default_profile.png" alt="Profile"
                         class="hd-profile-pic">
                </c:otherwise>
            </c:choose>
            <p class="hd-nickname">${sessionScope.user.nickname}</p>
            <div class="hd-notifications">알림</div>
            <ul class="hd-sub-menu">
                <li><a href="mypage">마이페이지</a></li>
                <li><a href="#">탬플릿 보관함</a></li>
                <li><a href="#">컨택트</a></li>
            </ul>
        </div>
        <ul class="hd-logout-menu">
            <li><a href="login?type=logout">Logout</a></li>
        </ul>
    </div>

    <div class="content">
        <jsp:include page="${content}"/>
    </div>

</div>

</body>
</html>