/*
    테스트 데이터 생성
 */


INSERT INTO users (email, nickname, password, profile_img_url, roll)
VALUES ('admin@test.com', '관리자', 'hash_123', 'https://picsum.photos/200', 'ADMIN');
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


INSERT INTO template (name, bg_color, type, cover_img_url)
VALUES ('생일 축하', 'eeeeee', 'BASE', 'https://picsum.photos/400/200');
INSERT INTO template (name, bg_color, type, cover_img_url)
VALUES ('감사 편지', 'ffffff', 'BASE', 'https://picsum.photos/400/200');
INSERT INTO template (name, bg_color, type, cover_img_url)
VALUES ('새해 인사', 'aaaaaa', 'BASE', 'https://picsum.photos/400/200');
INSERT INTO template (name, bg_color, type, cover_img_url)
VALUES ('크리스마스', '111111', 'BASE', 'https://picsum.photos/400/200');
INSERT INTO template (name, bg_color, type, cover_img_url)
VALUES ('커스텀 A', '222222', 'ADDED', 'https://picsum.photos/400/200');
INSERT INTO template (name, bg_color, type, cover_img_url)
VALUES ('커스텀 B', '000000', 'ADDED', 'https://picsum.photos/400/200');
INSERT INTO template (name, bg_color, type, cover_img_url)
VALUES ('졸업 축하', 'ffffff', 'BASE', 'https://picsum.photos/400/200');
INSERT INTO template (name, bg_color, type, cover_img_url)
VALUES ('입사 축하', 'ffffff', 'BASE', 'https://picsum.photos/400/200');
INSERT INTO template (name, bg_color, type, cover_img_url)
VALUES ('안부 인사', 'ffffff', 'BASE', 'https://picsum.photos/400/200');
INSERT INTO template (name, bg_color, type, cover_img_url)
VALUES ('이벤트 공지', 'ffffff', 'ADDED', 'https://picsum.photos/400/200');



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