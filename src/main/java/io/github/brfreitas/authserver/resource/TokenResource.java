/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package io.github.brfreitas.authserver.resource;

import io.github.brfreitas.authserver.domain.AuthenticationTokenImpl;
import io.github.brfreitas.authserver.domain.App;
import io.github.brfreitas.authserver.dto.Valid;
import io.github.brfreitas.authserver.jwt.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Bruno Freitas
 */
@RestController
@RequestMapping("/token")
public class TokenResource {

    @Autowired
    private TokenAuthenticationService tokenService;

    @RequestMapping(value = "/valid",
            method = RequestMethod.POST,
            produces = "application/json")
    public ResponseEntity<Valid> isValid(@RequestParam("token") String token) {
        Authentication auth = tokenService.getAuthentication(token);//new TokenAuthenticationService(service).getAuthentication(token);
        if(auth == null)
            return new ResponseEntity<Valid>(new Valid(false), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<Valid>(new Valid(true), HttpStatus.OK);
    }

    @RequestMapping(value = "/invalidate", method = RequestMethod.GET)
    public String logout(AuthenticationTokenImpl auth, HttpServletResponse response) {
        tokenService.deleteToken(auth.getPrincipal().toString().toLowerCase(), auth.getHash());
        return "Logout Successfully";
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<App> saveApp(@RequestBody App App) {
        return null;
    }

}
