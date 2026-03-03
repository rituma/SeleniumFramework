package pageEvents;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.time.Duration;

import base.baseTest;
import pageObjects.HomePageElements;
import utils.Contants;
import utils.ElementFetch;

public class FirstPage {
	ElementFetch ele=new ElementFetch();
	
	public void randomFlowofAmazon(String browser) throws MalformedURLException, URISyntaxException {
		baseTest.driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        baseTest.driver.get().get(Contants.url);
        baseTest.driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        baseTest.driver.get().switchTo().defaultContent();
		ele.getWebElement("XPATH", HomePageElements.ClickLogo).click();
		
	}
}
