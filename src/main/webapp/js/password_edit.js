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
    }
    
    // 비밀번호 확인 검사
    if (!confirmPassword) {
        document.getElementById('confirmPasswordError').textContent = '새 비밀번호 확인을 입력해주세요.';
        document.getElementById('confirmPasswordError').style.display = 'block';
        isValid = false;
    } else if (newPassword !== confirmPassword) {
        document.getElementById('confirmPasswordError').textContent = '새 비밀번호가 일치하지 않습니다.';
        document.getElementById('confirmPasswordError').style.display = 'block';
        isValid = false;
    }
    
    if (isValid) {
        // 폼 제출
        this.submit();
    }
});
