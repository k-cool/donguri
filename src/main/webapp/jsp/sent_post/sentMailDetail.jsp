<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8">
    <title>보낸 메일 상세</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sentPost.css">
</head>
<body>
<div class="sent-mail-container detail-page">
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
            <div class="detail-value">
                <div class="detail-content">${sentMail.content}</div>
            </div>
        </div>

        <div class="detail-row detail-status-row">
            <span class="detail-label">상태</span>
            <span class="detail-value">
            <span class="detail-status-value">${sentMail.status}</span>
        </span>
        </div>

        <div class="detail-row">
            <span class="detail-label">보낸 시각</span>
            <span class="detail-value">${sentMail.sentAt}</span>
        </div>
    </div>

    <div class="detail-btn-area">
        <button type="button"
                class="back-btn detail-link-btn"
                onclick="location.href='post?id=${sentMail.reservationId}'">
            실제 동구리 보기
        </button>

        <form action="${pageContext.request.contextPath}/sent-post" method="get">
            <input type="hidden" name="keyword" value="${keyword}">
            <button type="submit" class="back-btn">목록으로</button>
        </form>
    </div>
</div>

<div class="deco-cone-1"></div>
<div class="deco-cone-2"></div>
</body>
</html>