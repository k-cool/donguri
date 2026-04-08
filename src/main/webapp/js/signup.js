// =============================================================================
// 회원가입 사용자 흐름: 이메일 → 인증 → 닉네임 → 비밀번호 → 가입완료
// =============================================================================

// 전역 상태 변수
let isEmailVerified = false;        // 이메일 인증 완료 여부
let isEmailDuplicateChecked = false;  // 이메일 중복 확인 여부
let isNicknameChecked = false;       // 닉네임 중복 확인 여부

// =============================================================================
// 1단계: 이메일 중복 확인
// =============================================================================
function checkEmailDuplicate() {
    const email = document.getElementById("email").value;
    const messageDiv = document.getElementById("emailMessage");
    const sendBtn = document.getElementById("sendEmailBtn");

    if (!email) {
        messageDiv.textContent = "이메일을 입력해주세요.";
        messageDiv.style.color = "red";
        return;
    }

    // AJAX로 이메일 중복 확인
    fetch("email-check", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: "email=" + encodeURIComponent(email)
    })
        .then(response => response.json())
        .then(data => {
            if (data.exists) {
                messageDiv.textContent = "이미 사용 중인 이메일입니다.";
                messageDiv.style.color = "red";
                isEmailDuplicateChecked = false;
            } else {
                messageDiv.textContent = "사용 가능한 이메일입니다. 인증 요청을 눌러주세요.";
                messageDiv.style.color = "green";
                sendBtn.textContent = "이메일 인증";
                sendBtn.onclick = sendVerificationCode;
                isEmailDuplicateChecked = true;
            }
        })
        .catch(error => {
            messageDiv.textContent = "오류가 발생했습니다. 다시 시도해주세요.";
            messageDiv.style.color = "red";
        });
}

// =============================================================================
// 2단계: 이메일 인증 코드 발송
// =============================================================================
function sendVerificationCode() {
    const email = document.getElementById("email").value;
    const messageDiv = document.getElementById("emailMessage");
    const confirmArea = document.getElementById("emailConfirmArea");

    if (!isEmailDuplicateChecked) {
        messageDiv.textContent = "먼저 이메일 중복 확인을 해주세요.";
        messageDiv.style.color = "red";
        return;
    }

    // AJAX로 인증 코드 발송 요청
    fetch("email-send", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: "email=" + encodeURIComponent(email)
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // 인증 코드 입력 영역 표시
                confirmArea.style.display = "block";

                // 재발송인지 체크해서 메시지 변경
                const sendBtn = document.getElementById("sendEmailBtn");
                if (sendBtn.textContent === "재발송") {
                    messageDiv.textContent = "인증 코드가 재발송되었습니다. 이메일을 확인해주세요.";
                } else {
                    messageDiv.textContent = "인증 코드가 발송되었습니다. 이메일을 확인해주세요.";
                }
                messageDiv.style.color = "green";

                // 버튼 상태 변경
                sendBtn.textContent = "재발송";
                sendBtn.onclick = sendVerificationCode; // 재발송 기능 유지
            } else {
                messageDiv.textContent = "인증 코드 발송에 실패했습니다. 다시 시도해주세요.";
                messageDiv.style.color = "red";
            }
        })
        .catch(error => {
            messageDiv.textContent = "오류가 발생했습니다. 다시 시도해주세요.";
            messageDiv.style.color = "red";
        });
}

// 2단계: 인증 코드 검증
function verifyEmailCode() {
    const email = document.getElementById("email").value;
    const inputCode = document.getElementById("emailConfirmCode").value;
    const messageDiv = document.getElementById("emailMessage");

    if (!inputCode) {
        messageDiv.textContent = "인증 코드를 입력해주세요.";
        messageDiv.style.color = "red";
        return;
    }

    // AJAX로 인증 코드 검증 요청
    fetch("email-verify", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: "email=" + encodeURIComponent(email) + "&code=" + encodeURIComponent(inputCode)
    })
        .then(response => response.json())
        .then(data => {
            if (data.verified) {
                messageDiv.textContent = "이메일 인증이 완료되었습니다.";
                messageDiv.style.color = "green";
                isEmailVerified = true;

                // 버튼 및 입력 필드 비활성화
                document.getElementById("email").readOnly = true;
                document.getElementById("email").style.backgroundColor = "#e9ecef";

                document.getElementById("sendEmailBtn").disabled = true;
                document.getElementById("emailConfirmCode").disabled = true;
                document.getElementById("verifyCodeBtn").disabled = true;
                document.getElementById("verifyCodeBtn").textContent = "인증 완료";

                // 재발송 버튼 숨기기
                document.getElementById("sendEmailBtn").style.display = "none";
            } else {
                messageDiv.textContent = "인증 코드가 올바르지 않거나 유효시간이 만료되었습니다.";
                messageDiv.style.color = "red";
                isEmailVerified = false;
            }
        })
        .catch(error => {
            messageDiv.textContent = "오류가 발생했습니다. 다시 시도해주세요.";
            messageDiv.style.color = "red";
        });
}

// =============================================================================
// 3단계: 닉네임 중복 확인
// =============================================================================
function checkNickname() {
    const nickname = document.getElementById("nickname").value;
    const messageDiv = document.getElementById("nicknameMessage");

    // 메시지 div가 없으면 생성
    if (!messageDiv) {
        const nicknameGroup = document.getElementById("nickname").closest(".form-group");
        const newDiv = document.createElement("div");
        newDiv.id = "nicknameMessage";
        newDiv.className = "message";
        nicknameGroup.appendChild(newDiv);
    }

    const msgDiv = document.getElementById("nicknameMessage");

    if (!nickname) {
        msgDiv.textContent = "닉네임을 입력해주세요.";
        msgDiv.style.color = "red";
        return;
    }

    fetch("nickname-check", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: "nickname=" + encodeURIComponent(nickname)
    })
        .then(response => response.json())
        .then(data => {
            if (data.exists) {
                msgDiv.textContent = "이미 사용 중인 닉네임입니다.";
                msgDiv.style.color = "red";
                isNicknameChecked = false;
            } else {
                msgDiv.textContent = "사용 가능한 닉네임입니다.";
                msgDiv.style.color = "green";
                isNicknameChecked = true;
            }
        })
        .catch(error => {
            msgDiv.textContent = "오류가 발생했습니다. 다시 시도해주세요.";
            msgDiv.style.color = "red";
        });
}

// =============================================================================
// 비밀번호 강도 체크 함수
// =============================================================================

// 비밀번호 강도 계산
function checkPasswordStrength(password) {
    let strength = 0;

    // 길이 기반 강도 계산
    if (password.length <= 3) {
        strength = 0;  // 취약
    } else if (password.length >= 4 && password.length < 8) {
        strength = 1;  // 보통
    } else if (password.length >= 8) {
        // 8글자 이상이면 특수문자 확인
        if (/[!@#$%^&*()\-_=+\[\]{}|;:'",.<>\/?]/.test(password)) {
            strength = 2;  // 안전
        } else {
            strength = 1;  // 보통 (길이는 충족 but 특수문자 없음)
        }
    }

    return {
        level: getLevel(strength),
        color: getColor(strength),
        message: getMessage(strength)
    };
}

// 강도 레벨 반환
function getLevel(strength) {
    switch (strength) {
        case 0:
            return 'weak';
        case 1:
            return 'medium';
        case 2:
            return 'strong';
        default:
            return 'weak';
    }
}

// 강도별 색상 반환
function getColor(strength) {
    switch (strength) {
        case 0:
            return '#ff4444';  // 빨강
        case 1:
            return '#ffaa00';  // 노랑
        case 2:
            return '#00c851';  // 초록
        default:
            return '#ff4444';
    }
}

// 강도별 메시지 반환
function getMessage(strength) {
    switch (strength) {
        case 0:
            return '취약';
        case 1:
            return '보통';
        case 2:
            return '안전';
        default:
            return '취약';
    }
}

// 강도 게이지 UI 업데이트
function updateStrengthGauge(password) {
    const strength = checkPasswordStrength(password);

    // 게이지가 없으면 생성
    let gaugeContainer = document.getElementById('passwordStrengthContainer');
    if (!gaugeContainer) {
        const passwordGroup = document.getElementById("password").closest(".form-group");
        const container = document.createElement("div");
        container.id = "passwordStrengthContainer";
        container.style.marginTop = "8px";

        // 게이지 HTML 구조
        container.innerHTML = `
            <div style="display: flex; align-items: center; gap: 10px;">
                <div style="flex: 1;">
                    <div style="width: 100%; height: 8px; background: #eee; border-radius: 4px; overflow: hidden;">
                        <div id="strengthFill" style="height: 100%; width: 0%; transition: width 0.3s ease, background-color 0.3s ease;"></div>
                    </div>
                </div>
                <div id="strengthText" style="font-size: 12px; font-weight: bold; min-width: 40px;">취약</div>
            </div>
        `;
        passwordGroup.appendChild(container);
        gaugeContainer = document.getElementById('passwordStrengthContainer');
    }

    const fill = document.getElementById('strengthFill');
    const text = document.getElementById('strengthText');

    // 게이지 너비와 색상 업데이트
    const width = strength.level === 'weak' ? '33%' :
        strength.level === 'medium' ? '66%' : '100%';
    fill.style.width = width;
    fill.style.backgroundColor = strength.color;

    // 텍스트 업데이트
    text.textContent = strength.message;
    text.style.color = strength.color;
}

// =============================================================================
// 4단계: 비밀번호 확인 (실시간 검증)
// =============================================================================
function checkPassword() {
    const password = document.getElementById("password").value;
    const messageDiv = document.getElementById("passwordMessage");

    // 메시지 div가 없으면 생성
    if (!messageDiv) {
        const passwordGroup = document.getElementById("password").closest(".form-group");
        const newDiv = document.createElement("div");
        newDiv.id = "passwordMessage";
        newDiv.className = "message";
        passwordGroup.appendChild(newDiv);
    }

    const msgDiv = document.getElementById("passwordMessage");

    if (password === "") {
        msgDiv.textContent = "";
        // 게이지 숨기기
        const gauge = document.getElementById('passwordStrengthContainer');
        if (gauge) gauge.style.display = 'none';
        return;
    }

    // 강도 게이지 업데이트
    updateStrengthGauge(password);

    // 게이지가 숨겨져 있으면 다시 보이기
    const gauge = document.getElementById('passwordStrengthContainer');
    if (gauge) gauge.style.display = 'block';

    // 비밀번호 길이 검증
    if (password.length < 8) {
        msgDiv.textContent = "비밀번호는 최소 8자 이상이어야 합니다.";
        msgDiv.style.color = "red";
        return;
    }

    // 특수문자 검증
    const specialCharPattern = /[!@#$%^&*()\-_=+\[\]{}|;:'",.<>\/?]/;
    if (!specialCharPattern.test(password)) {
        msgDiv.textContent = "비밀번호에 특수문자 1개 이상 포함해야 합니다.";
        msgDiv.style.color = "red";
        return;
    }

    // 비밀번호 유효성 통과
    msgDiv.textContent = "";
}

function checkPasswordConfirm() {
    // 실시간 검증 제거 - 폼 제출 시에만 검증
    return;
}

// =============================================================================
// 5단계: 최종 가입 제출 검증
// =============================================================================
document.addEventListener("DOMContentLoaded", function () {
    // 초기 이벤트 리스너 설정
    document.getElementById("sendEmailBtn").onclick = checkEmailDuplicate;
    document.getElementById("checkNicknameBtn").onclick = checkNickname;
    document.getElementById("password").oninput = checkPassword;

    // 회원가입 폼 제출 시 최종 검증
    document.querySelector("form").onsubmit = function () {
        // 1. 이메일 인증 확인
        if (!isEmailVerified) {
            const emailInput = document.getElementById("email");
            const messageDiv = document.getElementById("emailMessage");
            emailInput.focus();
            messageDiv.textContent = "이메일 인증을 먼저 완료해주세요.";
            messageDiv.style.color = "red";
            return false;
        }

        // 2. 닉네임 중복확인
        if (!isNicknameChecked) {
            const nicknameInput = document.getElementById("nickname");
            const messageDiv = document.getElementById("nicknameMessage");

            // 메시지 div가 없으면 생성
            if (!messageDiv) {
                const nicknameGroup = nicknameInput.closest(".form-group");
                const newDiv = document.createElement("div");
                newDiv.id = "nicknameMessage";
                newDiv.className = "message";
                newDiv.textContent = "닉네임 중복확인을 먼저 해주세요.";
                newDiv.style.color = "red";
                nicknameGroup.appendChild(newDiv);
            } else {
                messageDiv.textContent = "닉네임 중복확인을 먼저 해주세요.";
                messageDiv.style.color = "red";
            }

            nicknameInput.focus();
            return false;
        }

        // 3. 비밀번호 유효성 검사
        const password = document.getElementById("password").value;
        const passwordConfirm = document.getElementById("passwordConfirm").value;

        // 비밀번호 길이 검증
        if (password.length < 8) {
            const passwordInput = document.getElementById("password");
            const messageDiv = document.getElementById("passwordMessage");
            passwordInput.focus();
            messageDiv.textContent = "비밀번호는 최소 8자 이상이어야 합니다.";
            messageDiv.style.color = "red";
            return false;
        }

        // 특수문자 검증
        const specialCharPattern = /[!@#$%^&*()\-_=+\[\]{}|;:'",.<>\/?]/;
        if (!specialCharPattern.test(password)) {
            const passwordInput = document.getElementById("password");
            const messageDiv = document.getElementById("passwordMessage");
            passwordInput.focus();
            messageDiv.textContent = "비밀번호에 특수문자 1개 이상 포함해야 합니다.";
            messageDiv.style.color = "red";
            return false;
        }

        // 비밀번호 일치 확인
        if (password !== passwordConfirm) {
            const passwordConfirmInput = document.getElementById("passwordConfirm");
            const messageDiv = document.getElementById("passwordConfirmMessage");
            
            // 메시지 div가 없으면 생성
            if (!messageDiv) {
                const passwordConfirmGroup = passwordConfirmInput.closest(".form-group");
                const newDiv = document.createElement("div");
                newDiv.id = "passwordConfirmMessage";
                newDiv.className = "message";
                passwordConfirmGroup.appendChild(newDiv);
            }
            
            const msgDiv = document.getElementById("passwordConfirmMessage");
            passwordConfirmInput.focus();
            msgDiv.textContent = "비밀번호가 일치하지 않습니다.";
            msgDiv.style.color = "red";
            return false;
        }

        // 모든 검증 통과 → 가입 진행
        return true;
    };

    // 인증 코드 확인 버튼 이벤트 리스너
    document.getElementById("verifyCodeBtn").onclick = verifyEmailCode;
});
