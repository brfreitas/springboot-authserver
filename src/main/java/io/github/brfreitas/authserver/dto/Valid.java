
package io.github.brfreitas.authserver.dto;

import lombok.Data;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Bruno Freitas
 */
@Data
public class Valid {

    private Boolean valid;

    public Valid(Boolean valid) {
        this.valid = valid;
    }
}
