<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div align="center">
    <h2>📬 도토리 상세보기</h2>

    <p><b>받는 사람:</b> ${r.recipientEmail}</p>
    <p><b>제목:</b> ${r.subject}</p>
    <p><b>예약 시간:</b> ${r.scheduledDate}</p>

    <hr>

    <p>${r.content}</p>

    <br>
    <a href="reservation?action=edit&id=${r.reservationId}">수정하기</a> |
    <a href="reservation?action=list">목록으로</a>
</div>
</body>
</html>
