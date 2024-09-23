package com.laboratorio.api;

import com.laboratorio.mindsapiinterface.MindsNotificationApi;
import com.laboratorio.mindsapiinterface.impl.MindsNotificationApiImpl;
import com.laboratorio.mindsapiinterface.model.response.MindsNotificationsResponse;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 23/09/2024
 * @updated 23/09/2024
 */
public class MindsNotificationApiTest {
    private MindsNotificationApi notificationApi;
    
    @BeforeEach
    public void initTest() throws Exception {
        notificationApi = new MindsNotificationApiImpl();
    }
    
    @Test
    public void getAllNotificationsWithLimit() throws Exception { // Con limit
        int limit = 3;
        
        MindsNotificationsResponse response = this.notificationApi.getAllNotifications(limit);

        assertTrue(response.getNotifications().size() > 5);
        assertTrue(response.getLoadNext() != null);
    }
    
    @Test
    public void getAllNotifications() throws Exception {
        MindsNotificationsResponse notificationListResponse = this.notificationApi.getAllNotifications(40);

        assertTrue(notificationListResponse.getNotifications().size() > 5);
    }
    
    @Test
    public void getAllNewNotifications() throws Exception {
        String posicionInicial = "1726771906";
        
        MindsNotificationsResponse response = this.notificationApi.getAllNotifications(0, posicionInicial);
        
        assertTrue(response.getNotifications().size() >= 2);
        assertTrue(Long.parseLong(posicionInicial) <= Long.parseLong(response.getLoadNext()));
    }
}