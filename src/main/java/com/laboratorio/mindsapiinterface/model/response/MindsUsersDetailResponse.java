package com.laboratorio.mindsapiinterface.model.response;

import com.laboratorio.mindsapiinterface.model.MindsAccount;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 19/09/2024
 * @updated 19/09/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MindsUsersDetailResponse {
    private String status;
    private List<MindsAccount> entities;
    private boolean require_login;
}