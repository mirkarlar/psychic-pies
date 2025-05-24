package nl.hilgenbos.psyichic_pies.security;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

@Component
public class JwtSigningKeyProvider {
    @Value("${jwt.key-store}")
    private String keystore;
    @Value("${jwt.key-store-password}")
    private String password;
    @Value("${jwt.key-alias}")
    private String alias;
    @Value("${jwt.key-store-type}")
    private String keystoreType;
    private Key privateKey;

    @PostConstruct
    public void init() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        Resource resource = new ClassPathResource(keystore);
        KeyStore keyStore = KeyStore.getInstance(keystoreType);
        keyStore.load(resource.getInputStream(), password.toCharArray());
        privateKey = keyStore.getKey(alias, password.toCharArray());
    }

    public Key getPrivateKey() {
        return privateKey;
    }


}
