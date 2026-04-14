package com.c1.donguri.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    // reservation
    private String reservationId;
    private Date scheduledDate;
    private String recipientEmail;

    // email_content
    private String subject;
    private String content;
    private String bgmUrl;

    // template
    private String coverImgUrl; // 유저가 선택한게 있으면 오버라이딩
    private String bgColor;

    // 생성
    private String renderedHtml;
}
