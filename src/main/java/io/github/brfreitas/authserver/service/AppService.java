package io.github.brfreitas.authserver.service;

import io.github.brfreitas.authserver.repository.AppRepository;
import io.github.brfreitas.authserver.domain.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service("AppService")
public class AppService {

    @Autowired
    private AppRepository appRepository;

    private  KeyGenerator gen;

    AppService() {
        try {
            gen = KeyGenerator.getInstance("AES");
            gen.init(128);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public boolean isValid(App App) {
        return App != null && App.getName() != null;
    }

    public String generateSecretKey() {
        return Base64.getEncoder().encodeToString(gen.generateKey().getEncoded());
    }

    public App create(App App){
        App.setClientId(generateSecretKey());
        App.setClientSecret(generateSecretKey());
        appRepository.save(App);
        return App;
    }
}
