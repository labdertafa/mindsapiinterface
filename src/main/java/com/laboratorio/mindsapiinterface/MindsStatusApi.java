package com.laboratorio.mindsapiinterface;

import com.laboratorio.mindsapiinterface.model.response.MindsPostResponse;
import com.laboratorio.mindsapiinterface.model.response.MindsUploadFileResponse;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 20/09/2024
 * @updated 20/09/2024
 */
public interface MindsStatusApi {
    MindsPostResponse postStatus(String text);
    MindsPostResponse postStatus(String text, String filePath);
    MindsUploadFileResponse uploadImage(String filePath);
}