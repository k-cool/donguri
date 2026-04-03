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
                <img src="${sessionScope.user.profileImgUrl}" alt="프로필 이미지" class="profile-img">
            </c:when>
            <c:otherwise>
                <img src="images/default_profile.png" alt="기본 이미지" class="profile-img">
            </c:otherwise>
        </c:choose>
    </div>

    <div class="user-info">
        <p><strong>이메일:</strong> ${sessionScope.user.email}</p>
        <p><strong>닉네임:</strong> ${sessionScope.user.nickname}</p>
    </div>

    <div class="btn-group">
        <button type="button" onclick="location.href='user-update'">정보 수정</button>
        <button type="button" onclick="location.href='user-delete'">회원 탈퇴</button>
    </div>
</div>

</body>
</html>