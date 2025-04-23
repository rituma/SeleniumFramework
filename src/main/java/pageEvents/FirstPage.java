package pageEvents;
import pageObjects.HomePageElements;
import utils.ElementFetch;

public class FirstPage {
	ElementFetch ele=new ElementFetch();
	
	public void randomFlowofAmazon() {
		ele.getWebElement("XPATH", HomePageElements.ClickLogo).click();
	}
}
