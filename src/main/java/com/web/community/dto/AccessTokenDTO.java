package com.web.community.dto;

import lombok.Data;

@Data
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String state;
    private String redirect_url;
}
