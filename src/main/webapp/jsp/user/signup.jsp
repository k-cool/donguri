<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="css/signup.css" rel="stylesheet">
<script src="js/signup.js"></script>
<div class="join">
    <div class="join-container">
        <h2>회원가입</h2>

        <form action="signup-do" method="post" enctype="multipart/form-data">

            <div class="form-group">
                <label for="email">이메일 (ID로 사용)</label>
                <div class="input-flex">
                    <input type="email" id="email" name="email" required placeholder="example@email.com">
                    <button type="button" id="sendEmailBtn" class="btn-secondary">중복 확인</button>
                </div>
                <div id="emailMessage" class="message"></div>
            </div>

            <div class="form-group" id="emailConfirmArea" style="display: none;">
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

            <div class="button-group">
                <button type="submit" class="sg-btn-signup">가입하기</button>
                <button type="button" class="sg-btn-cancel" onclick="history.back()">취소</button>
            </div>
        </form>
    </div>
</div>