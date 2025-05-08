package com.vihan.Drive.Management.Service.Interface;

import com.vihan.Drive.Management.Dto.User;

import java.util.List;

public interface AuthService {

    boolean isAuthenticated(String passwordEntered, String userId);

    List<String> generateKey(User user);
}
