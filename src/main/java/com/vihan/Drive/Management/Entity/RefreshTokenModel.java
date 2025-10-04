package com.vihan.Drive.Management.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenModel {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_refresh_token_user_id")
    )
    private UserModel user;

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

    @Column(name = "revoked")
    private boolean revoked;

    @Column(name = "issued_at", nullable = false)
    private Instant issuedAt;

    public boolean isExpired() {
        return Instant.now().isAfter(expiryDate);
    }

    public boolean isValid() {
        return !isExpired() && !revoked;
    }
}