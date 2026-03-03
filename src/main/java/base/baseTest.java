package base;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import utils.Contants;

public class baseTest {

	public ExtentSparkReporter sparkReporter;
	public ExtentReports extent;
	public ExtentTest logger;
	public static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	public static String browser;
    public static String gridURL= "http://192.168.1.11:4445";
	@BeforeTest
	public void beforeTestMethod() {
		sparkReporter = new ExtentSparkReporter(
				System.getProperty("user.dir") + File.separator + "reports" + File.separator + "SeleniumReports");
		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);
		sparkReporter.config().setTheme(Theme.DARK);
		extent.setSystemInfo("HostName", "");
		extent.setSystemInfo("Username", "");
		sparkReporter.config().setDocumentTitle("Automation Report");
		sparkReporter.config().setReportName("AmazonWebSiteReport");
	}

	@BeforeMethod
	@Parameters("browser")
	public void beforeMethodMethod(Method testMethod,String browser) throws MalformedURLException, URISyntaxException {
         logger = extent.createTest(testMethod.getName());
         baseTest.setupDriver(browser);
         //baseTest.setupDriverCapability(browser);
	}

	@AfterMethod
	public void afterMethod(ITestResult result) {
		if(result.getStatus()==ITestResult.FAILURE) {
			logger.log(Status.FAIL, MarkupHelper.createLabel(result.getName()+"Test case failed",ExtentColor.RED ));
		}
		else if(result.getStatus()==ITestResult.SUCCESS) {
			logger.log(Status.PASS, MarkupHelper.createLabel(result.getName()+"Test case success",ExtentColor.GREEN ));	
		}
		else if(result.getStatus()==ITestResult.SKIP) {
			logger.log(Status.SKIP, MarkupHelper.createLabel(result.getName()+"Test case skip",ExtentColor.AMBER));	
		}
		driver.get().quit();
	}
	
	@AfterTest
	public void afterTestMethod() {
		extent.flush();
	}

	public static void setupDriver(String browser) {
		if (browser.equalsIgnoreCase("firefox")) {
			driver.set(new FirefoxDriver());
		} else if (browser.equalsIgnoreCase("chrome")) {
			driver.set(new ChromeDriver());
		}
		else if(browser.equalsIgnoreCase("safari")) {
			driver.set(new SafariDriver());
		}
	}
	
	public static void setupDriverCapability(String browser) throws MalformedURLException, URISyntaxException {
		if(browser.equalsIgnoreCase("chrome")){
		    ChromeOptions options = new ChromeOptions();
		    driver.set(new RemoteWebDriver(new URI(gridURL).toURL(), options));
		}
		else if(browser.equalsIgnoreCase("safari")){
		    SafariOptions options = new SafariOptions();
		    driver.set(new RemoteWebDriver(new URI(gridURL).toURL(), options));
		}
	}
}
