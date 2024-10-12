package com.laboratorio.mindsapiinterface.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 06/10/2024
 * @updated 06/10/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MindsStatusEntity {
    private String guid;
    private String owner_guid;
    private long timestamp;
    private String urn;
    private MindsStatus entity;
}