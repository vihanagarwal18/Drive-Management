package com.vihan.Drive.Management.Service.Impl;

import com.vihan.Drive.Management.Service.Interface.DecryptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import static com.vihan.Drive.Management.Constants.Constant.ENCRYPT_DATA_ALGORITHM;

@Slf4j
@Service
@RequiredArgsConstructor
public class DecryptServiceImpl implements DecryptService {

    @Override
    public String decrypt(String data, String key) throws Exception {
        SecretKey secretKey = generateKey(key);

        // Configure the Cipher for AES encryption
        Cipher cipher = Cipher.getInstance(ENCRYPT_DATA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encryptedBytes = cipher.doFinal(data.getBytes());

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private SecretKey generateKey(String key) {
        // Make sure the key is 16 bytes (AES-128), 24 bytes (AES-192), or 32 bytes (AES-256)
        if (key.length() != 16) {
            throw new IllegalArgumentException("Key must be 16 bytes for AES-128 encryption.");
        }
        return new SecretKeySpec(key.getBytes(), ENCRYPT_DATA_ALGORITHM);
    }
}
