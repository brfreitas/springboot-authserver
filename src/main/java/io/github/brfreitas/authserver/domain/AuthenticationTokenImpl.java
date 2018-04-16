/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.brfreitas.authserver.domain;

import java.util.Collection;

import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.DigestUtils;

/**
 *
 * @author Bruno Freitas
 */
@Slf4j
@ToString(callSuper = true)
public class AuthenticationTokenImpl extends AbstractAuthenticationToken {

    @Setter
    private String AppId;

    public AuthenticationTokenImpl(String principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.AppId = principal;
    }

    public void authenticate() {
        if (getDetails() != null && getDetails() instanceof App) {
            setAuthenticated(true);
        } else {
            setAuthenticated(false);
        }
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getPrincipal() {
        return AppId != null ? AppId.toString() : "";
    }

    public String getHash() {
        return DigestUtils.md5DigestAsHex(String.format("%s_%s", AppId, ((App) getDetails()).getClientSecret()).getBytes());
    }

}
