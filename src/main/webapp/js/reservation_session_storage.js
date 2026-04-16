console.log("reservation_session_storage.js");

function saveReservation(e) {
    // e.preventDefault();

    const mapObj = {};
// 지금 화면의 모든 input을 선택
    const inputs = document.querySelectorAll('input');

// 선택된 요소를 순회하면서 mapObj객체에 key, value로 name, value를 저장
    inputs.forEach(input => {
        mapObj[input.name] = input.value;
    });

    // textarea 선택
    const textarea = document.querySelector('textarea');
    mapObj[textarea.name] = textarea.value;

    // session storage에 저장
    sessionStorage.setItem('reservation', JSON.stringify(mapObj));

    // e.submit();
}

function loadSavedReservation() {
    console.log("loadSavedReservation");

    // session storage에서 가져오기
    const mapObj = JSON.parse(sessionStorage.getItem('reservation'));

    // 없으면 그냥 리턴
    if (!mapObj) return;

    // 지금 화면의 모든 input을 선택
    const inputs = document.querySelectorAll('input');

    // 선택된 요소를 순회하면서 같은 name을 가진 input에 mapObj의 value를 저장
    inputs.forEach(input => {
        if (input.name === "coverImgUrl") {
            // file input은 value에 직접 값을 넣을수 없음. 브라우저가 보안상 막는다.
            return;
        }

        input.value = mapObj[input.name];
    });

    // textarea 별도 처리
    const textarea = document.querySelector('textarea');
    textarea.value = mapObj[textarea.name];

    sessionStorage.removeItem('reservation');
}

(() => {
    loadSavedReservation();
})();
