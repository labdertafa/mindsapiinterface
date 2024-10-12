package com.laboratorio.utils;

import com.laboratorio.mindsapiinterface.MindsStatusApi;
import com.laboratorio.mindsapiinterface.impl.MindsStatusApiImpl;
import com.laboratorio.mindsapiinterface.model.MindsStatus;
import com.laboratorio.mindsapiinterface.model.MindsStatusEntity;
import com.laboratorio.mindsapiinterface.model.response.MindsUserActivityResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 06/10/2024
 * @updated 06/10/2024
 */
public class MindsUtils {
    protected static final Logger log = LogManager.getLogger(MindsUtils.class);
    
    private MindsUtils() {
    }
    
    // Recupera la fecha de la última actividad de un usuario
    public static LocalDateTime getLastActivityDate(String userId) throws Exception {
        int limit = 5;
        
        try {
            MindsStatusApi statusApi = new MindsStatusApiImpl();
            
            MindsUserActivityResponse response = statusApi.getUserActivity(userId, limit);
            List<MindsStatusEntity> entities = response.getEntities();
            if (entities.isEmpty()) {
                return LocalDateTime.of(2000, 1, 1, 12, 0, 0);
            }
            
            MindsStatus status = entities.get(0).getEntity();
            if (status.getTime_updated() != null) {
                return LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(status.getTime_updated())), ZoneId.systemDefault());
            } else {
                if (status.getTime_created() != null) {
                    return LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(status.getTime_created())), ZoneId.systemDefault());
                }
            }
            
            return LocalDateTime.of(2000, 1, 1, 12, 0, 0);
        } catch (Exception e) {
            throw e;
        }
    }
    
    public static int daysFromLastActivity(String userId) throws Exception {
        LocalDateTime lastActivityDate = getLastActivityDate(userId);
        LocalDateTime now = LocalDateTime.now();
        long days = ChronoUnit.DAYS.between(lastActivityDate, now);
        
        return (int)days;
    }
}