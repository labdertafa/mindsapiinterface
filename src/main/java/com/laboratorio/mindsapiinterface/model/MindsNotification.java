package com.laboratorio.mindsapiinterface.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 23/09/2024
 * @updated 23/09/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MindsNotification {
    private String uuid;
    private String urn;
    private String to_guid;
    private String from_guid;
    private MindsAccount from;
    private String entity_urn;
    private MindsAccount entity;
    private boolean read;
    private long created_timestamp;
    private String type;
    private int merged_count;
    private String merge_key;
}