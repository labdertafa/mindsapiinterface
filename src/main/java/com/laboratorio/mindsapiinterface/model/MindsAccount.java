package com.laboratorio.mindsapiinterface.model;

import com.laboratorio.mindsapiinterface.utils.MindsApiConfig;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 18/09/2024
 * @updated 24/09/2024
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
    
    public boolean isSeguidorPotencial() {
        if ((this.blocked) || (this.blocked_by)) {
            return false;
        }
        
        if (this.subscriptions_count < 2) {
            return false;
        }

        return 2 * this.subscriptions_count >= this.subscribers_count;
    }
    
    public boolean isFuenteSeguidores() {
        MindsApiConfig config = MindsApiConfig.getInstance();
        int umbral = Integer.parseInt(config.getProperty("umbral_fuente_seguidores"));
        return this.subscribers_count >= umbral;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.guid);
        hash = 61 * hash + Objects.hashCode(this.username);
        hash = 61 * hash + Objects.hashCode(this.did);
        hash = 61 * hash + Objects.hashCode(this.urn);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MindsAccount other = (MindsAccount) obj;
        if (!Objects.equals(this.guid, other.guid)) {
            return false;
        }
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.did, other.did)) {
            return false;
        }
        return Objects.equals(this.urn, other.urn);
    }
}