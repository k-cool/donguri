package com.c1.donguri.reservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InsertReservationDTO {
    private String reservationId;
    private String fromId;
    private String emailContentId;
    private String recipientEmail;
    private String subject;
    private String content;
    private String templateId;
    private String coverImgUrl;
    private String bgmUrl;
    private String scheduledDate;
}