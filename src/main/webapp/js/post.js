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
        } else {
            stamp.classList.remove("show");
            imgWrapper.classList.remove("show");
            clickGuide.classList.remove("off");
            player.classList.remove("active");
            audio.pause();
        }

        clickable.classList.toggle("closed")
    })
}

clickable.addEventListener("click", handleClickClickable);

// bgm
const audio = document.getElementById("bgmPlayer");
const btn = document.getElementById("bgmToggle");
const playImg = document.getElementById("bgmTogglePlay");
const stopImg = document.getElementById("bgmToggleStop");


const toggleBgmPlayer = () => {
    if (audio.paused) {
        playImg.style.display = "none";
        stopImg.style.display = "block";
        console.log("play!")
        audio.play();
    } else {
        playImg.style.display = "block";
        stopImg.style.display = "none";
        console.log("pause!")
        audio.pause();
    }
};

btn.addEventListener("click", toggleBgmPlayer);