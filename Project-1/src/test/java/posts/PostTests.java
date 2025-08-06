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
 * POST tests for Posts endpoint
 */
@Tag("posts")
@Tag("post")
public class PostTests {
    
    private ApiClient apiClient;
    private ObjectMapper objectMapper;
    private JsonNode postsTestData;
    
    @BeforeEach
    void setUp() {
        apiClient = new ApiClient();
        objectMapper = new ObjectMapper();
        postsTestData = TestDataLoader.getPostCreationData();
    }
    
    @Test
    @DisplayName("POST New Post")
    void testCreatePost() throws Exception {
        String endpoint = TestDataLoader.getEndpoint("posts.base");
        int expectedStatus = TestDataLoader.getStatusCode("status.created");
        
        JsonNode testPost = TestDataLoader.getTestDataItem(postsTestData, 0);
        String requestBody = objectMapper.writeValueAsString(testPost);
        
        ApiClient.ApiResponse response = apiClient.post(endpoint, requestBody);
        
        assertEquals(expectedStatus, response.getStatusCode(), "Expected status code " + expectedStatus);
        
        JsonNode jsonResponse = objectMapper.readTree(response.getBody());
        
        // Validate response has expected fields
        assertTrue(jsonResponse.has("id"), "Response must have id field");
        assertTrue(jsonResponse.has("title"), "Response must have title field");
        assertTrue(jsonResponse.has("body"), "Response must have body field");
        assertTrue(jsonResponse.has("userId"), "Response must have userId field");
        
        // Validate that created post has an ID
        assertTrue(jsonResponse.get("id").asInt() > 0, "Created post ID must be positive");
        
        // Validate other fields match test data
        assertEquals(testPost.get("title").asText(), jsonResponse.get("title").asText(), 
            "Title must match test data");
        assertEquals(testPost.get("body").asText(), jsonResponse.get("body").asText(), 
            "Body must match test data");
        assertEquals(testPost.get("userId").asInt(), jsonResponse.get("userId").asInt(), 
            "UserId must match test data");
    }
    
    @Test
    @DisplayName("POST New Invalid Post")
    void testCreatePostNegative() throws Exception {
        String endpoint = TestDataLoader.getEndpoint("posts.base");
        int expectedStatus = TestDataLoader.getStatusCode("status.created");
        
        JsonNode negativeTestData = TestDataLoader.getInvalidPostCreationData();
        JsonNode testPost = TestDataLoader.getTestDataItem(negativeTestData, 0);
        String requestBody = objectMapper.writeValueAsString(testPost);
        
        ApiClient.ApiResponse response = apiClient.post(endpoint, requestBody);
        
        // Note: JSONPlaceholder API returns 201 even for invalid data
        // Typically we would expect a 400 or 422 for invalid data
        assertEquals(expectedStatus, response.getStatusCode(), "Expected status code " + expectedStatus);
        assertNotNull(response.getBody(), "Response body must not be null");
    }
}