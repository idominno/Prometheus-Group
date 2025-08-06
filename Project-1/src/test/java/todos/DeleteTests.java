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
 * DELETE tests for Todos endpoint
 */
@Tag("todos")
@Tag("delete")
public class DeleteTests {
    
    private ApiClient apiClient;
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setUp() {
        apiClient = new ApiClient();
        objectMapper = new ObjectMapper();
    }
    
    @Test
    @DisplayName("DELETE Todo")
    void testDeleteTodo() throws Exception {
        String endpoint = TestDataLoader.getEndpoint("todos.deleteuser");
        int expectedStatus = TestDataLoader.getStatusCode("status.success");
        
        ApiClient.ApiResponse response = apiClient.delete(endpoint);
        
        assertEquals(expectedStatus, response.getStatusCode(), "Expected status code " + expectedStatus);
        
        // JSONPlaceholder returns an empty object {} for successful deletions
        assertNotNull(response.getBody(), "Response body must not be null");
        
        // Validate response is valid JSON (even if empty)
        if (!response.getBody().trim().isEmpty()) {
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            assertNotNull(jsonResponse, "Response must be valid JSON");
        }
    }
    
    @Test
    @DisplayName("DELETE Non-Existent Todo")
    void testDeleteNonExistentTodo() throws Exception {
        String endpoint = TestDataLoader.getEndpoint("todos.negativeuser");
        int expectedStatus = TestDataLoader.getStatusCode("status.success");
        
        ApiClient.ApiResponse response = apiClient.delete(endpoint);
        
        // Note: JSONPlaceholder API returns 200 for DELETE operations on both existing and non-existent resources
        // Typically we would expect a 404 for non-existent resources
        assertEquals(expectedStatus, response.getStatusCode(), "Expected status code " + expectedStatus + " for non-existent todo");
        assertNotNull(response.getBody(), "Response body must not be null");
    }
}