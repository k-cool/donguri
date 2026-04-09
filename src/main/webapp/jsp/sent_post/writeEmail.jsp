<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>엽서 메일 작성</title>

    <!-- 공용 CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">

    <!-- 메일 전용 JS -->
    <script defer src="${pageContext.request.contextPath}/js/mail.js"></script>
</head>
<body>
<div class="wrap">
    <h2>엽서 메일 작성</h2>

    <!--
        action은 ReserveEmailC의 @WebServlet("/reserveEmail") 와 연결됨
    -->
    <form action="${pageContext.request.contextPath}/reserveEmail" method="post" id="emailForm">

        <!--
            templateId / senderId는 보통 로그인 세션이나 선택된 템플릿 값으로 들어감
        -->
        <input type="hidden" name="templateId" value="">
        <input type="hidden" name="senderId" value="USER_UUID_SAMPLE_001">

        <div class="row">
            <label for="receiverEmail">받는 사람 이메일</label>
            <input type="email" id="receiverEmail" name="receiverEmail" required>
        </div>

        <div class="row">
            <label for="subject">엽서 제목</label>
            <input type="text" id="subject" name="subject" maxlength="20" required>
        </div>

        <div class="row">
            <label for="content">엽서 내용</label>
            <textarea id="content" name="content" maxlength="500" required></textarea>
        </div>

        <div class="row">
            <label for="reserveDateTime">예약 일시</label>

            <!--
                step="300" = 5분 단위
                브라우저 UI에서 1차 제한
            -->
            <input type="datetime-local"
                   id="reserveDateTime"
                   name="reserveDateTime"
            <%--                   step="300"--%>
                   required>

            <%--            <small>5분 단위로만 설정 가능 (예: 10:00, 10:05, 10:10)</small>--%>
        </div>

        <div class="row">
            <button type="submit">예약하기</button>
        </div>
    </form>
</div>
</body>
</html>