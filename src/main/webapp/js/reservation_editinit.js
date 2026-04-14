document.addEventListener('DOMContentLoaded', function () {
    // 1. 템플릿 클릭 이벤트 연결 (기존 파일에 없다면 추가)
    const templateCards = document.querySelectorAll('.template-card');
    const selectedTemplateInput = document.getElementById('selectedTemplate');

    templateCards.forEach(card => {
        card.addEventListener('click', function () {
            templateCards.forEach(c => c.classList.remove('active'));
            this.classList.add('active');
            selectedTemplateInput.value = this.dataset.id;
        });
    });

    // 2. 초기 BGM 재생기 설정
    const bgmPlayer = document.getElementById('bgmPlayer');
    const initialBgm = document.getElementById('selectedBgm').value;

    if (initialBgm) {
        bgmPlayer.src = initialBgm;
        // 필요하다면 여기서 곡 제목 표시 로직을 추가하세요.
    }

    // 3. (선택사항) 선택된 카드가 슬라이더 밖이면 해당 위치로 스크롤 이동 로직 등을 추가할 수 있습니다.
});