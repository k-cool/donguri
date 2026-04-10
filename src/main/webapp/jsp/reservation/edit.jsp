<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>편지 예약 수정</title>
    <link rel="stylesheet" href="css/reservation_edit.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

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
            <select name="bgmUrl" id="bgmSelect">
                <option value="" ${empty r.bgm ? 'selected' : ''}>None</option>

                <optgroup label="Japanese Style">
                    <option value="bgm/PerituneMaterial_Sakuya2(chosic.com).mp3" ${r.bgm == 'bgm/PerituneMaterial_Sakuya2(chosic.com).mp3' ? 'selected' : ''}>
                        Sakuya2
                    </option>
                    <option value="bgm/Dreaming-Under-The-Stars-MP3(chosic.com).mp3" ${r.bgm == 'bgm/Dreaming-Under-The-Stars-MP3(chosic.com).mp3' ? 'selected' : ''}>
                        Dreaming Under The Stars
                    </option>
                    <option value="bgm/PerituneMaterial_Soft_Day(chosic.com).mp3" ${r.bgm == 'bgm/PerituneMaterial_Soft_Day(chosic.com).mp3' ? 'selected' : ''}>
                        Soft Day
                    </option>
                    <option value="bgm/jayjen-ray-of-hope(chosic.com).mp3" ${r.bgm == 'bgm/jayjen-ray-of-hope(chosic.com).mp3' ? 'selected' : ''}>
                        Ray of Hope
                    </option>
                    <option value="bgm/Double-Rainbow-chosic.com_.mp3" ${r.bgm == 'bgm/Double-Rainbow-chosic.com_.mp3' ? 'selected' : ''}>
                        Double Rainbow
                    </option>
                </optgroup>

                <optgroup label="Bright">
                    <option value="bgm/Soda(chosic.com).mp3" ${r.bgm == 'bgm/Soda(chosic.com).mp3' ? 'selected' : ''}>
                        Soda
                    </option>
                    <option value="bgm/Lights(chosic.com).mp3" ${r.bgm == 'bgm/Lights(chosic.com).mp3' ? 'selected' : ''}>
                        Lights
                    </option>
                    <option value="bgm/Roa-Haru-chosic.com_.mp3" ${r.bgm == 'bgm/Roa-Haru-chosic.com_.mp3' ? 'selected' : ''}>
                        Haru
                    </option>
                    <option value="bgm/Little-Wishes-chosic.com_.mp3" ${r.bgm == 'bgm/Little-Wishes-chosic.com_.mp3' ? 'selected' : ''}>
                        Little Wishes
                    </option>
                    <option value="bgm/Lobby-Time(chosic.com).mp3" ${r.bgm == 'bgm/Lobby-Time(chosic.com).mp3' ? 'selected' : ''}>
                        Lobby Time
                    </option>
                </optgroup>

                <optgroup label="Calm">
                    <option value="bgm/Spring-Flowers(chosic.com).mp3" ${r.bgm == 'bgm/Spring-Flowers(chosic.com).mp3' ? 'selected' : ''}>
                        Spring Flowers
                    </option>
                    <option value="bgm/Fall-In-Love-chosic.com_.mp3" ${r.bgm == 'bgm/Fall-In-Love(chosic.com_.mp3' ? 'selected' : ''}>
                        Fall In Love
                    </option>
                    <option value="bgm/Transcendence-chosic.com).mp3" ${r.bgm == 'bgm/Transcendence(chosic.com).mp3' ? 'selected' : ''}>
                        Transcendence
                    </option>
                    <option value="bgm/Walking-Home(chosic.com).mp3" ${r.bgm == 'bgm/Walking-Home(chosic.com).mp3' ? 'selected' : ''}>
                        Walking Home
                    </option>
                </optgroup>

                <optgroup label="Lo-fi">
                    <option value="bgm/Colorful-Flowers(chosic.com).mp3" ${r.bgm == 'bgm/Colorful-Flowers(chosic.com).mp3' ? 'selected' : ''}>
                        Colorful Flowers
                    </option>
                    <option value="bgm/echoes-in-blue-by-tokyo-music-walker-chosic.com_.mp3" ${r.bgm == 'bgm/echoes-in-blue-by-tokyo-music-walker-chosic.com_.mp3' ? 'selected' : ''}>
                        Echoes in Blue
                    </option>
                    <option value="bgm/tokyo-music-walker-day-off-chosic.com_.mp3" ${r.bgm == 'bgm/tokyo-music-walker-day-off-chosic.com_.mp3' ? 'selected' : ''}>
                        Day Off
                    </option>
                    <option value="bgm/Late-at-Night(chosic.com).mp3" ${r.bgm == 'bgm/Late-at-Night(chosic.com).mp3' ? 'selected' : ''}>
                        Late at Night
                    </option>
                </optgroup>

                <optgroup label="Gentle/Sad">
                    <option value="bgm/a-promise(chosic.com).mp3" ${r.bgm == 'bgm/a-promise(chosic.com).mp3' ? 'selected' : ''}>
                        A Promise
                    </option>
                    <option value="bgm/Daydreams(chosic.com).mp3" ${r.bgm == 'bgm/Daydreams(chosic.com).mp3' ? 'selected' : ''}>
                        Daydreams
                    </option>
                    <option value="bgm/Golden-Hour(chosic.com).mp3" ${r.bgm == 'bgm/Golden-Hour(chosic.com).mp3' ? 'selected' : ''}>
                        Golden Hour
                    </option>
                    <option value="bgm/scott-buckley-reverie(chosic.com).mp3" ${r.bgm == 'bgm/scott-buckley-reverie(chosic.com).mp3' ? 'selected' : ''}>
                        Reverie
                    </option>
                </optgroup>
            </select>
        </div>

        <!-- Preview -->
        <div style="margin-top:10px;">
            <audio id="bgmPlayer" controls style="width:300px;">
                <source id="bgmSource" src="" type="audio/mpeg">
            </audio>
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
<script src="js/bgmplay.js"></script>
<script>
    window.onload = function () {
        updateTemplatePreview('templateSelect', 'previewImg', 'templatePreview');
    };
</script>
</body>
</html>
