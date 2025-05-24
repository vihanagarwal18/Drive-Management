package com.vihan.Drive.Management.Dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthUser {

    private final String id;

    private final User user;

    private final String encryptionKey;

    private final String decryptionKey;

    private final String encryptedPassword;
}
