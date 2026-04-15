document.addEventListener("DOMContentLoaded", function () {
    // 1. 비밀번호 입력창 요소를 가져옵니다.
    const passwordInput = document.getElementById('password');

    // 2. 요소가 존재하고, JSP에서 설정한 data-login-error가 "true"인지 확인합니다.
    if (passwordInput && passwordInput.dataset.loginError === "true") {
        // 3. 비밀번호 창에 포커스를 줍니다.
        passwordInput.focus();
    }
});