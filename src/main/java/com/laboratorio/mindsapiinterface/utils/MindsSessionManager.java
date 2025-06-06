package com.laboratorio.mindsapiinterface.utils;

import com.laboratorio.clientapilibrary.model.ApiResponse;
import com.laboratorio.mindsapiinterface.exception.MindsApiException;
import com.laboratorio.mindsapiinterface.model.MindsSession;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 19/09/2024
 * @updated 06/06/2025
 */
public class MindsSessionManager {
    protected static final Logger log = LogManager.getLogger(MindsSessionManager.class);

    private static void logException(Exception e) {
        log.error("Error: " + e.getMessage());
        if (e.getCause() != null) {
            log.error("Causa: " + e.getCause().getMessage());
        }
    }
    
    private static String extractToken(String cookieValue) {
        int pos1 = cookieValue.indexOf("=");
        int pos2 = cookieValue.indexOf(";");
        return cookieValue.substring(pos1 + 1, pos2);
    }
    
    public static MindsSession getSessionData(String xVersion, ApiResponse response) {
        // Procesar todos los headers "Set-Cookie"
        List<String> setCookies = response.getHttpHeaders().get("Set-Cookie");
        log.info("Número de cookies encontradas: " + setCookies.size());
        if (setCookies == null) {
            throw new MindsApiException("No se encontraron las cookies esperadas en la respuesta HTTP");
        }

        StringBuilder cookies = null;
        String token = null;
        for (String cookieValue : setCookies) {
            log.debug("He encontrado este valor Set-Cookie: " + cookieValue);
            
            if (cookieValue.contains("XSRF-TOKEN=")) {
                token = extractToken(cookieValue);
            }
            
            if (cookies == null) {
                cookies = new StringBuilder(cookieValue);
            } else {
                cookies.append("; ");
                cookies.append(cookieValue);
            }
        }
        
        if (token == null) {
            throw new MindsApiException("No se encontró el token en la respuesta HTTP");
        }
        
        log.debug("La información de la sessión de Minds se extrajo correctamente");
        
        return new MindsSession(cookies.toString(), token, xVersion);
    }
    
    public static MindsSession loadSession(String filePath) throws Exception {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            MindsSession session = (MindsSession)ois.readObject();
            ois.close();
            fis.close();
            
            log.debug("La información de la sessión de Minds se recuperó correctamente");
            
            return session;
        } catch (Exception e) {
            log.error("Error recuperando la información de la session de Minds");
            logException(e);
            throw e;
        }
    }
    
    public static void saveSession(String filePath, MindsSession session) throws Exception {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(session);
            oos.close();
            fos.close();
            
            log.debug("La información de la sessión de Minds se almacenó correctamente");
        } catch (Exception e) {
            log.error("Error almacenando la información de la session de Minds");
            logException(e);
            throw e;
        }
    }
}