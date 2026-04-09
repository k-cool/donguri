<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>편지 예약 수정</title>
    <link rel="stylesheet" href="css/reservation_edit.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>

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
                    <option value="${t.templateId}"
                            data-url="${t.coverImgUrl}" ${r.templateId == t.templateId ? "selected" : ""}>
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
            <button type="submit">💌</button>
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