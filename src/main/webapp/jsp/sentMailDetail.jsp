<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<head>
    <title>보낸 메일 상세</title>
    <link rel="stylesheet" href="/css/sentMail.css">
</head>

<div class="sent-mail-container">
    <h1>보낸 메일 상세</h1>

    <div class="mail-detail-card">
        <div class="detail-row">
            <span class="detail-label">받는 사람</span>
            <span class="detail-value">${sentMail.recipientEmail}</span>
        </div>

        <div class="detail-row">
            <span class="detail-label">제목</span>
            <span class="detail-value detail-title">${sentMail.subject}</span>
        </div>

        <div class="detail-row detail-content-row">
            <span class="detail-label">내용</span>
            <div class="detail-value detail-content">${sentMail.content}</div>
        </div>

        <div class="detail-row">
            <span class="detail-label">상태</span>
            <span class="detail-value">${sentMail.status}</span>
        </div>

        <div class="detail-row">
            <span class="detail-label">보낸 시각</span>
            <span class="detail-value">${sentMail.sentAt}</span>
        </div>
    </div>

    <div class="detail-btn-area">
        <form action="${pageContext.request.contextPath}/sent-post" method="get">
            <input type="hidden" name="keyword" value="${keyword}">
            <button type="submit" class="back-btn">목록으로</button>
        </form>
    </div>
</div>