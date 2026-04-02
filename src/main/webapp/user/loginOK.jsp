<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="css/loginOK.css" rel="stylesheet">


<div class="login-ok-container">
    <div class="profile-area">
        <c:choose>
            <c:when test="${sessionScope.user.profileImgUrl != null}">
                <img src="${sessionScope.user.profileImgUrl}" alt="프로필"
                     style="width: 50px; height: 50px; border-radius: 50%;">
            </c:when>
            <c:otherwise>
                <img src="images/default_profile.png" alt="기본 프로필" style="width: 50px; height: 50px;">
            </c:otherwise>
        </c:choose>
    </div>

    <div class="welcome-msg">
        <p><strong>${sessionScope.user.nickname}</strong>님, 환영합니다!</p>
    </div>

    <div class="user-menu">
        <button type="button" onclick="location.href='mypage'">마이페이지</button>
        <button type="button" onclick="location.href='logout'">로그아웃</button>
    </div>
</div>