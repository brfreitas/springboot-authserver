package io.github.brfreitas.authserver.repository;

import io.github.brfreitas.authserver.domain.App;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class TokenRepository extends RedisRepository<App>{

    private static final String TOKEN_PREFIX_KEY = "TOKEN";

    public void save(String clientId, String hash, App app){
        setValue(getKey(clientId, hash), app, TimeUnit.SECONDS, 3600L, true);
    }

    public App get(String clientId, String hash){
        return getValue(getKey(clientId, hash), App.class);
    }

    public void del(String clientId, String hash){
        delKey(getKey(clientId, hash));
    }

    private String getKey(String clientId, String hash) {
        return String.format("%s:%s:%s", TOKEN_PREFIX_KEY, clientId, hash);
    }
}
