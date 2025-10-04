package com.vihan.Drive.Management.Dto;

import com.vihan.Drive.Management.Constants.AuthType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenValidationResponse {
    private String userId;
    private String username;
    private boolean valid;
    private Instant expirationTime;
    private String[] roles;
    private boolean issuedFromCookie;
    private AuthType authType;
}