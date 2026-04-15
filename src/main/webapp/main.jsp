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
    <script src="js/main.js" defer></script>
    <script src="js/header.js" defer></script>

</head>

<body>

<header>
    <div class="hd-header-top">
        <a href="main">
            <img class="hd-logo" src="image/logo.svg" alt="donguri post" height="40">
        </a>

        <div class="hd-header-right">
            <c:if test="${empty sessionScope.user and not (request.requestURL.toString().contains('login') or request.requestURL.toString().contains('signup'))}">
                <div class="nav-menu">
                    <button type="button" class="btn-signup" onclick="location.href='signup-do'">
                        회원가입
                    </button>
                    <button type="button" class="btn-login" onclick="location.href='login'">
                        로그인
                    </button>
                </div>
            </c:if>

            <c:if test="${not empty sessionScope.user}">
                <button id="hd-menuBtn" class="hd-menu-btn">
                    <img src="image/profile.svg" alt="menu">
                </button>
            </c:if>
        </div>
    </div>

    <%--    header 하단--%>
    <c:if test="${not (request.requestURL.toString().contains('login') or request.requestURL.toString().contains('signup'))}">
        <div class="hd-header-bottom">
            <nav class="hd-main-nav">
                <ul class="hd-nav-menu">
                    <a href="omikuji" class="hd-main-nav-btn">
                        <li>행운의 동구리</li>
                    </a>
                    <a href="reservation" class="hd-main-nav-btn">
                        <li>동구리 예약</li>
                    </a>
                    <a href="sent-post" class="hd-main-nav-btn">
                        <li>동구리 보관함</li>
                    </a>
                </ul>
            </nav>
        </div>
    </c:if>
</header>

<%--사이드메뉴--%>
<div id="hd-slideMenu">
    <div class="hd-user-info">

        <div class="hd-profile-frame">
            <img src=${sessionScope.user.profileImgUrl != null ? sessionScope.user.profileImgUrl :"/image/squirrel_img.png" }
                         alt="Profile" class="hd-profile-pic hd-default-pic">
        </div>

        <div class="hd-user-info-text">
            <p class="hd-user-email">이메일: ${sessionScope.user.email}</p>
            <p class="hd-nickname">닉네임: ${sessionScope.user.nickname}</p>
        </div>

        <ul class="hd-sub-menu">
            <li><a href="mypage">My page</a></li>
            <li><a href="template-list">Template Storage</a></li>
            <li><a href="inquiry">Contacts</a></li>
        </ul>

        <ul class="hd-logout-menu">
            <li><a href="login?type=logout">Logout</a></li>
        </ul>
    </div>
</div>


<%--본문--%>
<div class="container">
    <div class="content">
        <jsp:include page="${content}"/>
    </div>

</div>
</body>
</html>