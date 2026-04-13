package com.c1.donguri.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String userId;
    private String email;
    private String nickname;
    private String password;
    private String profileImgUrl;
    private String roll;        // 🚨 새로 추가: 권한 (ADMIN 또는 MEMBER)
    private Date omikujiAt;     // 🚨 새로 추가: 뽑기(오미쿠지) 한 날짜/시간
    private Date createdAt;
    private Date updatedAt;
    private String isDeleted;
}