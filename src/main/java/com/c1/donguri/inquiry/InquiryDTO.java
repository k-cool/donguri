package com.c1.donguri.inquiry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class InquiryDTO {
    private String inquiryId;
    private String name;
    private String phone;
    private String email;
    private String message;
    private Date createdAt;
    private Date updatedAt;
}
