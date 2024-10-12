package com.laboratorio.mindsapiinterface;

import com.laboratorio.mindsapiinterface.model.MindsStatus;
import com.laboratorio.mindsapiinterface.model.response.MindsUploadFileResponse;
import com.laboratorio.mindsapiinterface.model.response.MindsUserActivityResponse;
import java.util.List;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 20/09/2024
 * @updated 12/10/2024
 */
public interface MindsStatusApi {
    MindsStatus postStatus(String text);
    MindsStatus postStatus(String text, String filePath);
    MindsUploadFileResponse uploadImage(String filePath);
    boolean deleteStatus(String id);
    
    // Recupera los últimos posts de un usuario
    MindsUserActivityResponse getUserActivity(String userId, int limit);
    
    // Recupera el timeline global
    List<MindsStatus> getGlobalTimeline(int quantity);
}