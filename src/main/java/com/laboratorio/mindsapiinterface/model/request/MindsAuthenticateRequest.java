package com.laboratorio.mindsapiinterface.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 19/09/2024
 * @updated 19/09/2024
 */

@Getter @Setter @AllArgsConstructor
public class MindsAuthenticateRequest {
    private String username;
    private String password;
}