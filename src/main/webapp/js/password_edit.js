// =============================================================================
// 새 비밀번호 실시간 검증
// =============================================================================
function checkNewPassword() {
    const newPassword = document.getElementById("newPassword").value;
    const messageDiv = document.getElementById("newPasswordMessage");

    // 메시지 div가 없으면 생성
    if (!messageDiv) {
        const newPasswordGroup = document.getElementById("newPassword").closest(".form-group");
        const newDiv = document.createElement("div");
        newDiv.id = "newPasswordMessage";
        newDiv.className = "message";
        newPasswordGroup.appendChild(newDiv);
    }

    const msgDiv = document.getElementById("newPasswordMessage");

    if (newPassword === "") {
        msgDiv.textContent = "";
        return;
    }

    // 비밀번호 길이 검증
    if (newPassword.length < 8) {
        msgDiv.textContent = "비밀번호는 최소 8자 이상이어야 합니다.";
        msgDiv.style.color = "red";
        return;
    }

    // 특수문자 검증
    const specialCharPattern = /[!@#$%^&*()\-_=+\[\]{}|;:'",.<>\/?]/;
    if (!specialCharPattern.test(newPassword)) {
        msgDiv.textContent = "비밀번호에 특수문자 1개 이상 포함해야 합니다.";
        msgDiv.style.color = "red";
        return;
    }

    // 비밀번호 유효성 통과
    msgDiv.textContent = "";
}

// =============================================================================
// 비밀번호 확인 검증 (폼 제출 시에만)
// =============================================================================
function checkConfirmPassword() {
    // 실시간 검증 제거 - 폼 제출 시에만 검증
    return;
}

// =============================================================================
// 폼 제출 검증
// =============================================================================
document.addEventListener("DOMContentLoaded", function() {
    // 실시간 검증 이벤트 리스너
    document.getElementById("newPassword").oninput = checkNewPassword;
    
    // 폼 제출 시 최종 검증
    document.getElementById('passwordEditForm').addEventListener('submit', function(e) {
        e.preventDefault();
        
        // 에러 메시지 초기화
        document.querySelectorAll('.error-message').forEach(el => el.style.display = 'none');
        
        const currentPassword = document.getElementById('currentPassword').value;
        const newPassword = document.getElementById('newPassword').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        
        let isValid = true;
        
        // 현재 비밀번호 확인
        if (!currentPassword) {
            document.getElementById('currentPasswordError').textContent = '현재 비밀번호를 입력해주세요.';
            document.getElementById('currentPasswordError').style.display = 'block';
            isValid = false;
        }
        
        // 새 비밀번호 유효성 검사
        if (!newPassword) {
            document.getElementById('newPasswordError').textContent = '새 비밀번호를 입력해주세요.';
            document.getElementById('newPasswordError').style.display = 'block';
            isValid = false;
        } else if (newPassword.length < 8) {
            document.getElementById('newPasswordError').textContent = '비밀번호는 최소 8자 이상이어야 합니다.';
            document.getElementById('newPasswordError').style.display = 'block';
            isValid = false;
        } else {
            // 특수문자 검증
            const specialCharPattern = /[!@#$%^&*()\-_=+\[\]{}|;:'",.<>\/?]/;
            if (!specialCharPattern.test(newPassword)) {
                document.getElementById('newPasswordError').textContent = '비밀번호에 특수문자 1개 이상 포함해야 합니다.';
                document.getElementById('newPasswordError').style.display = 'block';
                isValid = false;
            }
        }
        
        // 비밀번호 확인 검사
        if (!confirmPassword) {
            document.getElementById('confirmPasswordError').textContent = '새 비밀번호 확인을 입력해주세요.';
            document.getElementById('confirmPasswordError').style.display = 'block';
            isValid = false;
        } else if (newPassword !== confirmPassword) {
            // 비밀번호 확인 메시지 div 생성
            let confirmMsgDiv = document.getElementById("confirmPasswordMessage");
            if (!confirmMsgDiv) {
                const confirmGroup = document.getElementById("confirmPassword").closest(".form-group");
                const newDiv = document.createElement("div");
                newDiv.id = "confirmPasswordMessage";
                newDiv.className = "message";
                confirmGroup.appendChild(newDiv);
                confirmMsgDiv = document.getElementById("confirmPasswordMessage");
            }
            
            confirmMsgDiv.textContent = "새 비밀번호가 일치하지 않습니다.";
            confirmMsgDiv.style.color = "red";
            isValid = false;
        }
        
        if (isValid) {
            // 폼 제출
            this.submit();
        }
    });
});
