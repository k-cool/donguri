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
});yId("scheduledWrapper").addEventListener("click", () => fp.open());