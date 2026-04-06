<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>보낸 메일함</title>
    <link rel="stylesheet" href="css/sentMail.css">
</head>
<body>
<div class="sent-mail-container">
    <h1>보낸 메일함</h1>

    <form action="sent.mail" method="get" class="search-form">
        <input type="text" name="keyword" placeholder="이메일, 제목, 내용 검색"
               value="${keyword}">
        <button>검색</button>
    </form>

    <div class="mail-list">
        <c:choose>
            <c:when test="${empty sentMails}">
                <div class="empty-message">조회된 메일이 없습니다.</div>
            </c:when>
            <c:otherwise>
                <c:forEach var="mail" items="${sentMails}">
                    <div class="mail-card">
                        <div><strong>받는 사람:</strong> ${sentMail.recipientEmail}</div>
                        <div><strong>제목:</strong> ${sentMail.subject}</div>
                        <div><strong>내용:</strong> ${sentMail.content}</div>
                        <div><strong>상태:</strong> ${sentMail.status}</div>
                        <div><strong>보낸 시각:</strong> ${sentMail.sentAt}</div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>