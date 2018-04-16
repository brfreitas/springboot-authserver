
package io.github.brfreitas.authserver.domain;

import java.io.Serializable;

/**
 *
 * @author Bruno Freitas
 */
public class App implements Serializable {

    private String clientId;

    private String clientSecret;

    private String name;

    private String description;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
