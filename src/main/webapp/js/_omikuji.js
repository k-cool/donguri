const squirrel = document.getElementById('squirrel');
const acorn = document.getElementById('acorn');
const overlay = document.getElementById('modal-overlay');


window.onload = function () {
    console.log("omikuji.js loaded");

    let isMoving = false;

    squirrel?.addEventListener('click', function () {
        if (isMoving) return;
        isMoving = true;

        drawOmikuji()

        const rect = squirrel.getBoundingClientRect();
        const startPos = {
            x: rect.left + rect.width / 2,
            y: rect.top + rect.height / 2
        };

        const treePos = {
            x: window.innerWidth - 180,
            y: 180
        };

        // 1. 나무로 가기
        moveTo(startPos, treePos, 1500, function () {
            acorn.style.opacity = '1';
            acorn.classList.remove('active'); // 초기화

            setTimeout(function () {
                // 2. 돌아오기
                const currentRect = squirrel.getBoundingClientRect();
                const currentPos = {
                    x: currentRect.left + currentRect.width / 2,
                    y: currentRect.top + currentRect.height / 2
                };

                moveTo(currentPos, startPos, 1500, function () {
                    isMoving = false;
                    // 수정: 도토리를 사라지게 하지 않고 효과 부여
                    acorn.classList.add('active');

                    // 약간의 딜레이 후 모달창 띄우기
                    setTimeout(showModal, 500);
                });
            }, 800);
        });
    });

    function moveTo(start, target, duration, callback) {
        const startTime = Date.now();
        squirrel.classList.add('hopping');

        function update() {
            const now = Date.now();
            const elapsed = now - startTime;
            const progress = Math.min(elapsed / duration, 1);

            const currentX = start.x + (target.x - start.x) * progress;
            const currentY = start.y + (target.y - start.y) * progress;

            squirrel.style.left = currentX + "px";
            squirrel.style.top = currentY + "px";

            if (acorn.style.opacity === "1") {
                const computedStyle = window.getComputedStyle(squirrel);
                const matrix = computedStyle.transform;
                let translateY = 0;
                if (matrix && matrix !== 'none') {
                    const values = matrix.split('(')[1].split(')')[0].split(',');
                    translateY = parseFloat(values[5]);
                }
                acorn.style.left = (currentX - 20) + "px";
                acorn.style.top = (currentY - 20 + translateY) + "px";
            }

            if (progress < 1) {
                requestAnimationFrame(update);
            } else {
                squirrel.classList.remove('hopping');
                if (callback) callback();
            }
        }

        requestAnimationFrame(update);
    }

    function showModal() {
        overlay.style.display = 'flex';
    }
};


function drawOmikuji() {
    console.log("draw-btn");

    const $luck = $("#luck")
    const $message = $("#message")
    $message.empty();

    $.ajax({
        url: "omikuji",
        type: "POST",
        data: {},
        success: (resData) => {
            console.log(resData);

            const omikuji = resData.omikuji;

            $luck.append(`${omikuji.luck}`);
            $message.append(`${omikuji.message}`);

        },
        error: (error) => {
            // 중복 메시지 삭제하고 로그만 남김
            console.log("오미쿠지 상태: " + error.status);
        }
    })
}

function closeModal() {
    squirrel.style.display = 'none';
    acorn.style.opacity = '0';
    acorn.classList.remove('active');
    overlay.style.display = 'none';

    location.href = "omikuji";
}