package com.laboratorio.api;

import com.laboratorio.mindsapiinterface.MindsStatusApi;
import com.laboratorio.mindsapiinterface.impl.MindsStatusApiImpl;
import com.laboratorio.mindsapiinterface.model.MindsStatus;
import com.laboratorio.mindsapiinterface.model.response.MindsUserActivityResponse;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 20/09/2024
 * @updated 07/10/2024
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MindsStatusApiTest {
    protected static final Logger log = LogManager.getLogger(MindsStatusApiTest.class);
    private static MindsStatusApi statusApi;
    private static String postId;
    
    @BeforeEach
    public void initTest() throws Exception {
        statusApi = new MindsStatusApiImpl();
    }
    
    @Test @Order(1)
    public void postStatus() {
        String text = "Hola, les saludo desde El laboratorio de Rafa. Post automático #SiguemeYTeSigo";
        String owner = "1676194796068147207";
        
        MindsStatus status = statusApi.postStatus(text);
        postId = status.getGuid();

        assertTrue(!status.getGuid().isEmpty());
        assertEquals(owner, status.getOwner_guid());
    }
    
    @Test @Order(2)
    public void deleteStatus() {
        boolean result = statusApi.deleteStatus(postId);
        
        assertTrue(result);
    }
    
/*    @Test
    public void uploadImage() throws Exception {
        String imagen = "C:\\Users\\rafa\\Pictures\\Formula_1\\Spa_1950.jpg";
        
        MindsUploadFileResponse uploadFileResponse = statusApi.uploadImage(imagen);
        
        assertTrue(true);
    } */
    
    @Test @Order(3)
    public void postImage() throws Exception {
        String imagen = "C:\\Users\\rafa\\Pictures\\Formula_1\\Spa_1950.jpg";
        String text = "Hola, les saludo desde El laboratorio de Rafa. Post automático #SiguemeYTeSigo";
        String owner = "1676194796068147207";
        
        MindsStatus status = statusApi.postStatus(text, imagen);
        postId = status.getGuid();
        
        assertTrue(!status.getGuid().isEmpty());
        assertEquals(owner, status.getOwner_guid());
    }
    
    @Test @Order(4)
    public void deleteStatusWithImage() {
        boolean result = statusApi.deleteStatus(postId);
        
        assertTrue(result);
    }
    
    @Test
    public void getUserActivity() {
        String userId = "903289035852619779";
        int limit = 5;
        
        MindsUserActivityResponse response = statusApi.getUserActivity(userId, limit);
        
        assertTrue(response.getEntities().size() >= 5);
    }
    
    @Test
    public void getGlobalTimeline() {
        int quantity = 50;
        
        List<MindsStatus> statuses = statusApi.getGlobalTimeline(quantity);
        int i = 0;
        for (MindsStatus status : statuses) {
            log.info(i + "-) Status: " + status.toString());
            i++;
        }
        
        assertEquals(quantity, statuses.size());
    }
}