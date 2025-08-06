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
 * PUT tests for Todos endpoint
 */
@Tag("todos")
@Tag("put")
public class PutTests {
    
    private ApiClient apiClient;
    private ObjectMapper objectMapper;
    private JsonNode todosTestData;
    
    @BeforeEach
    void setUp() {
        apiClient = new ApiClient();
        objectMapper = new ObjectMapper();
        todosTestData = TestDataLoader.getTodoUpdateData();
    }
    
    @Test
    @DisplayName("PUT Update Todo")
    void testUpdateTodo() throws Exception {
        JsonNode testTodo = TestDataLoader.getTestDataItem(todosTestData, 0);
        String endpoint = TestDataLoader.getEndpoint("todos.base") + "/" + testTodo.get("id").asInt();
        int expectedStatus = TestDataLoader.getStatusCode("status.success");
        String requestBody = objectMapper.writeValueAsString(testTodo);
        
        ApiClient.ApiResponse response = apiClient.put(endpoint, requestBody);
        
        assertEquals(expectedStatus, response.getStatusCode(), "Expected status code " + expectedStatus);
        
        JsonNode jsonResponse = objectMapper.readTree(response.getBody());
        
        // Validate all expected fields are present
        assertTrue(jsonResponse.has("id"), "Response must have id field");
        assertTrue(jsonResponse.has("title"), "Response must have title field");
        assertTrue(jsonResponse.has("completed"), "Response must have completed field");
        assertTrue(jsonResponse.has("userId"), "Response must have userId field");
        
        // Validate other fields were updated
        assertEquals(testTodo.get("title").asText(), jsonResponse.get("title").asText(), 
            "Title must match test data");
        assertEquals(testTodo.get("completed").asBoolean(), jsonResponse.get("completed").asBoolean(), 
            "Completed must match test data");
        assertEquals(testTodo.get("userId").asInt(), jsonResponse.get("userId").asInt(), 
            "UserId must match test data");
        
        // Validate the ID is preserved
        assertEquals(testTodo.get("id").asInt(), jsonResponse.get("id").asInt(), "Todo ID must be preserved");
    }
    
    @Test
    @DisplayName("PUT Invalid Todo")
    void testUpdateTodoNegative() throws Exception {
        JsonNode negativeTestData = TestDataLoader.getInvalidTodoUpdateData();
        JsonNode testTodo = TestDataLoader.getTestDataItem(negativeTestData, 0);
        String endpoint = TestDataLoader.getEndpoint("todos.base") + "/" + testTodo.get("id").asInt();
        int expectedStatus = TestDataLoader.getStatusCode("status.internalerror");
        String requestBody = objectMapper.writeValueAsString(testTodo);
        
        ApiClient.ApiResponse response = apiClient.put(endpoint, requestBody);
        
        assertEquals(expectedStatus, response.getStatusCode(), "Expected status code " + expectedStatus + " for invalid todo ID");
        assertNotNull(response.getBody(), "Response body must not be null");
    }
}