package com.laboratorio.mindsapiinterface.model;

import java.io.Serializable;
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
public class MindsSession implements Serializable {
    private String cookies;
    private String xToken;
    private String xVersion;
}