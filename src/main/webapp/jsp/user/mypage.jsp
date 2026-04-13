<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<html>
<head>
    <title>마이페이지</title>
    <link rel="stylesheet" href="css/mypage.css">
    <link href="css/header.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script src="js/header.js"></script>
</head>
<body>

<header>
    <div class="hd-header-top">
        <div class="hd-header-left">
            <span style="font-weight: bold; color: #5d4037; font-family: 'Courier New', monospace;">KOR / JP</span>
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
                <li><a href="omikuji">OMIKUJI</a></li>
                <li><a href="sent-post">Sent Post</a></li>
                <li><a href="#">Scheduled Post</a></li>
            </ul>
        </nav>
    </div>
</header>

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
<div class="mypage-wrapper">
    <h2>마이페이지</h2>

    <div class="profile-section">
        <c:choose>
            <c:when test="${not empty sessionScope.user.profileImgUrl}">
                <img src="${sessionScope.user.profileImgUrl}" alt="프로필 이미지" class="profile-img" id="profilePreview"
                     onclick="document.getElementById('profileUpload').click();" title="클릭해서 사진 변경">
            </c:when>
            <c:otherwise>
                <img src="images/default_profile.png" alt="기본 이미지" class="profile-img" id="profilePreview"
                     onclick="document.getElementById('profileUpload').click();" title="클릭해서 사진 변경">
            </c:otherwise>
        </c:choose>
        <input type="file" id="profileUpload" accept="image/*" style="display: none;" onchange="uploadProfileImage()">
    </div>

    <div class="user-info">
        <p><strong>이메일:</strong> ${sessionScope.user.email}</p>

        <div class="nickname-box">
            <strong>닉네임:</strong>
            <input type="text" id="nicknameInput" value="${sessionScope.user.nickname}">
            <button type="button" id="nicknameBtn" onclick="handleNicknameProcess()">수정</button>
        </div>
        <div id="nicknameMsg" style="font-size: 12px; margin-top: 5px; margin-left: 60px;"></div>
    </div>

    <div class="btn-group">
        <button type="button" onclick="location.href='../main'">홈으로</button>
        <button type="button" onclick="location.href='password-edit'">비밀번호 재설정</button>
        <button type="button" onclick="location.href='user-delete'">회원 탈퇴</button>
    </div>
</div>

<script src="js/mypage.js"></script>

</body>
</html>