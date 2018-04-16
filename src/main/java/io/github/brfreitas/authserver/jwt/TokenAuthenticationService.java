/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.brfreitas.authserver.jwt;

import io.github.brfreitas.authserver.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import io.github.brfreitas.authserver.domain.AuthenticationTokenImpl;
import io.github.brfreitas.authserver.domain.App;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.Sha512DigestUtils;

/**
 *
 * @author Bruno Freitas
 */
public class TokenAuthenticationService {

    private TokenRepository service;

    private long EXPIRATIONTIME = 1000 * 60 * 60; // 1 hr

    private String secret;

    private String tokenPrefix = "Bearer";

    private String headerString = "Authorization";

    public TokenAuthenticationService(TokenRepository service) {
        this.service = service;
        secret = Sha512DigestUtils.shaHex(System.getenv("ENC_KEY"));
    }

    public void addAuthentication(HttpServletResponse response, AuthenticationTokenImpl auth) {
        // We generate a token now.
        Map<String, Object> claims = new HashMap<>();
        claims.put("AppId", auth.getPrincipal());
        claims.put("hash", auth.getHash());
        String JWT = Jwts.builder()
                .setSubject(auth.getPrincipal().toString())
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        response.addHeader(headerString, tokenPrefix + " " + JWT);
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(headerString);
        return getAuthentication(token);
    }

    public Authentication getAuthentication(String token) {
        if (token == null) {
            return null;
        }
        //remove "Bearer" text
        token = token.replace(tokenPrefix, "").trim();

        //Validating the token
        if (token != null && !token.isEmpty()) {
            // parsing the token.`
            Claims claims = null;
            try {
                claims = Jwts.parser()
                        .setSigningKey(secret)
                        .parseClaimsJws(token).getBody();

            } catch ( Exception e) {
                return null;
            }

            //Valid token and now checking to see if the token is actally expired or alive by quering in redis.
            if (claims != null && claims.containsKey("ClientId")) {
                String clientId = claims.get("ClientId").toString();
                String hash = claims.get("hash").toString();
                App app = service.get(clientId, hash);
                if (app != null) {
                    AuthenticationTokenImpl auth = new AuthenticationTokenImpl(app.getClientId(), Collections.emptyList());
                    auth.setDetails(app);
                    auth.authenticate();
                    return auth;
                } else {
                    return new UsernamePasswordAuthenticationToken(null, null);
                }

            }
        }
        return null;
    }

    public void deleteToken(String clientId, String hash){
        service.del(clientId, hash);
    }
}
