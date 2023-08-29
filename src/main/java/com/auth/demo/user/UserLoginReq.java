package com.auth.demo.user;

import lombok.Data;

@Data
public class UserLoginReq {
    private String username;
    private String password;
}
