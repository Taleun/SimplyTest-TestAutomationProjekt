package simplyTest.autoprojekt.webAppPages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CartPage extends MenuHeaders {

	private String lastRemovedItemName;
	
	public CartPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath = "//header/h1")
	WebElement lblCartHdline;
	
	@FindBy(css = "div.cart-empty")
	WebElement lblEmptyCartMsg;
	
	@FindBy(xpath = "//div[@role='alert']")
	WebElement lblItemRemovedMsg;
	
	@FindBy(css = "div.quantity input")
	List<WebElement> editBoxQty;
	
	@FindBy(xpath = "//button[text()='Update cart']")
	WebElement btnUpdateCart;
	
	@FindBy(xpath = "//div[@role='alert']")
	WebElement lblConfirmUpdateMsg;
	
	@FindBy(xpath = "//form/table/tbody/tr[1]/td[@class='product-name']/a")
	WebElement lblNameFirstCartItem;
	
	@FindBy(xpath = "//form/table/tbody/tr[1]/td[@class='product-subtotal']/span/bdi")
	WebElement lblSubtotalFirstCartItem;
	
	@FindBy(xpath = "//form/table/tbody/tr[1]/td[@class='product-price']/span/bdi")
	WebElement lblUniquePriceFirstCartItem;
	
	@FindBy(xpath = "//form/table/tbody/tr[1]/td[@class='product-quantity']/div/input")
	WebElement lblQtyFirstCartItem;
	
	@FindBy(css = "div.wc-proceed-to-checkout a")
	WebElement btnCheckout;
	
	public WebElement getLblCartHdline() {
		return this.lblCartHdline;
	}
	
	public void setLblCartHdline(WebElement lblCartHdline) {
		this.lblCartHdline = lblCartHdline;
	}
	
	public String getLastRemovedItemName() {
		return lastRemovedItemName;
	}

	public void setLastRemovedItemName(String lastRemovedItemName) {
		this.lastRemovedItemName = lastRemovedItemName;
	}

	public WebElement getLblEmptyCartMsg() {
		return lblEmptyCartMsg;
	}

	public void setLblEmptyCartMsg(WebElement lblEmptyCartMsg) {
		this.lblEmptyCartMsg = lblEmptyCartMsg;
	}

	public WebElement getLblItemRemovedMsg() {
		return lblItemRemovedMsg;
	}

	public void setLblItemRemovedMsg(WebElement lblItemRemovedMsg) {
		this.lblItemRemovedMsg = lblItemRemovedMsg;
	}

	public List<WebElement> getEditBoxQty() {
		return editBoxQty;
	}

	public void setEditBoxQty(List<WebElement> editBoxQty) {
		this.editBoxQty = editBoxQty;
	}

	public WebElement getBtnUpdateCart() {
		return btnUpdateCart;
	}

	public void setBtnUpdateCart(WebElement btnUpdateCart) {
		this.btnUpdateCart = btnUpdateCart;
	}

	public WebElement getLblConfirmUpdateMsg() {
		return lblConfirmUpdateMsg;
	}

	public WebElement getLblNameFirstCartItem() {
		return lblNameFirstCartItem;
	}

	public void setLblNameFirstCartItem(WebElement lblNameFirstCartItem) {
		this.lblNameFirstCartItem = lblNameFirstCartItem;
	}

	public void setLblConfirmUpdateMsg(WebElement lblConfirmUpdateMsg) {
		this.lblConfirmUpdateMsg = lblConfirmUpdateMsg;
	}

	public WebElement getLblSubtotalFirstCartItem() {
		return lblSubtotalFirstCartItem;
	}

	public void setLblSubtotalFirstCartItem(WebElement lblSubtotalFirstCartItem) {
		this.lblSubtotalFirstCartItem = lblSubtotalFirstCartItem;
	}

	public WebElement getLblUniquePriceFirstCartItem() {
		return lblUniquePriceFirstCartItem;
	}

	public void setLblUniquePriceFirstCartItem(WebElement lblUniquePriceFirstCartItem) {
		this.lblUniquePriceFirstCartItem = lblUniquePriceFirstCartItem;
	}

	public WebElement getLblQtyFirstCartItem() {
		return lblQtyFirstCartItem;
	}

	public void setLblQtyFirstCartItem(WebElement lblQtyFirstCartItem) {
		this.lblQtyFirstCartItem = lblQtyFirstCartItem;
	}

	public WebElement getBtnCheckout() {
		return btnCheckout;
	}

	public void setBtnCheckout(WebElement btnCheckout) {
		this.btnCheckout = btnCheckout;
	}

		
	public String getRemovedItemName() {
		return lastRemovedItemName;
	}
	
	public boolean isCartPageEmpty() {
		return lblEmptyCartMsg.isDisplayed();
	}
	
	public boolean isCartEmptyMsgCorrect() {
		String expectedMsg = "Your cart is currently empty.";
		return lblEmptyCartMsg.getText().trim().equalsIgnoreCase(expectedMsg);
	}
	
	public ShoppingPage goToShoppingPage(WebDriver driver) {
		this.btnHomeMenu.click();
		return new ShoppingPage(driver);
	}
	
	public void incrementFirstCartItem() {
		editBoxQty.get(0).sendKeys(Keys.ARROW_UP);
	}
			
	public void updateCart() {
		btnUpdateCart.click();
	}
	
	public String getItemRemovedMsg() {
		return lblItemRemovedMsg.getAttribute("innerText").trim();
	}
	
	public boolean isUpdateCartMsgVisible() {
		WebDriverWait waitController = new WebDriverWait(driver, Duration.ofSeconds(10));
		waitController.until(ExpectedConditions.visibilityOf(lblConfirmUpdateMsg));
		return lblConfirmUpdateMsg.isDisplayed();
	}
	
	public String calcSubtotal() {
		double actualSubtotal = -1;
		String qty = lblQtyFirstCartItem.getAttribute("value");
		String uniquePrice = lblUniquePriceFirstCartItem.getText().substring(0, 5).replace(',', '.');
		actualSubtotal = Double.parseDouble(uniquePrice) * Integer.parseInt(qty);
		String formattedSubtotal = String.format("%.2f", actualSubtotal);
		System.out.println("Subtotal: " + formattedSubtotal);
		return formattedSubtotal;
	}
	
	public CheckoutPage proceedToCheckout(WebDriver driver) {
		waitForPageToBeReady(driver);
		new Actions(driver).scrollByAmount(0, 900).perform();
		btnCheckout.click();
		return new CheckoutPage(driver);
	}
	
	public String removeItem(String itemName) {
		lastRemovedItemName = itemName;
		WebElement removeIcon = driver.findElement(By.xpath("//form[@method='post']//tbody/tr/td/a[text()='"+itemName+"']/ancestor::tr/td/a[@class='remove']"));
		removeIcon.click();		
		return String.format("“%s” removed. Undo?", itemName);
	}	
	
}
