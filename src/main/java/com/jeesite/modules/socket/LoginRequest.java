package com.jeesite.modules.socket;

import lombok.Data;

@Data
public class LoginRequest {
    int code;
    String body;
}
