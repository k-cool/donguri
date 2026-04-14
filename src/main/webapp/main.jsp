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

    <%-- 🚨 범인이었던 style 태그 삭제 완료! --%>
</head>

<%
    System.out.println("main.jsp: " + request.getAttribute("cb"));
%>

<body>

<header>
    <div class="hd-header-top">
        <div class="hd-logo-center">
            <a href="main">
                <img src="image/logo.svg" alt="donguri post" height="40">
            </a>
        </div>
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
                        <%-- 🚨 슬래시(/) 제거 완료! --%>
                    <img src="image/profile.svg" alt="menu" width="80" height="70">
                </button>
            </c:if>
        </div>
    </div>

    <c:if test="${not (request.requestURL.toString().contains('login') or request.requestURL.toString().contains('signup'))}">
        <div class="hd-header-bottom">
            <nav class="hd-main-nav">
                <ul class="hd-nav-menu">
                    <li><a href="omikuji">동구리 뽑기</a></li>
                    <li><a href="reservation">동구리 예약</a></li>
                    <li><a href="sent-post">동구리 보관함</a></li>
                </ul>
            </nav>
        </div>
    </c:if>
</header>

<div class="test-action-buttons" style="padding-top: 150px; text-align: center; position: relative; z-index: 10;">
    <c:if test="${not empty loginPage}">
        <jsp:include page="${loginPage}"/>
    </c:if>
    <c:if test="${empty loginPage and not empty sessionScope.user}">
        <jsp:include page="jsp/user/login_ok.jsp"/>
    </c:if>
</div>
<div class="container">
    <div id="hd-slideMenu">
        <div class="hd-user-info">
            <c:choose>
                <c:when test="${sessionScope.user.profileImgUrl != null}">
                    <img src="${sessionScope.user.profileImgUrl}" alt="Profile" class="hd-profile-pic">
                </c:when>
                <c:otherwise>
                    <img src="image/background.png" alt="Profile" class="hd-profile-pic">
                </c:otherwise>
            </c:choose>
            <p class="hd-user-email">이메일: ${sessionScope.user.email}</p>
            <p class="hd-nickname">닉네임: ${sessionScope.user.nickname}</p>
            <ul class="hd-sub-menu">
                <li><a href="mypage">My page</a></li>
                <li><a href="template-list">Template Storage</a></li>
                <li><a href="#">Contacts</a></li>
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