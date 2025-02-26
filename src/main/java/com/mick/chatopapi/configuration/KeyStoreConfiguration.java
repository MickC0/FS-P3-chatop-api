package com.mick.chatopapi.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

@Configuration
public class KeyStoreConfiguration {

    @Value("${app.keystore.path}")
    private String keyStorePath;

    @Value("${app.keystore.alias}")
    private String keyAlias;

    @Value("${app.keystore.storepass}")
    private String storePass;

    @Value("${app.keystore.keypass}")
    private String keyPass;

    @Bean
    public KeyStore keyStore() {
        try (InputStream is = new FileInputStream(keyStorePath)) {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(is, storePass.toCharArray());
            return keyStore;
        } catch (Exception e) {
            throw new IllegalStateException("Unable to load the keystore: " + keyStorePath, e);
        }
    }

    @Bean
    public PrivateKey privateKey(KeyStore keyStore) {
        try {
            Key key = keyStore.getKey(keyAlias, keyPass.toCharArray());
            if (key instanceof PrivateKey privateKey) {
                return privateKey;
            }
            throw new IllegalStateException("The retrieved key is not of type PrivateKey");
        } catch (Exception e) {
            throw new IllegalStateException("Unable to retrieve the private key", e);
        }
    }

    @Bean
    public PublicKey publicKey(KeyStore keyStore) {
        try {
            Certificate cert = keyStore.getCertificate(keyAlias);
            if (cert == null) {
                throw new IllegalStateException("Certificate not found for alias : " + keyAlias);
            }
            return cert.getPublicKey();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to retrieve the public key", e);
        }
    }
}
