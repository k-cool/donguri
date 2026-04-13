document.addEventListener("DOMContentLoaded", function () {
    // 1. 기존 select 기반의 모든 곡 데이터
    const bgmData = {
        "Japanese Mood": [
            {title: "바람에 실려온 작은 마음", url: "bgm/PerituneMaterial_Sakuya2(chosic.com).mp3"},
            {title: "오늘을 담아", url: "bgm/Dreaming-Under-The-Stars-MP3(chosic.com).mp3"},
            {title: "말로 다 하지 못한 감정을 담아서", url: "bgm/PerituneMaterial_Soft_Day(chosic.com).mp3"},
            {title: "사라지지 않는 하루", url: "bgm/jayjen-ray-of-hope(chosic.com).mp3"},
            {title: "흘러가는 시간 위에", url: "bgm/Double-Rainbow-chosic.com_.mp3"}
        ],
        "Midnight Blue": [
            {title: "밤의 여운", url: "bgm/Spring-Flowers(chosic.com).mp3"},
            {title: "고요한 마음", url: "bgm/Fall-In-Love-chosic.com_.mp3"},
            {title: "Calm Journey", url: "bgm/Transcendence-chosic.com_.mp3"},
            {title: "조용히 전해지는 이야기", url: "bgm/Walking-Home-chosic.com_.mp3"}
        ],
        "Playful Day": [
            {title: "별사탕 스텝", url: "bgm/Soda(chosic.com).mp3"},
            {title: "슬라임 마을의 아침", url: "bgm/Lights(chosic.com).mp3"},
            {title: "작은 세계, 큰 모험", url: "bgm/Roa-Haru-chosic.com_.mp3"},
            {title: "♪ ON", url: "bgm/Little-Wishes-chosic.com_.mp3"},
            {title: "말랑말랑", url: "bgm/Lobby-Time(chosic.com).mp3"}
        ],
        "Lofi Chill": [
            {title: "Stay Cozy", url: "bgm/Colorful-Flowers(chosic.com).mp3"},
            {title: "雨の音 (Rainy Mood)", url: "bgm/echoes-in-blue-by-tokyo-music-walker-chosic.com_.mp3"},
            {title: "Tokyo Night Drive", url: "bgm/tokyo-music-walker-day-off-chosic.com_.mp3"},
            {title: "Coffee & Chill", url: "bgm/Late-at-Night(chosic.com).mp3"}
        ],
        "Calm & Sad": [
            {title: "Rainy Window", url: "bgm/a-promise(chosic.com).mp3"},
            {title: "흐르지 못한 이야기", url: "bgm/Daydreams-chosic.com_.mp3"},
            {title: "전하지 못한 말", url: "bgm/Golden-Hour-chosic.com_.mp3"},
            {title: "조용한 눈물", url: "bgm/scott-buckley-reverie(chosic.com).mp3"}
        ]
    };

    const track = document.getElementById('bgmTrack');
    const tabs = document.querySelectorAll('.tab-btn');
    const player = document.getElementById('bgmPlayer');
    const titleDisplay = document.getElementById('currentSongTitle');
    const selectedInput = document.getElementById('selectedBgm');

    // 2. 탭 전환 기능
    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            tabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');
            renderBgms(tab.dataset.genre);
        });
    });

    // 3. 곡 목록 생성 기능
    function renderBgms(genre) {
        track.innerHTML = '';
        track.style.transform = 'translateX(0)';

        bgmData[genre].forEach(song => {
            const card = document.createElement('div');
            card.className = 'bgm-card';

            card.onclick = () => {
                document.querySelectorAll('.bgm-card').forEach(c => c.classList.remove('active'));
                card.classList.add('active');

                player.src = song.url;
                player.play();
                titleDisplay.innerText = "♪ " + song.title + " ♪";
                selectedInput.value = song.url;
            };

            track.appendChild(card);
        });
    }

    // 4. 슬라이더 이동 버튼
    let currentX = 0;
    document.querySelector('.bgm-arrow.right').onclick = () => {
        if (currentX > -(track.scrollWidth - 250)) {
            currentX -= 77;
            track.style.transform = `translateX(${currentX}px)`;
        }
    };
    document.querySelector('.bgm-arrow.left').onclick = () => {
        if (currentX < 0) {
            currentX += 77;
            track.style.transform = `translateX(${currentX}px)`;
        }
    };

    // 초기 로드
    renderBgms("Japanese Mood");
});