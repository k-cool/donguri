package com.c1.donguri.scheduler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

// 보낸 메일 보관함 DTO
public class SentMailDTO {
    private String reservationId;
    private String recipientEmail;
    private String subject;
    private String content;
    private String status;
    private Date sentAt;
}
