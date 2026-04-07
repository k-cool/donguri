document.getElementById('templateSelect').addEventListener('change', function () {
    const templateId = this.value;
    const previewImg = document.getElementById('previewImg');

    if (!templateId) {
        previewImg.style.display = 'none';
        return;
    }


    fetch(`/api/templates/${templateId}`)
        .then(response => response.json())
        .then(data => {
            if (data.coverImgUrl) {
                previewImg.src = data.coverImgUrl;
                previewImg.style.display = 'block';
            }
        })
        .catch(err => console.error('이미지 로드 실패:', err));
});