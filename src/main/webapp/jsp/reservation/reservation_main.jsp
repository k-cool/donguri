<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>동구리 메일 서비스</title>
    <link rel="stylesheet" href="css/reservation_main.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>

<div class="container">

    <div class="paper-box">
        <h2 class="title">동구리 메일 서비스</h2>

        <p class="desc">
            일본의 연하장 문화를 참고하여, 온라인으로 원하는 시점에<br>
            원하는 대상에게 마음을 전할 수 있는 서비스를 만들고자 하였습니다.
        </p>

        <hr class="separator">

        <div class="section">
            <h3>서비스 목적</h3>
            <ul>
                <li>예약된 시간에 이메일을 전송하는 서버 스케줄링과 이메일 전송 기능 구현</li>
                <li>QR코드 및 모바일 웹 대응</li>
                <li>일본 사용자도 쉽게 사용할 수 있는 글로벌 서비스 구현</li>
            </ul>
        </div>

        <hr class="separator">

        <div class="section">
            <h3>주요 기능</h3>
            <ul>
                <li>회원 관리</li>
                <li>포스트 예약</li>
                <li>이메일 자동 발송</li>
                <li>엽서 보관함</li>
                <li>다국어 지원</li>
            </ul>
        </div>

        <a href="${isLoggedIn ? "reservation?action=list" : "login"}"
           class="btn">
            도토리 보내기
        </a>
    </div>

</div>

</body>
</html>