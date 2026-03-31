CREATE TABLE USER_TEST
(
    user_id  RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
    name     varchar2(10 char)         not null,
    email    varchar2(100 char) unique not null,
    password varchar2(255 char)        not null
);

INSERT INTO USER_TEST
VALUES (SYS_GUID(), 'siwon', 'test@gmail.com', 'testtest');
INSERT INTO USER_TEST
VALUES (SYS_GUID(), 'chaejin', 'test1@gmail.com', 'testtest');
INSERT INTO USER_TEST
VALUES (SYS_GUID(), 'kyungmin', 'test2@gmail.com', 'testtest');

SELECT *
FROM USER_TEST;