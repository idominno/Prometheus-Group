import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Keys;

import static org.junit.jupiter.api.Assertions.*;

/** 
 * ORIGINAL REQUIREMENTS:
 * 1. Navigate to Google
 * 2. Search for "Prometheus Group"
 * 3. Verify search results contain "Prometheus Group"
 * 4. Click on Careers link and validate 4 accordion dropdowns
 * 5. Click "View All Prometheus Jobs"
 * 6. Validate Senior Software Developer in Test (SDET) posting
 */
public class GoogleSearchTest extends BaseTest {
    
    private static final String GOOGLE_URL = "https://google.com";
    private static final String SEARCH_TERM = "Prometheus Group";
    
    @Test
    @DisplayName("Prometheus Group search and career page validation")
    public void testPrometheusGroupSearchAndCareers() {
        logger.info("Starting Prometheus Group search test");
        
        // Step 1 - Navigate to Google
        navigateToUrl(GOOGLE_URL);
        
        String pageTitle = driver.getTitle();
        logger.info("Current page title: " + pageTitle);
        assertTrue(pageTitle.toLowerCase().contains("google"), 
                   "Expected page title to contain 'Google', but was: " + pageTitle);
        
        // Step 2 - Search for "Prometheus Group"
        logger.info("Looking for Google search input field");
        WebElement searchBox = driver.findElement(By.name("q"));
        
        logger.info("Entering search term: " + SEARCH_TERM);
        searchBox.sendKeys(SEARCH_TERM);
        
        logger.info("Submitting search");
        searchBox.sendKeys(Keys.ENTER);
        
        // Step 3 - Verify search results contain "Prometheus Group"
        logger.info("Waiting for search results to load and verifying content");
        String resultsPageSource = driver.getPageSource();
        assertTrue(resultsPageSource.contains("Prometheus Group"), 
                   "Search results should contain 'Prometheus Group'");

        // TODO: Step 4 - Click on Careers link and validate accordion dropdowns
        // TODO: Step 5 - Click "View All Prometheus Jobs"
        // TODO: Step 6 - Validate Senior Software Developer in Test (SDET) posting
        
    }
}