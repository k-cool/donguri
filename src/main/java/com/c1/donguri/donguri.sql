/*
    테이블 모두 삭제. 데이터도 모두 유실 되므로 주의.
 */

-- 트리거 먼저 삭제 (테이블 DROP 시 자동 삭제되긴 하지만 명시적으로)
DROP TRIGGER trg_users_updated_at;
DROP TRIGGER trg_template_updated_at;
DROP TRIGGER trg_reservation_updated_at;
DROP TRIGGER trg_send_log_updated_at;
DROP TRIGGER trg_omikuji_updated_at;


-- 자식 테이블부터 DROP
DROP TABLE send_log CASCADE CONSTRAINTS;
DROP TABLE reservation CASCADE CONSTRAINTS;
DROP TABLE user_template CASCADE CONSTRAINTS;
DROP TABLE email_content CASCADE CONSTRAINTS;

-- 부모 테이블
DROP TABLE template CASCADE CONSTRAINTS;
DROP TABLE users CASCADE CONSTRAINTS;

-- 독립 테이블
DROP TABLE omikuji CASCADE CONSTRAINTS;

/*
    테이블 생성
 */

CREATE TABLE users
(
    user_id         RAW(16)     DEFAULT SYS_GUID() PRIMARY KEY,
    email           VARCHAR2(50)      NOT NULL UNIQUE,
    nickname        VARCHAR2(10 char) NOT NULL UNIQUE,
    password        VARCHAR2(100)     NOT NULL,
    profile_img_url VARCHAR2(500),
    created_at      DATE        DEFAULT SYSDATE,
    updated_at      DATE        DEFAULT SYSDATE,
    is_deleted      VARCHAR2(1) DEFAULT 'N',

    CONSTRAINT chk_users_is_deleted
        CHECK (is_deleted IN ('Y', 'N'))
);

CREATE OR REPLACE TRIGGER trg_users_updated_at
    BEFORE UPDATE
    ON users
    FOR EACH ROW
BEGIN
    :NEW.updated_at := SYSDATE;
END;


CREATE TABLE template
(
    template_id   RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
    name          VARCHAR2(20 char) NOT NULL,
    body_html     CLOB              NOT NULL,
    type          VARCHAR2(10)      NOT NULL,
    cover_img_url VARCHAR2(500)     NOT NULL,
    qr_url        VARCHAR2(500),
    created_at    DATE    DEFAULT SYSDATE,
    updated_at    DATE    DEFAULT SYSDATE,

    CONSTRAINT chk_template_type
        CHECK (type IN ('BASE', 'ADDED'))
);


CREATE OR REPLACE TRIGGER trg_template_updated_at
    BEFORE UPDATE
    ON template
    FOR EACH ROW
BEGIN
    :NEW.updated_at := SYSDATE;
END;



CREATE TABLE email_content
(
    email_content_id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
    template_id      RAW(16)            NOT NULL,
    sender_id        RAW(16)            NOT NULL,
    subject          VARCHAR2(20 char)  NOT NULL,
    content          VARCHAR2(500 char) NOT NULL,
    cover_img_url    VARCHAR2(500),
    bgm_url          VARCHAR2(500),
    created_at       DATE    DEFAULT SYSDATE,
    updated_at       DATE    DEFAULT SYSDATE,

    CONSTRAINT fk_email_template
        FOREIGN KEY (template_id)
            REFERENCES template (template_id)
                ON DELETE SET NULL,

    CONSTRAINT fk_email_sender
        FOREIGN KEY (sender_id)
            REFERENCES users (user_id)
                ON DELETE SET NULL
);


CREATE INDEX idx_email_template_id ON email_content (template_id);
CREATE INDEX idx_email_sender_id ON email_content (sender_id);


CREATE TABLE reservation
(
    reservation_id   RAW(16)     DEFAULT SYS_GUID() PRIMARY KEY,
    from_id          RAW(16)       NOT NULL,
    email_content_id RAW(16)       NOT NULL,
    recipient_email  VARCHAR2(100) NOT NULL,
    is_done          VARCHAR2(1) DEFAULT 'N',
    scheduled_date   DATE          NOT NULL,
    created_at       DATE        DEFAULT SYSDATE,
    updated_at       DATE        DEFAULT SYSDATE,

    CONSTRAINT fk_reservation_user
        FOREIGN KEY (from_id)
            REFERENCES users (user_id)
                ON DELETE SET NULL,

    CONSTRAINT fk_reservation_email
        FOREIGN KEY (email_content_id)
            REFERENCES email_content (email_content_id)
                ON DELETE SET NULL,

    CONSTRAINT chk_reservation_is_done
        CHECK (is_done IN ('Y', 'N'))
);

CREATE OR REPLACE TRIGGER trg_reservation_updated_at
    BEFORE UPDATE
    ON reservation
    FOR EACH ROW
BEGIN
    :NEW.updated_at := SYSDATE;
END;


-- 발송 대상 조회용 (스케줄러가 씀)
CREATE INDEX idx_reservation_schedule
    ON reservation (is_done, scheduled_date);

-- FK 조회 최적화
CREATE INDEX idx_reservation_from_id
    ON reservation (from_id);

CREATE INDEX idx_reservation_email_content_id
    ON reservation (email_content_id);


CREATE TABLE send_log
(
    send_log_id    RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
    reservation_id RAW(16)      NOT NULL,
    status         VARCHAR2(10) NOT NULL,
    error_message  CLOB,
    created_at     DATE    DEFAULT SYSDATE,
    updated_at     DATE    DEFAULT SYSDATE,

    CONSTRAINT fk_send_log_reservation
        FOREIGN KEY (reservation_id)
            REFERENCES reservation (reservation_id)
                ON DELETE SET NULL,

    CONSTRAINT chk_send_log_status
        CHECK (status IN ('SUCCESS', 'FAIL'))
);


CREATE OR REPLACE TRIGGER trg_send_log_updated_at
    BEFORE UPDATE
    ON send_log
    FOR EACH ROW
BEGIN
    :NEW.updated_at := SYSDATE;
END;


CREATE INDEX idx_send_log_reservation_id
    ON send_log (reservation_id);

CREATE INDEX idx_send_log_status
    ON send_log (status);



CREATE TABLE user_template
(
    user_template_id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
    user_id          RAW(16) NOT NULL,
    template_id      RAW(16) NOT NULL,
    created_at       DATE    DEFAULT SYSDATE,

    CONSTRAINT fk_user_template_user
        FOREIGN KEY (user_id)
            REFERENCES users (user_id)
                ON DELETE SET NULL,

    CONSTRAINT fk_user_template_template
        FOREIGN KEY (template_id)
            REFERENCES template (template_id)
                ON DELETE SET NULL,

    CONSTRAINT uq_user_template
        UNIQUE (user_id, template_id)
);


CREATE INDEX idx_user_template_user_id
    ON user_template (user_id);

CREATE INDEX idx_user_template_template_id
    ON user_template (template_id);


CREATE TABLE omikuji
(
    omikuji_id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
    luck       VARCHAR2(15) NOT NULL,
    message    VARCHAR2(100 char),
    created_at DATE    DEFAULT SYSDATE,
    updated_at DATE    DEFAULT SYSDATE,

    CONSTRAINT chk_omikuji_luck
        CHECK (luck IN ('BAD', 'SMALL_BAD', 'SMALL_GOOD', 'MIDDLE_GOOD', 'BIG_GOOD'))
);

CREATE OR REPLACE TRIGGER trg_omikuji_updated_at
    BEFORE UPDATE
    ON omikuji
    FOR EACH ROW
BEGIN
    :NEW.updated_at := SYSDATE;
END;

/*
    테스트 데이터 생성
 */


INSERT INTO users (email, nickname, password, profile_img_url)
VALUES ('user1@test.com', '스누피', 'hash_123', 'https://picsum.photos/200');
INSERT INTO users (email, nickname, password, profile_img_url)
VALUES ('user2@test.com', '찰리브라운', 'hash_123', 'https://picsum.photos/200');
INSERT INTO users (email, nickname, password, profile_img_url)
VALUES ('user3@test.com', '루시', 'hash_123', 'https://picsum.photos/200');
INSERT INTO users (email, nickname, password, profile_img_url)
VALUES ('user4@test.com', '라이너스', 'hash_123', 'https://picsum.photos/200');
INSERT INTO users (email, nickname, password, profile_img_url)
VALUES ('user5@test.com', '샐리', 'hash_123', 'https://picsum.photos/200');
INSERT INTO users (email, nickname, password, profile_img_url)
VALUES ('user6@test.com', '슈로더', 'hash_123', 'https://picsum.photos/200');
INSERT INTO users (email, nickname, password, profile_img_url)
VALUES ('user7@test.com', '페퍼민트', 'hash_123', 'https://picsum.photos/200');
INSERT INTO users (email, nickname, password, profile_img_url)
VALUES ('user8@test.com', '마시', 'hash_123', 'https://picsum.photos/200');
INSERT INTO users (email, nickname, password, profile_img_url)
VALUES ('user9@test.com', '우드스탁', 'hash_123', 'https://picsum.photos/200');
INSERT INTO users (email, nickname, password, profile_img_url)
VALUES ('user10@test.com', '픽펜', 'hash_123', 'https://picsum.photos/200');


INSERT INTO template (name, body_html, type, cover_img_url)
VALUES ('생일 축하', '<html><body>Happy Birthday!</body></html>', 'BASE', 'https://picsum.photos/400/200');
INSERT INTO template (name, body_html, type, cover_img_url)
VALUES ('감사 편지', '<html><body>Thank you so much!</body></html>', 'BASE', 'https://picsum.photos/400/200');
INSERT INTO template (name, body_html, type, cover_img_url)
VALUES ('새해 인사', '<html><body>Happy New Year!</body></html>', 'BASE', 'https://picsum.photos/400/200');
INSERT INTO template (name, body_html, type, cover_img_url)
VALUES ('크리스마스', '<html><body>Merry Christmas!</body></html>', 'BASE', 'https://picsum.photos/400/200');
INSERT INTO template (name, body_html, type, cover_img_url)
VALUES ('커스텀 A', '<html><body>Custom Content A</body></html>', 'ADDED', 'https://picsum.photos/400/200');
INSERT INTO template (name, body_html, type, cover_img_url)
VALUES ('커스텀 B', '<html><body>Custom Content B</body></html>', 'ADDED', 'https://picsum.photos/400/200');
INSERT INTO template (name, body_html, type, cover_img_url)
VALUES ('졸업 축하', '<html><body>Congratulations!</body></html>', 'BASE', 'https://picsum.photos/400/200');
INSERT INTO template (name, body_html, type, cover_img_url)
VALUES ('입사 축하', '<html><body>Welcome to Team!</body></html>', 'BASE', 'https://picsum.photos/400/200');
INSERT INTO template (name, body_html, type, cover_img_url)
VALUES ('안부 인사', '<html><body>How are you?</body></html>', 'BASE', 'https://picsum.photos/400/200');
INSERT INTO template (name, body_html, type, cover_img_url)
VALUES ('이벤트 공지', '<html><body>Special Event!</body></html>', 'ADDED', 'https://picsum.photos/400/200');



INSERT INTO email_content (template_id, sender_id, subject, content)
SELECT template_id, user_id, '생일축하해!', '벌써 일 년이 지났네. 축하해!'
FROM (SELECT template_id FROM template WHERE name = '생일 축하'),
     (SELECT user_id FROM users WHERE email = 'user1@test.com');

INSERT INTO email_content (template_id, sender_id, subject, content)
SELECT template_id, user_id, '고마워요', '도와주셔서 정말 감사합니다.'
FROM (SELECT template_id FROM template WHERE name = '감사 편지'),
     (SELECT user_id FROM users WHERE email = 'user2@test.com');

-- (반복 생략 없이 10개 생성을 위해 임의 조합 삽입)
INSERT INTO email_content (template_id, sender_id, subject, content)
SELECT template_id, user_id, 'Happy New Year', '새해 복 많이 받아!'
FROM (SELECT template_id FROM template WHERE name = '새해 인사'),
     (SELECT user_id FROM users WHERE email = 'user3@test.com');
INSERT INTO email_content (template_id, sender_id, subject, content)
SELECT template_id, user_id, 'Merry X-mas', '즐거운 성탄절!'
FROM (SELECT template_id FROM template WHERE name = '크리스마스'),
     (SELECT user_id FROM users WHERE email = 'user4@test.com');
INSERT INTO email_content (template_id, sender_id, subject, content)
SELECT template_id, user_id, '졸업축하!', '고생 많았어.'
FROM (SELECT template_id FROM template WHERE name = '졸업 축하'),
     (SELECT user_id FROM users WHERE email = 'user5@test.com');
INSERT INTO email_content (template_id, sender_id, subject, content)
SELECT template_id, user_id, '입사환영', '앞으로 잘 부탁해!'
FROM (SELECT template_id FROM template WHERE name = '입사 축하'),
     (SELECT user_id FROM users WHERE email = 'user6@test.com');
INSERT INTO email_content (template_id, sender_id, subject, content)
SELECT template_id, user_id, '별일없지?', '보고싶다 친구야.'
FROM (SELECT template_id FROM template WHERE name = '안부 인사'),
     (SELECT user_id FROM users WHERE email = 'user7@test.com');
INSERT INTO email_content (template_id, sender_id, subject, content)
SELECT template_id, user_id, '공지사항', '꼭 확인 부탁드립니다.'
FROM (SELECT template_id FROM template WHERE name = '이벤트 공지'),
     (SELECT user_id FROM users WHERE email = 'user8@test.com');
INSERT INTO email_content (template_id, sender_id, subject, content)
SELECT template_id, user_id, '커스텀 메일', '내가 만든 템플릿!'
FROM (SELECT template_id FROM template WHERE name = '커스텀 A'),
     (SELECT user_id FROM users WHERE email = 'user9@test.com');
INSERT INTO email_content (template_id, sender_id, subject, content)
SELECT template_id, user_id, '마지막 테스트', '테스트 데이터입니다.'
FROM (SELECT template_id FROM template WHERE name = '커스텀 B'),
     (SELECT user_id FROM users WHERE email = 'user10@test.com');

INSERT INTO reservation (from_id, email_content_id, recipient_email, is_done, scheduled_date)
SELECT sender_id, email_content_id, 'friend1@naver.com', 'N', SYSDATE + 1
FROM email_content
WHERE subject = '생일축하해!';

INSERT INTO reservation (from_id, email_content_id, recipient_email, is_done, scheduled_date)
SELECT sender_id, email_content_id, 'boss@company.com', 'N', SYSDATE + 2
FROM email_content
WHERE subject = '고마워요';

-- 8개 추가
INSERT INTO reservation (from_id, email_content_id, recipient_email, is_done, scheduled_date)
SELECT sender_id, email_content_id, 'receiver' || rownum || '@test.com', 'N', SYSDATE + rownum
FROM email_content
WHERE subject NOT IN ('생일축하해!', '고마워요');


INSERT INTO send_log (reservation_id, status, error_message)
SELECT reservation_id, 'SUCCESS', NULL
FROM reservation
WHERE recipient_email = 'friend1@naver.com';

INSERT INTO send_log (reservation_id, status, error_message)
SELECT reservation_id, 'FAIL', 'SMTP Connection Timeout'
FROM reservation
WHERE recipient_email = 'boss@company.com';

-- 나머지 성공 처리 로그
INSERT INTO send_log (reservation_id, status)
SELECT reservation_id, 'SUCCESS'
FROM (SELECT reservation_id FROM reservation WHERE is_done = 'N' AND ROWNUM <= 8);


INSERT INTO user_template (user_id, template_id)
SELECT u.user_id, t.template_id
FROM users u,
     template t
WHERE u.nickname = '스누피'
  AND t.type = 'BASE'
  AND ROWNUM <= 2;

INSERT INTO user_template (user_id, template_id)
SELECT u.user_id, t.template_id
FROM users u,
     template t
WHERE u.nickname = '찰리브라운'
  AND t.type = 'ADDED'
  AND ROWNUM <= 2;

-- 나머지 유저들 하나씩 매핑
INSERT INTO user_template (user_id, template_id)
SELECT user_id, (SELECT template_id FROM template WHERE name = '졸업 축하')
FROM users
WHERE nickname IN ('루시', '라이너스', '샐리', '슈로더', '페퍼민트', '마시');


INSERT INTO omikuji (luck, message)
VALUES ('BIG_GOOD', '오늘은 최고의 날! 모든 일이 술술 풀릴 거예요.');
INSERT INTO omikuji (luck, message)
VALUES ('MIDDLE_GOOD', '좋은 소식이 들려올 것 같은 예감.');
INSERT INTO omikuji (luck, message)
VALUES ('SMALL_GOOD', '소소한 행복이 찾아오는 하루입니다.');
INSERT INTO omikuji (luck, message)
VALUES ('SMALL_BAD', '조금 답답할 수 있지만 곧 지나갈 거예요.');
INSERT INTO omikuji (luck, message)
VALUES ('BAD', '오늘은 무리하지 말고 푹 쉬는 게 좋겠어요.');
INSERT INTO omikuji (luck, message)
VALUES ('BIG_GOOD', '복권 한 장 사보는 건 어때요? 운이 최고조입니다.');
INSERT INTO omikuji (luck, message)
VALUES ('MIDDLE_GOOD', '주변 사람들에게 칭찬을 듣게 될 거예요.');
INSERT INTO omikuji (luck, message)
VALUES ('SMALL_GOOD', '길을 걷다 우연히 맛집을 발견할 지도?');
INSERT INTO omikuji (luck, message)
VALUES ('SMALL_BAD', '작은 실수를 조심하세요. 확인 또 확인!');
INSERT INTO omikuji (luck, message)
VALUES ('BAD', '말조심이 필요한 날입니다. 침묵은 금!');

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
       e.subject AS email_subject,
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
       l.created_at AS sent_at
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