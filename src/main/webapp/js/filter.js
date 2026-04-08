document.addEventListener("DOMContentLoaded", function () {
    const params = new URLSearchParams(window.location.search);
    const keyword = params.get('keyword');

    if (keyword && keyword.trim() !== "") {
        const rows = document.querySelectorAll("tbody tr");

        rows.forEach(row => {
            // 이메일(0번)과 제목(3번) 칸 확인
            const targetCells = [row.cells[0], row.cells[3]];
            let isFound = false;

            targetCells.forEach(cell => {
                if (!cell) return;

                const text = cell.textContent;
                const regex = new RegExp(`(${keyword})`, 'gi');

                if (text.match(regex)) {
                    isFound = true; // 검색어 발견!

                    // 단어 하이라이트 처리
                    const newHTML = text.replace(regex, `<span class="text-highlight">$1</span>`);
                    const strongTag = cell.querySelector('strong');
                    if (strongTag) {
                        strongTag.innerHTML = newHTML;
                    } else {
                        cell.innerHTML = newHTML;
                    }
                }
            });

            // 검색어가 발견된 행(tr) 전체에 강조 클래스 추가
            if (isFound) {
                row.classList.add('row-highlight');
            }
        });
    }
});