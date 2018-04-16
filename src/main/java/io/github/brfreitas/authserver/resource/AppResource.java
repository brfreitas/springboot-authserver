/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package io.github.brfreitas.authserver.resource;

import io.github.brfreitas.authserver.domain.App;
import io.github.brfreitas.authserver.jwt.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Bruno Freitas
 */
@RestController
@RequestMapping("/app")
public class AppResource {

    @Autowired
    private TokenAuthenticationService tokenService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<App> saveApp(@RequestBody App App) {
        return null;
    }

}
