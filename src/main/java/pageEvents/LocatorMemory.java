package pageEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LocatorMemory {
    private String elementName;
    private List<String> locators;
    
    public LocatorMemory(String elementName) {
        this.elementName = elementName;
        this.locators = new ArrayList<>();
    }
    
    public void addLocator(By locator) {
        String locatorStr = locator.toString();
        if (!locators.contains(locatorStr)) {
            locators.add(0, locatorStr); // Add at beginning for recency
            if (locators.size() > 5) {
                locators.remove(locators.size() - 1); // Keep only 5 most recent
            }
        }
    }
    
    public List<By> getBackupLocators() {
        return locators.stream().map(this::stringToBy).collect(Collectors.toList());
    }
    
    private By stringToBy(String locatorStr) {
        if (locatorStr.startsWith("By.id:")) {
            return By.id(locatorStr.substring(7));
        } else if (locatorStr.startsWith("By.name:")) {
            return By.name(locatorStr.substring(9));
        } else if (locatorStr.startsWith("By.className:")) {
            return By.className(locatorStr.substring(14));
        } else if (locatorStr.startsWith("By.xpath:")) {
            return By.xpath(locatorStr.substring(10));
        } else if (locatorStr.startsWith("By.cssSelector:")) {
            return By.cssSelector(locatorStr.substring(16));
        }
        return By.xpath("//*");
    }
}

// Healing Strategy Interface
interface HealingStrategy {
    String getStrategyName();
    List<WebElement> findCandidates(WebDriver driver, By originalLocator, String elementName);
}

// Strategy 1: Find by ID
class IdStrategy implements HealingStrategy {
    public String getStrategyName() { return "ID Strategy"; }
    public List<WebElement> findCandidates(WebDriver driver, By originalLocator, String elementName) {
        try {
            return driver.findElements(By.xpath("//*[contains(@id, '" + elementName.toLowerCase() + "')]"));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}

// Strategy 2: Find by Name
class NameStrategy implements HealingStrategy {
    public String getStrategyName() { return "Name Strategy"; }
    public List<WebElement> findCandidates(WebDriver driver, By originalLocator, String elementName) {
        try {
            return driver.findElements(By.xpath("//*[contains(@name, '" + elementName.toLowerCase() + "')]"));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}

// Strategy 3: Find by Class Name
class ClassNameStrategy implements HealingStrategy {
    public String getStrategyName() { return "Class Name Strategy"; }
    public List<WebElement> findCandidates(WebDriver driver, By originalLocator, String elementName) {
        try {
            return driver.findElements(By.xpath("//*[contains(@class, '" + elementName.toLowerCase() + "')]"));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}

// Strategy 4: Find by Text Content
class TextStrategy implements HealingStrategy {
    public String getStrategyName() { return "Text Content Strategy"; }
    public List<WebElement> findCandidates(WebDriver driver, By originalLocator, String elementName) {
        try {
            return driver.findElements(By.xpath("//*[contains(text(), '" + elementName + "')]"));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}

// Strategy 5: Find by Placeholder
class PlaceholderStrategy implements HealingStrategy {
    public String getStrategyName() { return "Placeholder Strategy"; }
    public List<WebElement> findCandidates(WebDriver driver, By originalLocator, String elementName) {
        try {
            return driver.findElements(By.xpath("//*[contains(@placeholder, '" + elementName + "')]"));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}

// Strategy 6: Find by Aria-Label
class AriaLabelStrategy implements HealingStrategy {
    public String getStrategyName() { return "Aria-Label Strategy"; }
    public List<WebElement> findCandidates(WebDriver driver, By originalLocator, String elementName) {
        try {
            return driver.findElements(By.xpath("//*[contains(@aria-label, '" + elementName + "')]"));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}

// Strategy 7: Find by Tag Type
class TagTypeStrategy implements HealingStrategy {
    public String getStrategyName() { return "Tag Type Strategy"; }
    public List<WebElement> findCandidates(WebDriver driver, By originalLocator, String elementName) {
        try {
            if (elementName.toLowerCase().contains("button")) {
                return driver.findElements(By.tagName("button"));
            } else if (elementName.toLowerCase().contains("input") || elementName.toLowerCase().contains("search")) {
                return driver.findElements(By.tagName("input"));
            } else if (elementName.toLowerCase().contains("link")) {
                return driver.findElements(By.tagName("a"));
            }
        } catch (Exception e) {}
        return new ArrayList<>();
    }
}

// Strategy 8: Find by Partial Attribute Match
class PartialAttributeStrategy implements HealingStrategy {
    public String getStrategyName() { return "Partial Attribute Strategy"; }
    public List<WebElement> findCandidates(WebDriver driver, By originalLocator, String elementName) {
        List<WebElement> candidates = new ArrayList<>();
        try {
            candidates.addAll(driver.findElements(By.xpath("//*[contains(@id, 'search')]")));
            candidates.addAll(driver.findElements(By.xpath("//*[contains(@name, 'search')]")));
            candidates.addAll(driver.findElements(By.xpath("//*[contains(@type, 'search')]")));
        } catch (Exception e) {}
        return candidates;
    }
}

// Strategy 9: CSS Selector Strategy
class CssSelectorStrategy implements HealingStrategy {
    public String getStrategyName() { return "CSS Selector Strategy"; }
    public List<WebElement> findCandidates(WebDriver driver, By originalLocator, String elementName) {
        try {
            return driver.findElements(By.cssSelector("input[type='text'], input[type='search']"));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}

// Strategy 10: Relative Position Strategy
class RelativePositionStrategy implements HealingStrategy {
    public String getStrategyName() { return "Relative Position Strategy"; }
    public List<WebElement> findCandidates(WebDriver driver, By originalLocator, String elementName) {
        try {
            // Find elements in header or search bar areas
            List<WebElement> candidates = new ArrayList<>();
            candidates.addAll(driver.findElements(By.xpath("//header//input")));
            candidates.addAll(driver.findElements(By.xpath("//nav//input")));
            candidates.addAll(driver.findElements(By.xpath("//*[@id='nav-search']//input")));
            return candidates;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}