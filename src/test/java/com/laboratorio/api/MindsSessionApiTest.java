package com.laboratorio.api;

import com.laboratorio.clientapilibrary.utils.ReaderConfig;
import com.laboratorio.mindsapiinterface.MindsSessionApi;
import com.laboratorio.mindsapiinterface.exception.MindsApiException;
import com.laboratorio.mindsapiinterface.impl.MindsSessionApiImpl;
import com.laboratorio.mindsapiinterface.model.MindsSession;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 19/09/2024
 * @updated 09/05/2025
 */
public class MindsSessionApiTest {
    private static ReaderConfig config;
    private static MindsSessionApi sessionApi;
    
    @BeforeEach
    public void initTest() throws Exception {
        config = new ReaderConfig("config//minds_api.properties");
        sessionApi = new MindsSessionApiImpl();
    }
    
    @Test
    public void authenticateUser() throws Exception {
        String username = config.getProperty("minds_username");
        String password = config.getProperty("minds_password");
        String xVersion = config.getProperty("minds_x_version");
        
        MindsSession session = sessionApi.authenticateUser(username, password);
        
        assertEquals(xVersion, session.getXVersion());
    }
    
    @Test
    public void authenticateInvalidUser() throws Exception {
        String username = "fsdlofjsdlkflksdafnmjlskdafnjlsad";
        String password = "kdfjsdñlfñsdfkjsñdfksñdfkñaskfañfksad";
        
        assertThrows(MindsApiException.class, () -> {
            sessionApi.authenticateUser(username, password);
        });
    }
}