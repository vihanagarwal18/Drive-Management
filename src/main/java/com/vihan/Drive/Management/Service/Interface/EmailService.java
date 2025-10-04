package com.vihan.Drive.Management.Service.Interface;

public interface EmailService {

    void sendPasswordResetEmail(String to, String token, String username);
}