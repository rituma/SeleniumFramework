package pageEvents;

import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class selfHealingLocator{
    private WebDriver driver;
    private WebDriverWait wait;
    private Map<String, LocatorMemory> locatorMemory;
    private static final String MEMORY_FILE = "locator_memory.json";
    private Gson gson;
    
    public selfHealingLocator(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.gson = new Gson();
        this.locatorMemory = loadMemory();
    }
    
    // Main method to find element with self-healing
    public WebElement findElement(By locator, String elementName) {
        System.out.println("\n=== Attempting to find: " + elementName + " ===");
        System.out.println("Primary locator: " + locator.toString());
        
        // Try primary locator
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            System.out.println("✓ SUCCESS: Found element using primary locator");
            saveSuccessfulLocator(elementName, locator);
            return element;
        } catch (TimeoutException e) {
            System.out.println("✗ FAILED: Primary locator failed");
        }
        
        // Try backup locators from memory
        if (locatorMemory.containsKey(elementName)) {
            System.out.println("→ Trying backup locators from memory...");
            List<By> backupLocators = locatorMemory.get(elementName).getBackupLocators();
            for (By backup : backupLocators) {
                try {
                    WebElement element = driver.findElement(backup);
                    if (element.isDisplayed()) {
                        System.out.println("✓ SUCCESS: Found using backup locator: " + backup.toString());
                        return element;
                    }
                } catch (Exception ex) {
                    continue;
                }
            }
        }
        
        // Initiate AI-powered self-healing
        System.out.println("→ INITIATING AI SELF-HEALING...");
        WebElement healedElement = healLocator(locator, elementName);
        
        if (healedElement != null) {
            System.out.println("✓ SUCCESS: Self-healing found the element!");
            return healedElement;
        } else {
            System.out.println("✗ FAILED: Self-healing could not find element");
            throw new NoSuchElementException("Element not found: " + elementName);
        }
    }
    
    // AI-powered healing strategies
    private WebElement healLocator(By originalLocator, String elementName) {
        List<HealingStrategy> strategies = Arrays.asList(
            new IdStrategy(),
            new NameStrategy(),
            new ClassNameStrategy(),
            new TextStrategy(),
            new PlaceholderStrategy(),
            new AriaLabelStrategy(),
            new TagTypeStrategy(),
            new PartialAttributeStrategy(),
            new CssSelectorStrategy(),
            new RelativePositionStrategy()
        );
        
        for (HealingStrategy strategy : strategies) {
            try {
                System.out.println("  Trying strategy: " + strategy.getStrategyName());
                List<WebElement> candidates = strategy.findCandidates(driver, originalLocator, elementName);
                
                if (!candidates.isEmpty()) {
                    WebElement bestMatch = scoreCandidates(candidates, elementName);
                    if (bestMatch != null) {
                        By newLocator = extractLocator(bestMatch);
                        System.out.println("  → Found match with: " + strategy.getStrategyName());
                        System.out.println("  → New locator: " + newLocator.toString());
                        saveSuccessfulLocator(elementName, newLocator);
                        return bestMatch;
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }
        return null;
    }
    
    // Score candidates based on AI similarity analysis
    private WebElement scoreCandidates(List<WebElement> candidates, String elementName) {
        Map<WebElement, Double> scores = new HashMap<>();
        
        for (WebElement element : candidates) {
            try {
                double score = 0.0;
                
                // Check visibility (high priority)
                if (element.isDisplayed()) score += 5.0;
                if (element.isEnabled()) score += 3.0;
                
                // Check text similarity
                String text = element.getText();
                if (text != null && !text.isEmpty()) {
                    if (elementName.toLowerCase().contains(text.toLowerCase()) ||
                        text.toLowerCase().contains(elementName.toLowerCase())) {
                        score += 4.0;
                    }
                }
                
                // Check attributes
                String id = element.getDomAttribute("id");
                String name = element.getDomAttribute("name");
                String className = element.getDomAttribute("class");
                String placeholder = element.getDomAttribute("placeholder");
                String ariaLabel = element.getDomAttribute("aria-label");
                
                if (containsKeyword(id, elementName)) score += 4.0;
                if (containsKeyword(name, elementName)) score += 4.0;
                if (containsKeyword(className, elementName)) score += 2.0;
                if (containsKeyword(placeholder, elementName)) score += 3.0;
                if (containsKeyword(ariaLabel, elementName)) score += 3.0;
                
                // Check tag relevance
                String tagName = element.getTagName();
                if (isRelevantTag(tagName, elementName)) score += 2.0;
                
                scores.put(element, score);
            } catch (Exception e) {
                continue;
            }
        }
        
        // Return element with highest score (minimum threshold: 3.0)
        return scores.entrySet().stream()
            .filter(entry -> entry.getValue() >= 3.0)
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }
    
    private boolean containsKeyword(String attribute, String keyword) {
        if (attribute == null || keyword == null) return false;
        return attribute.toLowerCase().contains(keyword.toLowerCase()) ||
               keyword.toLowerCase().contains(attribute.toLowerCase());
    }
    
    private boolean isRelevantTag(String tagName, String elementName) {
        String lower = elementName.toLowerCase();
        if (lower.contains("button") && tagName.equals("button")) return true;
        if (lower.contains("input") && tagName.equals("input")) return true;
        if (lower.contains("link") && tagName.equals("a")) return true;
        if (lower.contains("text") && tagName.equals("input")) return true;
        return false;
    }
    
    // Extract best locator from element
    private By extractLocator(WebElement element) {
        String id = element.getDomAttribute("id");
        if (id != null && !id.isEmpty()) {
            return By.id(id);
        }
        
        String name = element.getDomAttribute("name");
        if (name != null && !name.isEmpty()) {
            return By.name(name);
        }
        
        String className = element.getDomAttribute("class");
        if (className != null && !className.isEmpty()) {
            return By.className(className.split(" ")[0]);
        }
        
        return By.xpath(generateXPath(element));
    }
    
    private String generateXPath(WebElement element) {
        return (String) ((JavascriptExecutor) driver).executeScript(
            "function getXPath(element) {" +
            "  if (element.id !== '') return '//*[@id=\"' + element.id + '\"]';" +
            "  if (element === document.body) return '/html/body';" +
            "  var ix = 0;" +
            "  var siblings = element.parentNode.childNodes;" +
            "  for (var i = 0; i < siblings.length; i++) {" +
            "    var sibling = siblings[i];" +
            "    if (sibling === element) return getXPath(element.parentNode) + '/' + element.tagName.toLowerCase() + '[' + (ix + 1) + ']';" +
            "    if (sibling.nodeType === 1 && sibling.tagName === element.tagName) ix++;" +
            "  }" +
            "}" +
            "return getXPath(arguments[0]);", element);
    }
    
    // Memory management
    private void saveSuccessfulLocator(String elementName, By locator) {
        if (!locatorMemory.containsKey(elementName)) {
            locatorMemory.put(elementName, new LocatorMemory(elementName));
        }
        locatorMemory.get(elementName).addLocator(locator);
        saveMemory();
    }
    
    private Map<String, LocatorMemory> loadMemory() {
        try {
            File file = new File(MEMORY_FILE);
            if (file.exists()) {
                Reader reader = new FileReader(file);
                Map<String, LocatorMemory> memory = gson.fromJson(reader, 
                    new TypeToken<Map<String, LocatorMemory>>(){}.getType());
                reader.close();
                return memory != null ? memory : new HashMap<>();
            }
        } catch (Exception e) {
            System.out.println("Could not load memory file: " + e.getMessage());
        }
        return new HashMap<>();
    }
    
    private void saveMemory() {
        try {
            Writer writer = new FileWriter(MEMORY_FILE);
            gson.toJson(locatorMemory, writer);
            writer.close();
        } catch (Exception e) {
            System.out.println("Could not save memory file: " + e.getMessage());
        }
    }
}
