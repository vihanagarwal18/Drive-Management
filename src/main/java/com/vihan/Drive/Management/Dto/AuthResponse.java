package com.vihan.Drive.Management.Dto;

import com.vihan.Drive.Management.Constants.AuthType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String token;

    private String userId;

    private String username;

    private String message;
    
    private AuthType authType;
}