package com.laboratorio.mindsapiinterface.model.response;

import com.laboratorio.mindsapiinterface.model.MindsAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 18/09/2024
 * @updated 18/09/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MindsGetAccountResponse {
    private String status;
    private MindsAccount channel;
    private boolean require_login;
}