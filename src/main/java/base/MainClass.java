package base;

import pageEvents.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class MainClass {
    
    public static void main(String[] args) {
        // Setup ChromeDriver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        
        WebDriver driver = new ChromeDriver(options);
        selfHealingLocator healer = new selfHealingLocator(driver);
        
        try {
            // Navigate to Amazon
            System.out.println("Navigating to Amazon...");
            driver.get("https://www.amazon.com");
            Thread.sleep(3000);
            
            // Step 1: Click on Sign In button
            WebElement signInButton = healer.findElement(
                By.id("nav-link-accountList"), 
                "signin"
            );
            signInButton.click();
            Thread.sleep(2000);
            
            // Step 2: Enter Email/Phone
            WebElement emailInput = healer.findElement(
                By.id("ap_email"), 
                "email"
            );
            emailInput.sendKeys("your_email@example.com");
            Thread.sleep(1000);
            
            // Step 3: Click Continue
            WebElement continueButton = healer.findElement(
                By.id("continue"), 
                "continue"
            );
            continueButton.click();
            Thread.sleep(2000);
            
            // Step 4: Enter Password
            WebElement passwordInput = healer.findElement(
                By.id("ap_password"), 
                "password"
            );
            passwordInput.sendKeys("your_password");
            Thread.sleep(1000);
            
            // Step 5: Click Sign In
            WebElement signInSubmit = healer.findElement(
                By.id("signInSubmit"), 
                "signin"
            );
            signInSubmit.click();
            Thread.sleep(3000);
            
            // Step 6: Search for iPhone 13 (INTENTIONALLY WRONG XPATH)
            System.out.println("\n========================================");
            System.out.println("TESTING SELF-HEALING WITH WRONG XPATH");
            System.out.println("========================================");
            
            WebElement searchBox = healer.findElement(
                By.xpath("//input[@id='wrong_search_box_xpath']"),  // WRONG XPATH!
                "search"
            );
            searchBox.sendKeys("iPhone 13");
            Thread.sleep(1000);
            
            // Step 7: Click Search Button (also with potentially wrong locator)
            WebElement searchButton = healer.findElement(
                By.xpath("//input[@id='wrong_search_button']"),  // WRONG XPATH!
                "search"
            );
            searchButton.click();
            Thread.sleep(3000);
            
            System.out.println("\n✓✓✓ TEST COMPLETED SUCCESSFULLY! ✓✓✓");
            System.out.println("Self-healing successfully identified and fixed broken locators!");
            
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Keep browser open for 5 seconds to see results
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {}
            driver.quit();
        }
    }
}