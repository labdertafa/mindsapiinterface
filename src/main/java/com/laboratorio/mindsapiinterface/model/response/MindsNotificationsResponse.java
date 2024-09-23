package com.laboratorio.mindsapiinterface.model.response;

import com.google.gson.annotations.SerializedName;
import com.laboratorio.mindsapiinterface.model.MindsNotification;
import java.util.List;
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
public class MindsNotificationsResponse {
    private String status;
    private List<MindsNotification> notifications;
    @SerializedName("load-next")
    private String loadNext;
}