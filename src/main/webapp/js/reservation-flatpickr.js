document.addEventListener("DOMContentLoaded", function () {
    const dateInput = document.getElementById("scheduledDate");
    const display = document.getElementById("selectedDateDisplay");
    const container = document.querySelector(".date-picker-container");
    const initialDate = dateInput ? dateInput.value : "";

    // 1. Flatpickr 초기화
    const fp = flatpickr("#scheduledDate", {
        enableTime: true,
        dateFormat: "Y-m-d H:i",
        defaultDate: initialDate || null,
        minDate: initialDate ? null : "today",
        time_24hr: true,
        minuteIncrement: 1,
        disableMobile: "true", // 모바일 환경에서도 동일한 UI 유지
        onChange: (selectedDates, dateStr) => {
            if (display) display.innerText = dateStr;
            dateInput.value = dateStr;
        }
    });

    // 2. 초기 표시 설정
    if (initialDate && display) {
        display.innerText = initialDate;
    }

    // 3. [핵심] 한 줄 전체 클릭 이벤트 적용
    if (container) {
        container.style.cursor = "pointer"; // 마우스 포인터 효과
        container.addEventListener("click", () => fp.open());
    }

    // 4. 폼 제출 유효성 검사
    const form = document.querySelector("form");

    if (form) {
        form.addEventListener("submit", function (e) {
            const scheduledDate = dateInput.value;

            if (!scheduledDate || scheduledDate.trim() === "") {
                e.preventDefault();

                // 경고 스타일 적용
                container.style.border = "2px solid #ff6b6b";
                container.style.backgroundColor = "#ffe0e0";
                display.innerText = "⚠️ 예약 시간을 선택해주세요";
                display.style.color = "#956534";
                display.style.fontWeight = "bold";

                // 3초 후 복구
                setTimeout(() => {
                    container.style.border = "1px solid #ccc";
                    container.style.backgroundColor = "transparent";
                    display.innerText = initialDate || "선택된 시간 없음";
                    display.style.color = "#333";
                    display.style.fontWeight = "normal";
                }, 3000);

                setTimeout(() => fp.open(), 500);
            }
        });
    }
});