package com.laboratorio.mindsapiinterface.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 07/10/2024
 * @updated 07/10/2024
 */

@Getter @Setter @AllArgsConstructor
public class MindsActivityRequest {
    private String operationName;
    private MindsActivityVariables variables;
    private String query;

    public MindsActivityRequest(int limit, String query) {
        this.operationName = "FetchNewsfeed";
        this.variables = new MindsActivityVariables(limit);
        this.query = query;
    }
    
    public MindsActivityRequest(int limit, String cursor, String query) {
        this.operationName = "FetchNewsfeed";
        this.variables = new MindsActivityVariables(limit, cursor);
        this.query = query;
    }
}