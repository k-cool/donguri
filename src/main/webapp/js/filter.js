document.addEventListener("DOMContentLoaded", function () {
    const params = new URLSearchParams(window.location.search);
    const keyword = params.get('keyword');

    if (keyword && keyword.trim() !== "") {
        const cards = document.querySelectorAll(".acorn-card");

        cards.forEach(card => {
            const subjectElement = card.querySelector(".subject");
            if (!subjectElement) return;

            const text = subjectElement.textContent;
            const regex = new RegExp(`(${keyword})`, 'gi');

            if (text.match(regex)) {
                const newHTML = text.replace(regex, `<span class="text-highlight">$1</span>`);
                subjectElement.innerHTML = newHTML;
                card.classList.add('row-highlight');
            }
        });
    }

    const filterSelects = document.querySelectorAll(".filter-basket select");
    filterSelects.forEach(select => {
        select.addEventListener("change", function () {
            this.form.submit();
        });
    });
});