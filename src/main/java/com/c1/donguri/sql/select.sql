/*
    ==========================================================
    [프로젝트 테스트 및 데이터 조회용 SELECT 쿼리 모음]
    스키마: users, template, email_content, reservation,
           send_log, user_template, omikuji
    ==========================================================
*/


-- 1. 사용자 목록 조회 (users)
-- 탈퇴하지 않은('N') 사용자들을 가입일 최신순으로 조회합니다.
SELECT user_id, email, nickname, created_at
FROM users
WHERE is_deleted = 'N'
ORDER BY created_at DESC;


-- 2. 특정 사용자가 작성한 이메일 상세 조회 (3개 테이블 JOIN)
-- 작성자의 닉네임, 사용된 템플릿 이름, 이메일 제목과 내용을 한꺼번에 가져옵니다.
SELECT u.nickname AS sender_name,
       t.name     AS template_name,
       e.subject,
       e.content,
       e.created_at
FROM email_content e
         JOIN users u ON e.sender_id = u.user_id
         JOIN template t ON e.template_id = t.template_id
WHERE u.email = 'user1@test.com';


-- 3. 발송 대기 중인 예약 목록 조회 (reservation + email_content JOIN)
-- 아직 발송되지 않은('N') 예약 건을 발송 예정일 순으로 조회합니다.
SELECT r.reservation_id,
       r.recipient_email,
       r.scheduled_date,
       e.subject,
       e.CONTENT,
       r.is_done
FROM reservation r
         JOIN email_content e ON r.email_content_id = e.email_content_id
WHERE r.is_done = 'N'
ORDER BY r.scheduled_date ASC;


-- 4. 이메일 발송 로그 및 대상자 확인 (send_log + reservation JOIN)
-- 어떤 이메일이 성공/실패했는지, 실패했다면 에러 메시지가 무엇인지 확인합니다.
SELECT l.send_log_id,
       r.recipient_email,
       l.status,
       l.error_message,
       l.created_at
           AS sent_at
FROM send_log l
         LEFT JOIN reservation r ON l.reservation_id = r.reservation_id
ORDER BY l.created_at DESC;


-- 5. 사용자의 개인 템플릿 보관함 조회 (user_template + template JOIN)
-- 사용자가 '저장' 또는 '보관'한 템플릿 목록을 확인합니다.
SELECT u.nickname,
       t.name        AS saved_template_name,
       t.type,
       ut.created_at AS saved_at
FROM user_template ut
         JOIN users u ON ut.user_id = u.user_id
         JOIN template t ON ut.template_id = t.template_id
WHERE u.nickname = '스누피';


-- 6. 템플릿 타입별 수량 통계 (Group By)
-- 현재 시스템에 등록된 기본(BASE) 및 추가(ADDED) 템플릿 개수를 파악합니다.
SELECT type,
       COUNT(*) AS total_count
FROM template
GROUP BY type;


-- 7. 오늘의 운세 랜덤 추출 (omikuji)
-- 오미쿠지 테이블에서 무작위로 1개의 행을 뽑아 출력합니다.
SELECT *
FROM (SELECT luck, message
      FROM omikuji)
WHERE ROWNUM = 1;

-- 8. [참고] RAW(16) 타입의 ID로 직접 조회 시 예시
-- Oracle의 RAW 타입은 HEXTORAW 함수를 사용하여 검색하는 것이 가장 안전합니다.
-- SELECT * FROM users WHERE user_id = HEXTORAW('A1B2C3D4E5F6...이미 저장된 HEX값');

