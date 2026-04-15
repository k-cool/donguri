/*
    이메일 수령처를 내가 지정한 이메일 주소로 수정
*/

UPDATE RESERVATION
SET RECIPIENT_EMAIL = 'k76054266@gmail.com';

/*
    이메일 수령 시간을 원하는 시간으로 수정
*/

UPDATE RESERVATION
SET SCHEDULED_DATE = TO_DATE('2026-04-06 01:00:00', 'YYYY-MM-DD HH24:MI:SS') -- 수정할 일시
WHERE RESERVATION_ID =
      '4EB542F1B0FE7BACE063835E000AFD6B'; -- 수정 대상 RESERVATION_ID

SELECT *
FROM RESERVATION
ORDER BY SCHEDULED_DATE;

/*
    오미쿠지 뽑기 초기화
*/

UPDATE USERS
SET OMIKUJI_AT = null
WHERE USER_ID = 'B88A1419C174496296EA43AAD4E5857F';


/*
    BGM 데이터 입력
*/

UPDATE EMAIL_CONTENT
SET BGM_URL = 'https://www.chosic.com/wp-content/uploads/2021/02/PerituneMaterial_Sakuya2(chosic.com).mp3';
