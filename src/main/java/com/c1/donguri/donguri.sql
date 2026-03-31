CREATE TABLE users (
                       user_id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
                       email VARCHAR2(100) NOT NULL UNIQUE,
                       nickname VARCHAR2(50) NOT NULL UNIQUE,
                       password VARCHAR2(255) NOT NULL,
                       profile_img_url VARCHAR2(500),
                       created_at DATE DEFAULT SYSDATE,
                       updated_at DATE DEFAULT SYSDATE,
                       is_deleted VARCHAR2(1) DEFAULT 'N',

                       CONSTRAINT chk_users_is_deleted
                           CHECK (is_deleted IN ('Y', 'N'))
);

CREATE OR REPLACE TRIGGER trg_users_updated_at
BEFORE UPDATE ON users
                  FOR EACH ROW
BEGIN
    :NEW.updated_at := SYSDATE;
END;


