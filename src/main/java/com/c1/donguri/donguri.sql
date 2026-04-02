-- 1. 기존 테이블이 있다면 삭제 (신규 생성을 위해)
DROP TABLE users;

-- 2. 테이블 생성 (user_id를 RAW(16)으로 변경 및 자동 생성 설정)
CREATE TABLE users
(
    user_id         RAW(16)     DEFAULT SYS_GUID() PRIMARY KEY,
    email           VARCHAR2(100) UNIQUE NOT NULL,
    nickname        VARCHAR2(50) UNIQUE  NOT NULL,
    password        VARCHAR2(255)        NOT NULL,
    profile_img_url VARCHAR2(500),
    created_at      DATE        DEFAULT SYSDATE,
    updated_at      DATE        DEFAULT SYSDATE,
    is_deleted      VARCHAR2(1) DEFAULT 'N' CHECK (is_deleted IN ('Y', 'N'))
);

-- 3. updated_at 트리거 (기존과 동일)
CREATE OR REPLACE TRIGGER trg_users_updated_at
    BEFORE UPDATE
    ON users
    FOR EACH ROW
BEGIN
    :NEW.updated_at := SYSDATE;
END;
/