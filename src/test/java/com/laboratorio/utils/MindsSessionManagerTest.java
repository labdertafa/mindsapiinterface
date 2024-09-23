package com.laboratorio.utils;

import com.laboratorio.mindsapiinterface.model.MindsSession;
import com.laboratorio.mindsapiinterface.utils.MindsApiConfig;
import com.laboratorio.mindsapiinterface.utils.MindsSessionManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 19/09/2024
 * @updated 19/09/2024
 */
public class MindsSessionManagerTest {
    /* @Test
    public void init() throws Exception {
        String sessionFilePath = MindsApiConfig.getInstance().getProperty("minds_session_file");
        MindsSession session = new MindsSession("75a872ccac60078b0bd99f70be667c00d456fcfd725842bd44e2e7aac4c3341f65286da4920622b6d0199a9482785b54033f827668949b645b514b5782cdefd2", "75a872ccac60078b0bd99f70be667c00d456fcfd725842bd44e2e7aac4c3341f65286da4920622b6d0199a9482785b54033f827668949b645b514b5782cdefd2", "1452425022");
        MindsSessionManager.saveSession(sessionFilePath, session);
        
        assertTrue(true);
    } */
    
    @Test
    public void loadSession() throws Exception {
        String sessionFilePath = MindsApiConfig.getInstance().getProperty("minds_session_file");
        MindsSession session = MindsSessionManager.loadSession(sessionFilePath);
        
        assertTrue(session != null);
    }
}