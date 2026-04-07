// 이메일 중복 확인 및 인증 요청 로직
let isEmailVerified = false;
let isEmailDuplicateChecked = false;
let isNicknameChecked = false;

// 이메일 중복 확인 함수
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

// 인증 코드 발송 함수
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

// 닉네임 중복 확인 함수
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

// 비밀번호 확인 함수
function checkPassword() {
    const password = document.getElementById("password").value;
    const passwordConfirm = document.getElementById("passwordConfirm").value;
    const messageDiv = document.getElementById("passwordMessage");

    // 메시지 div가 없으면 생성
    if (!messageDiv) {
        const passwordGroup = document.getElementById("passwordConfirm").closest(".form-group");
        const newDiv = document.createElement("div");
        newDiv.id = "passwordMessage";
        newDiv.className = "message";
        passwordGroup.appendChild(newDiv);
    }

    const msgDiv = document.getElementById("passwordMessage");

    if (passwordConfirm === "") {
        msgDiv.textContent = "";
        return;
    }

    if (password === passwordConfirm) {
        msgDiv.textContent = "비밀번호가 일치합니다.";
        msgDiv.style.color = "green";
    } else {
        msgDiv.textContent = "비밀번호가 일치하지 않습니다.";
        msgDiv.style.color = "red";
    }
}

// 초기 이벤트 리스너 설정
document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("sendEmailBtn").onclick = checkEmailDuplicate;

    // 닉네임 중복 확인 버튼 이벤트 리스너 추가
    document.getElementById("checkNicknameBtn").onclick = checkNickname;

    // 비밀번호 확인 이벤트 리스너 추가
    document.getElementById("password").oninput = checkPassword;
    document.getElementById("passwordConfirm").oninput = checkPassword;

    // 회원가입 폼 제출 시 확인
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

        // 3. 비밀번호 일치 확인
        const password = document.getElementById("password").value;
        const passwordConfirm = document.getElementById("passwordConfirm").value;

        if (password !== passwordConfirm) {
            const passwordConfirmInput = document.getElementById("passwordConfirm");
            const messageDiv = document.getElementById("passwordMessage");
            passwordConfirmInput.focus();
            messageDiv.textContent = "비밀번호가 일치하지 않습니다.";
            messageDiv.style.color = "red";
            return false;
        }

        return true;
    };

    // 인증 코드 확인 버튼 이벤트 리스너 추가
    document.getElementById("verifyCodeBtn").onclick = function () {
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
                    document.getElementById("email").readOnly = true; // <-- disabled 대신 readOnly(대문자 O) 사용
                    document.getElementById("email").style.backgroundColor = "#e9ecef"; // 선택사항: 막힌 것처럼 회색으로 보이게 함

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
    };
});
