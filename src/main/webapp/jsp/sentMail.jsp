<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>보낸 메일함</title>
    <link rel="stylesheet" href="/css/sentMail.css">

    <script src="js/sentMail.js"></script>
</head>
<body>
<div class="sent-mail-container">
    <h1>보낸 메일함</h1>

    <form action="${pageContext.request.contextPath}/sentMail" method="get" class="search-form">
        <input type="text" name="keyword" placeholder="이메일, 제목, 내용 검색" value="${keyword}">
        <button type="submit">검색</button>
    </form>

    <!-- 상세 이동용 form -->
    <form id="detailForm" method="post" action="${pageContext.request.contextPath}/sentMailDetail">
        <input type="hidden" name="reservationId" id="reservationId">
        <input type="hidden" name="keyword" id="detailKeyword" value="${keyword}">
    </form>

    <div class="mail-list">
        <c:choose>
            <c:when test="${empty sentMails}">
                <div class="empty-message">조회된 메일이 없습니다.</div>
            </c:when>
            <c:otherwise>
                <c:forEach var="mail" items="${sentMails}">
                    <form id="detailForm" method="post"
                          action="/sentMailDetail">
                        <input type="hidden" name="reservationId" id="reservationId">
                        <div class="mail-card" onclick="goDetail('${mail.reservationId}')">
                            <div class="mail-row">
                                <span class="mail-label">받는 사람:</span>
                                <span class="mail-value">${mail.recipientEmail}</span>
                            </div>
                            <div class="mail-row title-row">
                                <span class="mail-label">제목:</span>
                                <span class="mail-value title-text">${mail.subject}</span>
                            </div>
                            <div class="mail-row">
                                <span class="mail-label">상태:</span>
                                <span class="mail-value status-text">${mail.status}</span>
                            </div>
                            <div class="mail-row">
                                <span class="mail-label">보낸 시각:</span>
                                <span class="mail-value date-text">${mail.sentAt}</span>
                            </div>
                        </div>
                    </form>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>