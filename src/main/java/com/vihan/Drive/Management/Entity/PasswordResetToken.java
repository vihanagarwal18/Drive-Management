package com.vihan.Drive.Management.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(targetEntity = AuthUserModel.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "auth_user_id")
    private AuthUserModel authUser;

    @Column(nullable = false)
    private Date expiryDate;
}