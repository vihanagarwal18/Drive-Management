package com.vihan.Drive.Management.Entity;

import com.vihan.Drive.Management.Dto.LoginDetails;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserModel {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Embedded
    private LoginDetails loginDetails;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "encryption_key", nullable = false)
    private String encryptionKey;

    @Column(name = "decryption_key", nullable = false)
    private String decryptionKey;
}