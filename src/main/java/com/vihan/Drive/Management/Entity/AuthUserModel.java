package com.vihan.Drive.Management.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(
    name = "auth_users",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_auth_user_id", columnNames = "id"),
        @UniqueConstraint(name = "uk_auth_user_user_id", columnNames = "user_id"),
        @UniqueConstraint(name = "uk_auth_encryption_key", columnNames = "encryption_key"),
        @UniqueConstraint(name = "uk_auth_decryption_key", columnNames = "decryption_key")
    },
    indexes = {
        @Index(name = "idx_auth_user_id", columnList = "user_id")
    }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUserModel {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_auth_user_user_id")
    )
    private UserModel user;

    @NotBlank(message = "Encryption key is required")
    @Size(min = 32, max = 64, message = "Encryption key must be between 32 and 64 characters")
    @Column(name = "encryption_key", nullable = false, length = 64)
    private String encryptionKey;

    @NotBlank(message = "Decryption key is required")
    @Size(min = 32, max = 64, message = "Decryption key must be between 32 and 64 characters")
    @Column(name = "decryption_key", nullable = false, length = 64)
    private String decryptionKey;

    @NotBlank(message = "Encrypted password is required")
    @Column(name = "encrypted_password", nullable = false, length = 255)
    private String encryptedPassword;
}