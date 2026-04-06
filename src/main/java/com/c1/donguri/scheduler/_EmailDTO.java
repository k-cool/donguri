package com.c1.donguri.scheduler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/*

    사진 기준 컬럼:
    - email_content_id
    - template_id
    - sender_id
    - _title
    - subject
    - content
    - created_at
    - updated_at

    + 예약 발송을 위해 화면에서 잠깐 쓰는 값들:
    - receiverEmail
    - reserveDateTime

*/

@Data
@AllArgsConstructor
@NoArgsConstructor

public class _EmailDTO {
    //    private byte emailContentId;
    private String emailContentId;
    private String templateId;
    private String senderId;
    private String subject;
    private String content;
    private String coverImgUrl;
    private Date createdAt;
    private Date updatedAt;

    // DB에는 없지만 화면/예약 처리에서 필요한 값
    private String receiverEmail;
    private Date reserveDateTime;
    private String title;

}