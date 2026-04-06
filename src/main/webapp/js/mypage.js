// 프로필 이미지 수정 비동기 통신
function uploadProfileImage() {
    const fileInput = document.getElementById('profileUpload');
    const file = fileInput.files[0];

    if (!file) return;

    const formData = new FormData();
    formData.append("profileImage", file);

    fetch('update-profile-img', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert("프로필 이미지가 변경되었습니다.");
                document.getElementById('profilePreview').src = data.newImageUrl;
            } else {
                alert("이미지 업로드 실패: " + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert("서버 통신 중 오류가 발생했습니다.");
        });
}

// 닉네임 수정 및 중복확인 비동기 통신
function updateNickname() {
    const newNickname = document.getElementById('nicknameInput').value;

    if (newNickname.trim() === '') {
        alert("닉네임을 입력해주세요.");
        return;
    }

    const params = new URLSearchParams();
    params.append("nickname", newNickname);

    fetch('update-nickname', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: params
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert("닉네임이 성공적으로 변경되었습니다.");
            } else {
                alert("변경 불가: " + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert("서버 통신 중 오류가 발생했습니다.");
        });
}