package com.vihan.Drive.Management.Dto;

import com.vihan.Drive.Management.Constants.AuthType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for authentication requests with specified authentication type
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthTypeRequest {
    private String username;
    private String password;
    private AuthType authType;
    private String clientCertificate;
    private String digestNonce;
}