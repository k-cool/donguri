document.addEventListener('DOMContentLoaded', function () {
    const templateSelect = document.getElementById('templateSelect');
    const previewImg = document.getElementById('previewImg');

    if (templateSelect && previewImg) {
        templateSelect.addEventListener('change', function () {
            // 1. 선택된 <option> 요소를 가져옵니다.
            const selectedOption = this.options[this.selectedIndex];

            // 2. option에 저장해둔 data-url 값을 가져옵니다.
            const imgUrl = selectedOption.getAttribute('data-url');

            // 3. 이미지 URL이 있고, 빈 값이 아니면 보여줍니다.
            if (imgUrl && imgUrl.trim() !== "") {
                previewImg.src = imgUrl;
                previewImg.style.display = 'block'; // 숨겨져 있던 이미지 표시
            } else {
                // 선택이 해제되었거나 URL이 없으면 숨깁니다.
                previewImg.src = '';
                previewImg.style.display = 'none';
            }
        });
    }
});