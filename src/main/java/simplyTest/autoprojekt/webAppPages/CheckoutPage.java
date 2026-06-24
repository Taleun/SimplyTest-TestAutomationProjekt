package simplyTest.autoprojekt.webAppPages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CheckoutPage extends MenuHeaders{

	
	
	public CheckoutPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath = "//header/h1")
	WebElement lblHeadlineCheckout;
	
	@FindBy(id = "billing_first_name")
	WebElement inputFirstName;
	
	@FindBy(id = "billing_last_name")
	WebElement inputLastName;
	
	@FindBy(id = "select2-billing_country-container")
	WebElement selectCountryArrow;
	
	@FindBy(css = "input.select2-search__field")
	WebElement inputCountry;
	
	@FindBy(id = "billing_address_1")
	WebElement inputAddress;
	
	@FindBy(id = "billing_postcode")
	WebElement inputPostcode;
	
	@FindBy(id = "billing_city")
	WebElement inputCity;
	
	@FindBy(id = "billing_phone")
	WebElement inputPhoneNumber;
	
	@FindBy(id = "billing_email")
	WebElement inputEmail;
	
	@FindBy(id = "order_comments")
	WebElement inputComment;
	
	@FindBy(id = "place_order")
	WebElement btnPlaceOrder;
	
	
	
	public WebElement getLblHeadlineCheckout() {
		return lblHeadlineCheckout;
	}

	public void setLblHeadlineCheckout(WebElement lblHeadlineCheckout) {
		this.lblHeadlineCheckout = lblHeadlineCheckout;
	}

	public WebElement getInputFirstName() {
		return inputFirstName;
	}

	public void setInputFirstName(WebElement inputFirstName) {
		this.inputFirstName = inputFirstName;
	}

	public WebElement getInputLastName() {
		return inputLastName;
	}

	public void setInputLastName(WebElement inputLastName) {
		this.inputLastName = inputLastName;
	}

	public WebElement getSelectCountryArrow() {
		return selectCountryArrow;
	}

	public void setSelectCountryArrow(WebElement selectCountryArrow) {
		this.selectCountryArrow = selectCountryArrow;
	}

	public WebElement getInputCountry() {
		return inputCountry;
	}

	public void setInputCountry(WebElement inputCountry) {
		this.inputCountry = inputCountry;
	}

	public WebElement getInputAddress() {
		return inputAddress;
	}

	public void setInputAddress(WebElement inputAddress) {
		this.inputAddress = inputAddress;
	}

	public WebElement getInputPostcode() {
		return inputPostcode;
	}

	public void setInputPostcode(WebElement inputPostcode) {
		this.inputPostcode = inputPostcode;
	}

	public WebElement getInputCity() {
		return inputCity;
	}

	public void setInputCity(WebElement inputCity) {
		this.inputCity = inputCity;
	}

	public WebElement getInputPhoneNumber() {
		return inputPhoneNumber;
	}

	public void setInputPhoneNumber(WebElement inputPhoneNumber) {
		this.inputPhoneNumber = inputPhoneNumber;
	}

	public WebElement getInputEmail() {
		return inputEmail;
	}

	public void setInputEmail(WebElement inputEmail) {
		this.inputEmail = inputEmail;
	}

	public WebElement getInputComment() {
		return inputComment;
	}

	public void setInputComment(WebElement inputComment) {
		this.inputComment = inputComment;
	}

	public WebElement getBtnPlaceOrder() {
		return btnPlaceOrder;
	}

	public void setBtnPlaceOrder(WebElement btnPlaceOrder) {
		this.btnPlaceOrder = btnPlaceOrder;
	}

	public boolean isCheckoutPageLoaded() {
		waitForPageToBeReady(driver);
		return lblHeadlineCheckout.isDisplayed();
	}
	
	public void selectCountryGermany(String countryName) {
		selectCountryArrow.click();
		inputCountry.sendKeys(countryName);
		List<WebElement> resultListCountry = driver.findElements(By.cssSelector("ul#select2-billing_country-results li"));
		WebElement foundCountry = resultListCountry.stream().filter(c -> c.getText().equalsIgnoreCase("Germany")).findFirst().orElse(null);
		foundCountry.click();
	}
	
	public OrderConfirmationPage placeOrder () {
		waitForPageToBeReady(driver);
		btnPlaceOrder.click();
		return new OrderConfirmationPage(driver);
	}
	
	public void fillBillingDetails(String firstname, String lastname, String country, String address, String city, String postcode, String phone, String email) {
		typeIfNotEmpty(inputFirstName, firstname);
		typeIfNotEmpty(inputLastName, lastname);
		selectCountryGermany(country);
		typeIfNotEmpty(inputAddress, address);
		typeIfNotEmpty(inputCity, city);
		typeIfNotEmpty(inputPostcode, postcode);
		typeIfNotEmpty(inputPhoneNumber, phone);
		typeIfNotEmpty(inputEmail, email);
	}
	
	private void typeIfNotEmpty(WebElement element, String value) {
	    if (value != null && !value.trim().isEmpty()) {
	        element.clear();
	        element.sendKeys(value);
	    }
	}
}
