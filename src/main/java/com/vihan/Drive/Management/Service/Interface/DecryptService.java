package com.vihan.Drive.Management.Service.Interface;

import com.vihan.Drive.Management.Dto.User;

import java.util.List;

public interface DecryptService {

    String decrypt(String data, String key) throws Exception;

//    boolean isDecrypted(String data);
}
