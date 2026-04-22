function loadConfirm() {

    // session storage에서 가져오기
    const mapObj = JSON.parse(sessionStorage.getItem('reservation'));

    // 없으면 그냥 리턴
    if (!mapObj) return;

    // 지금 화면의 모든 input을 선택
    const inputs = document.querySelectorAll('input');

    // 선택된 요소를 순회하면서 같은 name을 가진 input에 mapObj의 value를 저장
    inputs.forEach(input => {
        if (!input.name || input.name === "action") return;

        console.log(input.name);


        if (input.name === "coverImgUrl") {
            input.value = mapObj["fileUrl"];
            return;
        }

        input.value = mapObj[input.name];
    });

    // // textarea 별도 처리
    // const textarea = document.querySelector('textarea');
    // console.log(textarea);


    // textarea.value = mapObj[textarea.name];


    // sessionStorage.removeItem('reservation');

    // content 줄맞춤
    const contentText = document.querySelector(".content-text");

    console.log("??", contentText)
    if (contentText && contentText.innerText) {
        const temp = contentText.innerText;
        contentText.innerText = "";
        console.log("??", temp.split("<br>"))

        temp.split("\n").forEach(text => {
            const line = document.createElement("div");
            line.innerText = text;
            contentText.appendChild(line);

            console.log("??")
        })
    }
}

(() => {
    loadConfirm();
})();

