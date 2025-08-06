package todos;

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
 * GET tests for Todos endpoint
 */
@Tag("todos")
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
    @DisplayName("GET All Todos")
    void testGetAllTodos() throws Exception {
        String endpoint = TestDataLoader.getEndpoint("todos.base");
        int expectedStatus = TestDataLoader.getStatusCode("status.success");
        
        ApiClient.ApiResponse response = apiClient.get(endpoint);
        
        assertEquals(expectedStatus, response.getStatusCode(), "Expected status code " + expectedStatus);
        
        // Validate response body is not null and not empty
        assertNotNull(response.getBody(), "Response body must not be null");
        assertFalse(response.getBody().isEmpty(), "Response body must not be empty");
        
        JsonNode jsonArray = objectMapper.readTree(response.getBody());
        assertTrue(jsonArray.isArray(), "Response must be an array");
        
        // Validate todos have expected structure if any todos exist
        if (jsonArray.size() > 0) {
            JsonNode firstTodo = jsonArray.get(0);
            assertTrue(firstTodo.has("id"), "Todo must have id field");
            assertTrue(firstTodo.has("title"), "Todo must have title field");
            assertTrue(firstTodo.has("completed"), "Todo must have completed field");
            assertTrue(firstTodo.has("userId"), "Todo must have userId field");
        }
    }
    
    @Test
    @DisplayName("GET Specific Todo")
    void testGetSpecificTodo() throws Exception {
        String endpoint = TestDataLoader.getEndpoint("todos.getuser");
        int expectedStatus = TestDataLoader.getStatusCode("status.success");
        
        ApiClient.ApiResponse response = apiClient.get(endpoint);
        
        assertEquals(expectedStatus, response.getStatusCode(), "Expected status code " + expectedStatus);
        
        JsonNode jsonObject = objectMapper.readTree(response.getBody());
        assertFalse(jsonObject.isArray(), "Response must be a single object");
        
        // Validate fields are present 
        int expectedId = Integer.parseInt(endpoint.substring(endpoint.lastIndexOf('/') + 1));
        assertEquals(expectedId, jsonObject.get("id").asInt(), "Todo ID must be " + expectedId);
        assertFalse(jsonObject.get("title").asText().isEmpty(), "Title must not be empty");
        assertNotNull(jsonObject.get("completed"), "Completed must not be null");
        assertTrue(jsonObject.get("userId").asInt() > 0, "UserId must be positive");
    }
    
    @Test
    @DisplayName("GET Non-Existent Todo")
    void testGetNonExistentTodo() throws Exception {
        String endpoint = TestDataLoader.getEndpoint("todos.negativeuser");
        int expectedStatus = TestDataLoader.getStatusCode("status.notfound");
        
        ApiClient.ApiResponse response = apiClient.get(endpoint);
        
        assertEquals(expectedStatus, response.getStatusCode(), "Expected status code " + expectedStatus + " for non-existent todo");
        assertNotNull(response.getBody(), "Response body must not be null");
    }
}