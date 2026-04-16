function uploadImg() {
    const fileInput = document.getElementById("fileInput");

    const formData = new FormData();
    formData.append("coverImgUrl", fileInput.files[0]);

    fetch("/reservation", {
        method: "PUT",
        headers: {
            // "Content-Type": "multipart/form-data"
        },
        body: formData
    }).then(res => {
        return res.text();
    }).then(data => {
        console.log(data);
        const input = document.querySelector("input[name='fileUrl']");
        input.value = data;
    }).catch(console.error);
}

const fileInput = document.getElementById("fileInput");
fileInput.addEventListener("change", uploadImg);