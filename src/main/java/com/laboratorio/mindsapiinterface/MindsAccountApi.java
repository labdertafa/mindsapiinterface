package com.laboratorio.mindsapiinterface;

import com.laboratorio.mindsapiinterface.model.MindsAccount;
import com.laboratorio.mindsapiinterface.model.response.MindsAccountListResponse;
import java.util.List;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 18/09/2024
 * @updated 23/09/2024
 */
public interface MindsAccountApi {
    MindsAccount getAccountByUsername(String username);
    
    MindsAccountListResponse getFollowers(String id) throws Exception;
    MindsAccountListResponse getFollowers(String id, int limit) throws Exception;
    MindsAccountListResponse getFollowers(String id, int limit, int quantity) throws Exception;
    MindsAccountListResponse getFollowers(String id, int limit, int quantity, String posicionInicial) throws Exception;
    
    MindsAccountListResponse getFollowings(String id) throws Exception;
    MindsAccountListResponse getFollowings(String id, int limit) throws Exception;
    MindsAccountListResponse getFollowings(String id, int limit, int quantity) throws Exception;
    MindsAccountListResponse getFollowings(String id, int limit, int quantity, String posicionInicial) throws Exception;
    
    boolean followAccount(String id);
    boolean unfollowAccount(String id);
    
    MindsAccount checkrelationship(String username);
    
    // Consultar las sugerencias de seguimiento
    List<MindsAccount> getSuggestions();
    List<MindsAccount> getSuggestions(int limit);
}