package com.laboratorio.mindsapiinterface.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 07/10/2024
 * @updated 15/10/2024
 */

@Getter @Setter @AllArgsConstructor
public class MindsActivityRequest {
    private String operationName;
    private MindsActivityVariables variables;
    private String query;

    public MindsActivityRequest(int limit, String algorithm, String query) {
        this.operationName = "FetchNewsfeed";
        this.variables = new MindsActivityVariables(limit, algorithm);
        this.query = query;
    }
    
    public MindsActivityRequest(int limit, String algorithm, String query, String cursor) {
        this.operationName = "FetchNewsfeed";
        this.variables = new MindsActivityVariables(limit, algorithm, cursor);
        this.query = query;
    }
}