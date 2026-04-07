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
        img.src = "";
        img.style.display = 'none';
        if (previewDiv) previewDiv.style.display = 'none';
    }
}

function initTemplatePreview(selectId, previewImgId, previewDivId) {
    window.addEventListener('load', function () {
        updateTemplatePreview(selectId, previewImgId, previewDivId);
    });
}