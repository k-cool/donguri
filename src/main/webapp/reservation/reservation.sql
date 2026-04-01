
DROP TABLE reservation CASCADE CONSTRAINTS;
DROP TABLE users CASCADE CONSTRAINTS;
DROP TABLE email_content CASCADE CONSTRAINTS;
PURGE RECYCLEBIN;

-- users 테이블
CREATE TABLE users (
                       user_id VARCHAR2(36) PRIMARY KEY
);

-- email_content 테이블
CREATE TABLE email_content (
                               email_content_id VARCHAR2(36) PRIMARY KEY
);

--  reservation 테이블
CREATE TABLE reservation (
                             reservation_id VARCHAR2(36) PRIMARY KEY,

                             from_id VARCHAR2(36) NOT NULL,
                             email_content_id VARCHAR2(36) NOT NULL,

                             recipient_email VARCHAR2(100) NOT NULL,

                             is_done VARCHAR2(1) DEFAULT 'N'
                                 CHECK (is_done IN ('Y','N')),

                             scheduled_date DATE NOT NULL,

                             created_at DATE DEFAULT SYSDATE,
                             updated_at DATE DEFAULT SYSDATE
);

SELECT * FROM reservation;

-- fk 추가
ALTER TABLE reservation
    ADD CONSTRAINT fk_res_user
        FOREIGN KEY (from_id)
            REFERENCES users(user_id);

ALTER TABLE reservation
    ADD CONSTRAINT fk_res_email
        FOREIGN KEY (email_content_id)
            REFERENCES email_content(email_content_id);

-- updated_at 자동 갱신 트리거
CREATE OR REPLACE TRIGGER trg_reservation_updated
    BEFORE UPDATE ON reservation
    FOR EACH ROW
BEGIN
    :NEW.updated_at := SYSDATE;
END;


--  UUID 자동 생성 트리거
CREATE OR REPLACE TRIGGER trg_reservation_uuid
    BEFORE INSERT ON reservation
    FOR EACH ROW
BEGIN
    IF :NEW.reservation_id IS NULL THEN
        :NEW.reservation_id := SYS_GUID();
    END IF;
END;
