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

let isNicknameChecked = false; // 중복 확인 통과 여부를 저장하는 변수

function handleNicknameProcess() {
    const btn = document.getElementById('nicknameBtn');
    const input = document.getElementById('nicknameInput');
    const msg = document.getElementById('nicknameMsg');
    const newNickname = input.value.trim();

    // 1단계: 처음 '수정' 버튼을 눌렀을 때
    if (btn.innerText === '수정' && !isNicknameChecked) {
        input.value = ''; // 기존 글자 지우기
        btn.innerText = '중복확인'; // 버튼 글자 변경
        msg.innerText = ''; // 메시지 초기화
        input.focus();
        return;
    }

    // 2단계: '중복확인' 버튼을 눌렀을 때
    if (btn.innerText === '중복확인') {
        if (newNickname === '') {
            alert("닉네임을 입력해주세요.");
            return;
        }

        const params = new URLSearchParams();
        params.append("nickname", newNickname);

        // 주의: 이 통신을 받으려면 백엔드에 'check-nickname' 주소를 처리하는 컨트롤러가 필요합니다.
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
                    msg.style.color = 'green';
                    msg.innerText = '사용가능한 닉네임입니다.';
                    btn.innerText = '수정';
                    isNicknameChecked = true;
                } else {
                    msg.style.color = 'red';
                    msg.innerText = '존재하는 닉네임입니다.';
                    isNicknameChecked = false;
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert("서버 통신 중 오류가 발생했습니다.");
            });
        return;
    }

    // 3단계: 중복확인 통과 후, 다시 바뀐 '수정' 버튼을 눌러서 최종 저장할 때
    if (btn.innerText === '수정' && isNicknameChecked) {
        // 2단계에서 이미 업데이트했으므로 건너뛰기
        alert("닉네임이 성공적으로 변경되었습니다.");
        msg.innerText = '';
        isNicknameChecked = false; // 다음 변경을 위해 상태 초기화

        // 페이지 새로고침으로 변경된 닉네임 반영
        location.reload();
    }

}

// 부가 기능: 사용자가 사용 가능 판정을 받은 뒤에 input 창의 글자를 맘대로 수정해버리는 것을 방지
document.getElementById('nicknameInput').addEventListener('input', function () {
    const btn = document.getElementById('nicknameBtn');
    const msg = document.getElementById('nicknameMsg');

    if (isNicknameChecked) {
        isNicknameChecked = false; // 글자가 한 글자라도 바뀌면 다시 확인받게 만듦
        btn.innerText = '중복확인';
        msg.innerText = '';
    }
});