/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package io.github.brfreitas.authserver;

import io.github.brfreitas.authserver.repository.TokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.brfreitas.authserver.jwt.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author Bruno Freitas
 */
@SpringBootApplication
@EnableAutoConfiguration
public class Application {

    @Autowired
    private TokenRepository tokenRepository;

    @Bean
    public TokenAuthenticationService tokenAuthService() {
        return new TokenAuthenticationService(tokenRepository);
    }
    
    @Bean
    public ObjectMapper mapper(){
        return new ObjectMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
