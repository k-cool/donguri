package com.c1.donguri.reservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReservationDTO {

    private String reservationId;
    private String fromId;
    private String emailContentId;

    private String senderEmail;
    private String recipientEmail;

    private String subject;
    private String content;

    private String templateId;
    private String bgm;

    private String scheduledDate;
    private String isDone;
}