package com.laboratorio.mindsapiinterface.model.request;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 19/09/2024
 * @updated 23/09/2024
 */

@Getter @Setter @AllArgsConstructor
public class MindsPostRequest {
    private String message;
    private String wire_threshold;
    private String paywall;
    private String time_created;
    private boolean mature;
    private List<String> nsfw;
    private List<String> tags;
    private String access_id;
    private String license;
    private boolean post_to_permaweb;
    private boolean entity_guid_update;
    private String title;
    private List<String> attachment_guids;
    private boolean is_rich;

    public MindsPostRequest(String message) {
        this.message = message;
        this.time_created = Long.toString(Instant.now().getEpochSecond());
        this.nsfw = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.access_id = "2";
        this.license = "all-rights-reserved";
        this.entity_guid_update = true;
    }
    
    public MindsPostRequest(String message, String imageId) {
        this.message = message;
        this.time_created = Long.toString(Instant.now().getEpochSecond());
        this.nsfw = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.access_id = "2";
        this.license = "all-rights-reserved";
        this.attachment_guids  =new ArrayList<>();
        this.attachment_guids.add(imageId);
    }
}