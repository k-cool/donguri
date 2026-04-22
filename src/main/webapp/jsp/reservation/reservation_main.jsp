<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>동구리 메일 서비스</title>
    <link rel="stylesheet" href="css/reservation_main.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>

<div class="r-container">

    <div class="paper-box">

        <h2 class="title">동구리 메일 서비스</h2>

        <p class="desc">
            가장 좋은 때에 닿기를 바라는 마음,<br>
            <strong>동구리 메일</strong><br><br>

            일본의 연하장 문화처럼,<br>
            당신의 진심을 소중히 보관했다가<br>
            약속한 그 순간 가장 따뜻한 모습으로 배달합니다.<br>
            서두르지 않아 더 깊은, 우리만의 마음 전달 서비스를 만나보세요.<br>
        </p>

        <hr class="separator">

        <div class="section">
            <h3>서비스 목적</h3>
            <ul>
                <li>기다림의 미학 → 메시지의 가치 상승</li>
                <li>정서적 교감 → 감성 전달 극대화</li>
                <li>기억 보관 → 시간이 지나도 꺼내볼 수 있는 편지</li>
            </ul>
        </div>

        <hr class="separator">

        <div class="section">
            <h3>주요 기능</h3>
            <ul>
                <li>회원 관리 (User)</li>
                <li>예약 발송 (Reservation)</li>
                <li>자동 메일 전송 (Scheduler)</li>
                <li>템플릿 & QR 경험</li>
            </ul>
        </div>

        <a href="${isLoggedIn ? "reservation?action=list" : "login"}"
           class="btn">
            동구리 보내기
        </a>

    </div>

</div>

</body>
</html>
