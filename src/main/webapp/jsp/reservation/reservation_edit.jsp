<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>편지 예약 수정</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
    <style>
        /* 기본 배경: 부드러운 모래색 */
        body {
            background-color: #fdfaf5;
            color: #5d4037;
            font-family: 'Pretendard', 'Malgun Gothic', sans-serif;
            margin: 0;
            padding: 40px 20px;
        }

        /* 다이어리/나무판 느낌의 컨테이너 */
        .form-container {
            max-width: 550px;
            width: 100%;
            margin: 0 auto;
            padding: 40px;
            background: #ffffff;
            border-radius: 30px;
            /* 나무 느낌의 이중 테두리 */
            border: 8px solid #8e6546;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            position: relative;
        }

        /* 제목 스타일 */
        h2 {
            color: #5d4037;
            font-size: 1.6rem;
            margin-bottom: 30px;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
        }

        /* 폼 라벨: 나무 표지판 느낌 */
        .form-row label {
            display: block;
            font-weight: bold;
            color: #8d6e63;
            margin-bottom: 8px;
            font-size: 0.95rem;
        }

        /* 입력창 공통 스타일 */
        .form-row input,
        .form-row textarea,
        .form-row select {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e0d5c1;
            border-radius: 12px;
            background-color: #fdfbf9;
            color: #3e2723;
            font-size: 1rem;
            transition: all 0.3s ease;
            outline: none;
        }

        .form-row input:focus,
        .form-row textarea:focus {
            border-color: #8e6546;
            background-color: #fff;
        }

        /* 읽기 전용 닉네임 창 */
        input[readonly] {
            background-color: #f1ece9 !important;
            border-color: #d7ccc8 !important;
            color: #a1887f;
        }

        /* 날짜 선택 커스텀 */
        .date-picker-container {
            display: flex;
            align-items: center;
            gap: 12px;
            background: #f4ece1;
            padding: 10px 15px;
            border-radius: 12px;
            border: 1px dashed #8e6546;
            cursor: pointer;
        }

        #scheduledWrapper {
            font-size: 1.4rem;
            background: none;
            border: none;
        }

        /* 버튼 그룹 */
        .btn-group {
            display: flex;
            gap: 15px;
            justify-content: center;
            margin-top: 30px;
        }

        button, .link-btn {
            flex: 1;
            padding: 15px;
            border: none;
            border-radius: 15px;
            font-size: 1rem;
            font-weight: bold;
            cursor: pointer;
            transition: transform 0.2s, background 0.3s;
            text-align: center;
            text-decoration: none;
        }

        /* 수정 완료 버튼: 짙은 나무색 */
        button[type="submit"] {
            background: #795548;
            color: white;
        }

        button[type="submit"]:hover {
            background: #5d4037;
            transform: translateY(-3px);
        }

        /* 취소 버튼: 연한 나무색 */
        .link-btn {
            background: #d7ccc8;
            color: #5d4037 !important;
        }

        .link-btn:hover {
            background: #bcaaa4;
            transform: translateY(-3px);
        }

        /* 도토리 배경 패턴 */
        body::after {
            content: "";
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-image: url('https://www.transparenttextures.com/patterns/wood-pattern.png'); /* 은은한 나무결 */
            opacity: 0.05;
            pointer-events: none;
            z-index: -1;
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
            <label>받는 이메일</label>
            <input name="recipientEmail" value="${r.recipientEmail}" required
                   oninput="this.value = this.value.replace(/[^a-zA-Z0-9@._-]/g,'')">
        </div>

        <div class="form-row">
            <label>제목</label>
            <input name="subject" value="${r.subject}" required>
        </div>

        <div class="form-row">
            <label>내용</label>
            <textarea name="content" rows="6" required>${r.content}</textarea>
        </div>

        <div class="form-row">
            <label>예약 시간</label>
            <div class="date-picker-container">
                <div id="scheduledWrapper">📅</div>
                <div id="selectedDateDisplay" class="selected-date">${r.scheduledDate}</div>
                <input type="text" id="scheduledDate" name="scheduledDate" value="${r.scheduledDate}"
                       style="display:none;" required>
            </div>
        </div>

        <div class="form-row">
            <label>템플릿</label>
            <select name="templateId" id="templateSelect">
                <option value="">-- 보관함 템플릿 선택 --</option>
                <c:forEach items="${templateList}" var="t">
                    <option value="${t.templateId}" data-url="${t.coverImgUrl}" ${r.templateId == t.templateId ? "selected" : ""}>
                            ${t.name}
                    </option>
                </c:forEach>
            </select>

            <div id="templatePreview" style="margin-top: 15px; text-align: center;">
                <img id="previewImg" src="" alt="템플릿 미리보기"
                     style="display:none; max-width:100%; height:auto; border-radius: 8px; border: 1px solid #ddd; box-shadow: 0 4px 8px rgba(0,0,0,0.1);">
            </div>
        </div>

        <div id="templatePreview" style="margin-top: 10px; text-align: center;">
            <img id="previewImg" src="" width="150"
                 style="border-radius: 8px; border: 1px solid #ddd; display: none;">
        </div>

        <div class="form-row">
            <label>BGM</label>
            <select name="bgmUrl">
                <option value="none" ${r.bgm == 'none' ? 'selected' : ''}>없음</option>
                <option value="piano" ${r.bgm == 'piano' ? 'selected' : ''}>피아노</option>
                <option value="lofi" ${r.bgm == 'lofi' ? 'selected' : ''}>Lo-fi</option>
            </select>
        </div>
        
        <div style="display: flex; gap: 10px; justify-content: center; margin-top: 20px;">
            <button type="submit">💌 수정 완료</button>
            <a href="reservation?action=detail&id=${r.reservationId}" class="link-btn"
               style="background: #a6a6a6;">취소</a>
        </div>
        </form>
</div>

<script src="js/reservation-flatpickr.js"></script>
<script src="js/reservation-template.js"></script>
<script>
    window.onload = function () {
        updateTemplatePreview('templateSelect', 'previewImg', 'templatePreview');
    };
</script>
</body>
</html>