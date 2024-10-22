package com.laboratorio.mindsapiinterface.impl;

import com.google.gson.JsonSyntaxException;
import com.laboratorio.clientapilibrary.model.ApiMethodType;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.clientapilibrary.model.ApiResponse;
import com.laboratorio.mindsapiinterface.MindsAccountApi;
import com.laboratorio.mindsapiinterface.exception.MindsApiException;
import com.laboratorio.mindsapiinterface.model.MindsAccount;
import com.laboratorio.mindsapiinterface.model.response.MindsAccountListResponse;
import com.laboratorio.mindsapiinterface.model.response.MindsActionResponse;
import com.laboratorio.mindsapiinterface.model.response.MindsEntityListResponse;
import com.laboratorio.mindsapiinterface.model.response.MindsGetAccountResponse;
import com.laboratorio.mindsapiinterface.model.response.MindsSuggestionsResponse;
import com.laboratorio.mindsapiinterface.model.response.MindsUsersDetailResponse;
import com.laboratorio.mindsapiinterface.utils.InstruccionInfo;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Rafael
 * @version 1.2
 * @created 18/09/2024
 * @updated 22/10/2024
 */
public class MindsAccountApiImpl extends MindsBaseApi implements MindsAccountApi {
    public MindsAccountApiImpl() throws Exception {
    }
    
    @Override
    public List<MindsAccount> getAccountsById(List<String> usersId) throws Exception {
        try {
            MindsUsersDetailResponse mindsUsersDetailResponse = this.getUsersDetail(usersId);
            if (!mindsUsersDetailResponse.getStatus().equals("success")) {
                throw new MindsApiException(MindsBaseApi.class.getName(), "Error, consultando los detalles de una lista de entidades.");
            }
            
            return mindsUsersDetailResponse.getEntities();
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Override
    public MindsAccount getAccountByUsername(String username) {
        String endpoint = this.apiConfig.getProperty("getAccountByUsername_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getAccountByUsername_ok_status"));
        
        try {
            String uri = endpoint + "/" + username;
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.GET);
            request = this.addContentHeader(request);
            
            ApiResponse response = this.client.executeApiRequest(request);
            
            MindsGetAccountResponse mindsGetAccountResponse =  this.gson.fromJson(response.getResponseStr(), MindsGetAccountResponse.class);
            if (!mindsGetAccountResponse.getStatus().equals("success")) {
                throw new MindsApiException(MindsAccountApiImpl.class.getName(), "Se ha producido un error buscando al usuario " + username);
            }
            
            return mindsGetAccountResponse.getChannel();
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new MindsApiException(MindsAccountApiImpl.class.getName(), e.getMessage());
        }
    }   

    @Override
    public MindsAccountListResponse getFollowers(String id) throws Exception {
        return this.getFollowers(id, 0);
    }

    @Override
    public MindsAccountListResponse getFollowers(String id, int limit) throws Exception {
        return this.getFollowers(id, limit, 0);
    }

    @Override
    public MindsAccountListResponse getFollowers(String id, int limit, int quantity) throws Exception {
        return this.getFollowers(id, limit, quantity, null);
    }

    @Override
    public MindsAccountListResponse getFollowers(String id, int limit, int quantity, String posicionInicial) throws Exception {
        String endpoint = this.apiConfig.getProperty("getFollowers_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getFollowers_ok_status"));
        int defaultLimit = Integer.parseInt(this.apiConfig.getProperty("getFollowers_default_limit"));
        int maxLimit = Integer.parseInt(this.apiConfig.getProperty("getFollowers_max_limit"));
        int usedLimit = limit;
        if ((limit == 0) || (limit > maxLimit)) {
            usedLimit = defaultLimit;
        }
        InstruccionInfo instruccionInfo = new InstruccionInfo(endpoint, null, okStatus, usedLimit);
        return this.getSubscribersList(instruccionInfo, id, quantity, posicionInicial);
    }
    
    @Override
    public List<String> getFollowersIds(String userId, int limit) throws Exception {
        MindsAccountListResponse response = this.getFollowers(userId, limit);
        return response.getUsers().stream()
                .map(account -> account.getUrn())
                .collect(Collectors.toList());
    }

    @Override
    public MindsAccountListResponse getFollowings(String id) throws Exception {
        return this.getFollowings(id, 0);
    }

    @Override
    public MindsAccountListResponse getFollowings(String id, int limit) throws Exception {
        return this.getFollowings(id, limit, 0);
    }

    @Override
    public MindsAccountListResponse getFollowings(String id, int limit, int quantity) throws Exception {
        return this.getFollowings(id, limit, quantity, null);
    }
    
    protected InstruccionInfo getFollowingsInstruccionInfo(int limit) {
        String endpoint = this.apiConfig.getProperty("getFollowings_endpoint");
        String complementoUrl = this.apiConfig.getProperty("getFollowings_complemento_url");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getFollowings_ok_status"));
        int defaultLimit = Integer.parseInt(this.apiConfig.getProperty("getFollowings_default_limit"));
        int maxLimit = Integer.parseInt(this.apiConfig.getProperty("getFollowings_max_limit"));
        int usedLimit = limit;
        if ((limit == 0) || (limit > maxLimit)) {
            usedLimit = defaultLimit;
        }
        return new InstruccionInfo(endpoint, complementoUrl, okStatus, usedLimit);
    }

    @Override
    public MindsAccountListResponse getFollowings(String id, int limit, int quantity, String posicionInicial) throws Exception {
        InstruccionInfo instruccionInfo = getFollowingsInstruccionInfo(limit);
        return this.getSubscriptionsList(instruccionInfo, id, quantity, posicionInicial);
    }
    
    @Override
    public List<String> getFollowingsIds(String userId, int limit) throws Exception {
        InstruccionInfo instruccionInfo = getFollowingsInstruccionInfo(limit);
        MindsEntityListResponse response = this.getEntityList(instruccionInfo, userId, limit, null);
        return response.getEntities().stream()
                .map(entity -> entity.getUrn())
                .collect(Collectors.toList());
    }

    @Override
    public boolean followAccount(String id) {
        String endpoint = this.apiConfig.getProperty("followAccount_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("followAccount_ok_status"));
        
        try {
            String uri = endpoint + "/" + id;
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.POST);
            request = this.addSessionHeader(request);
            request = this.addContentHeader(request);
            request.addApiHeader("Origin", "https://www.minds.com");
            request.addApiHeader("Referer", "https://www.minds.com");
            
            ApiResponse response = this.client.executeApiRequest(request);
            MindsActionResponse mindsActionResponse = this.gson.fromJson(response.getResponseStr(), MindsActionResponse.class);
            if (!mindsActionResponse.getStatus().equals("success")) {
                log.error("Ha ocurrido un error intentando seguir el usuario con id: " + id);
                return false;
            }
            
            return true;
        } catch (JsonSyntaxException e) {
            logException(e);
            throw  e;
        } catch (Exception e) {
            throw new MindsApiException(MindsAccountApiImpl.class.getName(), e.getMessage());
        }
    }

    @Override
    public boolean unfollowAccount(String id) {
        String endpoint = this.apiConfig.getProperty("unfollowAccount_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("unfollowAccount_ok_status"));
        
        try {
            String uri = endpoint + "/" + id;
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.DELETE);
            request = this.addSessionHeader(request);
            request = this.addContentHeader(request);
            request.addApiHeader("Origin", "https://www.minds.com");
            request.addApiHeader("Referer", "https://www.minds.com");
            
            ApiResponse response = this.client.executeApiRequest(request);
            MindsActionResponse actionResponse = this.gson.fromJson(response.getResponseStr(), MindsActionResponse.class);
            if (!actionResponse.getStatus().equals("success")) {
                log.error("Ha ocurrido un error intentando dejar de seguir al usuario con id: " + id);
                return false;
            }
            
            return true;
        } catch (JsonSyntaxException e) {
            logException(e);
            throw  e;
        } catch (Exception e) {
            throw new MindsApiException(MindsAccountApiImpl.class.getName(), e.getMessage());
        }
    }

    @Override
    public MindsAccount checkrelationship(String username) {
        String endpoint = this.apiConfig.getProperty("getAccountByUsername_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getAccountByUsername_ok_status"));
        
        try {
            String uri = endpoint + "/" + username;
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.GET);
            request = this.addSessionHeader(request);
            request = this.addContentHeader(request);
            request.addApiHeader("Origin", "https://www.minds.com");
            request.addApiHeader("Referer", "https://www.minds.com");
            
            ApiResponse response = this.client.executeApiRequest(request);
            
            MindsGetAccountResponse getAccountResponse =  this.gson.fromJson(response.getResponseStr(), MindsGetAccountResponse.class);
            if (!getAccountResponse.getStatus().equals("success")) {
                throw new MindsApiException(MindsAccountApiImpl.class.getName(), "Se ha producido un error buscando al usuario " + username);
            }
            
            return getAccountResponse.getChannel();
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new MindsApiException(MindsAccountApiImpl.class.getName(), e.getMessage());
        }
    }

    @Override
    public List<MindsAccount> getSuggestions() {
        return this.getSuggestions(0);
    }

    @Override
    public List<MindsAccount> getSuggestions(int limit) {
        String endpoint = this.apiConfig.getProperty("getSuggestions_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getSuggestions_ok_status"));
        int defaultLimit = Integer.parseInt(this.apiConfig.getProperty("getSuggestions_default_limit"));
        int maxLimit = Integer.parseInt(this.apiConfig.getProperty("getSuggestions_max_limit"));
        int usedLimit = limit;
        if ((limit == 0) || (limit > maxLimit)) {
            usedLimit = defaultLimit;
        }
        
        try {
            String uri = endpoint;
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.GET);
            request.addApiPathParam("limit", Integer.toString(usedLimit));
            request.addApiPathParam("offset", "0");
            request = this.addSessionHeader(request);
            request = this.addContentHeader(request);
            request.addApiHeader("Origin", "https://www.minds.com");
            request.addApiHeader("Referer", "https://www.minds.com");
            
            ApiResponse response = this.client.executeApiRequest(request);
            
            MindsSuggestionsResponse suggestionsResponse =  this.gson.fromJson(response.getResponseStr(), MindsSuggestionsResponse.class);
            
            return suggestionsResponse.getUsers();
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new MindsApiException(MindsAccountApiImpl.class.getName(), e.getMessage());
        }
    }
}