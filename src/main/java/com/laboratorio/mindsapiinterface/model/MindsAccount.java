package com.laboratorio.mindsapiinterface.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 18/09/2024
 * @updated 23/09/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MindsAccount {
    private String guid;
    private String type;
    private boolean subtype;
    private String time_created;
    private String time_updated;
    private String container_guid;
    private String owner_guid;
    private boolean site_guid;
    private String access_id;
    private List<String> tags;
    private String name;
    private String username;
    private String language;
    private String icontime;
    private boolean legacy_guid;
    private boolean featured_id;
    private String banned;
    private boolean ban_reason;
    private String website;
    private String briefdescription;
    private String gender;
    private String city;
    private boolean boostProPlus;
    private String monetized;
    private boolean signup_method;
    private List<MindsSocialProfil> social_profiles;
    private boolean feature_flags;
    private boolean plus;
    private boolean hashtags;
    private boolean verified;
    private boolean founder;
    private boolean disabled_boost;
    private boolean boost_autorotate;
    private List<String> pinned_posts;
    private boolean is_mature;
    private boolean mature_lock;
    private long last_accepted_tos;
    private int opted_in_hashtags;
    private String last_avatar_upload;
    private boolean canary;
    private String theme;
    private boolean toaster_notifications;
    private int mode;
    private String btc_address;
    private String surge_token;
    private boolean hide_share_buttons;
    private boolean allow_unsubscribed_contact;
    private List<String> dismissed_widgets;
    private int liquidity_spot_opt_out;
    private String did;
    private boolean chat;
    private String urn;
    private boolean subscribed;
    private boolean subscriber;
    private int subscribers_count;
    private int subscriptions_count;
    private int impressions;
    private int boost_rating;
    private boolean pro;
    private String plus_method;
    private boolean pro_published;
    private boolean rewards;
    private boolean is_admin;
    private int onchain_booster;
    private boolean email_confirmed;
    private String eth_wallet;
    private int rating;
    private boolean disable_autoplay_videos;
    private String icon_url;
    private String source;
    private String canonical_url;
    private MindsAvatarUrl avatar_url;
    private List<MindsCarousel> carousels;
    private boolean blocked;
    private boolean blocked_by;
}