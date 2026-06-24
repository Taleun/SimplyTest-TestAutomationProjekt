package simplyTest.autoprojekt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.bonigarcia.wdm.WebDriverManager;
import simplyTest.autoprojekt.entities.Shop;
import simplyTest.autoprojekt.webAppPages.ShoppingPage;

public class BaseForTests {

	protected WebDriver driver;
	protected ShoppingPage shoppingPage;
	protected ExtentReports extent;
	protected ExtentSparkReporter reporter;
	
		
	private WebDriver initializeDriver () throws IOException {
		System.out.println("-------------- Driver initialization starts. -----------------");
		
		Shop.instantiateShopItems();
		
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "\\src\\main\\resources\\GlobalData.properties");
		prop.load(fis);
		String browserToUse = prop.getProperty("browser");
		switch(browserToUse) 
		{
			case "chrome" :			
				WebDriverManager.chromedriver().setup();
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--incognito", "--start-maximized");
				driver = new ChromeDriver(options);
				System.out.println("------------ The chrome driver has been set up ------------");
				break;			
			case "edge" :
				WebDriverManager.edgedriver().setup();
				driver = new EdgeDriver();
				System.out.println("------------ The edge driver has been set up ------------");
				break;
			default :
				System.out.println("No expected browser name has been received. Check in the properties file.");
		}
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));	
		driver.manage().window().maximize();
		
		
		return driver;
	}
	
	//Extent = Umfang, Ausmaß, usw. Report = Bericht. Extent report = umfangreicher Bericht
	@BeforeTest(alwaysRun = true)
	public void config() {
		if(reporter != null)
		{
			reporter = new ExtentSparkReporter(System.getProperty("user.dir")+"\\reports\\autoprojekt.html");
			reporter.config().setDocumentTitle("SimplyTest Autoprojekt Test results");
			reporter.config().setReportName("Test Results");
		}
		extent = new ExtentReports();
		extent.attachReporter(reporter);
		extent.setSystemInfo("Tester", "Max Mustermann");
	}
	
	
	@BeforeMethod(alwaysRun = true)
	public void startApplikation() throws IOException {
		driver = initializeDriver();
		shoppingPage = new ShoppingPage(driver);
		shoppingPage = shoppingPage.loadShoppingPage();		
	}	
	
	
	@AfterMethod(alwaysRun = true)
	public void tearDown() throws InterruptedException {
//		Thread.sleep(3000);
		if(driver != null) {
			driver.quit();
		}
	}
	
	
	@DataProvider(name = "internDataDrivenGetItemNames")
	public String[] getData() {
		return new String[] {"Album", "Beanie", "Beanie with Logo",	"Belt", "Hoodie with Zipper", "Long Sleeve Tee", "Polo", "Sunglasses", "T-Shirt", "T-Shirt with Logo"};
	}
	
	@DataProvider(name = "externDataDrivenGetItemNames")
	public String[] getExternJsonItemNames() throws IOException {
		List<String> listOfItemNames = getJSONData(System.getProperty("user.dir")+"\\src\\test\\resources\\testdata\\itemName.json");
		return listOfItemNames.toArray(new String[listOfItemNames.size()]);
	}
	
	//This function captures screenshots. It should be called when a test case failed.
	public void takeScreenshot(String testcaseName) throws IOException {
		//First cast the driver to the Typ able to take screenshots
		TakesScreenshot ts = (TakesScreenshot) driver;
		//Take the screenshot and save it to a well defined local memory place
		File screenshotSrcFile = ts.getScreenshotAs(OutputType.FILE);
		String screenshotFilepath = System.getProperty("user.dir") + "\\reports\\" + testcaseName + ".png";
		File screenshotDstFile = new File(screenshotFilepath);
		FileUtils.copyFile(screenshotSrcFile, screenshotDstFile);
		System.out.println("A screenshot has been snapped and saved in: " + screenshotFilepath);
	}
	
	// Diese Methode erhöht die Abstraktion und Wiederverwendbarkeit, in dem Testdaten aus anderen JSON-Dateien eingescannt und verarbeitet werden.
	private List<String> getJSONData(String filePath) throws IOException {
		//1. step: Scan the json file
		String jsonContent = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);
		//2. step: Map the file contain in a suitable data structure
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(jsonContent);
		List<String> itemNames = mapper.readValue(root.get("itemNames").toString(), new TypeReference<List<String>>() {});
		//3. step: Return the data so that they can provide the test methods
		return itemNames;
	}
}
