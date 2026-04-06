<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>마이페이지</title>
    <link rel="stylesheet" href="css/mypage.css">
</head>
<body>

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
            <button type="button" onclick="updateNickname()">수정(중복확인)</button>
        </div>
    </div>

    <div class="btn-group">
        <button type="button" onclick="location.href='/'">홈으로</button>
        <button type="button" onclick="location.href='password-edit'">비밀번호 재설정</button>
        <button type="button" onclick="location.href='user-delete'">회원 탈퇴</button>
    </div>
</div>

<script src="js/mypage.js"></script>

</body>
</html>