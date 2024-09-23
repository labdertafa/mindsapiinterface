package com.laboratorio.mindsapiinterface.model.response;

import com.google.gson.annotations.SerializedName;
import com.laboratorio.mindsapiinterface.model.MindsAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 20/09/2024
 * @updated 23/09/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MindsPostResponse {
    private String guid;
    private String type;
    private String time_created;
    private String time_updated;
    private String container_guid;
    private String owner_guid;
    private String access_id;
    private String title;
    private boolean blurb;
    private boolean perma_url;
    private String message;
    private MindsAccount ownerObj;
    private boolean containerObj;
    private boolean thumbnail_src;
    private boolean remind_object;
    private boolean entity_guid;
    private boolean featured;
    private boolean featured_guid;
    private boolean custom_type;
    @SerializedName("thumbs:up:count")
    private int thumbsUpCount;
    @SerializedName("thumbs:down:count")
    private int thumbsDownCount;
    private boolean p2p_boosted;
    private boolean mature;
    private boolean monetized;
    private boolean edited;
    private boolean wire_totals;
    private int boost_rejection_reason;
    private boolean pending;
    private int rating;
    private boolean ephemeral;
    private boolean hide_impressions;
    private boolean pinned;
    private boolean permaweb_id;
    private boolean site_membership;
    private String source;
    @SerializedName("comments:count")
    private int commentsCount;
    private String urn;
    private int impressions;
    private int reminds;
    private boolean allow_comments;
    private String license;
    private int quotes;
}