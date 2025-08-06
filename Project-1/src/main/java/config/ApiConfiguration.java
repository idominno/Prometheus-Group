package config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Configuration for API requests
 */
public class ApiConfiguration {
    private static ApiConfiguration instance;
    private final Properties properties;
    private final Map<String, String> headers;
    
    private ApiConfiguration() {
        properties = new Properties();
        headers = new HashMap<>();
        loadConfiguration();
    }
    
    public static synchronized ApiConfiguration getInstance() {
        if (instance == null) {
            instance = new ApiConfiguration();
        }
        return instance;
    }
    
    // load config from properties file, use defaults if file missing
    private void loadConfiguration() {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
        
        // set defaults for any missing properties
        properties.setProperty("api.base.url", 
            properties.getProperty("api.base.url", "https://jsonplaceholder.typicode.com"));
        properties.setProperty("api.timeout", 
            properties.getProperty("api.timeout", "30"));
        
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        headers.put("User-Agent", "API-Test-Framework/1.0");
    }
    
    public String getBaseUrl() {
        return properties.getProperty("api.base.url");
    }
    
    public int getTimeout() {
        return Integer.parseInt(properties.getProperty("api.timeout"));
    }
    
    public Map<String, String> getDefaultHeaders() {
        return new HashMap<>(headers);
    }
    
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
    
    public void addHeader(String key, String value) {
        headers.put(key, value);
    }
}