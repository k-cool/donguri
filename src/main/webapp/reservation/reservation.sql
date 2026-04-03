
DROP TABLE reservation CASCADE CONSTRAINTS;
DROP TABLE users CASCADE CONSTRAINTS;
DROP TABLE email_content CASCADE CONSTRAINTS;
PURGE RECYCLEBIN;


CREATE TABLE users /*이메일을 예약하는 사용자 정보*/
(
    user_id VARCHAR2(36) PRIMARY KEY /*사용자 자체 번호*/
);


CREATE TABLE email_content /*이메일 콘텐츠저장*/
(
    email_content_id VARCHAR2(36) PRIMARY KEY
);


CREATE TABLE reservation
(
    reservation_id   VARCHAR2(36) PRIMARY KEY, /*각 예약 자체를 고유하게 식별하기 위함 ID*/
    from_id          VARCHAR2(36)   NOT NULL, /*누가예약했는지요,예약한사용자아이디*/
    email_content_id VARCHAR2(36)   NOT NULL,/*이메일 콘텐츠 아이디*/
    sender_email     VARCHAR2(100)  NOT NULL,/*보내는 이메일 주소*/
    recipient_email  VARCHAR2(100)  NOT NULL,/*받는이메일주소*/
    title            VARCHAR2(200)  NOT NULL,/*이메일제목*/
    email_message    VARCHAR2(4000) NOT NULL,/*이메일본문*/
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