document.getElementById('userDeleteForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    // 에러 메시지 초기화
    document.querySelectorAll('.error-message').forEach(el => el.style.display = 'none');
    
    const password = document.getElementById('password').value;
    
    // 비밀번호 확인
    if (!password) {
        document.getElementById('passwordError').textContent = '비밀번호를 입력해주세요.';
        document.getElementById('passwordError').style.display = 'block';
        return;
    }
    
    // 회원 탈퇴 확인
    if (confirm('정말로 회원 탈퇴를 진행하시겠습니까?\n모든 데이터가 삭제되며 복구할 수 없습니다.')) {
        // 폼 제출
        this.submit();
    }
});
