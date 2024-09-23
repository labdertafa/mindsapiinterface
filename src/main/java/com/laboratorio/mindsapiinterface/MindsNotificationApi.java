package com.laboratorio.mindsapiinterface;

import com.laboratorio.mindsapiinterface.model.response.MindsNotificationsResponse;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 23/09/2024
 * @updated 23/09/2024
 */
public interface MindsNotificationApi {
    MindsNotificationsResponse getAllNotifications() throws Exception;
    MindsNotificationsResponse getAllNotifications(int limit) throws Exception;
    MindsNotificationsResponse getAllNotifications(int limit, String posicionInicial) throws Exception;
}