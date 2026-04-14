/*
    테이블 생성
 */

CREATE TABLE users
(
    user_id         RAW(16)      DEFAULT SYS_GUID() PRIMARY KEY,
    email           VARCHAR2(50)      NOT NULL UNIQUE,
    nickname        VARCHAR2(10 char) NOT NULL UNIQUE,
    password        VARCHAR2(100)     NOT NULL,
    profile_img_url VARCHAR2(500),
    roll            VARCHAR2(10) DEFAULT 'MEMBER',
    omikuji_at      DATE,
    created_at      DATE         DEFAULT SYSDATE,
    updated_at      DATE         DEFAULT SYSDATE,
    is_deleted      VARCHAR2(1)  DEFAULT 'N',

    CONSTRAINT chk_users_is_deleted
        CHECK (is_deleted IN ('Y', 'N')),
    CONSTRAINT chk_users_roll
        CHECK (roll IN ('ADMIN', 'MEMBER'))
);

CREATE OR REPLACE TRIGGER trg_users_updated_at
    BEFORE UPDATE
    ON users
    FOR EACH ROW
BEGIN
    :NEW.updated_at := SYSDATE;
END;
/

CREATE TABLE template
(
    template_id   RAW(16)     DEFAULT SYS_GUID() PRIMARY KEY,
    name          VARCHAR2(20 char) NOT NULL,
    body_html     CLOB              NOT NULL,
    type          VARCHAR2(10)      NOT NULL,
    cover_img_url VARCHAR2(500)     NOT NULL,
    qr_url        VARCHAR2(500),
    bg_color      VARCHAR2(8) DEFAULT '5D4037',
    created_at    DATE        DEFAULT SYSDATE,
    updated_at    DATE        DEFAULT SYSDATE,

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
/



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
SELECT template_id
FROM template;

SELECT user_id
FROM users;

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
/


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
/

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
/


CREATE TABLE inquiry
(
    inquiry_id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
    name       VARCHAR2(30 char)   not null,
    phone      VARCHAR2(20 char)   not null,
    email      VARCHAR2(50 char)   not null,
    message    VARCHAR2(1000 char) not null,
    created_at DATE    DEFAULT SYSDATE,
    updated_at DATE    DEFAULT SYSDATE
);

CREATE OR REPLACE TRIGGER trg_inquiry_updated_at
    BEFORE UPDATE
    ON inquiry
    FOR EACH ROW
BEGIN
    :NEW.updated_at := SYSDATE;
END;
/