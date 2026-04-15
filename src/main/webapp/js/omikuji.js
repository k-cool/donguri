let isMoving = false;

window.onload = function () {
    const squirrel = document.getElementById('squirrel');
    const goImg = document.getElementById('squirrel_go');
    const comeImg = document.getElementById('squirrel_come');
    const tree = document.getElementById('tree');
    const overlay = document.getElementById('modal-overlay');

    if (squirrel && tree) {
        // [중요] 나무를 못질하듯 고정하는 작업
        // right 속성을 지우고 현재 위치를 left 값으로 강제 고정합니다.
        const initialTreeX = tree.offsetLeft;
        const initialTreeY = tree.offsetTop;

        tree.style.right = 'auto'; // right 속성 제거
        tree.style.left = initialTreeX + 'px'; // 고정된 left 값 부여
        tree.style.top = initialTreeY + 'px';

        squirrel.addEventListener('click', function () {
            if (isMoving) return;
            isMoving = true;

            drawOmikuji();

            const startX = squirrel.offsetLeft;
            const startY = squirrel.offsetTop;

            // 이미 고정된 나무 좌표를 기준으로 목표 지점 설정
            const targetX = initialTreeX - 100;
            const targetY = initialTreeY + 130;

            moveTo(squirrel, startX, startY, targetX, targetY, 1500, function () {
                goImg.style.opacity = '0';
                setTimeout(() => {
                    goImg.style.display = 'none';

                    // 나무 흔들기 (좌표와 무관한 transform 애니메이션만 실행)
                    tree.classList.add('shake');

                    setTimeout(() => {
                        tree.classList.remove('shake');
                        comeImg.style.display = 'block';
                        setTimeout(() => { comeImg.style.opacity = '1'; }, 50);

                        setTimeout(() => {
                            moveTo(squirrel, targetX, targetY, startX, startY, 1500, function () {
                                isMoving = false;
                                if (overlay) overlay.style.display = 'flex';
                            });
                        }, 500);
                    }, 600);
                }, 500);
            });
        });
    }

    function moveTo(element, startX, startY, endX, endY, duration, callback) {
        const startTime = Date.now();
        element.classList.add('hopping');

        function update() {
            const now = Date.now();
            const elapsed = now - startTime;
            const progress = Math.min(elapsed / duration, 1);

            element.style.left = (startX + (endX - startX) * progress) + "px";
            element.style.top = (startY + (endY - startY) * progress) + "px";

            if (progress < 1) {
                requestAnimationFrame(update);
            } else {
                element.classList.remove('hopping');
                if (callback) callback();
            }
        }
        requestAnimationFrame(update);
    }
};

function drawOmikuji() {
    const $luck = $("#luck");
    const $message = $("#message");
    $luck.empty();
    $message.empty();

    $.ajax({
        url: "omikuji",
        type: "POST",
        success: (resData) => {
            if (resData.omikuji) {
                $luck.text(resData.omikuji.luck);
                $message.text(resData.omikuji.message);
            }
        },
        error: (error) => {
            if (error.status === 409) {
                $message.text("오늘은 이미 운세를 확인하셨습니다. 내일 다시 만나요!");
            }
        }
    });
}

function closeModal() {
    const overlay = document.getElementById('modal-overlay');
    if (overlay) overlay.style.display = 'none';
    location.reload();
}