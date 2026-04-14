package com.c1.donguri.reservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class _TemplateDTO {
    private String templateId;
    //    private String name;
    private String templateName;
    private String type;
    private String bgColor;
    private String coverImgUrl;
    private String savedAt;
}