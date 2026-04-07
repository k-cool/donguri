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
    private Date createdAt;
    private Date updatedAt;
    private String isDeleted;
}