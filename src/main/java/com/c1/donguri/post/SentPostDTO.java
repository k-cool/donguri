package com.c1.donguri.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor

// 보낸 메일 보관함 DTO
public class SentPostDTO {
    private String userId;
    private String reservationId;
    private String recipientEmail;
    private String subject;
    private String content;
    private String status;
    private String isDone;
    private Timestamp sentAt;
}
