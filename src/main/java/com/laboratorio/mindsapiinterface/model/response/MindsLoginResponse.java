package com.laboratorio.mindsapiinterface.model.response;

import com.laboratorio.mindsapiinterface.model.MindsAccount;
import java.util.List;
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
public class MindsLoginResponse {
    private String status;
    private MindsAccount user;
    private List<String> permissions;
    private boolean opt_out_analytics;
}