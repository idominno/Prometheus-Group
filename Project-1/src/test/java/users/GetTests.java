package users;

import client.ApiClient;
import util.TestDataLoader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GET tests for Users endpoint
 */
@Tag("users")
@Tag("get")
public class GetTests {
    
    private ApiClient apiClient;
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setUp() {
        apiClient = new ApiClient();
        objectMapper = new ObjectMapper();
    }
    
    @Test
    @DisplayName("GET All Users")
    void testGetAllUsers() throws Exception {
        String endpoint = TestDataLoader.getEndpoint("users.base");
        int expectedStatus = TestDataLoader.getStatusCode("status.success");
        
        ApiClient.ApiResponse response = apiClient.get(endpoint);
        
        assertEquals(expectedStatus, response.getStatusCode(), "Expected status code " + expectedStatus);
        
        // Validate response body is not null and not empty
        assertNotNull(response.getBody(), "Response body must not be null");
        assertFalse(response.getBody().isEmpty(), "Response body must not be empty");
        
        JsonNode jsonArray = objectMapper.readTree(response.getBody());
        assertTrue(jsonArray.isArray(), "Response must be an array");
        
        // Validate first user has expected structure if any users exist
        if (jsonArray.size() > 0) {
            JsonNode firstUser = jsonArray.get(0);
            assertTrue(firstUser.has("id"), "User must have id field");
            assertTrue(firstUser.has("name"), "User must have name field");
            assertTrue(firstUser.has("email"), "User must have email field");
            assertTrue(firstUser.has("username"), "User must have username field");
        }
    }
    
    @Test
    @DisplayName("GET Specific User")
    void testGetSpecificUser() throws Exception {
        String endpoint = TestDataLoader.getEndpoint("users.getuser");
        int expectedStatus = TestDataLoader.getStatusCode("status.success");
        
        ApiClient.ApiResponse response = apiClient.get(endpoint);
        
        assertEquals(expectedStatus, response.getStatusCode(), "Expected status code " + expectedStatus);
        
        JsonNode jsonObject = objectMapper.readTree(response.getBody());
        assertFalse(jsonObject.isArray(), "Response must be a single object");
        
        // Validate fields are present 
        int expectedId = Integer.parseInt(endpoint.substring(endpoint.lastIndexOf('/') + 1));
        assertEquals(expectedId, jsonObject.get("id").asInt(), "User ID must be " + expectedId);
        assertFalse(jsonObject.get("name").asText().isEmpty(), "Name must not be empty");
        assertFalse(jsonObject.get("username").asText().isEmpty(), "Username must not be empty");
        assertFalse(jsonObject.get("email").asText().isEmpty(), "Email must not be empty");
    }
    
    @Test
    @DisplayName("GET Non-Existent User")
    void testGetNonExistentUser() throws Exception {
        String endpoint = TestDataLoader.getEndpoint("users.negativeuser");
        int expectedStatus = TestDataLoader.getStatusCode("status.notfound");
        
        ApiClient.ApiResponse response = apiClient.get(endpoint);
        
        assertEquals(expectedStatus, response.getStatusCode(), "Expected status code " + expectedStatus + " for non-existent user");
        assertNotNull(response.getBody(), "Response body must not be null");
    }
}