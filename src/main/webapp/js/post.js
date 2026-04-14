const clickable = document.querySelector(".clickable");
const clickGuide = document.querySelector(".click-guide");
const openables = document.querySelectorAll(".openable");
const stamp = document.querySelector(".stamp");
const imgWrapper = document.querySelector(".img-wrapper");
const player = document.querySelector(".player");

handleClickClickable = (e) => {
    openables.forEach(clickable => {
        const isClosed = clickable.classList.contains("closed");

        if (isClosed) {
            stamp.classList.add("show");
            imgWrapper.classList.add("show");
            clickGuide.classList.add("off");
            player.classList.add("active");
            audio.play();
            btn.textContent = "🎵 재생중";
        } else {
            stamp.classList.remove("show");
            imgWrapper.classList.remove("show");
            clickGuide.classList.remove("off");
            player.classList.remove("active");
            audio.pause();
            btn.textContent = "🔇 정지됨";
        }

        clickable.classList.toggle("closed")
    })
}

clickable.addEventListener("click", handleClickClickable);

// bgm
const audio = document.getElementById("bgmPlayer");
const btn = document.getElementById("bgmToggle");

btn.addEventListener("click", () => {
    if (audio.paused) {
        audio.play();
        btn.textContent = "🎵 재생중";
    } else {
        audio.pause();
        btn.textContent = "🔇 정지됨";
    }
});