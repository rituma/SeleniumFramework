package qa.test;

import org.testng.annotations.Test;
import base.baseTest;
import pageEvents.FirstPage;

public class processRun extends baseTest{
	FirstPage page=new FirstPage();
  @Test
  public void goToAmazon() {
	  page.randomFlowofAmazon();
  }
}
