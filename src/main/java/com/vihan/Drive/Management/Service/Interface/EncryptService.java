package com.vihan.Drive.Management.Service.Interface;

import com.vihan.Drive.Management.Dto.User;

import java.util.List;

public interface EncryptService {

    String encrypt(String data, String key) throws Exception;



//    boolean isEncrypted(String data);

}
