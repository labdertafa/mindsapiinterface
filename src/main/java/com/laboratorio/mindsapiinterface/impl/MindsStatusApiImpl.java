package com.laboratorio.mindsapiinterface.impl;

import com.laboratorio.clientapilibrary.model.ApiMethodType;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.clientapilibrary.model.ApiResponse;
import com.laboratorio.mindsapiinterface.MindsStatusApi;
import com.laboratorio.mindsapiinterface.exception.MindsApiException;
import com.laboratorio.mindsapiinterface.model.request.MindsPostRequest;
import com.laboratorio.mindsapiinterface.model.MindsStatus;
import com.laboratorio.mindsapiinterface.model.activity.MindsActivityEdge;
import com.laboratorio.mindsapiinterface.model.request.MindsActivityRequest;
import com.laboratorio.mindsapiinterface.model.response.MindsActivityResponse;
import com.laboratorio.mindsapiinterface.model.response.MindsUploadFileResponse;
import com.laboratorio.mindsapiinterface.model.response.MindsUserActivityResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Rafael
 * @version 1.3
 * @created 20/09/2024
 * @updated 17/12/2025
 */
public class MindsStatusApiImpl extends MindsBaseApi implements MindsStatusApi {
    public MindsStatusApiImpl() throws Exception {
        super();
    }
    
    @Override
    public MindsStatus postStatus(String text) {
        String endpoint = this.apiConfig.getProperty("postStatus_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("postStatus_ok_status"));
        
        try {
            MindsPostRequest postRequest = new MindsPostRequest(text);
            String requestJson = this.gson.toJson(postRequest);
            
            String uri = endpoint;
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.PUT, requestJson);
            request = this.addSessionHeader(request);
            request = this.addContentHeader(request);
            request.addApiHeader(ORIGIN, MINDS_SITE);
            request.addApiHeader(REFERER, MINDS_SITE);
            
            ApiResponse response = this.client.executeApiRequest(request);
            log.debug("Response postStatus: {}", response.getResponseStr());
            
            return this.gson.fromJson(response.getResponseStr(), MindsStatus.class);
        } catch (Exception e) {
            throw new MindsApiException("Error posteando un estado en Minds: " + text, e);
        }
    }

    @Override
    public MindsStatus postStatus(String text, String filePath) {
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
            request.addApiHeader(ORIGIN, MINDS_SITE);
            request.addApiHeader(REFERER, MINDS_SITE);
            
            ApiResponse response = this.client.executeApiRequest(request);
            log.debug("Response postStatus con imagen: {}", response.getResponseStr());
            
            return this.gson.fromJson(response.getResponseStr(), MindsStatus.class);
        } catch (MindsApiException e) {
            throw e;
        } catch (Exception e) {
            throw new MindsApiException("Error posteando un estado con imagen en Minds: " + text, e);
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
            request.addApiHeader(ORIGIN, MINDS_SITE);
            request.addApiHeader(REFERER, "https://www.minds.com/newsfeed/subscriptions/for-you");
            request.addFileFormData("file", filePath);
            
            ApiResponse response = this.client.executeApiRequest(request);
            log.debug("Response uploadImage: {}", response.getResponseStr());
            
            MindsUploadFileResponse uploadFileResponse = this.gson.fromJson(response.getResponseStr(), MindsUploadFileResponse.class);
            
            if (!uploadFileResponse.getStatus().equals("success")) {
                log.error("Error: " + response.getResponseStr());
                throw new MindsApiException("Se ha producido un error subiendo un archivo al servidor Minds: " + filePath);
            }
            
            return uploadFileResponse;
        } catch (MindsApiException e) {
            throw e;
        } catch (Exception e) {
            throw new MindsApiException("Error subiendo una imagen a Minds: " + filePath, e);
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
            request.addApiHeader(ORIGIN, MINDS_SITE);
            request.addApiHeader(REFERER, MINDS_SITE);
            
            this.client.executeApiRequest(request);
            
            return true;
        } catch (Exception e) {
            throw new MindsApiException("Error eliminado el post de Minds con id: " + id, e);
        }
    }

    @Override
    public MindsUserActivityResponse getUserActivity(String userId, int limit) {
        String endpoint = this.apiConfig.getProperty("getUserActivity_endpoint");
        int maxLimit = Integer.parseInt(this.apiConfig.getProperty("getUserActivity_max_limit"));
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getUserActivity_ok_status"));
        
        int usedLimit = Math.min(limit, maxLimit);
        
        try {
            String uri = endpoint + "/" + userId;
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.GET);
            request.addApiPathParam("sync", "1");
            request.addApiPathParam("limit", Integer.toString(usedLimit));
            request = this.addSessionHeader(request);
            request = this.addContentHeader(request);
            request.addApiHeader(ORIGIN, MINDS_SITE);
            request.addApiHeader(REFERER, MINDS_SITE);
            
            ApiResponse response = this.client.executeApiRequest(request);
            log.debug("Response getUserActivity: {}", response.getResponseStr());
            
            return this.gson.fromJson(response.getResponseStr(), MindsUserActivityResponse.class);
        } catch (Exception e) {
            throw new MindsApiException("Error recuperando la actividad del usuario Minds con id: " + userId, e);
        }
    }
    
    // Función que devuelve una página de cabeceras de estados de un timeline
    private MindsActivityResponse getTimelinePage(String uri, int okStatus, int limit, String algorithm, String query, String posicionInicial) {
        try {
            MindsActivityRequest activityRequest;
            if (posicionInicial != null) {
                activityRequest = new MindsActivityRequest(limit, algorithm, query, posicionInicial);
            } else {
                activityRequest = new MindsActivityRequest(limit, algorithm, query);
            }
            String requestJson = this.gson.toJson(activityRequest);
            
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.POST, requestJson);
            request = this.addSessionHeader(request);
            request = this.addContentHeader(request);
            request.addApiHeader(ORIGIN, MINDS_SITE);
            request.addApiHeader(REFERER, MINDS_SITE);
            
            ApiResponse response = this.client.executeApiRequest(request);
            log.debug("Response getTimelinePage: {}", response.getResponseStr());
                        
            return this.gson.fromJson(response.getResponseStr(), MindsActivityResponse.class);
        } catch (Exception e) {
            throw new MindsApiException("Error recuperando una página del timeline de Minds", e);
        }
    }

    @Override
    public List<MindsStatus> getGlobalTimeline(int quantity) {
        String query = this.apiConfig.getProperty("newsFeedQuery");
        String endpoint = this.apiConfig.getProperty("getGlobalTimeLine_endpoint");
        int limit = Integer.parseInt(this.apiConfig.getProperty("getGlobalTimeLine_max_limit"));
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getGlobalTimeLine_ok_status"));
        String algorithm = this.apiConfig.getProperty("newsFeed_Algorithm");
        
        List<MindsStatus> statuses = null;
        boolean continuar = true;
        String cursor = null;        
        String uri = endpoint;

        do {
            MindsActivityResponse mindsActivityResponse = this.getTimelinePage(uri, okStatus, limit, algorithm, query, cursor);
            List<MindsActivityEdge> results = mindsActivityResponse.getData().getNewsfeed().getEdges();
            log.debug("Elementos recuperados total: " + results.size());
            List<MindsStatus> filteredResults = results.stream()
                    .filter(edge -> edge.get__typename().equals("ActivityEdge"))
                    .map(edge -> this.gson.fromJson(edge.getNode().getLegacy(), MindsStatus.class))
                    .collect(Collectors.toList());
            log.debug("Elementos recuperados filtrados: " + filteredResults.size());


            if (statuses == null) {
                statuses = filteredResults;
            } else {
                statuses.addAll(filteredResults);
            }

            cursor = mindsActivityResponse.getData().getNewsfeed().getPageInfo().getEndCursor();
            log.debug("getGlobalTimeline. Recuperados: " + statuses.size() + ". Cursor: " + cursor);
            if (filteredResults.isEmpty()) {
                continuar = false;
            } else {
                if ((cursor == null) || (statuses.size() >= quantity)) {
                    continuar = false;
                }
            }
        } while (continuar);

        return statuses.subList(0, Math.min(quantity, statuses.size()));
    }
}