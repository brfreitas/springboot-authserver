/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package io.github.brfreitas.authserver.config;

import java.util.Collections;

import io.github.brfreitas.authserver.domain.App;
import io.github.brfreitas.authserver.domain.AuthenticationTokenImpl;
import io.github.brfreitas.authserver.repository.AppRepository;
import io.github.brfreitas.authserver.repository.TokenRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 *
 * @author Bruno Freitas
 */
public class AuthenticationProviderImpl implements org.springframework.security.authentication.AuthenticationProvider {

    private AppRepository appRepository;
    private TokenRepository tokenRepository;

    public AuthenticationProviderImpl(AppRepository appRepository, TokenRepository tokenRepository) {
        this.appRepository = appRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String AppId = authentication.getPrincipal() + "";

        if (AppId == null || AppId.length() < 5) {
            throw new BadCredentialsException("AppId not found.");
        }

        //Right now just authenticate on the basis of the user=pass
        App sApp = (App) appRepository.getByClientId(AppId);
        if (sApp != null && sApp.getClientSecret().equals(authentication.getCredentials())) {
            AuthenticationTokenImpl auth = new AuthenticationTokenImpl(sApp.getClientId(), Collections.emptyList());
            auth.setAuthenticated(true);
            auth.setDetails(sApp);
            tokenRepository.save(AppId.toLowerCase(), auth.getHash(), sApp);
            return auth;
        } else {
            throw new BadCredentialsException("Invalid AppId or AppSecret.");
        }
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(UsernamePasswordAuthenticationToken.class);
    }

}
