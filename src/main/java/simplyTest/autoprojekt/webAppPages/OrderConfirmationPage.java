package simplyTest.autoprojekt.webAppPages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class OrderConfirmationPage extends MenuHeaders{

	 	
	public OrderConfirmationPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath = "//h1[text()='Order received']")
	WebElement lblOrderConfirmationMsg;
	
	
	public WebElement getLblOrderConfirmationMsg() {
		return lblOrderConfirmationMsg;
	}


	public void setLblOrderConfirmationMsg(WebElement lblOrderConfirmationMsg) {
		this.lblOrderConfirmationMsg = lblOrderConfirmationMsg;
	}


	public boolean isOrderConfirmationPage() {
		return this.lblOrderConfirmationMsg.isDisplayed(); 
	}
}
