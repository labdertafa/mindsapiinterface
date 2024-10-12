package com.laboratorio.mindsapiinterface.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 20/09/2024
 * @updated 07/10/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MindsStatus {
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
    private boolean thumbnail_src;
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

    @Override
    public String toString() {
        return "MindsStatus{" + "guid=" + guid + ", type=" + type + ", time_created=" + time_created + ", time_updated=" + time_updated + ", owner_guid=" + owner_guid + ", title=" + title + ", message=" + message + ", urn=" + urn + ", impressions=" + impressions + '}';
    }
}