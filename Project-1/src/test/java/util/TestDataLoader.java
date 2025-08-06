package util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility to load test data and configuration
 */
public class TestDataLoader {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static Properties testProperties = null;
    
    // load properties from test.properties file
    private static synchronized Properties getTestProperties() {
        if (testProperties == null) {
            testProperties = new Properties();
            try (InputStream inputStream = TestDataLoader.class.getClassLoader()
                    .getResourceAsStream("test.properties")) {
                
                if (inputStream == null) {
                    throw new RuntimeException("Could not find test.properties");
                }
                
                testProperties.load(inputStream);
                
            } catch (IOException e) {
                throw new RuntimeException("Failed to load test.properties", e);
            }
        }
        return testProperties;
    }
    
    // get endpoint URLs
    public static String getEndpoint(String key) {
        String value = getTestProperties().getProperty(key);
        if (value == null) {
            throw new RuntimeException("Endpoint key not found: " + key);
        }
        return value;
    }
    
    // get status codes
    public static int getStatusCode(String key) {
        String value = getTestProperties().getProperty(key);
        if (value == null) {
            throw new RuntimeException("Status code key not found: " + key);
        }
        return Integer.parseInt(value);
    }
    
    // load json test data from file
    public static JsonNode loadTestData(String resourcePath) {
        try (InputStream inputStream = TestDataLoader.class.getClassLoader()
                .getResourceAsStream(resourcePath)) {
            
            if (inputStream == null) {
                throw new RuntimeException("Could not find resource: " + resourcePath);
            }
            
            return objectMapper.readTree(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test data from: " + resourcePath, e);
        }
    }
    
    public static JsonNode getPostCreationData() {
        return loadTestData("testdata/posts-create.json");
    }
    
    public static JsonNode getUserCreationData() {
        return loadTestData("testdata/users-create.json");
    }
    
    public static JsonNode getTodoCreationData() {
        return loadTestData("testdata/todos-create.json");
    }
    
    public static JsonNode getPostUpdateData() {
        return loadTestData("testdata/posts-update.json");
    }
    
    public static JsonNode getUserUpdateData() {
        return loadTestData("testdata/users-update.json");
    }
    
    public static JsonNode getTodoUpdateData() {
        return loadTestData("testdata/todos-update.json");
    }
    
    public static JsonNode getInvalidPostCreationData() {
        return loadTestData("testdata/posts-create-invalid.json");
    }
    
    public static JsonNode getInvalidUserCreationData() {
        return loadTestData("testdata/users-create-invalid.json");
    }
    
    public static JsonNode getInvalidTodoCreationData() {
        return loadTestData("testdata/todos-create-invalid.json");
    }
    
    public static JsonNode getInvalidPostUpdateData() {
        return loadTestData("testdata/posts-update-invalid.json");
    }
    
    public static JsonNode getInvalidUserUpdateData() {
        return loadTestData("testdata/users-update-invalid.json");
    }
    
    public static JsonNode getInvalidTodoUpdateData() {
        return loadTestData("testdata/todos-update-invalid.json");
    }
    
    // get specific test item by index
    public static JsonNode getTestDataItem(JsonNode testData, int index) {
        if (testData.isArray() && index >= 0 && index < testData.size()) {
            return testData.get(index);
        }
        throw new RuntimeException("Invalid test data index: " + index);
    }
}