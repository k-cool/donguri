/*
    도토리 떨어지는 애니메이션
*/

console.log("home.js");

var acorn = document.getElementById("acorn");
var bottomGroup = document.getElementById("bottomGroup");
var scene = document.getElementById("scene");

var x = window.innerWidth / 2 - 25;
var y = 150;
var velocityY = 0;
var velocityX = 0;
var gravity = 0.25;
var angle = 0;
var phase = "sky";
var startTime = Date.now();
var cloudsVisible = true; // 구름 상태 관리 변수

// ☁️ 구름 생성
var clouds = [];
for (var i = 0; i < 10; i++) {
    var el = document.createElement("div");
    el.className = "cloud";
    el.innerText = "☁️";
    scene.appendChild(el);
    clouds.push({
        el: el,
        x: Math.random() * window.innerWidth,
        y: Math.random() * window.innerHeight,
        speed: 5 + Math.random() * 10
    });
}

// 💡 구름을 사라지게 하는 별도 함수
function hideClouds() {
    if (!cloudsVisible) return; // 이미 실행 중이면 무시
    cloudsVisible = false;

    var cloudElements = document.querySelectorAll('.cloud');
    for (var i = 0; i < cloudElements.length; i++) {
        cloudElements[i].style.opacity = "0"; // 서서히 투명하게
    }

    // 투명해진 후 요소를 완전히 제거 (성능 최적화)
    setTimeout(function () {
        for (var i = 0; i < cloudElements.length; i++) {
            if (cloudElements[i].parentNode) {
                cloudElements[i].parentNode.removeChild(cloudElements[i]);
            }
        }
    }, 500);
}

function animate() {
    var now = Date.now();
    var elapsed = (now - startTime) / 1000;

    // 1. 구름 이동 (구름이 보일 때만 계산)
    if (cloudsVisible) {
        for (var i = 0; i < clouds.length; i++) {
            var c = clouds[i];
            c.y -= c.speed;
            if (c.y < -100) {
                c.y = window.innerHeight + 50;
                c.x = Math.random() * window.innerWidth;
            }
            c.el.style.transform = "translate(" + c.x + "px, " + c.y + "px)";
        }
    }

    // 2. 단계별 물리 로직
    if (phase === "sky") {
        y = 150 + Math.sin(elapsed * 5) * 5;
        angle = Math.sin(elapsed * 10) * 20;

        if (elapsed > 2) {
            phase = "fall";
            bottomGroup.className = "bottom-group show";
        }
    } else if (phase === "fall" || phase === "bounce") {
        velocityY += gravity;
        y += velocityY;
        x += velocityX;
        angle += velocityX;
    }

    // 3. 충돌 계산
    var mailboxTop = window.innerHeight - 170;
    var groundTop = window.innerHeight - 140;

    // 🎯 [수정된 시점] 우체통에 부딪히기 직전에 구름 제거 함수 호출
    if (phase === "fall" && y >= (mailboxTop - 500)) {
        hideClouds(); // 지면에 도착하기 전에 구름 삭제 실행
    }

    // 우체통 실제 충돌
    if (phase === "fall" && y >= mailboxTop) {
        y = mailboxTop;
        velocityY = -8;
        velocityX = 5;
        phase = "bounce";
    }

    // 바닥 충돌 및 정지
    if (phase === "bounce" && y >= groundTop) {
        y = groundTop;
        velocityY *= -0.5;
        velocityX *= 0.8;
        if (Math.abs(velocityY) < 1) {
            phase = "stop";
        }
    }

    // 4. 도토리 렌더링
    acorn.style.transform = "translate(" + x + "px, " + y + "px) rotate(" + angle + "deg)";

    if (phase !== "stop") {
        requestAnimationFrame(animate);
    }
}

animate();