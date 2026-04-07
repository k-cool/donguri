package com.c1.donguri.scheduler;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EmailJobDTO {
    private String reservationId;
    private String recipientEmail;
    private Date scheduledDate;
    private String subject;
    private String content;
    private String isDone;
    private String bodyHtml;
    private String coverImgUrl;


    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static EmailJobDTO fromJSON(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, EmailJobDTO.class);
    }
}