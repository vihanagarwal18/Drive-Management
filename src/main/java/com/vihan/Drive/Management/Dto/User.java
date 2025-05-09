package com.vihan.Drive.Management.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
