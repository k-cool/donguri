package com.c1.donguri.template;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateDTO {
    private String templateId;
    private String name;
    private String bgColor;
    private String type;
    private String coverImgUrl;
    private String qrUrl;
    private String createdAt;
    private String updatedAt;
}
