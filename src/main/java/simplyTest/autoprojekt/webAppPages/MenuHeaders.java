package simplyTest.autoprojekt.webAppPages;

import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MenuHeaders {

	
	protected WebDriver driver;
	
	public MenuHeaders (WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
		
	@FindBy(xpath = "//header[@id='masthead']//ul[@class='nav-menu']//a[text()='Home']")
	WebElement btnHomeMenu;
	
	@FindBy(xpath = "//header[@id='masthead']//ul[@class='nav-menu']//a[text()='Cart']")
	WebElement btnCartMenu;
	
	@FindBy(xpath = "//header[@id='masthead']//ul[@class='nav-menu']//a[contains(text(),'Checkout')]")
	WebElement btnCheckoutMenu;
	
	
	
	public WebDriver getDriver() {
		return driver;
	}



	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}



	public WebElement getBtnHomeMenu() {
		return btnHomeMenu;
	}



	public void setBtnHomeMenu(WebElement btnHomeMenu) {
		this.btnHomeMenu = btnHomeMenu;
	}



	public WebElement getBtnCartMenu() {
		return btnCartMenu;
	}



	public void setBtnCartMenu(WebElement btnCartMenu) {
		this.btnCartMenu = btnCartMenu;
	}



	public WebElement getBtnCheckoutMenu() {
		return btnCheckoutMenu;
	}



	public void setBtnCheckoutMenu(WebElement btnCheckoutMenu) {
		this.btnCheckoutMenu = btnCheckoutMenu;
	}



	public void waitForPageToBeReady(WebDriver driver) {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	  
	    wait.until(webDriver -> ((JavascriptExecutor) webDriver)
	            .executeScript("return document.readyState").equals("complete"));

	    wait.until(webDriver -> (Boolean) ((JavascriptExecutor) webDriver)
	            .executeScript("return (window.jQuery == null) || (jQuery.active === 0);"));
	}
	
}
