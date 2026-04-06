document.addEventListener("DOMContentLoaded", function () {

    const dateInput = document.getElementById("scheduledDate");
    const initialDate = dateInput.value;

    const now = new Date();

    const minDateSetting = initialDate ? null : now;

    const fp = flatpickr("#scheduledDate", {
        enableTime: true,
        dateFormat: "Y-m-d H:i",
        defaultDate: initialDate || null, // 기존 날짜가 있으면 달력에 표시
        minDate: minDateSetting,
        time_24hr: true,
        minuteIncrement: 1,
        onChange: (selectedDates, dateStr) => {
            document.getElementById("selectedDateDisplay").innerText = dateStr;
            dateInput.value = dateStr; // hidden input에 값 동기화
        }
    });


    if (initialDate) {
        document.getElementById("selectedDateDisplay").innerText = initialDate;
    }

    document.getElementById("scheduledWrapper").addEventListener("click", () => fp.open());

    // 폼 제출 시 유효성 검사 로직
    document.querySelector("form").addEventListener("submit", function (e) {
        const scheduledDate = document.getElementById("scheduledDate").value;

        if (!scheduledDate || scheduledDate.trim() === "") {
            e.preventDefault();

            const datePickerContainer = document.querySelector(".date-picker-container");
            const selectedDateDisplay = document.getElementById("selectedDateDisplay");

            datePickerContainer.style.border = "2px solid #ff6b6b";
            datePickerContainer.style.backgroundColor = "#ffe0e0";
            selectedDateDisplay.innerText = "⚠️ 예약 시간을 선택해주세요";
            selectedDateDisplay.style.color = "#956534";
            selectedDateDisplay.style.fontWeight = "bold";

            setTimeout(() => {
                datePickerContainer.style.border = "1px solid #ccc";
                datePickerContainer.style.backgroundColor = "transparent";
                selectedDateDisplay.innerText = initialDate || "선택된 시간 없음";
                selectedDateDisplay.style.color = "#333";
                selectedDateDisplay.style.fontWeight = "normal";
            }, 3000);

            setTimeout(() => fp.open(), 500);
        }
    });
});