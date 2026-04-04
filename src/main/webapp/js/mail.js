window.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("emailForm");
    const reserveInput = document.getElementById("reserveDateTime");

    form.addEventListener("submit", function (e) {
        const value = reserveInput.value;

        // 값이 없으면 진행 막기
        if (!value) {
            alert("예약 일시를 입력해주세요.");
            reserveInput.focus();
            e.preventDefault();
            return;
        }

        // 사용자가 선택한 예약 시간
        const selectedDate = new Date(value);

        // 현재 시간
        const now = new Date();

        // 과거 시간 체크
        if (selectedDate <= now) {
            alert("현재 시간 이후로만 예약할 수 있습니다.");
            reserveInput.focus();
            e.preventDefault();
            return;
        }

        // 5분 단위 체크
        // const minute = selectedDate.getMinutes();
        // if (minute % 5 !== 0) {
        //     alert("예약 시간은 5분 단위로만 설정 가능합니다.");
        //     reserveInput.focus();
        //     e.preventDefault();
        // }
    });
});