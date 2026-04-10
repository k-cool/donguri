<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="css/reservation_detail.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

</head>
<body>
<div class="detail-card">
    <div align="center">
        <h2>📬 도토리 상세보기</h2>

        <div class="info-section">
            <p><span class="info-label">받는 사람</span> ${r.recipientEmail}</p>
            <p><span class="info-label">제목</span> <b>${r.subject}</b></p>
        </div>

        <div class="content-area">
            ${r.content}
        </div>

        <hr>

        <p class="date-info">
            🕰️ 이 추억은 <b>${r.scheduledDate}</b>에 배달될 예정이야.
        </p>

        <div class="btn-group">
            <a href="reservation?action=edit&id=${r.reservationId}" class="btn btn-edit">수정하고싶어요!</a>
            <a href="reservation?action=list" class="btn btn-list">도토리 심기</a>
        </div>
    </div>
</div>
</body>
</html>
