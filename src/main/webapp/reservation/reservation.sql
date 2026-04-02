
DROP TABLE reservation CASCADE CONSTRAINTS;
DROP TABLE users CASCADE CONSTRAINTS;
DROP TABLE email_content CASCADE CONSTRAINTS;
PURGE RECYCLEBIN;


CREATE TABLE users
(
    user_id VARCHAR2(36) PRIMARY KEY
);


CREATE TABLE email_content
(
    email_content_id VARCHAR2(36) PRIMARY KEY
);


CREATE TABLE reservation
(
    reservation_id   VARCHAR2(36) PRIMARY KEY,
    from_id          VARCHAR2(36)   NOT NULL,
    email_content_id VARCHAR2(36)   NOT NULL,
    sender_email     VARCHAR2(100)  NOT NULL,
    recipient_email  VARCHAR2(100)  NOT NULL,
    title            VARCHAR2(200)  NOT NULL,
    email_message    VARCHAR2(4000) NOT NULL,
    template_id      VARCHAR2(36),
    bgm              VARCHAR2(50),
    scheduled_date   DATE           NOT NULL,
    is_done          VARCHAR2(1) DEFAULT 'N' CHECK (is_done IN ('Y', 'N')),
    created_at       DATE        DEFAULT SYSDATE,
    updated_at       DATE        DEFAULT SYSDATE
);


ALTER TABLE reservation
    ADD CONSTRAINT fk_res_email
        FOREIGN KEY (email_content_id)
            REFERENCES email_content (email_content_id);

-- 예약 UUID 자동 생성 트리거
CREATE OR REPLACE TRIGGER trg_reservation_uuid
    BEFORE INSERT
    ON reservation
    FOR EACH ROW
BEGIN
    IF :NEW.reservation_id IS NULL THEN
        :NEW.reservation_id := LOWER(RAWTOHEX(SYS_GUID()));
    END IF;
END;



CREATE OR REPLACE TRIGGER trg_reservation_updated
    BEFORE UPDATE
    ON reservation
    FOR EACH ROW
BEGIN
    :NEW.updated_at := SYSDATE;
END;



INSERT INTO email_content(email_content_id)
VALUES ('test_content');

COMMIT;