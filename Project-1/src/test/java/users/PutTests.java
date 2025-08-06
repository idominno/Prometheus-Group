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
 * PUT tests for Users endpoint
 */
@Tag("users")
@Tag("put")
public class PutTests {
    
    private ApiClient apiClient;
    private ObjectMapper objectMapper;
    private JsonNode usersTestData;
    
    @BeforeEach
    void setUp() {
        apiClient = new ApiClient();
        objectMapper = new ObjectMapper();
        usersTestData = TestDataLoader.getUserUpdateData();
    }
    
    @Test
    @DisplayName("PUT Update User")
    void testUpdateUser() throws Exception {
        JsonNode testUser = TestDataLoader.getTestDataItem(usersTestData, 0);
        String endpoint = TestDataLoader.getEndpoint("users.base") + "/" + testUser.get("id").asInt();
        int expectedStatus = TestDataLoader.getStatusCode("status.success");
        String requestBody = objectMapper.writeValueAsString(testUser);
        
        ApiClient.ApiResponse response = apiClient.put(endpoint, requestBody);
        
        assertEquals(expectedStatus, response.getStatusCode(), "Expected status code " + expectedStatus);
        
        JsonNode jsonResponse = objectMapper.readTree(response.getBody());
        
        // Validate the response is a valid JSON object
        assertFalse(jsonResponse.isArray(), "Response must be a single object");
        
        // Validate all expected fields are present
        assertTrue(jsonResponse.has("id"), "Response must have id field");
        assertTrue(jsonResponse.has("name"), "Response must have name field");
        assertTrue(jsonResponse.has("username"), "Response must have username field");
        assertTrue(jsonResponse.has("email"), "Response must have email field");
        
        // Validate other fields were updated
        assertEquals(testUser.get("name").asText(), jsonResponse.get("name").asText(), 
            "Name must match test data");
        assertEquals(testUser.get("username").asText(), jsonResponse.get("username").asText(), 
            "Username must match test data");
        assertEquals(testUser.get("email").asText(), jsonResponse.get("email").asText(), 
            "Email must match test data");
        
        // Validate ID is preserved
        assertEquals(testUser.get("id").asInt(), jsonResponse.get("id").asInt(), "User ID must be preserved");
    }
    
    @Test
    @DisplayName("PUT Update User")
    void testUpdateUserNegative() throws Exception {
        JsonNode negativeTestData = TestDataLoader.getInvalidUserUpdateData();
        JsonNode testUser = TestDataLoader.getTestDataItem(negativeTestData, 0);
        String endpoint = TestDataLoader.getEndpoint("users.base") + "/" + testUser.get("id").asInt();
        int expectedStatus = TestDataLoader.getStatusCode("status.internalerror");
        String requestBody = objectMapper.writeValueAsString(testUser);
        
        ApiClient.ApiResponse response = apiClient.put(endpoint, requestBody);
        
        assertEquals(expectedStatus, response.getStatusCode(), "Expected status code " + expectedStatus + " for invalid user ID");
        assertNotNull(response.getBody(), "Response body must not be null");
    }
}