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

    // 템플릿 카드 클릭 이벤트
    cards.forEach((card, index) => {
        card.addEventListener("click", () => {
            // 기존 active 클래스 제거
            cards.forEach(c => c.classList.remove("active"));
            
            // 클릭된 카드에 active 클래스 추가
            card.classList.add("active");
            
            // 선택된 템플릿 ID를 hidden input에 설정
            const selectedTemplateInput = document.getElementById("selectedTemplate");
            if (selectedTemplateInput) {
                const templateId = card.getAttribute('data-id');
                selectedTemplateInput.value = templateId;
                console.log("selected template:", templateId);
            }
        });
    });

});
