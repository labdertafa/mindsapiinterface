package com.laboratorio.mindsapiinterface;

import com.laboratorio.mindsapiinterface.model.MindsSession;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 19/09/2024
 * @updated 19/09/2024
 */
public interface MindsSessionApi {
    MindsSession authenticateUser(String username, String password);
}