package com.vihan.Drive.Management.Service.Interface;

import com.vihan.Drive.Management.Entity.UserModel;
import java.util.List;

public interface AuthService {

    boolean isAuthenticated(String passwordEntered, String userId) throws Exception;

    List<String> generateKey(UserModel user);
}
