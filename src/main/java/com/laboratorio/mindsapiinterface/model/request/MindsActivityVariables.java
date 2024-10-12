package com.laboratorio.mindsapiinterface.model.request;

import java.util.List;
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
public class MindsActivityVariables {
    private String algorithm;
    private int limit;
    private List<String> inFeedNoticesDelivered;
    private String cursor;

    public MindsActivityVariables(int limit) {
        this.algorithm = "latest";
        this.limit = limit;
        this.inFeedNoticesDelivered = List.of("boost-latest-post", "invite-friends", "boost-channel", "plus-upgrade");
    }
    
    public MindsActivityVariables(int limit, String cursor) {
        this.algorithm = "latest";
        this.limit = limit;
        this.inFeedNoticesDelivered = List.of("boost-latest-post", "invite-friends", "boost-channel", "plus-upgrade");
        this.cursor = cursor;
    }
}