/*
    도토리 떨어지는 애니메이션
*/

console.log("home.js");

var acorn = document.getElementById("acorn");
var bottomGroup = document.getElementById("bottomGroup");
var scene = document.getElementById("scene");

// 💡 수정포인트 1: 화면 전체(window)가 아닌 도화지(scene)의 너비를 기준으로 중앙 계산
var x = scene.clientWidth / 2 - 25;
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
        // 💡 수정포인트 2: 구름 생성 위치도 scene 기준으로 변경
        x: Math.random() * scene.clientWidth,
        y: Math.random() * scene.clientHeight,
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
                // 💡 수정포인트 3: 구름 재배치 위치도 scene 기준
                c.y = scene.clientHeight + 50;
                c.x = Math.random() * scene.clientWidth;
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

        // 🎯 [새로 추가된 부분] 좌우 화면 밖으로 나가지 않도록 투명 벽 만들기
        var acornWidth = 50; // 도토리 너비
        if (x < 0) {
            x = 0; // 왼쪽 벽에 부딪히면
            velocityX *= -0.8; // 반대로 튕겨냄
        } else if (x > scene.clientWidth - acornWidth) {
            x = scene.clientWidth - acornWidth; // 오른쪽 벽에 부딪히면
            velocityX *= -0.8; // 반대로 튕겨냄
        }
    }

    // 3. 충돌 계산
    // 💡 수정포인트 4: 충돌 기준점을 window.innerHeight에서 scene.clientHeight로 변경
    var mailboxTop = scene.clientHeight - 170;

    // 💡 수정포인트 5: 땅바닥 두께(100px) + 도토리 높이(50px) = 150px
    var groundTop = scene.clientHeight - 150;

    // 🎯 우체통에 부딪히기 직전에 구름 제거 함수 호출
    if (phase === "fall" && y >= (mailboxTop - 500)) {
        hideClouds(); // 지면에 도착하기 전에 구름 삭제 실행
    }

    // 우체통 실제 충돌
    if (phase === "fall" && y >= mailboxTop) {
        y = mailboxTop;
        velocityY = -8;
        velocityX = 5; // 우측으로 튕겨 나가는 힘
        phase = "bounce";
    }

    // 바닥 충돌 및 정지
    if (phase === "bounce" && y >= groundTop) {
        y = groundTop;
        velocityY *= -0.5;
        velocityX *= 0.8; // 바닥 마찰력으로 점점 느려짐
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