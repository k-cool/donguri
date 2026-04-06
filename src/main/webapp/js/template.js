/**
 * 템플릿 선택 시 미리보기 이미지를 변경하는 함수
 * @param {string} selectId - select 태그의 ID
 * @param {string} previewImgId - img 태그의 ID
 * @param {string} previewDivId - img를 감싸고 있는 div의 ID (선택사항)
 */
function updateTemplatePreview(selectId, previewImgId, previewDivId) {
    const select = document.getElementById(selectId);
    const img = document.getElementById(previewImgId);
    const previewDiv = document.getElementById(previewDivId);

    if (!select || !img) return;

    const selectedOption = select.options[select.selectedIndex];
    const imgUrl = selectedOption.getAttribute('data-img');

    if (imgUrl && imgUrl.trim() !== "") {
        img.src = imgUrl;
        img.style.display = 'inline-block';
        if (previewDiv) previewDiv.style.display = 'block';
    } else {
        img.style.display = 'none';
        if (previewDiv) previewDiv.style.display = 'none';
    }
}


function initTemplatePreview(selectId, previewImgId, previewDivId) {
    window.addEventListener('load', function () {
        updateTemplatePreview(selectId, previewImgId, previewDivId);
    });
}