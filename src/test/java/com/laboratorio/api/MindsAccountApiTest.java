package com.laboratorio.api;

import com.laboratorio.mindsapiinterface.MindsAccountApi;
import com.laboratorio.mindsapiinterface.exception.MindsApiException;
import com.laboratorio.mindsapiinterface.impl.MindsAccountApiImpl;
import com.laboratorio.mindsapiinterface.model.MindsAccount;
import com.laboratorio.mindsapiinterface.model.response.MindsAccountListResponse;
import com.laboratorio.mindsapiinterface.utils.MindsApiConfig;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 18/09/2024
 * @updated 12/10/2024
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MindsAccountApiTest {
    private static MindsAccountApi accountApi;
    
    @BeforeEach
    public void initTest() throws Exception {
        accountApi = new MindsAccountApiImpl();
    }
    
    @Test
    public void getAccountsById() throws Exception {
        String id = "urn:user:903289035852619779";
        List<String> usersId = List.of(id);
        
        List<MindsAccount> accounts = accountApi.getAccountsById(usersId);
        
        assertEquals(1, accounts.size());
        assertEquals("903289035852619779", accounts.get(0).getGuid());
    }
    
    @Test
    public void getAccountByUsername() {
        String username = "x999";
        
        MindsAccount account = accountApi.getAccountByUsername(username);
        
        assertEquals(username, account.getUsername());
    }
    
    @Test
    public void findAccountByInvalidUsername() {
        String acct = "@ZZZWWWWPPPSSSDDGGGFF";
        
        assertThrows(MindsApiException.class, () -> {
            accountApi.getAccountByUsername(acct);
        });
    }
    
    @Test
    public void get40Followers() throws Exception {
        String id = "1618283433321041923";
        int maxLimit = 100;
        int cantidad = 40;
        
        MindsAccountListResponse accountListResponse = accountApi.getFollowers(id, maxLimit, cantidad);

        assertEquals(cantidad, accountListResponse.getUsers().size());
        assertTrue(!accountListResponse.getLoadNext().isEmpty());
    }
    
    @Test
    public void get30FollowersLimit5() throws Exception {
        String id = "1618283433321041923";
        int maxLimit = 5;
        int cantidad = 30;
        
        MindsAccountListResponse accountListResponse = accountApi.getFollowers(id, maxLimit, cantidad);

        assertEquals(cantidad, accountListResponse.getUsers().size());
        assertTrue(!accountListResponse.getLoadNext().isEmpty());
    }
    
    @Test
    public void get70FollowersDefaultLimit() throws Exception {
        String id = "1618283433321041923";
        int defaultLimit = 0;
        int cantidad = 70;
        
        MindsAccountListResponse accountListResponse = accountApi.getFollowers(id, defaultLimit, cantidad);

        // assertEquals(cantidad, accountListResponse.getUsers().size());
        assertTrue(!accountListResponse.getLoadNext().isEmpty());
    }
    
    @Test
    public void getAllFollowers() throws Exception {     // Usa default limit
        String id = "1618283433321041923";
        
        MindsAccountListResponse accountListResponse = accountApi.getFollowers(id);
        assertTrue(accountListResponse.getLoadNext().isBlank());
        assertTrue(accountListResponse.getUsers().size() > 100);
    }
    
    @Test
    public void getFollowersInvalidId() throws Exception {
        String id = "1125349753AAABBB60";
        
        MindsAccountListResponse accountListResponse = accountApi.getFollowers(id);
        
        assertTrue(accountListResponse.getUsers() == null);
        assertTrue(accountListResponse.getLoadNext().isBlank());
    }
    
    @Test
    public void get40Followings() throws Exception {
        String id = "1618283433321041923";
        int maxLimit = 100;
        int cantidad = 40;
        
        MindsAccountListResponse accountListResponse = accountApi.getFollowings(id, maxLimit, cantidad);

        assertEquals(cantidad, accountListResponse.getUsers().size());
        assertTrue(!accountListResponse.getLoadNext().isEmpty());
    }
    
    @Test
    public void get30FollowingsLimit10() throws Exception {
        String id = "1618283433321041923";
        int maxLimit = 10;
        int cantidad = 30;
        
        MindsAccountListResponse accountListResponse = accountApi.getFollowings(id, maxLimit, cantidad);

        // assertEquals(cantidad, accountListResponse.getUsers().size());
        assertTrue(!accountListResponse.getLoadNext().isEmpty());
    }
    
    @Test
    public void get70FollowingsDefaultLimit() throws Exception {
        String id = "1618283433321041923";
        int defaultLimit = 0;
        int cantidad = 70;
        
        MindsAccountListResponse accountListResponse = accountApi.getFollowings(id, defaultLimit, cantidad);

        // assertEquals(cantidad, accountListResponse.getUsers().size());
        assertTrue(!accountListResponse.getLoadNext().isEmpty());
    }
    
    @Test
    public void getAllFollowings() throws Exception {     // Usa default limit
        String id = "1618283433321041923";
        
        MindsAccountListResponse accountListResponse = accountApi.getFollowings(id);
        assertTrue(!accountListResponse.getLoadNext().isBlank());
        assertTrue(accountListResponse.getUsers().size() > 100);
    }
    
    @Test
    public void getFollowingsInvalidId() throws Exception {
        String id = "1125349753AAABBB60";
        
        MindsAccountListResponse accountListResponse = accountApi.getFollowings(id);
        
        assertTrue(accountListResponse.getUsers() == null);
        assertTrue(!accountListResponse.getLoadNext().isBlank());
    }
    
    @Test @Order(1)
    public void followAccount() {
        String id = "1026159398378020872";
        
        boolean result = accountApi.followAccount(id);
        
        assertTrue(result);
    }
    
    @Test
    public void followInvalidAccount() {
        String id = "9998283433321041923";
        
        assertThrows(MindsApiException.class, () -> {
            accountApi.followAccount(id);
        });
    }
    
    @Test @Order(2)
    public void unfollowAccount() {
        String id = "1026159398378020872";
        
        boolean result = accountApi.unfollowAccount(id);
        
        assertTrue(result);
    }
    
    @Test
    public void unfollowInvalidAccount() {
        String id = "9998283433321041923";
        
        assertThrows(MindsApiException.class, () -> {
            this.accountApi.unfollowAccount(id);
        });
    }
    
    @Test
    public void checkMutualRelationship() {
        String username = "x999";
        
        MindsAccount account = accountApi.checkrelationship(username);
        
        assertEquals(username, account.getUsername());
        assertTrue(account.isSubscribed());
        assertTrue(account.isSubscriber());
    }
    
    @Test
    public void getSuggestionsWithoutLimit() {
        int defaultLimit = Integer.parseInt(MindsApiConfig.getInstance().getProperty("getSuggestions_default_limit"));
        
        List<MindsAccount> accounts = accountApi.getSuggestions();
        
        assertEquals(defaultLimit, accounts.size());
    }
    
    @Test
    public void getSuggestionsWithLimit() {
        int limit = 10;
        
        List<MindsAccount> accounts = accountApi.getSuggestions(limit);
        
        assertEquals(limit, accounts.size());
    }
}