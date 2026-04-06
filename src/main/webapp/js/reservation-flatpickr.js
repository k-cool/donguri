// reservation-flatpickr.js
document.addEventListener("DOMContentLoaded", function() {
    const now = new Date();
    now.setMinutes(now.getMinutes() + 1);

    const fp = flatpickr("#scheduledDate", {
        enableTime: true,
        dateFormat: "Y-m-d H:i",
        minDate: now,
        time_24hr: true,
        minuteIncrement: 1,
        onChange: (selectedDates, dateStr) => {
            document.getElementById("selectedDateDisplay").innerText = dateStr;
        }
    });

    document.getElementById("scheduledWrapper").addEventListener("click", () => fp.open());
});


    document.querySelector("form").addEventListener("submit", function(e) {
        const scheduledDate = document.getElementById("scheduledDate").value;
        
        if (!scheduledDate || scheduledDate.trim() === "") {
            e.preventDefault();

            const datePickerContainer = document.querySelector(".date-picker-container");
            datePickerContainer.style.border = "2px solid #ff6b6b";
            datePickerContainer.style.backgroundColor = "#ffe0e0";
            

            const selectedDateDisplay = document.getElementById("selectedDateDisplay");
            selectedDateDisplay.innerText = "⚠️ 예약 시간을 선택해주세요";
            selectedDateDisplay.style.color = "#956534";
            selectedDateDisplay.style.fontWeight = "bold";
            

            setTimeout(() => {
                datePickerContainer.style.border = "1px solid #ccc";
                datePickerContainer.style.backgroundColor = "transparent";
                selectedDateDisplay.innerText = "선택된 시간 없음";
                selectedDateDisplay.style.color = "#333";
                selectedDateDisplay.style.fontWeight = "normal";
            }, 3000);
            

            setTimeout(() => fp.open(), 500);
        }
    });