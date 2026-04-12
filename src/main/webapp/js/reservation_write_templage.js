document.addEventListener("DOMContentLoaded", () => {

    const track = document.getElementById("templateTrack");
    const cards = document.querySelectorAll(".template-card");

    let currentIndex = 0;
    const visibleCount = 3;

    // 카드 + gap 정확히 계산 (자동 계산)
    const cardWidth = cards[0].offsetWidth;
    const gap = 12; // CSS gap
    const moveWidth = cardWidth + gap;

    /* ▶ 오른쪽 */
    document.getElementById("tempRight").addEventListener("click", () => {
        if (currentIndex < cards.length - visibleCount) {
            currentIndex++;
            track.style.transform = `translateX(-${currentIndex * moveWidth}px)`;
        }
    });

    /* ◀ 왼쪽 */
    document.getElementById("tempLeft").addEventListener("click", () => {
        if (currentIndex > 0) {
            currentIndex--;
            track.style.transform = `translateX(-${currentIndex * moveWidth}px)`;
        }
    });

});