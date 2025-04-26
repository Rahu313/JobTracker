package com.jobtracker.service;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
@Service
public class EncryptionService {
	  private static final String PASSWORD = "a1b2c3d4e5f67890123456789abcdef"; // Should be a valid 32-byte key for AES-256
	    private static final String SALT = "a1b2c3d4e5f67890"; // Should be a valid 16-byte key for AES


	    private static final TextEncryptor encryptor = Encryptors.text(PASSWORD, SALT);

	    public static String encrypt(Long id) {
	        return encryptor.encrypt(String.valueOf(id));
	    }

	    public static Long decrypt(String encryptedId) {
	        String decrypted = encryptor.decrypt(encryptedId);
	        return Long.parseLong(decrypted);
	    
}
}
