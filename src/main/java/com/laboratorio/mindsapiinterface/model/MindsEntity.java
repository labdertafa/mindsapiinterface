package com.laboratorio.mindsapiinterface.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 19/09/2024
 * @updated 19/09/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MindsEntity {
    private String guid;
    private String owner_guid;
    private long timestamp;
    private String urn;
    private String entity;
}