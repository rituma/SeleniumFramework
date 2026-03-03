package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import base.baseTest;

public class ElementFetch {
	public WebElement getWebElement(String identifierType,String identifierValue) {
		switch(identifierType) {
		case "XPATH":
			return baseTest.driver.get().findElement(By.xpath(identifierValue));
		case "CSS":
			return baseTest.driver.get().findElement(By.cssSelector(identifierValue));
		case "class":
			return baseTest.driver.get().findElement(By.className(identifierValue));
		}
		return null;
	}

}
