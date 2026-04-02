<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>편지 예약 작성</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
    <style>

        body::after {
            content:"";
            position:fixed; top:0; left:0; width:100%; height:100%;
            background: url('https://cdn-icons-png.flaticon.com/128/1202/1202088.png') repeat;
            opacity:0.15; /* 진하게 */
            z-index:-1;
        }

        body {
            font-family: Arial, sans-serif;
            margin:0; padding:0;
            display:flex; justify-content:center; align-items:flex-start;
            background-color:#fff8f0;
        }

        .form-container {
            max-width:500px;
            width:90%;
            margin:40px auto;
            padding:30px;
            border-radius:10px;
            background: rgba(255,255,255,0.95);
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            text-align:center; /* 전체 가운데 정렬 */
        }

        .form-container h2 {
            font-size:24px;
            margin-bottom:20px;
        }

        .form-row {
            display:flex;
            flex-direction:column;
            margin-bottom:15px;
            text-align:left;
        }

        .form-row label {
            font-weight:bold;
            font-size:16px;
            margin-bottom:5px;
        }

        .form-row input,
        .form-row textarea,
        .form-row select {
            padding:8px 10px;
            font-size:14px;
            border:1px solid #ccc;
            border-radius:5px;
            width:100%;
            box-sizing:border-box;
        }

        #scheduledWrapper {
            display:inline-flex;
            align-items:center;
            cursor:pointer;
            border:1px solid #ccc;
            padding:5px;
            border-radius:4px;
            justify-content:center;
            margin-bottom:5px;
        }

        #selectedDateDisplay {
            font-size:14px;
            color:#333;
            margin-top:5px;
        }

        button, .link-btn {
            padding:10px 20px;
            border:none;
            border-radius:6px;
            background: #8e6546;
            color:white;
            cursor:pointer;
            font-size:16px;
            text-decoration:none;
            margin-top:10px;
            display:inline-block;
            transition:background 0.2s;
        }

        button:hover, .link-btn:hover {
            background: #bf8c72;
        }
    </style>
</head>
<body>

<div class="form-container">
    <h2>🌰 도토리 예약바구니 🌰</h2>

    <form action="reservation" method="post">
        <div class="form-row">
            <label>닉네임</label>
            <input name="fromId" value="${sessionScope.user.nickname}" placeholder="닉네임 입력" required>
        </div>
        <div class="form-row">
            <label>보내는 이메일</label>
            <input name="senderEmail" placeholder="내 이메일 입력" required>
        </div>
        <div class="form-row">
            <label>받는 이메일</label>
            <input name="recipientEmail" placeholder="받는 사람 이메일" required>
        </div>
        <div class="form-row">
            <label>제목</label>
            <input name="title" placeholder="제목 입력" required>
        </div>
        <div class="form-row">
            <label>내용</label>
            <textarea name="message" rows="6" placeholder="내용 입력" required></textarea>
        </div>
        <div class="form-row">
            <label>예약 시간</label>
            <div id="scheduledWrapper">📅</div>
            <input type="text" id="scheduledDate" name="scheduledDate" style="display:none;" required>
            <div id="selectedDateDisplay"></div>
        </div>
        <div class="form-row">
            <label>템플릿</label>
            <select name="templateId">
                <option value="1">기본</option>
                <option value="2">감성</option>
                <option value="3">생일</option>
            </select>
        </div>
        <div class="form-row">
            <label>BGM</label>
            <select name="bgm">
                <option value="none">없음</option>
                <option value="piano">피아노</option>
                <option value="lofi">Lo-fi</option>
            </select>
        </div>
        <button type="submit">💌 예약하기</button>
    </form>

    <a href="reservation?action=list" class="link-btn">📬 목록 보기</a>
</div>
<script>

    const now = new Date();
    now.setMinutes(now.getMinutes() + 1);


    const fp = flatpickr("#scheduledDate", {
        enableTime: true,          // 시간 선택 허용
        dateFormat: "Y-m-d H:i",   // 날짜 + 시간 포맷
        minDate: now,               // 최소 선택 가능 시간
        time_24hr: true,            // 24시간 형식
        minuteIncrement: 1,         // 1분 단위로 선택 가능
        onChange: (selectedDates, dateStr) => {
            // 선택한 날짜 표시
            document.getElementById("selectedDateDisplay").innerText = dateStr;
        }
    });


    document.getElementById("scheduledWrapper").addEventListener("click", () => fp.open());
</script>

</body>
</html>