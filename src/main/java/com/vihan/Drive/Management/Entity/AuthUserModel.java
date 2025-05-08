package com.vihan.Drive.Management.Entity;

import com.vihan.Drive.Management.Dto.User;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "auth_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUserModel {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "encryption_key", nullable = false)
    private String encryptionKey;

    @Column(name = "decryption_key", nullable = false)
    private String decryptionKey;

    @Column(name = "encrypted_password", nullable = false)
    private String encryptedPassword;
}