package com.vihan.Drive.Management.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    private String id;

    private LoginDetails loginDetails;

    private String name;

    private String email;

    private String phoneNumber;

    private String address;

    private String encryptionKey;

    private String decryptionKey;
}
