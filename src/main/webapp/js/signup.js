// 이메일 중복 확인 및 인증 요청 로직
let isEmailVerified = false;
let isEmailDuplicateChecked = false;

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
    fetch("check-email-duplicate", {
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
            sendBtn.textContent = "인증 요청";
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
    fetch("send-verification-email", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: "email=" + encodeURIComponent(email)
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            messageDiv.textContent = "인증 코드가 발송되었습니다.";
            messageDiv.style.color = "green";
            confirmArea.style.display = "block";
        } else {
            messageDiv.textContent = "인증 코드 발송에 실패했습니다.";
            messageDiv.style.color = "red";
        }
    })
    .catch(error => {
        messageDiv.textContent = "오류가 발생했습니다. 다시 시도해주세요.";
        messageDiv.style.color = "red";
    });
}

// 초기 이벤트 리스너 설정
document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("sendEmailBtn").onclick = checkEmailDuplicate;
});
