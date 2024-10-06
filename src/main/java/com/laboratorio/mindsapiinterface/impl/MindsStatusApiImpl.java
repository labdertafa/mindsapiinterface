package com.laboratorio.mindsapiinterface.impl;

import com.google.gson.JsonSyntaxException;
import com.laboratorio.clientapilibrary.model.ApiMethodType;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.clientapilibrary.model.ApiResponse;
import com.laboratorio.mindsapiinterface.MindsStatusApi;
import com.laboratorio.mindsapiinterface.exception.MindsApiException;
import com.laboratorio.mindsapiinterface.model.request.MindsPostRequest;
import com.laboratorio.mindsapiinterface.model.response.MindsPostResponse;
import com.laboratorio.mindsapiinterface.model.response.MindsUploadFileResponse;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 20/09/2024
 * @updated 06/10/2024
 */
public class MindsStatusApiImpl extends MindsBaseApi implements MindsStatusApi {
    public MindsStatusApiImpl() throws Exception {
    }
    
    @Override
    public MindsPostResponse postStatus(String text) {
        String endpoint = this.apiConfig.getProperty("postStatus_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("postStatus_ok_status"));
        
        try {
            MindsPostRequest postRequest = new MindsPostRequest(text);
            String requestJson = this.gson.toJson(postRequest);
            
            String uri = endpoint;
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.PUT, requestJson);
            request = this.addSessionHeader(request);
            request = this.addContentHeader(request);
            request.addApiHeader("Origin", "https://www.minds.com");
            request.addApiHeader("Referer", "https://www.minds.com");
            
            ApiResponse response = this.client.executeApiRequest(request);
            
            return this.gson.fromJson(response.getResponseStr(), MindsPostResponse.class);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new MindsApiException(MindsStatusApiImpl.class.getName(), e.getMessage());
        }
    }

    @Override
    public MindsPostResponse postStatus(String text, String filePath) {
        String endpoint = this.apiConfig.getProperty("postStatus_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("postStatus_ok_status"));
        
        try {
            MindsPostRequest postRequest;
            if (filePath == null) {
                postRequest = new MindsPostRequest(text);
            } else {
                MindsUploadFileResponse uploadFileResponse = this.uploadImage(filePath);
                postRequest = new MindsPostRequest(text, uploadFileResponse.getGuid());
            }
            String requestJson = this.gson.toJson(postRequest);
            
            String uri = endpoint;
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.PUT, requestJson);
            request = this.addSessionHeader(request);
            request = this.addContentHeader(request);
            request.addApiHeader("Origin", "https://www.minds.com");
            request.addApiHeader("Referer", "https://www.minds.com");
            
            ApiResponse response = this.client.executeApiRequest(request);
            
            return this.gson.fromJson(response.getResponseStr(), MindsPostResponse.class);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new MindsApiException(MindsStatusApiImpl.class.getName(), e.getMessage());
        }
    }

    @Override
    public MindsUploadFileResponse uploadImage(String filePath) {
        String endpoint = this.apiConfig.getProperty("UploadImage_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("UploadImage_ok_status"));
        
        try {
            String uri = endpoint;
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.POST);
            request = this.addSessionHeader(request);
            request.addApiHeader("Accept", "application/json, text/plain, */*");
            request.addApiHeader("Accept-Encoding", "gzip, deflate, br, zstd");
            request.addApiHeader("Connection", "keep-alive");
            request.addApiHeader("Origin", "https://www.minds.com");
            request.addApiHeader("Referer", "https://www.minds.com/newsfeed/subscriptions/for-you");
            request.addFileFormData("file", filePath);
            
            ApiResponse response = this.client.executeApiRequest(request);
            
            MindsUploadFileResponse uploadFileResponse = this.gson.fromJson(response.getResponseStr(), MindsUploadFileResponse.class);
            
            if (!uploadFileResponse.getStatus().equals("success")) {
                log.error("Error: " + response.getResponseStr());
                throw new MindsApiException(MindsAccountApiImpl.class.getName(), "Se ha producido un error subiendo un archivo al servidor Minds: " + filePath);
            }
            
            return uploadFileResponse;
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new MindsApiException(MindsStatusApiImpl.class.getName(), e.getMessage());
        }
    }

    @Override
    public boolean deleteStatus(String id) {
        String endpoint = this.apiConfig.getProperty("deleteStatus_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("deleteStatus_ok_status"));
        
        try {
            String uri = endpoint + "/" + id;
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.DELETE);
            request = this.addSessionHeader(request);
            request = this.addContentHeader(request);
            request.addApiHeader("Origin", "https://www.minds.com");
            request.addApiHeader("Referer", "https://www.minds.com");
            
            this.client.executeApiRequest(request);
            
            return true;
        } catch (Exception e) {
            throw new MindsApiException(MindsStatusApiImpl.class.getName(), e.getMessage());
        }
    }
}