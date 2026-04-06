<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>편지 예약 수정</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
    <style>

        body::after {
            content: "";
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: url('https://cdn-icons-png.flaticon.com/128/1202/1202088.png') repeat;
            opacity: 0.15; /* 진하게 */
            z-index: -1;
        }

        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: flex-start;
            background-color: #fff8f0;
        }

        .form-container {
            max-width: 500px;
            width: 90%;
            margin: 40px auto;
            padding: 30px;
            border-radius: 10px;
            background: rgba(255, 255, 255, 0.95);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            text-align: center; /* 전체 가운데 정렬 */
        }

        .form-container h2 {
            font-size: 24px;
            margin-bottom: 20px;
        }

        .form-row {
            display: flex;
            flex-direction: column;
            margin-bottom: 15px;
            text-align: left;
        }

        .form-row label {
            font-weight: bold;
            font-size: 16px;
            margin-bottom: 5px;
        }

        .form-row input,
        .form-row textarea,
        .form-row select {
            padding: 8px 10px;
            font-size: 14px;
            border: 1px solid #ccc;
            border-radius: 5px;
            width: 100%;
            box-sizing: border-box;
        }

        #scheduledWrapper {
            display: inline-flex;
            align-items: center;
            cursor: pointer;
            border: 1px solid #ccc;
            padding: 5px;
            border-radius: 4px;
            justify-content: center;
            margin-bottom: 5px;
        }

        #selectedDateDisplay {
            font-size: 14px;
            color: #333;
            margin-top: 5px;
        }

        button, .link-btn {
            padding: 10px 20px;
            border: none;
            border-radius: 6px;
            background: #8e6546;
            color: white;
            cursor: pointer;
            font-size: 16px;
            text-decoration: none;
            margin-top: 10px;
            display: inline-block;
            transition: background 0.2s;
        }

        button:hover, .link-btn:hover {
            background: #bf8c72;
        }

        /*달력 위치꾸민거임*/
        .form-row {
            margin-bottom: 15px;
            display: flex;
            flex-direction: column;
            font-family: Arial, sans-serif;
        }

        .date-picker-container {
            display: flex;
            align-items: center;
            gap: 10px; /* 아이콘과 날짜 사이 간격 */
            cursor: pointer;
        }

        #scheduledWrapper {
            font-size: 1.5em;
            padding: 5px 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            background-color: #f0f0f0;
            transition: background-color 0.2s;
        }

        #scheduledWrapper:hover {
            background-color: #e0e0e0;
        }

        .selected-date {
            font-size: 1em;
            color: #333;
        }
    </style>
</head>
<body>

<div class="form-container">
    <h2>🌰 도토리 예약 수정 🌰</h2>

    <form action="reservation" method="post">

        <input type="hidden" name="action" value="update">
        <input type="hidden" name="id" value="${r.reservationId}">

        <div class="form-row">
            <label>닉네임</label>
            <input name="fromId" value="${r.fromId}" placeholder="닉네임 입력" required>
        </div>

        <div class="form-row">
            <label>받는 이메일</label>
            <input name="recipientEmail" value="${r.recipientEmail}" placeholder="영어만 입력 가능" required
                   oninput="this.value = this.value.replace(/[^a-zA-Z0-9@._-]/g,'')">
            <small style="color: gray; font-size: 0.8em;">
                영어, 숫자, @ . _ - 만 입력 가능합니다.
            </small>
        </div>

        <div class="form-row">
            <label>제목</label>
            <input name="subject" value="${r.subject}" placeholder="제목 입력" required>
        </div>

        <div class="form-row">
            <label>내용</label>
            <textarea name="content" rows="6" placeholder="내용 입력" required>${r.content}</textarea>
        </div>

        <div class="form-row">
            <label>예약 시간</label>
            <div class="date-picker-container">
                <div id="scheduledWrapper">📅</div>
                <div id="selectedDateDisplay" class="selected-date">${r.scheduledDate}</div>

                <input type="text" id="scheduledDate" name="scheduledDate" value="${r.scheduledDate}" style="display:none;" required>
            </div>
        </div>

        <div class="form-row">
            <label>템플릿</label>
            <select name="templateId">
                <option value="1" ${r.templateId == '1' ? 'selected' : ''}>기본</option>
                <option value="2" ${r.templateId == '2' ? 'selected' : ''}>감성</option>
                <option value="3" ${r.templateId == '3' ? 'selected' : ''}>생일</option>
            </select>
        </div>

        <div class="form-row">
            <label>BGM</label>
            <select name="bgmUrl">
                <option value="none" ${r.bgmUrl == 'none' ? 'selected' : ''}>없음</option>
                <option value="piano" ${r.bgmUrl == 'piano' ? 'selected' : ''}>피아노</option>
                <option value="lofi" ${r.bgmUrl == 'lofi' ? 'selected' : ''}>Lo-fi</option>
            </select>
        </div>

        <button type="submit">💌 수정하기</button>
    </form>

    <a href="reservation?action=list" class="link-btn">📬 목록 보기</a>
</div>

<script src="js/reservation-flatpickr.js"></script>
</body>
</html>
