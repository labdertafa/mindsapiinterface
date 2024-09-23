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
public class MindsCarousel {
    private String guid;
    private boolean top_offset;
    private String src;
}