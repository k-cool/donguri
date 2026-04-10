/*
    테이블 모두 삭제. 데이터도 모두 유실 되므로 주의.
 */

-- 트리거 먼저 삭제 (테이블 DROP 시 자동 삭제되긴 하지만 명시적으로)
DROP TRIGGER trg_users_updated_at;
DROP TRIGGER trg_template_updated_at;
DROP TRIGGER trg_reservation_updated_at;
DROP TRIGGER trg_send_log_updated_at;
DROP TRIGGER trg_omikuji_updated_at;
DROP TRIGGER trg_inquiry_updated_at;


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
DROP TABLE inquiry CASCADE CONSTRAINTS;
