<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>편지 예약 작성</title>
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
            opacity: 0.15;
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
            text-align: center;
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

        .date-picker-container {
            display: flex;
            align-items: center;
            gap: 10px;
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

        #templatePreview {
            margin-top: 10px;
            text-align: center;
            display: none;
        }

        #previewImg {
            width: 150px;
            border-radius: 8px;
            border: 1px solid #ddd;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }
    </style>
</head>
<body>

<div class="form-container">
    <h2>🌰 도토리 예약바구니 🌰</h2>

    <form action="reservation" method="post">
        <input type="hidden" name="action" value="confirm">

        <div class="form-row">
            <label>닉네임</label>
            <input name="fromId" value="${sessionScope.user.nickname}" placeholder="닉네임 입력" required>
        </div>

        <div class="form-row">
            <label>받는 이메일</label>
            <input name="recipientEmail" placeholder="영어만 입력 가능" required
                   oninput="this.value = this.value.replace(/[^a-zA-Z0-9@._-]/g,'')">
            <small style="color: gray; font-size: 0.8em;">
                영어, 숫자, @ . _ - 만 입력 가능합니다.
            </small>
        </div>

        <div class="form-row">
            <label>제목</label>
            <input name="subject" placeholder="제목 입력" required>
        </div>

        <div class="form-row">
            <label>내용</label>
            <textarea name="content" rows="6" placeholder="내용 입력" required></textarea>
        </div>

        <div class="form-row">
            <label>예약 시간</label>
            <div class="date-picker-container">
                <div id="scheduledWrapper">📅</div>
                <div id="selectedDateDisplay" class="selected-date">선택된 시간 없음</div>
                <input type="text" id="scheduledDate" name="scheduledDate" style="display:none;" required>
            </div>
        </div>

        <div class="form-row">
            <label>템플릿</label>
            <select name="templateId" id="templateSelect"
                    onchange="updateTemplatePreview('templateSelect', 'previewImg', 'templatePreview')">
                <option value="">-- 보관함 템플릿 선택 --</option>
                <c:forEach var="t" items="${templateList}">
                    <option value="${t.templateId}" data-img="${t.coverImgUrl}">
                        [${t.type}] ${t.templateName}
                    </option>
                </c:forEach>
            </select>

            <div id="templatePreview">
                <img id="previewImg" src="" alt="템플릿 미리보기">
            </div>
        </div>

        <div class="form-row">
            <label>BGM</label>
            <select name="bgmUrl">
                <option value="none">없음</option>
                <option value="piano">피아노</option>
                <option value="lofi">Lo-fi</option>
            </select>
        </div>

        <button type="submit">💌 예약하기</button>
    </form>

    <a href="reservation?action=list" class="link-btn">📬 목록 보기</a>
</div>

<script src="js/reservation-flatpickr.js"></script>
<script src="js/reservation-template.js"></script>
</body>
</html>