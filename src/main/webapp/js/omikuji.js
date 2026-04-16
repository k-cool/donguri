let isMoving = false;

window.onload = function () {
    const squirrel = document.getElementById('squirrel');
    const goImg = document.getElementById('squirrel_go');
    const comeImg = document.getElementById('squirrel_come');
    const tree = document.getElementById('tree');
    const overlay = document.getElementById('modal-overlay');

    if (squirrel && tree) {
        squirrel.addEventListener('click', function () {
            if (isMoving) return;
            isMoving = true;

            drawOmikuji();

            const startX = squirrel.offsetLeft;
            const startY = squirrel.offsetTop;

            const treeRect = tree.getBoundingClientRect();
            const targetX = treeRect.left + (treeRect.width / 3.8);
            const targetY = treeRect.top + (treeRect.height / 2.8);

            // 1. 나무로 이동
            moveTo(squirrel, startX, startY, targetX, targetY, 1500, function () {
                goImg.style.opacity = '0';
                setTimeout(() => {
                    goImg.style.display = 'none';
                    tree.classList.add('shake');

                    setTimeout(() => {
                        tree.classList.remove('shake');
                        comeImg.style.display = 'block';
                        setTimeout(() => {
                            comeImg.style.opacity = '1';
                        }, 50);

                        // 2. 다시 원래 자리(startX, startY)로 복귀
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

            if (progress < 1) requestAnimationFrame(update);
            else {
                element.classList.remove('hopping');
                if (callback) callback();
            }
        }

        requestAnimationFrame(update);
    }
};


const TEXT_MAP = {
    BIG_GOOD: "대길",
    MIDDLE_GOOD: "중길",
    SMALL_GOOD: "소길",
    SMALL_BAD: "소흉",
    BAD: "흉",
};

function drawOmikuji() {
    $.ajax({
        url: "omikuji",
        type: "POST",
        success: (resData) => {
            if (resData.omikuji) {
                $("#luck").text(TEXT_MAP[resData.omikuji.luck]);
                $("#message").text(resData.omikuji.message);
            }
        }
    });
}

function closeModal() {
    location.reload();
}

