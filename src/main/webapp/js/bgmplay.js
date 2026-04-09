document.addEventListener("DOMContentLoaded", function () {

    const select = document.getElementById("bgmSelect");
    const player = document.getElementById("bgmPlayer");
    const source = document.getElementById("bgmSource");

    select.addEventListener("change", function () {

        const selectedUrl = select.value;

        // 없음 선택 시
        if (!selectedUrl) {
            player.pause();
            source.src = "";
            player.load();
            return;
        }

        // 선택한 음악 적용
        source.src = selectedUrl;
        player.load();

        // 자동 재생 시도
        player.play().catch(err => {
            console.log("자동재생 막힘:", err);
        });
    });

});