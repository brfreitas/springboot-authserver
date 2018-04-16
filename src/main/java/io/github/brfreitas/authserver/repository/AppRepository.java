package io.github.brfreitas.authserver.repository;

import io.github.brfreitas.authserver.domain.App;
import org.springframework.stereotype.Repository;

@Repository
public class AppRepository extends RedisRepository<App>{

    private static final String APP_PREFIX_KEY = "APP";

    public void save(App app){
        setValue(getKey(app.getClientId()), app, true);
    }

    private String getKey(String clientId) {
        return String.format("%s:%s", APP_PREFIX_KEY, clientId);
    }

    public App getByClientId(String clientId){
        return getValue(getKey(clientId), App.class);
    }
}
