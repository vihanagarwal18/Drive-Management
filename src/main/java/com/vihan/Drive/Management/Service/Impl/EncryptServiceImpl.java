package com.vihan.Drive.Management.Service.Impl;

import com.vihan.Drive.Management.Security.CryptoUtil;
import com.vihan.Drive.Management.Service.Interface.EncryptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EncryptServiceImpl implements EncryptService {

    private final CryptoUtil cryptoUtil;

    @Override
    public String encrypt(String data, String encryptionKey) {
        try {
            return cryptoUtil.encrypt(data, encryptionKey);
        } catch (Exception e) {
            log.error("Encryption failed", e);
            throw new RuntimeException("Encryption failed: " + e.getMessage(), e);
        }
    }
}
