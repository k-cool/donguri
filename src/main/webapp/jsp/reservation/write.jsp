<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>편지 예약 작성</title>
    <link rel="stylesheet" href="css/reservation_write.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
    <meta name="viewport" content="width=device-width, initial-scale=1">

</head>
<body>

<div class="form-container">
    <div>

        <form action="reservation" method="post">
            <h2>너무나도 그리운 상대에게</h2>
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

                    <optgroup label="Japanese Mood">
                        <option value="bgm/PerituneMaterial_Sakuya2(chosic.com).mp3">바람에 실려온 작은 마음</option>
                        <option value="bgm/Dreaming-Under-The-Stars-MP3(chosic.com).mp3">오늘을 담아</option>
                        <option value="bgm/PerituneMaterial_Soft_Day(chosic.com).mp3">말로 다 하지 못한 감정을 담아서</option>
                        <option value="bgm/jayjen-ray-of-hope(chosic.com).mp3">사라지지 않는 하루</option>
                        <option value="bgm/Double-Rainbow-chosic.com_.mp3">흘러가는 시간 위에</option>
                    </optgroup>

                    <optgroup label="Playful Day">
                        <option value="bgm/Soda(chosic.com).mp3">별사탕 스텝</option>
                        <option value="bgm/Lights(chosic.com).mp3">슬라임 마을의 아침</option>
                        <option value="bgm/Roa-Haru-chosic.com_.mp3">작은 세계, 큰 모험</option>
                        <option value="bgm/Little-Wishes-chosic.com_.mp3">♪ ON</option>
                        <option value="bgm/Lobby-Time(chosic.com).mp3">말랑말랑</option>
                    </optgroup>

                    <optgroup label="Midnight Blue">
                        <option value="bgm/Spring-Flowers(chosic.com).mp3">밤의 여운</option>
                        <option value="bgm/Fall-In-Love-chosic.com_.mp3">고요한 마음</option>
                        <option value="bgm/Transcendence-chosic.com_.mp3">Calm Journey</option>
                        <option value="bgm/Walking-Home-chosic.com_.mp3">조용히 전해지는 이야기</option>
                    </optgroup>

                    <optgroup label="Lofi Chill">
                        <option value="bgm/Colorful-Flowers(chosic.com).mp3">Stay Cozy</option>
                        <option value="bgm/echoes-in-blue-by-tokyo-music-walker-chosic.com_.mp3">雨の音 (Rainy Mood)
                        </option>
                        <option value="bgm/tokyo-music-walker-day-off-chosic.com_.mp3">Tokyo Night Drive</option>
                        <option value="bgm/Late-at-Night(chosic.com).mp3">Coffee & Chill</option>
                    </optgroup>

                    <optgroup label="Calm & Sad">
                        <option value="bgm/a-promise(chosic.com).mp3">Rainy Window</option>
                        <option value="bgm/Daydreams-chosic.com_.mp3">흐르지 못한 이야기</option>
                        <option value="bgm/Golden-Hour-chosic.com_.mp3">전하지 못한 말</option>
                        <option value="bgm/scott-buckley-reverie(chosic.com).mp3">조용한 눈물</option>
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
    </div>
    <a href="reservation?action=list" class="link-btn">📬 도토리 예약 안 하고 그냥 돌아갈래!</a>
</div>
<script src="js/reservation-flatpickr.js"></script>
<script src="js/reservation-template.js"></script>
<script src="js/bgmplay.js"></script>

</body>
</html>