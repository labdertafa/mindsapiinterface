package com.laboratorio.mindsapiinterface.model;

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
public class MindsAvatarUrl {
    private String tiny;
    private String small;
    private String medium;
    private String large;
    private String master;
}