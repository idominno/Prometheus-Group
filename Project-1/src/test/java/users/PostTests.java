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
 * POST tests for Users endpoint using external JSON test data.
 */
@Tag("users")
@Tag("post")
public class PostTests {
    
    private ApiClient apiClient;
    private ObjectMapper objectMapper;
    private JsonNode usersTestData;
    
    @BeforeEach
    void setUp() {
        apiClient = new ApiClient();
        objectMapper = new ObjectMapper();
        usersTestData = TestDataLoader.getUserCreationData();
    }
    
    @Test
    @DisplayName("POST Create User")
    void testCreateUser() throws Exception {
        String endpoint = TestDataLoader.getEndpoint("users.base");
        int expectedStatus = TestDataLoader.getStatusCode("status.created");
        
        JsonNode testUser = TestDataLoader.getTestDataItem(usersTestData, 0);
        String requestBody = objectMapper.writeValueAsString(testUser);
        
        ApiClient.ApiResponse response = apiClient.post(endpoint, requestBody);
        
        assertEquals(expectedStatus, response.getStatusCode(), "Expected status code " + expectedStatus);
        
        JsonNode jsonResponse = objectMapper.readTree(response.getBody());
        
        // Validate response has expected fields
        assertTrue(jsonResponse.has("id"), "Response must have id field");
        assertTrue(jsonResponse.has("name"), "Response must have name field");
        assertTrue(jsonResponse.has("username"), "Response must have username field");
        assertTrue(jsonResponse.has("email"), "Response must have email field");
        assertTrue(jsonResponse.has("phone"), "Response must have phone field");
        assertTrue(jsonResponse.has("website"), "Response must have website field");
        
        // Validate that created user has an ID
        assertTrue(jsonResponse.get("id").asInt() > 0, "Created user ID must be positive");
        
        // Validate other fields match test data
        assertEquals(testUser.get("name").asText(), jsonResponse.get("name").asText(), 
            "Name must match test data");
        assertEquals(testUser.get("username").asText(), jsonResponse.get("username").asText(), 
            "Username must match test data");
        assertEquals(testUser.get("email").asText(), jsonResponse.get("email").asText(), 
            "Email must match test data");
        assertEquals(testUser.get("phone").asText(), jsonResponse.get("phone").asText(), 
            "Phone must match test data");
        assertEquals(testUser.get("website").asText(), jsonResponse.get("website").asText(), 
            "Website must match test data");
    }
    
    @Test
    @DisplayName("POST Create User")
    void testCreateUserNegative() throws Exception {
        String endpoint = TestDataLoader.getEndpoint("users.base");
        int expectedStatus = TestDataLoader.getStatusCode("status.created");
        
        JsonNode negativeTestData = TestDataLoader.getInvalidUserCreationData();
        JsonNode testUser = TestDataLoader.getTestDataItem(negativeTestData, 0);
        String requestBody = objectMapper.writeValueAsString(testUser);
        
        ApiClient.ApiResponse response = apiClient.post(endpoint, requestBody);
        
        // Note: JSONPlaceholder API returns 201 even for invalid data
        // Typically we would expect a 400 or 422 for invalid data
        assertEquals(expectedStatus, response.getStatusCode(), "Expected status code " + expectedStatus);
        assertNotNull(response.getBody(), "Response body must not be null");
    }
}