package com.vihan.Drive.Management.Constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing different authentication types
 */
@Getter
@AllArgsConstructor
public enum AuthType {
    BASIC_AUTH("BASIC"),
    FORM_AUTH("FORM"),
    CLIENT_CERT_AUTH("CLIENT_CERT"),
    DIGEST_AUTH("DIGEST");
    
    private final String value;
}