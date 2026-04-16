document.addEventListener("DOMContentLoaded", () => {

    const track = document.getElementById("templateTrack");
    const cards = document.querySelectorAll(".template-card");

    let currentIndex = 0;
    const visibleCount = 3;

    // 카드 + gap 정확히 계산 (자동 계산)
    // const cardWidth = cards[0].offsetWidth;
    const cardWidth = 100;
    const gap = 15; // CSS gap
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


    function initTemplate(cardLength, moveWidth) {
        let hasTemplateId = false;


        // 현재 URL의 query string 가져오기
        const params = new URLSearchParams(window.location.search);

// templateId 값 추출
        const templateId = params.get('templateId');

        console.log(templateId);

        const input = document.getElementById('selectedTemplate');
        const cards = document.querySelectorAll(".template-card");
        const track = document.getElementById("templateTrack");

        if (templateId && input && cards.length > 0) {
            input.value = templateId;

            cards.forEach((c, i) => {

                c.classList.remove("active");
                const templateIdData = c.getAttribute('data-id');

                if (templateId === templateIdData) {
                    c.classList.add("active");

                    currentIndex = i;

                    track.style.transform = `translateX(-${currentIndex * moveWidth}px)`;

                    hasTemplateId = true;
                }
            });
        }


        return hasTemplateId;
    }


    const hasTemplateId = initTemplate(cards.length, moveWidth);


    // DOMContentLoaded 이벤트 마지막 부분에 추가
    if (!hasTemplateId && cards.length > 0) {
        cards[0].click(); // 첫 번째 카드를 강제로 클릭 시뮬레이션
    }

});

