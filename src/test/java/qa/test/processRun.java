package qa.test;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import base.baseTest;
import pageEvents.FirstPage;

public class processRun extends baseTest{
	FirstPage page=new FirstPage();
  @Parameters({"browser"})
  @Test
  public void goToAmazonWithChrome(String browser) throws MalformedURLException, URISyntaxException {
	  page.randomFlowofAmazon(browser);
  }
  
  @Test
  @Parameters({"browser"})
  public void goToAmazonWithSafari(String browser) throws MalformedURLException, URISyntaxException {
	  page.randomFlowofAmazon(browser);
  }
}
