package com.vihan.Drive.Management.Service.Impl;

import com.vihan.Drive.Management.Security.CryptoUtil;
import com.vihan.Drive.Management.Service.Interface.DecryptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DecryptServiceImpl implements DecryptService {

    private final CryptoUtil cryptoUtil;

    @Override
    public String decrypt(String encryptedData, String decryptionKey) {
        try {
            return cryptoUtil.decrypt(encryptedData, decryptionKey);
        } catch (Exception e) {
            log.error("Decryption failed", e);
            throw new RuntimeException("Decryption failed: " + e.getMessage(), e);
        }
    }
}
