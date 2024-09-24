package com.laboratorio.mindsapiinterface.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.laboratorio.clientapilibrary.ApiClient;
import com.laboratorio.clientapilibrary.impl.ApiClientImpl;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.mindsapiinterface.exception.MindsApiException;
import com.laboratorio.mindsapiinterface.model.MindsAccount;
import com.laboratorio.mindsapiinterface.model.MindsEntity;
import com.laboratorio.mindsapiinterface.model.MindsSession;
import com.laboratorio.mindsapiinterface.model.response.MindsAccountListResponse;
import com.laboratorio.mindsapiinterface.model.response.MindsEntityListResponse;
import com.laboratorio.mindsapiinterface.model.response.MindsUsersDetailResponse;
import com.laboratorio.mindsapiinterface.utils.InstruccionInfo;
import com.laboratorio.mindsapiinterface.utils.MindsApiConfig;
import com.laboratorio.mindsapiinterface.utils.MindsSessionManager;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 18/09/2024
 * @updated 24/09/2024
 */
public class MindsBaseApi {
    protected static final Logger log = LogManager.getLogger(MindsBaseApi.class);
    protected final ApiClient client;
    protected MindsSession session;
    protected MindsApiConfig apiConfig;
    protected final Gson gson;

    public void setSession(MindsSession session) {
        this.session = session;
    }
    
    public MindsBaseApi() throws Exception {
        this.apiConfig = MindsApiConfig.getInstance();
        String sessionFilePath = this.apiConfig.getProperty("minds_session_file");
        this.session = MindsSessionManager.loadSession(sessionFilePath);
        this.client = new ApiClientImpl();
        this.gson = new Gson();
    }
    
    protected void logException(Exception e) {
        log.error("Error: " + e.getMessage());
        if (e.getCause() != null) {
            log.error("Causa: " + e.getCause().getMessage());
        }
    }
    
    protected ApiRequest addSessionHeader(ApiRequest request) {
        request.addApiHeader("Cookie", session.getCookies());
        request.addApiHeader("X-Version", session.getXVersion());
        request.addApiHeader("X-Xsrf-Token", session.getXToken());
        
        return request;
    }
    
    protected ApiRequest addContentHeader(ApiRequest request) {
        request.addApiHeader("Content-Type", "application/json");
        request.addApiHeader("Accept", "application/json, text/plain, */*");
        request.addApiHeader("Accept-Encoding", "gzip, deflate, br, zstd");
        
        return request;
    }
    
    // Función que devuelve una página de seguidores o seguidos de una cuenta
    private String getAccountPage(String uri, int okStatus, int limit, String posicionInicial) throws Exception {
        try {
            ApiRequest request = new ApiRequest(uri, okStatus);
            request.addApiPathParam("limit", Integer.toString(limit));
            if (posicionInicial != null) {
                request.addApiPathParam("from_timestamp", posicionInicial);
            }
            request = this.addContentHeader(request);
            
            return this.client.executeGetRequest(request);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new MindsApiException(MindsBaseApi.class.getName(), e.getMessage());
        }
    }
    
    // Función que obtiene los seguidores de una cuenta
    protected MindsEntityListResponse getAccountList(InstruccionInfo instruccionInfo, String userId, int quantity, String posicionInicial) throws Exception {
        List<MindsAccount> accounts = null;
        boolean continuar = true;
        int limit = instruccionInfo.getLimit();
        int okStatus = instruccionInfo.getOkStatus();
        String max_id = posicionInicial;
        
        if (quantity > 0) {
            limit = Math.min(limit, quantity);
        }
        
        String uri = instruccionInfo.getEndpoint() + "/" + userId;
        
        try {
            do {
                String jsonStr = this.getAccountPage(uri, okStatus, limit, max_id);
                MindsAccountListResponse accountListResponse = this.gson.fromJson(jsonStr, MindsAccountListResponse.class);
                if (!accountListResponse.getStatus().equals("success")) {
                    throw new MindsApiException(MindsAccountApiImpl.class.getName(), "Se ha producido un error recuperando una página de cuentas");
                }
                if (accounts == null) {
                    accounts = accountListResponse.getUsers();
                } else {
                    accounts.addAll(accountListResponse.getUsers());
                }
                
                max_id = accountListResponse.getLoadNext();
                log.debug("getSubscribersList. Cantidad: " + quantity + ". Recuperados: " + accounts.size() + ". Max_id: " + max_id);
                if (quantity > 0) {
                    if ((accounts.size() >= quantity) || (max_id.isBlank())) {
                        continuar = false;
                    }
                } else {
                    if ((max_id.isBlank()) || (accountListResponse.getUsers().size() < limit)) {
                        continuar = false;
                    }
                }
            } while (continuar);
            
            List<MindsEntity> entities = accounts.stream()
                    .map(ac -> new MindsEntity(ac.getGuid(), ac.getGuid(), Long.parseLong(ac.getTime_created()), ac.getUrn(), null))
                    .collect(Collectors.toList());

            if (quantity == 0) {
                return new MindsEntityListResponse("success", entities, max_id);
            }
            
            return new MindsEntityListResponse("success", entities.subList(0, Math.min(quantity, entities.size())), max_id);
        } catch (Exception e) {
            throw e;
        }
    }
    
    private MindsAccountListResponse getEntitiesDetails(MindsEntityListResponse entityListResponse) throws Exception {
        // Se obtiene el máximo de elementos a tratar por ciclo
        int maxLimit = Integer.parseInt(this.apiConfig.getProperty("getUsersDetail_max_limit"));
        List<MindsEntity> list = entityListResponse.getEntities();
        int totalSize = list.size();

        List<MindsAccount> accounts = null;
        for (int i = 0; i < totalSize; i += maxLimit) {
            // Obtener el índice final del bloque
            int endIndex = Math.min(i + maxLimit, totalSize);
            List<MindsEntity> sublist = list.subList(i, endIndex);

            // Procesar el bloque actual
            MindsUsersDetailResponse usersDetailResponse = this.getUsersDetail(sublist);
            if (!usersDetailResponse.getStatus().equals("success")) {
                throw new MindsApiException(MindsBaseApi.class.getName(), "Error, consultando los detalles de una lista de entidades. Respuesta inesperada.");
            }

            if (accounts == null) {
                accounts = usersDetailResponse.getEntities();
            } else {
                accounts.addAll(usersDetailResponse.getEntities());
            }
            log.debug("getSubscriptionsList. Recuperados: " + usersDetailResponse.getEntities().size() + ". Total: " + accounts.size());
        }
        
        return new MindsAccountListResponse("success", accounts, entityListResponse.getLoadNext());
    }
    
    protected MindsAccountListResponse getSubscribersList(InstruccionInfo instruccionInfo, String userId, int quantity, String posicionInicial) throws Exception {
        try {
            MindsEntityListResponse entityListResponse = this.getAccountList(instruccionInfo, userId, quantity, posicionInicial);
            return this.getEntitiesDetails(entityListResponse);
        } catch (Exception e) {
            throw e;
        }
    }
    
    // Función que obtiene los datos de los usuarios asociados a una lista de entidades por su URN
    private MindsUsersDetailResponse getUsersDetail(List<MindsEntity> entities) throws Exception {
        String endpoint = this.apiConfig.getProperty("getUsersDetail_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getUsersDetail_ok_status"));
        int maxLimit = Integer.parseInt(this.apiConfig.getProperty("getUsersDetail_max_limit"));
        
        if (entities.size() > maxLimit) {
            throw new MindsApiException(MindsBaseApi.class.getName(), "Error, se está pidiendo los detalles de demasiados urns: " + maxLimit);
        }
        if (entities.isEmpty()) {
            throw new MindsApiException(MindsBaseApi.class.getName(), "Error, noy ahy ningún usuario al cual consultar el detalle");
        }
        
        try {
            StringBuilder urns = null;
            for (MindsEntity entity : entities) {
                if (urns == null) {
                    urns = new StringBuilder(entity.getUrn());
                } else {
                    urns.append(",");
                    urns.append(entity.getUrn());
                }
            }
            ApiRequest request = new ApiRequest(endpoint, okStatus);
            request.addApiPathParam("urns", urns.toString());
            request.addApiPathParam("export_user_counts", "true");
            request = this.addContentHeader(request);
            
            String jsonStr = this.client.executeGetRequest(request);
            
            return this.gson.fromJson(jsonStr, MindsUsersDetailResponse.class);
        } catch (JsonSyntaxException e) {
            logException(e);
            throw e;
        } catch (Exception e) {
            throw new MindsApiException(MindsBaseApi.class.getName(), e.getMessage());
        }
    }
    
    // Función que obtiene los seguidos por una cuenta
    protected MindsEntityListResponse getEntityList(InstruccionInfo instruccionInfo, String userId, int quantity, String posicionInicial) throws Exception {
        List<MindsEntity> entities = null;
        boolean continuar = true;
        boolean sumar;
        int limit = instruccionInfo.getLimit();
        int okStatus = instruccionInfo.getOkStatus();
        String max_id = posicionInicial;
        
        if (quantity > 0) {
            limit = Math.min(limit, quantity);
        }
        
        String uri = instruccionInfo.getEndpoint() + "/" + userId + "/" + instruccionInfo.getComplementoUrl();
        
        try {
            do {
                String jsonStr = this.getAccountPage(uri, okStatus, limit, max_id);
                MindsEntityListResponse entityListResponse = this.gson.fromJson(jsonStr, MindsEntityListResponse.class);
                if (!entityListResponse.getStatus().equals("success")) {
                    throw new MindsApiException(MindsAccountApiImpl.class.getName(), "Se ha producido un error recuperando una página de entidades");
                }
                if (entities == null) {
                    entities = entityListResponse.getEntities();
                    sumar = true;
                } else {
                    entities.addAll(entityListResponse.getEntities());
                    sumar = false;
                }
                
                max_id = entityListResponse.getLoadNext();
                log.debug("getAccountList. Cantidad: " + quantity + ". Recuperados: " + entities.size() + ". Max_id: " + max_id);
                if (quantity > 0) {
                    if ((entities.size() >= quantity) || (max_id.isBlank())) {
                        continuar = false;
                    }
                } else {
                    if ((max_id.isBlank()) || (entityListResponse.getEntities().size() < limit)) {
                        continuar = false;
                    }
                }
                // Si es la primera búsqueda, se ajusta el max_id
                if (sumar) {
                    int valor = Integer.parseInt(max_id) + 1;
                    max_id = Integer.toString(valor);
                }
            } while (continuar);

            if (quantity == 0) {
                return new MindsEntityListResponse("success", entities, max_id);
            }
            
            return new MindsEntityListResponse("success", entities.subList(0, Math.min(quantity, entities.size())), max_id);
        } catch (Exception e) {
            throw e;
        }
    }
    
    // Función que obtiene los seguidos por una cuenta
    protected MindsAccountListResponse getSubscriptionsList(InstruccionInfo instruccionInfo, String userId, int quantity, String posicionInicial) throws Exception {
        try {
            MindsEntityListResponse entityListResponse = this.getEntityList(instruccionInfo, userId, quantity, posicionInicial);
            return this.getEntitiesDetails(entityListResponse);
        } catch (Exception e) {
            throw e;
        }
    }
}