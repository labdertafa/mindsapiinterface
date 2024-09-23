package com.laboratorio.api;

import com.laboratorio.mindsapiinterface.MindsStatusApi;
import com.laboratorio.mindsapiinterface.impl.MindsStatusApiImpl;
import com.laboratorio.mindsapiinterface.model.response.MindsPostResponse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 20/09/2024
 * @updated 23/09/2024
 */
public class MindsStatusApiTest {
    private static MindsStatusApi statusApi;
    
    @BeforeEach
    public void initTest() throws Exception {
        statusApi = new MindsStatusApiImpl();
    }
    
    @Test
    public void postStatus() {
        String text = "Hola, les saludo desde El laboratorio de Rafa. Post automático #SiguemeYTeSigo";
        String owner = "1676194796068147207";
        
        MindsPostResponse status = statusApi.postStatus(text);

        assertTrue(!status.getGuid().isEmpty());
        assertEquals(owner, status.getOwner_guid());
    }
    
    @Test
    public void postImage() throws Exception {
        String imagen = "C:\\Users\\rafa\\Pictures\\Formula_1\\Spa_1950.jpg";
        String text = "Hola, les saludo desde El laboratorio de Rafa. Post automático #SiguemeYTeSigo";
        String owner = "1676194796068147207";
        
        MindsPostResponse status = statusApi.postStatus(text, imagen);
        assertTrue(!status.getGuid().isEmpty());
        assertEquals(owner, status.getOwner_guid());
    }
}