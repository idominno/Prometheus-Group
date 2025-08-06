package posts;

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
 * GET tests for Posts endpoint
 */
@Tag("posts")
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
    @DisplayName("GET All Posts")
    void testGetAllPosts() throws Exception {
        String endpoint = TestDataLoader.getEndpoint("posts.base");
        int expectedStatus = TestDataLoader.getStatusCode("status.success");
        
        ApiClient.ApiResponse response = apiClient.get(endpoint);
        
        assertEquals(expectedStatus, response.getStatusCode(), "Expected status code " + expectedStatus);
        
        // Validate response body is not null and not empty
        assertNotNull(response.getBody(), "Response body must not be null");
        assertFalse(response.getBody().isEmpty(), "Response body must not be empty");
        
        JsonNode jsonArray = objectMapper.readTree(response.getBody());
        assertTrue(jsonArray.isArray(), "Response must be an array");
        
        // Validate posts have expected structure if any posts exist
        if (jsonArray.size() > 0) {
            JsonNode firstPost = jsonArray.get(0);
            assertTrue(firstPost.has("id"), "Post must have id field");
            assertTrue(firstPost.has("title"), "Post must have title field");
            assertTrue(firstPost.has("body"), "Post must have body field");
            assertTrue(firstPost.has("userId"), "Post must have userId field");
        }
    }
    
    @Test
    @DisplayName("GET Specific Post")
    void testGetSpecificPost() throws Exception {
        String endpoint = TestDataLoader.getEndpoint("posts.getuser");
        int expectedStatus = TestDataLoader.getStatusCode("status.success");
        
        ApiClient.ApiResponse response = apiClient.get(endpoint);
        
        assertEquals(expectedStatus, response.getStatusCode(), "Expected status code " + expectedStatus);
        
        JsonNode jsonObject = objectMapper.readTree(response.getBody());
        assertFalse(jsonObject.isArray(), "Response must be a single object");
        
        // Validate fields are present 
        int expectedId = Integer.parseInt(endpoint.substring(endpoint.lastIndexOf('/') + 1));
        assertEquals(expectedId, jsonObject.get("id").asInt(), "Post ID must be " + expectedId);
        assertFalse(jsonObject.get("title").asText().isEmpty(), "Title must not be empty");
        assertFalse(jsonObject.get("body").asText().isEmpty(), "Body must not be empty");
        assertTrue(jsonObject.get("userId").asInt() > 0, "UserId must be positive");
    }
    
    @Test
    @DisplayName("GET Non-Existent Post")
    void testGetNonExistentPost() throws Exception {
        String endpoint = TestDataLoader.getEndpoint("posts.negativeuser");
        int expectedStatus = TestDataLoader.getStatusCode("status.notfound");
        
        ApiClient.ApiResponse response = apiClient.get(endpoint);
        
        assertEquals(expectedStatus, response.getStatusCode(), "Expected status code " + expectedStatus + " for non-existent post");
        assertNotNull(response.getBody(), "Response body must not be null");
    }
}