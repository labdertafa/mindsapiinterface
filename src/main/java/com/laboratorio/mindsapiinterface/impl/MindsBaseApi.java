package com.laboratorio.mindsapiinterface.impl;

import com.google.gson.Gson;
import com.laboratorio.clientapilibrary.ApiClient;
import com.laboratorio.clientapilibrary.model.ApiMethodType;
import com.laboratorio.clientapilibrary.model.ApiRequest;
import com.laboratorio.clientapilibrary.model.ApiResponse;
import com.laboratorio.clientapilibrary.utils.ReaderConfig;
import com.laboratorio.mindsapiinterface.exception.MindsApiException;
import com.laboratorio.mindsapiinterface.model.MindsAccount;
import com.laboratorio.mindsapiinterface.model.MindsEntity;
import com.laboratorio.mindsapiinterface.model.MindsSession;
import com.laboratorio.mindsapiinterface.model.response.MindsAccountListResponse;
import com.laboratorio.mindsapiinterface.model.response.MindsEntityListResponse;
import com.laboratorio.mindsapiinterface.model.response.MindsUsersDetailResponse;
import com.laboratorio.mindsapiinterface.utils.InstruccionInfo;
import com.laboratorio.mindsapiinterface.utils.MindsSessionManager;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Rafael
 * @version 1.3
 * @created 18/09/2024
 * @updated 17/12/2025
 */
public class MindsBaseApi {
    protected static final String SUCCESS = "success";
    protected static final String ORIGIN = "Origin";
    protected static final String REFERER = "Referer";
    protected static final String MINDS_SITE = "https://www.minds.com";
    
    protected static final Logger log = LogManager.getLogger(MindsBaseApi.class);
    protected final ApiClient client;
    protected MindsSession session;
    protected ReaderConfig apiConfig;
    protected final Gson gson;

    public void setSession(MindsSession session) {
        this.session = session;
    }
    
    public MindsBaseApi() throws Exception {
        this.apiConfig = new ReaderConfig("config//minds_api.properties");
        String sessionFilePath = this.apiConfig.getProperty("minds_session_file");
        this.session = MindsSessionManager.loadSession(sessionFilePath);
        this.gson = new Gson();
        String proxyHost = this.apiConfig.getProperty("minds_proxy_host");
        String proxyPortStr = this.apiConfig.getProperty("minds_proxy_port");
        String certificatePath = this.apiConfig.getProperty("minds_proxy_certificate");
        if (proxyHost != null && !proxyHost.isBlank() && proxyPortStr != null && !proxyPortStr.isBlank()
                && certificatePath != null && !certificatePath.isBlank()) {
            int proxyPort = Integer.parseInt(proxyPortStr);
            this.client = new ApiClient(proxyHost, proxyPort, certificatePath);
        } else {
            this.client = new ApiClient();
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
    private String getAccountPage(String uri, int okStatus, int limit, String posicionInicial) {
        try {
            ApiRequest request = new ApiRequest(uri, okStatus, ApiMethodType.GET);
            request.addApiPathParam("limit", Integer.toString(limit));
            if (posicionInicial != null) {
                request.addApiPathParam("from_timestamp", posicionInicial);
            }
            request = this.addSessionHeader(request);
            request = this.addContentHeader(request);
            
            ApiResponse response = this.client.executeApiRequest(request);
            log.debug("Response getAccountPage: {}", response.getResponseStr());

            return response.getResponseStr();
        } catch (Exception e) {
            throw new MindsApiException("Error recuperando una página de cuentas Minds", e);
        }
    }
    
    boolean isContinuarGetAccountList(int quantity, List<MindsAccount> accounts, String maxId,
            MindsAccountListResponse accountListResponse, int limit) {
        log.debug("getSubscribersList. Cantidad: " + quantity + ". Recuperados: " + accounts.size() + ". Max_id: " + maxId);
        if (quantity > 0) {
            if ((accounts.size() >= quantity) || (maxId.isBlank())) {
                return false;
            }
        } else {
            if ((maxId.isBlank()) || (accountListResponse.getUsers().size() < limit)) {
                return false;
            }
        }
            
        return true;
    }
    
    // Función que obtiene los seguidores de una cuenta
    protected MindsEntityListResponse getAccountList(InstruccionInfo instruccionInfo, String userId, int quantity, String posicionInicial) {
        List<MindsAccount> accounts = null;
        boolean continuar = true;
        int limit = instruccionInfo.getLimit();
        int okStatus = instruccionInfo.getOkStatus();
        String maxId = posicionInicial;
        
        if (quantity > 0) {
            limit = Math.min(limit, quantity);
        }
        
        String uri = instruccionInfo.getEndpoint() + "/" + userId;
        
        do {
            String jsonStr = this.getAccountPage(uri, okStatus, limit, maxId);
            MindsAccountListResponse accountListResponse = this.gson.fromJson(jsonStr, MindsAccountListResponse.class);
            if (!accountListResponse.getStatus().equals(SUCCESS)) {
                throw new MindsApiException("Se ha producido un error recuperando una página de cuentas");
            }
            if (accounts == null) {
                accounts = accountListResponse.getUsers();
            } else {
                accounts.addAll(accountListResponse.getUsers());
            }

            maxId = accountListResponse.getLoadNext();
            continuar = this.isContinuarGetAccountList(quantity, accounts, maxId, accountListResponse, limit);
        } while (continuar);

        List<MindsEntity> entities = accounts.stream()
                .map(ac -> new MindsEntity(ac.getGuid(), ac.getGuid(), Long.parseLong(ac.getTime_created()), ac.getUrn(), null))
                .collect(Collectors.toList());

        if (quantity == 0) {
            return new MindsEntityListResponse(SUCCESS, entities, maxId);
        }

        return new MindsEntityListResponse(SUCCESS, entities.subList(0, Math.min(quantity, entities.size())), maxId);
    }
    
    private MindsAccountListResponse getEntitiesDetails(MindsEntityListResponse entityListResponse) {
        // Se obtiene el máximo de elementos a tratar por ciclo
        int maxLimit = Integer.parseInt(this.apiConfig.getProperty("getUsersDetail_max_limit"));
        List<MindsEntity> list = entityListResponse.getEntities();
        int totalSize = list.size();

        List<MindsAccount> accounts = null;
        for (int i = 0; i < totalSize; i += maxLimit) {
            // Obtener el índice final del bloque
            int endIndex = Math.min(i + maxLimit, totalSize);
            List<String> usersId = list.subList(i, endIndex).stream()
                    .map(e -> e.getUrn())
                    .collect(Collectors.toList());

            // Procesar el bloque actual
            MindsUsersDetailResponse usersDetailResponse = this.getUsersDetail(usersId);
            if (!usersDetailResponse.getStatus().equals(SUCCESS)) {
                throw new MindsApiException("Error, consultando los detalles de una lista de entidades. Respuesta inesperada.");
            }

            if (accounts == null) {
                accounts = usersDetailResponse.getEntities();
            } else {
                accounts.addAll(usersDetailResponse.getEntities());
            }
            log.debug("getSubscriptionsList. Recuperados: " + usersDetailResponse.getEntities().size() + ". Total: " + accounts.size());
        }
        
        return new MindsAccountListResponse(SUCCESS, accounts, entityListResponse.getLoadNext());
    }
    
    protected MindsAccountListResponse getSubscribersList(InstruccionInfo instruccionInfo, String userId, int quantity, String posicionInicial) {
        MindsEntityListResponse entityListResponse = this.getAccountList(instruccionInfo, userId, quantity, posicionInicial);
        return this.getEntitiesDetails(entityListResponse);
    }
    
    // Función que obtiene los datos de los usuarios asociados a una lista de entidades por su URN
    protected MindsUsersDetailResponse getUsersDetail(List<String> usersId) {
        String endpoint = this.apiConfig.getProperty("getUsersDetail_endpoint");
        int okStatus = Integer.parseInt(this.apiConfig.getProperty("getUsersDetail_ok_status"));
        int maxLimit = Integer.parseInt(this.apiConfig.getProperty("getUsersDetail_max_limit"));
        
        if (usersId.size() > maxLimit) {
            throw new MindsApiException("Error, se está pidiendo los detalles de demasiados urns: " + maxLimit);
        }
        if (usersId.isEmpty()) {
            throw new MindsApiException("Error, no hay ningún usuario al cual consultar el detalle");
        }
        
        try {
            StringBuilder urns = null;
            for (String userUrn : usersId) {
                if (urns == null) {
                    urns = new StringBuilder(userUrn);
                } else {
                    urns.append(",");
                    urns.append(userUrn);
                }
            }
            if (urns == null) {
                urns = new StringBuilder("");
            }
            ApiRequest request = new ApiRequest(endpoint, okStatus, ApiMethodType.GET);
            request.addApiPathParam("urns", urns.toString());
            request.addApiPathParam("export_user_counts", "true");
            request = this.addSessionHeader(request);
            request = this.addContentHeader(request);
            
            ApiResponse response = this.client.executeApiRequest(request);
            log.debug("Response getUsersDetail: {}", response.getResponseStr());
            
            return this.gson.fromJson(response.getResponseStr(), MindsUsersDetailResponse.class);
        } catch (Exception e) {
            throw new MindsApiException("Error recuperando los detalles de una lista de usuarios Minds", e);
        }
    }
    
    private boolean isContinuarGetEntityList(int quantity, List<MindsEntity> entities, String maxId,
            MindsEntityListResponse entityListResponse, int limit) {
        log.debug("getAccountList. Cantidad: " + quantity + ". Recuperados: " + entities.size() + ". Max_id: " + maxId);
        if (quantity > 0) {
            if ((entities.size() >= quantity) || (maxId.isBlank())) {
                return false;
            }
        } else {
            if ((maxId.isBlank()) || (entityListResponse.getEntities().size() < limit)) {
                return false;
            }
        }
        
        return true;
    }
    
    // Función que obtiene los seguidos por una cuenta
    protected MindsEntityListResponse getEntityList(InstruccionInfo instruccionInfo, String userId, int quantity, String posicionInicial) {
        List<MindsEntity> entities = null;
        boolean continuar = true;
        boolean sumar;
        int limit = instruccionInfo.getLimit();
        int okStatus = instruccionInfo.getOkStatus();
        String maxId = posicionInicial;
        
        if (quantity > 0) {
            limit = Math.min(limit, quantity);
        }
        
        String uri = instruccionInfo.getEndpoint() + "/" + userId + "/" + instruccionInfo.getComplementoUrl();
        
        do {
            String jsonStr = this.getAccountPage(uri, okStatus, limit, maxId);
            MindsEntityListResponse entityListResponse = this.gson.fromJson(jsonStr, MindsEntityListResponse.class);
            if (!entityListResponse.getStatus().equals(SUCCESS)) {
                throw new MindsApiException("Se ha producido un error recuperando una página de entidades");
            }
            if (entities == null) {
                entities = entityListResponse.getEntities();
                sumar = true;
            } else {
                entities.addAll(entityListResponse.getEntities());
                sumar = false;
            }

            maxId = entityListResponse.getLoadNext();
            continuar = this.isContinuarGetEntityList(quantity, entities, maxId, entityListResponse, limit);
            // Si es la primera búsqueda, se ajusta el max_id
            if (sumar) {
                int valor = Integer.parseInt(maxId) + 1;
                maxId = Integer.toString(valor);
            }
        } while (continuar);

        if (quantity == 0) {
            return new MindsEntityListResponse(SUCCESS, entities, maxId);
        }

        return new MindsEntityListResponse(SUCCESS, entities.subList(0, Math.min(quantity, entities.size())), maxId);
    }
    
    // Función que obtiene los seguidos por una cuenta
    protected MindsAccountListResponse getSubscriptionsList(InstruccionInfo instruccionInfo, String userId, int quantity, String posicionInicial) {
        MindsEntityListResponse entityListResponse = this.getEntityList(instruccionInfo, userId, quantity, posicionInicial);
        return this.getEntitiesDetails(entityListResponse);
    }
}