<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>도토리 예약 수정</title>
    <link rel="stylesheet" href="css/reservation_edit.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>

<div class="form-container">
    <div>
        <form action="reservation" method="post">
            <h2>🌰 도토리 예약 수정 🌰</h2>
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
                <textarea name="content" required>${r.content}</textarea>
            </div>

            <div class="form-row">
                <label>예약 시간</label>
                <div class="date-picker-container" id="datePickerBtn"
                     style="cursor: pointer; display: flex; align-items: center; border: 1px solid #ccc; padding: 10px; border-radius: 5px;">
                    <div id="scheduledWrapper" style="margin-right: 10px;">📅</div>
                    <div id="selectedDateDisplay" class="selected-date" style="flex-grow: 1;">
                        ${r.scheduledDate != null ? r.scheduledDate : '선택된 시간 없음'}
                    </div>
                    <input type="text" id="scheduledDate" name="scheduledDate" value="${r.scheduledDate}"
                           style="display:none;" required>
                </div>
            </div>

            <div class="template-slider form-row">
                <label>템플릿</label>
                <div class="slider-row">
                    <button type="button" id="tempLeft" class="arrow">◀</button>

                    <div class="template-track-wrapper">
                        <div class="template-track" id="templateTrack">
                            <c:forEach items="${templateList}" var="t">
                                <div class="template-card ${r.templateId == t.templateId ? 'active' : ''}"
                                     data-id="${t.templateId}">
                                    <img src="${t.coverImgUrl}">
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <button type="button" id="tempRight" class="arrow">▶</button>
                </div>
            </div>
            <input type="hidden" name="templateId" id="selectedTemplate" value="${r.templateId}">

            <div class="bgm-section">
                <label>B G M</label>
                <div class="bgm-tabs">
                    <button type="button" class="tab-btn active" data-genre="Japanese Mood">Japanese Mood</button>
                    <button type="button" class="tab-btn" data-genre="Midnight Blue">Midnight Blue</button>
                    <button type="button" class="tab-btn" data-genre="Playful Day">Playful Day</button>
                    <button type="button" class="tab-btn" data-genre="Lofi Chill">Lofi Chill</button>
                    <button type="button" class="tab-btn" data-genre="Calm & Sad">Calm</button>
                </div>

                <div class="bgm-container">
                    <button type="button" class="bgm-arrow left">◀</button>
                    <div class="bgm-window">
                        <div class="bgm-track" id="bgmTrack"></div>
                    </div>
                    <button type="button" class="bgm-arrow right">▶</button>
                </div>

                <input type="hidden" name="bgmUrl" id="selectedBgm" value="${r.bgm}">
                <div class="player-wrapper">
                    <audio id="bgmPlayer" controls src="${r.bgm}"></audio>
                    <p id="currentSongTitle"></p>
                </div>
            </div>

            <div style="display: flex; gap: 10px; justify-content: center; margin-top: 20px;">
                <button type="submit">수정하기</button>
                <a href="reservation?action=detail&id=${r.reservationId}" class="link-btn" style="background: #a6a6a6;">취소</a>
            </div>
        </form>
    </div>
</div>

<script src="js/reservation-flatpickr.js"></script>
<script src="js/reservation_write_template.js"></script>
<script src="js/bgmplay.js"></script>
<script src="js/reservation_editinit.js"></script>

</body>
</html>