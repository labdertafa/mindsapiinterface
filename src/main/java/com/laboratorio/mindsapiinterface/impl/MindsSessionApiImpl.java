package com.laboratorio.mindsapiinterface.impl;

import com.google.gson.JsonSyntaxException;
import com.laboratorio.clientapilibrary.model.ApiMethodType;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.clientapilibrary.model.ApiResponse;
import com.laboratorio.mindsapiinterface.MindsSessionApi;
import com.laboratorio.mindsapiinterface.exception.MindsApiException;
import com.laboratorio.mindsapiinterface.model.MindsSession;
import com.laboratorio.mindsapiinterface.model.request.MindsAuthenticateRequest;
import com.laboratorio.mindsapiinterface.model.response.MindsLoginResponse;
import com.laboratorio.mindsapiinterface.utils.MindsSessionManager;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 19/09/2024
 * @updated 06/10/2024
 */
public class MindsSessionApiImpl extends MindsBaseApi implements MindsSessionApi {
    public MindsSessionApiImpl() throws Exception {
    }

    @Override
    public MindsSession authenticateUser(String username, String password) {
        String endpoint = this.apiConfig.getProperty("authenticateUser_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("authenticateUser_ok_status"));
        
        try {
            MindsAuthenticateRequest loginRequest = new MindsAuthenticateRequest(username, password);
            String requestJson = this.gson.toJson(loginRequest);
            
            String uri = endpoint;
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.POST, requestJson);
            request = this.addSessionHeader(request);
            request = this.addContentHeader(request);
            request.addApiHeader("Origin", "https://www.minds.com");
            request.addApiHeader("Referer", "https://www.minds.com/login");
            
            ApiResponse response = this.client.executeApiRequest(request);
            log.debug("JSON: " + response.getResponseStr());
            MindsLoginResponse loginResponse = this.gson.fromJson(response.getResponseStr(), MindsLoginResponse.class);
            if (!loginResponse.getStatus().equals("success")) {
                throw new MindsApiException(MindsAccountApiImpl.class.getName(), "Se ha producido un error autenticando al usuario " + username);
            }
            
            MindsSession newSession = MindsSessionManager.getSessionData(this.session.getXVersion(), response);
            String sessionFilePath = this.apiConfig.getProperty("minds_session_file");
            MindsSessionManager.saveSession(sessionFilePath, newSession);
            this.setSession(newSession);
            
            return this.session;
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new MindsApiException(MindsSessionApiImpl.class.getName(), e.getMessage());
        }
    }
}