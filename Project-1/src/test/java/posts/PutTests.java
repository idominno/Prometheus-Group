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
 * PUT tests for Posts endpoint
 */
@Tag("posts")
@Tag("put")
public class PutTests {
    
    private ApiClient apiClient;
    private ObjectMapper objectMapper;
    private JsonNode postsTestData;
    
    @BeforeEach
    void setUp() {
        apiClient = new ApiClient();
        objectMapper = new ObjectMapper();
        postsTestData = TestDataLoader.getPostUpdateData();
    }
    
    @Test
    @DisplayName("PUT Update Post")
    void testUpdatePost() throws Exception {
        JsonNode testPost = TestDataLoader.getTestDataItem(postsTestData, 0);
        String endpoint = TestDataLoader.getEndpoint("posts.base") + "/" + testPost.get("id").asInt();
        int expectedStatus = TestDataLoader.getStatusCode("status.success");
        String requestBody = objectMapper.writeValueAsString(testPost);
        
        ApiClient.ApiResponse response = apiClient.put(endpoint, requestBody);
        
        assertEquals(expectedStatus, response.getStatusCode(), "Expected status code " + expectedStatus);
        
        JsonNode jsonResponse = objectMapper.readTree(response.getBody());
        
        // Validate all expected fields are present
        assertTrue(jsonResponse.has("id"), "Response must have id field");
        assertTrue(jsonResponse.has("title"), "Response must have title field");
        assertTrue(jsonResponse.has("body"), "Response must have body field");
        assertTrue(jsonResponse.has("userId"), "Response must have userId field");
        
        // Validate all fields were updated
        assertEquals(testPost.get("title").asText(), jsonResponse.get("title").asText(), 
            "Title must match test data");
        assertEquals(testPost.get("body").asText(), jsonResponse.get("body").asText(), 
            "Body must match test data");
        assertEquals(testPost.get("userId").asInt(), jsonResponse.get("userId").asInt(), 
            "UserId must match test data");
        
        // Validate the ID is preserved 
        assertEquals(testPost.get("id").asInt(), jsonResponse.get("id").asInt(), "Post ID must be preserved");
    }
    
    @Test
    @DisplayName("PUT Invalid Post")
    void testUpdatePostNegative() throws Exception {
        JsonNode negativeTestData = TestDataLoader.getInvalidPostUpdateData();
        JsonNode testPost = TestDataLoader.getTestDataItem(negativeTestData, 0);
        String endpoint = TestDataLoader.getEndpoint("posts.base") + "/" + testPost.get("id").asInt();
        int expectedStatus = TestDataLoader.getStatusCode("status.internalerror");
        String requestBody = objectMapper.writeValueAsString(testPost);
        
        ApiClient.ApiResponse response = apiClient.put(endpoint, requestBody);
        
        assertEquals(expectedStatus, response.getStatusCode(), "Expected status code " + expectedStatus + " for invalid post ID");
        assertNotNull(response.getBody(), "Response body must not be null");
    }
}