import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Base test class that provides common WebDriver setup and teardown functionality.
 */
public abstract class BaseTest {
    
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected WebDriver driver;
    
    /**
     * Uses WebDriverManager to automatically handle Chrome driver setup.
     */
    @BeforeEach
    public void setUp() {
        logger.info("Setting up WebDriver for test execution");
        
        try {
            WebDriverManager.chromedriver().setup();
            
            driver = new ChromeDriver();
            
            driver.manage().window().maximize();
            
            logger.info("WebDriver setup completed successfully");
            
        } catch (Exception e) {
            logger.error("Failed to setup WebDriver: " + e.getMessage(), e);
            throw new RuntimeException("WebDriver setup failed", e);
        }
    }
    
    @AfterEach
    public void tearDown() {
        if (driver != null) {
            logger.info("Closing WebDriver");
            try {
                driver.quit();
                logger.info("WebDriver closed successfully");
            } catch (Exception e) {
                logger.error("Error closing WebDriver: " + e.getMessage(), e);
            }
        }
    }
    
    /**
     * Helper method to navigate to URL with logging
     */
    protected void navigateToUrl(String url) {
        logger.info("Navigating to URL: " + url);
        driver.get(url);
        logger.info("Successfully navigated to: " + driver.getCurrentUrl());
    }
}