package com.laboratorio.mindsapiinterface.model.request;

import java.util.List;
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
public class MindsActivityVariables {
    private String algorithm;
    private int limit;
    private List<String> inFeedNoticesDelivered;
    private String cursor;

    public MindsActivityVariables(int limit, String algorithm) {
        this.algorithm = algorithm;
        this.limit = limit;
        switch (algorithm) {
            case "latest" -> {
                this.inFeedNoticesDelivered = List.of("boost-latest-post", "invite-friends", "boost-channel", "plus-upgrade");
            }
            case "groups" -> {
                this.inFeedNoticesDelivered = List.of("verify-uniqueness", "invite-friends", "boost-channel", "plus-upgrade");
            }
            default -> {
                this.inFeedNoticesDelivered = List.of("boost-latest-post", "invite-friends", "boost-channel", "plus-upgrade");
            }
        }
    }
    
    public MindsActivityVariables(int limit, String algorithm, String cursor) {
        this.algorithm = algorithm;
        this.limit = limit;
        switch (algorithm) {
            case "latest" -> {
                this.inFeedNoticesDelivered = List.of("boost-latest-post", "invite-friends", "boost-channel", "plus-upgrade");
            }
            case "groups" -> {
                this.inFeedNoticesDelivered = List.of("verify-uniqueness", "invite-friends", "boost-channel", "plus-upgrade");
            }
            default -> {
                this.inFeedNoticesDelivered = List.of("boost-latest-post", "invite-friends", "boost-channel", "plus-upgrade");
            }
        }
        this.cursor = cursor;
    }
}