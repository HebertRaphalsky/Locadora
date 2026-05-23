package com.example.locadora.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security")
public class AppSecurityProperties {

    private SecurityMode mode = SecurityMode.SECURE;

    // ✅ Recomendado: sem default hardcoded (defina em application.properties)
    private String encryptionKey;

    public SecurityMode getMode() {
        return mode;
    }

    public void setMode(SecurityMode mode) {
        this.mode = mode;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public boolean isSecureMode() {
        return SecurityMode.SECURE.equals(mode);
    }

    public boolean isInsecureMode() {
        return SecurityMode.INSECURE.equals(mode);
    }
}