package com.laboratorio.mindsapiinterface.model.response;

import com.google.gson.annotations.SerializedName;
import com.laboratorio.mindsapiinterface.model.MindsStatusEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 06/10/2024
 * @updated 06/10/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MindsUserActivityResponse {
    private String success;
    private List<MindsStatusEntity> entities;
    @SerializedName("load-next")
    private String loadNext;
}