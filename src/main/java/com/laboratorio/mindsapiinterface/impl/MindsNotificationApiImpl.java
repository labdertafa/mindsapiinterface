package com.laboratorio.mindsapiinterface.impl;

import com.laboratorio.clientapilibrary.model.ApiMethodType;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.clientapilibrary.model.ApiResponse;
import com.laboratorio.mindsapiinterface.MindsNotificationApi;
import com.laboratorio.mindsapiinterface.exception.MindsApiException;
import com.laboratorio.mindsapiinterface.model.MindsNotification;
import com.laboratorio.mindsapiinterface.model.response.MindsNotificationsResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Rafael
 * @version 1.2
 * @created 23/09/2024
 * @updated 17/12/2024
 */
public class MindsNotificationApiImpl extends MindsBaseApi implements MindsNotificationApi {
    public MindsNotificationApiImpl() throws Exception {
        super();
    }
    
    @Override
    public MindsNotificationsResponse getAllNotifications() throws Exception {
        return this.getAllNotifications(0);
    }

    @Override
    public MindsNotificationsResponse getAllNotifications(int limit) throws Exception {
        return this.getAllNotifications(limit, null);
    }

    // Función que devuelve una página de notificaciones de una cuenta
    private MindsNotificationsResponse getNotificationPage(String uri, int limit, int okStatus, String posicionInicial) throws Exception {
        try {
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.GET);
            request.addApiPathParam("limit", Integer.toString(limit));
            request.addApiPathParam("offset", posicionInicial);
            
            request = this.addContentHeader(request);
            request = this.addSessionHeader(request);
            
            ApiResponse response = this.client.executeApiRequest(request);
            log.debug("Response getNotificationPage: {}", response.getResponseStr());
            
            MindsNotificationsResponse notificationsResponse = this.gson.fromJson(response.getResponseStr(), MindsNotificationsResponse.class);
            if (!notificationsResponse.getStatus().equals(SUCCESS)) {
                throw new MindsApiException("Se ha producido un error recuperando una página de notificaciones");
            }
            
            return notificationsResponse;
        } catch (MindsApiException e) {
            throw e;
        } catch (Exception e) {
            throw new MindsApiException("Error recuperando una página de notificaciones en Minds");
        }
    }
    
    private boolean isContinuar(List<MindsNotification> notifications, String cursor,
            MindsNotificationsResponse notificationsResponse, int usedLimit,String posicionInicial) {
        log.debug("getFollowers. Recuperados: " + notifications.size() + ". Load next: " + cursor);
        if ((notificationsResponse.getNotifications() == null) ||(notificationsResponse.getNotifications().isEmpty())) {
            return false;
        } else {
            if ((notificationsResponse.getNotifications().size() < usedLimit) || (cursor == null)) {
                return false;
            }

            if (posicionInicial != null) {
                List<MindsNotification> list = notificationsResponse.getNotifications();
                for (int i = list.size() - 1; i >= 0; i--) {
                    long notiftime = list.get(i).getCreated_timestamp();
                    if (notiftime < Long.parseLong(posicionInicial)) {
                        return false;
                    }
                }
            }
        }
            
        return true;
    }

    @Override
    public MindsNotificationsResponse getAllNotifications(int limit, String posicionInicial) throws Exception {
        String endpoint = this.apiConfig.getProperty("getNotifications_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getNotifications_ok_status"));
        int defaultLimit = Integer.parseInt(this.apiConfig.getProperty("getNotifications_default_limit"));
        int maxLimit = Integer.parseInt(this.apiConfig.getProperty("getNotifications_max_limit"));
        int usedLimit = limit;
        if ((limit == 0) || (limit > maxLimit)) {
            usedLimit = defaultLimit;
        }
        List<MindsNotification> notifications = null;
        boolean continuar = true;
        String cursor = "";
        String nuevaPosicionInicial = posicionInicial;
        
        do {
            MindsNotificationsResponse notificationsResponse = this.getNotificationPage(endpoint, usedLimit, okStatus, cursor);
            if (notifications == null) {
                notifications = notificationsResponse.getNotifications();
                if (!notifications.isEmpty()) {
                    nuevaPosicionInicial = Long.toString(notifications.get(0).getCreated_timestamp());
                }
            } else {
                if (notificationsResponse.getNotifications() != null) {
                    notifications.addAll(notificationsResponse.getNotifications());
                }
            }

            cursor = notificationsResponse.getLoadNext();
            continuar = this.isContinuar(notifications, cursor, notificationsResponse, usedLimit, posicionInicial);
        } while (continuar);

        if (posicionInicial == null) {
            return new MindsNotificationsResponse(SUCCESS, notifications, nuevaPosicionInicial);
        }

        List<MindsNotification> filtredNotifications = notifications.stream()
                .filter(n -> n.getCreated_timestamp() > Long.parseLong(posicionInicial))
                .collect(Collectors.toList());
        log.debug("filtredNotificationsList size: " + filtredNotifications.size());
        return new MindsNotificationsResponse(SUCCESS, filtredNotifications, nuevaPosicionInicial);
    }
}