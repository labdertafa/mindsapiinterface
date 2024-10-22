package com.laboratorio.mindsapiinterface;

import com.laboratorio.mindsapiinterface.model.MindsAccount;
import com.laboratorio.mindsapiinterface.model.response.MindsAccountListResponse;
import java.util.List;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 18/09/2024
 * @updated 22/10/2024
 */
public interface MindsAccountApi {
    List<MindsAccount> getAccountsById(List<String> usersId) throws Exception;
    MindsAccount getAccountByUsername(String username);
    
    MindsAccountListResponse getFollowers(String id) throws Exception;
    MindsAccountListResponse getFollowers(String id, int limit) throws Exception;
    MindsAccountListResponse getFollowers(String id, int limit, int quantity) throws Exception;
    MindsAccountListResponse getFollowers(String id, int limit, int quantity, String posicionInicial) throws Exception;
    
    List<String> getFollowersIds(String userId, int limit) throws Exception;
    
    MindsAccountListResponse getFollowings(String id) throws Exception;
    MindsAccountListResponse getFollowings(String id, int limit) throws Exception;
    MindsAccountListResponse getFollowings(String id, int limit, int quantity) throws Exception;
    MindsAccountListResponse getFollowings(String id, int limit, int quantity, String posicionInicial) throws Exception;
    
    List<String> getFollowingsIds(String userId, int limit) throws Exception;
    
    boolean followAccount(String id);
    boolean unfollowAccount(String id);
    
    MindsAccount checkrelationship(String username);
    
    // Consultar las sugerencias de seguimiento
    List<MindsAccount> getSuggestions();
    List<MindsAccount> getSuggestions(int limit);
}