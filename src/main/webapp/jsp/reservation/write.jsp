<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>편지 예약 작성</title>
    <link rel="stylesheet" href="css/reservation_write.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>

</head>
<body>

<div class="form-container">
    <h2>너무나도 그리운 상대에게</h2>

    <form action="reservation" method="post">
        <input type="hidden" name="action" value="confirm">


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

        <div class="form-row">
            <label>템플릿</label>
            <select name="templateId" id="templateSelect">
                <option value="">-- 보관함 템플릿 선택 --</option>
                <c:forEach items="${templateList}" var="t">
                    <option value="${t.templateId}" data-url="${t.coverImgUrl}">
                            ${t.name}
                    </option>
                </c:forEach>
            </select>

            <div id="templatePreview" style="margin-top: 15px; text-align: center;">
                <img id="previewImg" src="" alt="템플릿 미리보기"
                     style="display:none; max-width:100%; height:auto; border-radius: 8px; border: 1px solid #ddd; box-shadow: 0 4px 8px rgba(0,0,0,0.1);">
            </div>
        </div>

        <div class="form-row">
            <label>BGM</label>


            <select name="bgmUrl" id="bgmSelect">
                <option value="">없음</option>

                <optgroup label="일본풍">
                    <option value="bgm/PerituneMaterial_Sakuya2(chosic.com).mp3">Sakuya2</option>
                    <option value="bgm/Dreaming-Under-The-Stars-MP3(chosic.com).mp3">Dreaming Under The Stars</option>
                    <option value="bgm/PerituneMaterial_Soft_Day(chosic.com).mp3">Soft Day</option>
                    <option value="bgm/jayjen-ray-of-hope(chosic.com).mp3">Ray of Hope</option>
                    <option value="bgm/Double-Rainbow-chosic.com_.mp3">Double Rainbow</option>
                </optgroup>

                <optgroup label="밝은">
                    <option value="bgm/Soda(chosic.com).mp3">Soda</option>
                    <option value="bgm/Lights(chosic.com).mp3">Lights</option>
                    <option value="bgm/Roa-Haru-chosic.com_.mp3">Haru</option>
                    <option value="bgm/Little-Wishes-chosic.com_.mp3">Little Wishes</option>
                    <option value="bgm/Lobby-Time(chosic.com).mp3">Lobby Time</option>
                </optgroup>

                <optgroup label="차분한">
                    <option value="bgm/Spring-Flowers(chosic.com).mp3">Spring Flowers</option>
                    <option value="bgm/Fall-In-Love-chosic.com_.mp3">Fall In Love</option>
                    <option value="bgm/Transcendence-chosic.com_.mp3">Transcendence</option>
                    <option value="bgm/Walking-Home-chosic.com_.mp3">Walking Home</option>
                </optgroup>

                <optgroup label="Lo-fi">
                    <option value="bgm/Colorful-Flowers(chosic.com).mp3">Colorful Flowers</option>
                    <option value="bgm/echoes-in-blue-by-tokyo-music-walker-chosic.com_.mp3">Echoes in Blue</option>
                    <option value="bgm/tokyo-music-walker-day-off-chosic.com_.mp3">Day Off</option>
                    <option value="bgm/Late-at-Night(chosic.com).mp3">Late at Night</option>
                </optgroup>

                <optgroup label="잔잔/슬픔">
                    <option value="bgm/a-promise(chosic.com).mp3">A Promise</option>
                    <option value="bgm/Daydreams-chosic.com_.mp3">Daydreams</option>
                    <option value="bgm/Golden-Hour-chosic.com_.mp3">Golden Hour</option>
                    <option value="bgm/scott-buckley-reverie(chosic.com).mp3">Reverie</option>
                </optgroup>


        </div>
        </select>

        <!-- 🎧 미리듣기 -->
        <div style="margin-top:10px;">
            <audio id="bgmPlayer" controls style="width:300px;">
                <source id="bgmSource" src="" type="audio/mpeg">
            </audio>
        </div>

        <button type="submit">💌 예약하기</button>
    </form>

    <a href="reservation?action=list" class="link-btn">📬 도토리 예약 안 하고 그냥 돌아갈래!</a>
</div>
<script src="js/reservation-flatpickr.js"></script>
<script src="js/reservation-template.js"></script>
<script src="js/bgmplay.js"></script>

</body>
</html>