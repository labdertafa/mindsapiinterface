package com.laboratorio.mindsapiinterface.model.activity;

import java.util.List;
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
public class MindsNewsfeed {
    private List<MindsActivityEdge> edges;
    private MindsPageInfo pageInfo;
    private String __typename;
}