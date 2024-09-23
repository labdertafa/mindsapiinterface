package com.laboratorio.mindsapiinterface.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 20/09/2024
 * @updated 20/09/2024
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MindsUploadFileResponse {
    private String status;
    private String guid;
    private String location;
}