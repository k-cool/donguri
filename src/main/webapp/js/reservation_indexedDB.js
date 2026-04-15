// DB 설정
console.log("Indexed DB INIT")

/*
    저장하기
 */

const dbName = "ImageStorageDB";
const storeName = "tempImages";

function saveImageToDB(file) {
    const request = indexedDB.open(dbName, 1);

    // DB가 없거나 버전이 바뀔 때 테이블(ObjectStore) 생성
    request.onupgradeneeded = (e) => {
        const db = e.target.result;
        if (!db.objectStoreNames.contains(storeName)) {
            db.createObjectStore(storeName, {keyPath: "id"});
        }
    };

    request.onsuccess = (e) => {
        const db = e.target.result;
        const transaction = db.transaction([storeName], "readwrite");
        const store = transaction.objectStore(storeName);

        // 이미지 파일을 ID 'currentUpload'로 저장 (기존 데이터는 덮어씀)
        const putRequest = store.put({id: "currentUpload", fileData: file});

        putRequest.onsuccess = () => {
            console.log("이미지 저장 완료!");
        };
    };
}

// HTML input 이벤트 연결
document.getElementById('fileInput')?.addEventListener('change', (e) => {
    const file = e.target.files[0];
    if (file) {
        saveImageToDB(file);
        const fileName = file.name;
        document.getElementById('fileName').value = fileName;
    }
});


/*
    불러오기
 */

// 1. DB에서 이미지 불러오기
function loadImageFromDB() {
    const request = indexedDB.open(dbName, 1);

    request.onsuccess = (e) => {
        const db = e.target.result;
        const transaction = db.transaction([storeName], "readonly");
        const store = transaction.objectStore(storeName);
        const getRequest = store.get("currentUpload");

        getRequest.onsuccess = () => {
            if (getRequest.result) {
                const file = getRequest.result.fileData; // 파일 데이터


                // 미리보기 출력 (img 태그의 src에 할당)
                const imgUrl = URL.createObjectURL(file);

                const coverImg = document.getElementById('cover-img');
                coverImg.src = imgUrl;

                console.log(imgUrl);

                // 전송을 위해 전역 변수나 클로저에 보관
                // window.selectedFile = file;
            }
        };
    };
}

(() => {
    loadImageFromDB();
    console.log("loadImageFromDB called")
})();

// 2. 서버로 멀티파트 업로드 (자바 서블릿 전송)
// async function uploadToServer() {
//     if (!window.selectedFile) return alert("파일이 없습니다.");
//
//     const formData = new FormData();
//     formData.append("uploadFile", window.selectedFile); // 서블릿의 @MultipartConfig가 받을 이름
//     formData.append("otherData", "someValue");
//
//     try {
//         const response = await fetch("/uploadServlet", { // 서블릿 매핑 경로
//             method: "POST",
//             body: formData
//         });
//
//         if (response.ok) {
//             alert("서버 등록 성공!");
//             // 업로드 후 DB 청소 (선택 사항)
//             indexedDB.deleteDatabase(dbName);
//         }
//     } catch (error) {
//         console.error("업로드 실패:", error);
//     }
// }

