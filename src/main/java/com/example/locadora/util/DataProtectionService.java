package com.example.locadora.util;

import com.example.locadora.config.AppSecurityProperties;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class DataProtectionService {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String KEY_ALGO = "AES";
    private static final int IV_LENGTH_BYTES = 12;      // recomendado para GCM
    private static final int TAG_LENGTH_BITS = 128;     // tag de autenticação (16 bytes)

    private final AppSecurityProperties securityProperties;
    private final SecureRandom secureRandom = new SecureRandom();

    public DataProtectionService(AppSecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    public String protect(String plainText) {
        if (plainText == null) {
            return null;
        }

        try {
            byte[] iv = new byte[IV_LENGTH_BYTES];
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BITS, iv);
            cipher.init(Cipher.ENCRYPT_MODE, buildKey(), spec);

            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            // Armazenar IV + ciphertext (cipherText já inclui a tag GCM no final)
            byte[] out = ByteBuffer.allocate(iv.length + cipherText.length)
                    .put(iv)
                    .put(cipherText)
                    .array();

            return Base64.getEncoder().encodeToString(out);

        } catch (Exception e) {
            throw new IllegalStateException("Falha ao cifrar documento", e);
        }
    }

    public String reveal(String storedValue) {
        if (storedValue == null) {
            return null;
        }

        try {
            byte[] allBytes = Base64.getDecoder().decode(storedValue);
            ByteBuffer bb = ByteBuffer.wrap(allBytes);

            byte[] iv = new byte[IV_LENGTH_BYTES];
            bb.get(iv);

            byte[] cipherText = new byte[bb.remaining()];
            bb.get(cipherText);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BITS, iv);
            cipher.init(Cipher.DECRYPT_MODE, buildKey(), spec);

            byte[] plain = cipher.doFinal(cipherText);
            return new String(plain, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new IllegalStateException("Falha ao decifrar documento", e);
        }
    }

    private SecretKeySpec buildKey() {
        try {
            String keyMaterial = securityProperties.getEncryptionKey();
            if (keyMaterial == null || keyMaterial.isBlank()) {
                throw new IllegalStateException("EncryptionKey não configurada (app.security.encryptionKey)");
            }

            // Deriva 256-bit key a partir do texto configurado (melhor que truncar)
            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(keyMaterial.getBytes(StandardCharsets.UTF_8));

            return new SecretKeySpec(hash, KEY_ALGO);

        } catch (Exception e) {
            throw new IllegalStateException("Falha ao derivar chave de criptografia", e);
        }
    }
}