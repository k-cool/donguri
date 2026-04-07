<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>편지 예약 작성</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
    <style>
        /* 1. 배경: 깊은 숲속 원목 느낌 */
        body {
            background-color: #d7b899;
            background-image: url('https://www.transparenttextures.com/patterns/wood-pattern.png');
            color: #3e2723;
            font-family: 'Apple SD Gothic Neo', 'Malgun Gothic', sans-serif;
            margin: 0;
            padding: 40px 20px;
            display: flex;
            justify-content: center;
        }

        /* 2. 메인 컨테이너: 나무 판자 위에 놓인 편지봉투 느낌 */
        .form-container {
            max-width: 550px;
            width: 100%;
            background: #fffef2; /* 편지지 색상 */
            padding: 40px;
            border-radius: 5px;
            box-shadow: 0 1px 4px rgba(0, 0, 0, 0.3),
            0 0 40px rgba(0, 0, 0, 0.1) inset,
            15px 15px 0px rgba(0, 0, 0, 0.1); /* 바닥 그림자 */
            border: 1px solid #e0d5c1;
            position: relative;
        }

        /* 편지지 상단 장식 (도토리 아이콘) */
        .form-container::before {
            content: "🌰";
            position: absolute;
            top: -25px;
            left: 50%;
            transform: translateX(-50%);
            font-size: 3rem;
            filter: drop-shadow(2px 2px 2px rgba(0, 0, 0, 0.2));
        }

        h2 {
            font-size: 1.8rem;
            color: #5d4037;
            border-bottom: 2px dashed #d7ccc8;
            padding-bottom: 20px;
            margin-bottom: 30px;
        }

        .form-row {
            margin-bottom: 20px;
            text-align: left;
        }

        /* 라벨: 연필로 꾹꾹 눌러쓴 느낌 */
        .form-row label {
            font-weight: bold;
            color: #795548;
            font-size: 1rem;
            display: block;
            margin-bottom: 8px;
        }

        /* 입력창: 테두리를 없애고 밑줄만 주어 편지지 느낌 강조 */
        .form-row input,
        .form-row textarea,
        .form-row select {
            padding: 12px 10px;
            font-size: 1rem;
            border: none;
            border-bottom: 1px solid #d7ccc8;
            background: transparent;
            width: 100%;
            box-sizing: border-box;
            outline: none;
            transition: border-color 0.3s;
        }

        .form-row input:focus,
        .form-row textarea:focus {
            border-bottom: 2px solid #8e6546;
        }

        /* 예약 날짜 선택 박스 */
        .date-picker-container {
            background: #fdf5f0;
            border-radius: 10px;
            padding: 15px;
            border: 1px solid #efebe9;
            margin-top: 5px;
        }

        #scheduledWrapper {
            font-size: 1.5rem;
            margin-right: 10px;
        }

        /* 버튼 스타일: 묵직한 나무 버튼 */
        button[type="submit"] {
            width: 100%;
            padding: 18px;
            background: #5d4037;
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 1.2rem;
            font-weight: bold;
            cursor: pointer;
            box-shadow: 0 4px 0 #3e2723; /* 버튼 입체감 */
            margin-top: 20px;
            transition: all 0.1s;
        }

        button[type="submit"]:active {
            box-shadow: 0 0px 0 #3e2723;
            transform: translateY(4px);
        }

        /* 돌아가기 링크 버튼 */
        .link-btn {
            display: block;
            margin-top: 25px;
            color: #8d6e63;
            text-decoration: none;
            font-size: 0.9rem;
            border-bottom: 1px solid #8d6e63;
            display: inline-block;
        }

        .link-btn:hover {
            color: #5d4037;
            border-color: #5d4037;
        }

        /* 템플릿 미리보기 이미지 */
        #previewImg {
            margin-top: 15px;
            border: 5px solid white;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            transform: rotate(-2deg); /* 사진이 툭 던져진 느낌 */
        }
    </style>
</head>
<body>

<div class="form-container">
    <h2>너무나도 그리운 상대에게</h2>

    <form action="reservation" method="post">
        <input type="hidden" name="action" value="confirm">

        <div class="form-row">
            <label>닉네임</label>
            <input name="fromId"
                   value="${sessionScope.insertReservation.fromId != null ? sessionScope.insertReservation.fromId : ''}"
                   placeholder="닉네임 입력" required>
        </div>

        <div class="form-row">
            <label>받는 이메일</label>
            <input name="recipientEmail" value="${sessionScope.insertReservation.recipientEmail}"
                   placeholder="영어만 입력 가능" required
                   oninput="this.value = this.value.replace(/[^a-zA-Z0-9@._-]/g,'')">
            <small style="color: gray; font-size: 0.8em;">
                영어, 숫자, @ . _ - 만 입력 가능합니다.
            </small>
        </div>

        <div class="form-row">
            <label>제목</label>
            <input name="subject" value="${sessionScope.insertReservation.subject}" placeholder="제목 입력" required>
        </div>

        <div class="form-row">
            <label>내용</label>
            <textarea name="content" rows="6" placeholder="내용 입력"
                      required>${sessionScope.insertReservation.content}</textarea>
        </div>

        <div class="form-row">
            <label>예약 시간</label>
            <div class="date-picker-container">
                <div id="scheduledWrapper">📅</div>
                <div id="selectedDateDisplay"
                     class="selected-date">${sessionScope.insertReservation.scheduledDate != null ? sessionScope.insertReservation.scheduledDate : '선택된 시간 없음'}</div>
                <input type="text" id="scheduledDate" name="scheduledDate"
                       value="${sessionScope.insertReservation.scheduledDate}" style="display:none;" required>
            </div>
        </div>

        <div class="form-row">
            <label>템플릿</label>

            <select name="templateId" id="templateSelect">
                <%--                onchange="updateTemplatePreview('templateSelect', 'previewImg', 'templatePreview')">--%>
                <option value="">-- 보관함 템플릿 선택 --</option>

                <c:forEach items="${templateList}" var="t">
                    <option value="${t.templateId}">
                            ${t.name}
                    </option>
                </c:forEach>
            </select>

            <div id="templatePreview">
                <img id="previewImg" src="" alt="템플릿 미리보기" style="display:none; max-width:200px;">
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

    <a href="reservation?action=list" class="link-btn">📬 도토리 예약 안 하고 그냥 돌아갈래!</a>
</div>
<script src="js/reservation-flatpickr.js"></script>
<script src="js/reservation-template.js"></script>

</body>
</html>