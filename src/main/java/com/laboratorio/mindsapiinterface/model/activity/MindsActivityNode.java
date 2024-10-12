package com.laboratorio.mindsapiinterface.model.activity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 07/10/2024
 * @updated 07/10/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MindsActivityNode {
    private String id;
    private boolean dismissible;
    private String goalButtonUrl;
    private String goalButtonText;
    private String legacy;
    private String __typename;  // Nos interensan los ActivityNode
}