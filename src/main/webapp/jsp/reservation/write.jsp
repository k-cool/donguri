<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<title>편지 예약 작성</title>
<link rel="stylesheet" href="css/reservation_write.css">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
<script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
<script src="js/reservation_write_template.js"></script>
<meta name="viewport" content="width=device-width, initial-scale=1">

<script src="js/reservation-flatpickr.js" defer></script>
<script src="js/reservation_write_template.js" defer></script>
<script src="js/bgmplay.js" defer></script>


<div class="write">

    <div class="form-container">
        <form action="reservation" method="post">
            <input type="hidden" name="templateId" id="selectedTemplate">
            <input type="hidden" name="bgmUrl" id="selectedBgm">


            <h2>{ 너무나도 그리운 누군가에게 }</h2>
            <input type="hidden" name="action" value="confirm">


            <div class="form-row">
                <label>받는 이메일</label>

                <div class="input-row">
                    <input name="recipientEmail"
                           placeholder="영어, 숫자, @ . _ - 만 입력 가능합니다."
                           required>
                </div>
            </div>

            <div class="form-row">
                <label>제목</label>

                <input name="subject" value="${sessionScope.insertReservation.subject}" placeholder="제목 입력" required>
            </div>

            <div class="form-row">
                <label>내용</label>

                <textarea name="content" placeholder="내용 입력"
                          required
                          style="height: 120px; resize: none; overflow-y: auto;">${sessionScope.insertReservation.content}</textarea>
            </div>

            <div class="form-row">
                <label>예약 시간</label>

                <div class="date-picker-container" id="datePickerBtn"
                     style="cursor: pointer; display: flex; align-items: center; border: 1px solid #ccc; padding: 10px; border-radius: 5px;">
                    <div id="scheduledWrapper" style="margin-right: 10px;">📅</div>
                    <div id="selectedDateDisplay" class="selected-date" style="flex-grow: 1;">
                        ${sessionScope.insertReservation.scheduledDate != null ? sessionScope.insertReservation.scheduledDate : '선택된 시간 없음'}
                    </div>
                    <input type="text" id="scheduledDate" name="scheduledDate"
                           value="${sessionScope.insertReservation.scheduledDate}" style="display:none;" required>
                </div>
            </div>

            <div class="template-slider form-row">
                <label>템플릿</label>

                <div class="slider-row">
                    <button type="button" id="tempLeft" class="arrow">◀</button>

                    <div class="template-track-wrapper">
                        <div class="template-track" id="templateTrack">
                            <c:forEach var="t" items="${templateList}">
                                <div class="template-card" data-id="${t.templateId}">
                                    <img src="${t.coverImgUrl}">
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <button type="button" id="tempRight" class="arrow">▶</button>
                </div>
            </div>


            <div class="form-row">


                <div class="bgm-section">
                    <label>B G M</label>

                    <div>

                        <div class="bgm-tabs">
                            <button type="button" class="tab-btn active" data-genre="Japanese Mood">Japanese Mood
                            </button>
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
                    </div>


                    <div class="player-wrapper">
                        <audio id="bgmPlayer" controls></audio>
                    </div>
                </div>

            </div>

            <div class="btn-wrapper">
                <button type="submit" class="reservation-btn">예약하기</button>
                <a href="reservation?action=list" class="link-btn">돌아가기</a>
            </div>
        </form>

    </div>

</div>
