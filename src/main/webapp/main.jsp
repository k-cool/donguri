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

<%
    System.out.println("main.jsp: " + request.getAttribute("cb"));
%>

<%--<c:if test="${cb != null}">--%>

<%--    <div>${cb}</div>--%>

<%--    <script>--%>
<%--        location.href = "${cb}";--%>
<%--    </script>--%>
<%--    --%>
<%--</c:if>--%>

<body>
<div class="container">
    <header>
        <div class="header-left">
            <button id="menuBtn" class="menu-btn">
                <img src="https://donguri-dev.s3.ap-northeast-2.amazonaws.com/ui_asset/%EC%A0%84%EC%86%A1%EC%84%9C%EB%B9%84%EC%8A%A4/logo.svg"
                     alt="menu" width="30"
                     height="30">
            </button>
        </div>


        <nav class="main-nav">
            <ul class="nav-menu">
                <li><a href="main" class="logo-center">
                    <img src="https://donguri-dev.s3.ap-northeast-2.amazonaws.com/ui_asset/%EC%A0%84%EC%86%A1%EC%84%9C%EB%B9%84%EC%8A%A4/logo.svg"
                         alt="donguri post" width="120" height="40">
                </a></li>
                <li><a href="#">동구리 예약</a></li>
                <li><a href="#">동구리 보관함</a></li>
                <li><a href="#">동구리 뽑기</a></li>
            </ul>
        </nav>

        <div class="user-auth-area">
            <c:if test="${not empty loginPage}">
                <jsp:include page="${loginPage}"/>
            </c:if>
            <c:if test="${empty loginPage and not empty sessionScope.user}">
                <jsp:include page="jsp/user/login_ok.jsp"/>
            </c:if>
        </div>
    </header>

    <div id="slideMenu">
        <div class="user-info">
            <c:choose>
                <c:when test="${sessionScope.user.profileImgUrl != null}">
                    <img src="${sessionScope.user.profileImgUrl}" alt="Profile" class="profile-pic">
                </c:when>
                <c:otherwise>
                    <img src="https://s3.amazonaws.com/donguri-dev/ui_asset/default_profile.png" alt="Profile"
                         class="profile-pic">
                </c:otherwise>
            </c:choose>
            <p class="nickname">${sessionScope.user.nickname}</p>
            <div class="notifications">Notifications</div>
            <ul class="sub-menu">
                <li><a href="mypage">My Page</a></li>
                <li><a href="#">Template Storage</a></li>
                <li><a href="#">Contacts</a></li>
            </ul>
        </div>
        <ul class="main-menu">
            <li><a href="login?type=logout">Logout</a></li>
        </ul>
    </div>

    <div class="content">
        <jsp:include page="${content}"/>
    </div>

</div>
</body>
</html>