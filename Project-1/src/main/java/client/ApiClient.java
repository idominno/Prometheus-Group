package client;

import config.ApiConfiguration;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * API client for making HTTP requests to the JSONPlaceholder API
 */
public class ApiClient {
    private static final Logger logger = LoggerFactory.getLogger(ApiClient.class);
    private final CloseableHttpClient client;
    private final ApiConfiguration config;
    
    public ApiClient() {
        this.config = ApiConfiguration.getInstance();
        RequestConfig requestConfig = RequestConfig.custom()
                .setResponseTimeout(Timeout.ofSeconds(config.getTimeout()))
                .build();
                
        this.client = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
    }
    
    public ApiResponse get(String endpoint) throws IOException {
        return get(endpoint, null);
    }
    
    public ApiResponse get(String endpoint, Map<String, String> headers) throws IOException {
        String url = buildUrl(endpoint);
        HttpGet request = new HttpGet(url);
        addHeaders(request, headers);
        
        logRequest("GET", url, null);
        return client.execute(request, createResponseHandler("GET"));
    }
    
    public ApiResponse post(String endpoint, String jsonBody) throws IOException {
        return post(endpoint, jsonBody, null);
    }
    
    public ApiResponse post(String endpoint, String jsonBody, Map<String, String> headers) throws IOException {
        String url = buildUrl(endpoint);
        HttpPost request = new HttpPost(url);
        request.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
        addHeaders(request, headers);
        
        logRequest("POST", url, jsonBody);
        return client.execute(request, createResponseHandler("POST"));
    }
    
    public ApiResponse put(String endpoint, String jsonBody) throws IOException {
        return put(endpoint, jsonBody, null);
    }
    
    public ApiResponse put(String endpoint, String jsonBody, Map<String, String> headers) throws IOException {
        String url = buildUrl(endpoint);
        HttpPut request = new HttpPut(url);
        request.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
        addHeaders(request, headers);
        
        logRequest("PUT", url, jsonBody);
        return client.execute(request, createResponseHandler("PUT"));
    }
    
    public ApiResponse delete(String endpoint) throws IOException {
        return delete(endpoint, null);
    }
    
    public ApiResponse delete(String endpoint, Map<String, String> headers) throws IOException {
        String url = buildUrl(endpoint);
        HttpDelete request = new HttpDelete(url);
        addHeaders(request, headers);
        
        logRequest("DELETE", url, null);
        return client.execute(request, createResponseHandler("DELETE"));
    }
    
    private String buildUrl(String endpoint) {
        return config.getBaseUrl() + endpoint;
    }
    
    private void logRequest(String method, String url, String body) {
        if (body != null) {
            logger.info("{} request to: {} with body: {}", method, url, body);
        } else {
            logger.info("{} request to: {}", method, url);
        }
    }
    
    private String truncateForLogging(String text) {
        return text.length() > 200 ? text.substring(0, 200) + "..." : text;
    }
    
    private HttpClientResponseHandler<ApiResponse> createResponseHandler(String method) {
        return response -> {
            String responseBody = response.getEntity() != null ? EntityUtils.toString(response.getEntity()) : "";
            logger.info("{} response: {} - {}", method, response.getCode(), truncateForLogging(responseBody));
            return new ApiResponse(response.getCode(), responseBody);
        };
    }
    
    private void addHeaders(org.apache.hc.core5.http.HttpRequest request, Map<String, String> additionalHeaders) {
        // get defaults from config
        Map<String, String> defaultHeaders = config.getDefaultHeaders();
        for (Map.Entry<String, String> header : defaultHeaders.entrySet()) {
            request.addHeader(header.getKey(), header.getValue());
        }
        
        // overlay any additional headers
        if (additionalHeaders != null) {
            for (Map.Entry<String, String> header : additionalHeaders.entrySet()) {
                request.addHeader(header.getKey(), header.getValue());
            }
        }
    }
    
    public void close() throws IOException {
        client.close();
    }
    
    public static class ApiResponse {
        private final int statusCode;
        private final String body;
        
        public ApiResponse(int statusCode, String body) {
            this.statusCode = statusCode;
            this.body = body;
        }
        
        public int getStatusCode() {
            return statusCode;
        }
        
        public String getBody() {
            return body;
        }
    }
}