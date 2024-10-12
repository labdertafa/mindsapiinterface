package com.laboratorio.mindsapiinterface.model.response;

import com.laboratorio.mindsapiinterface.model.activity.MindsActivityData;
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
public class MindsActivityResponse {
    private MindsActivityData data;
}