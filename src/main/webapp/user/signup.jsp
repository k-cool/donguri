<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="join-container">
    <h2>회원가입</h2>

    <form action="signup-do" method="post" enctype="multipart/form-data">

        <div class="form-group">
            <label for="email">이메일 (ID로 사용)</label>
            <div class="input-flex">
                <input type="email" id="email" name="email" required placeholder="example@email.com">
                <button type="button" id="sendEmailBtn" class="btn-secondary">인증 요청</button>
            </div>
        </div>

        <div class="form-group" id="emailConfirmArea">
            <label for="emailConfirmCode">인증 코드</label>
            <div class="input-flex">
                <input type="text" id="emailConfirmCode" name="emailConfirmCode" placeholder="인증번호 6자리">
                <button type="button" id="verifyCodeBtn" class="btn-secondary">확인</button>
            </div>
        </div>

        <div class="form-group">
            <label for="nickname">닉네임</label>
            <div class="input-flex">
                <input type="text" id="nickname" name="nickname" required>
                <button type="button" id="checkNicknameBtn" class="btn-secondary">중복 확인</button>
            </div>
        </div>

        <div class="form-group">
            <label for="password">비밀번호</label>
            <input type="password" id="password" name="password" required>
        </div>

        <div class="form-group">
            <label for="passwordConfirm">비밀번호 확인</label>
            <input type="password" id="passwordConfirm" name="passwordConfirm" required>
        </div>

        <div class="form-group">
            <label for="profileImage">프로필 이미지 (선택)</label>
            <input type="file" id="profileImage" name="file" accept="image/*">
        </div>

        <button type="submit" class="btn-signup">가입하기</button>
    </form>
</div>