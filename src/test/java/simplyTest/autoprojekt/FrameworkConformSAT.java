package simplyTest.autoprojekt;

import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import simplyTest.autoprojekt.webAppPages.CartPage;
import simplyTest.autoprojekt.webAppPages.CheckoutPage;
import simplyTest.autoprojekt.webAppPages.OrderConfirmationPage;
import simplyTest.autoprojekt.webAppPages.ShoppingPage;

public class FrameworkConformSAT {

	public static void main(String[] args) throws InterruptedException {
		
		// Einige Konfigurationseinstellungen sollen mit ChromeOptions Objekte eingestellt werden, bevor die Webanwendung aufgerufen wird.
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--incognito", "--start-maximized");
		options.setImplicitWaitTimeout(Duration.ofSeconds(7));
		
		WebDriver driver = new ChromeDriver(options);
		
	
		ShoppingPage shoppingpage = new ShoppingPage(driver);
		shoppingpage.loadShoppingPage();
		Assert.assertTrue(shoppingpage.isShoppingPage(), "We expected the String Shop as headline");
		System.out.println("The first assertion passed. The shop headline is correct.");
		
		// Als nächstes möchten prüfen, dass der Warenkorb leer ist, bevor man selbst einkaufen kann.
		CartPage cartpage = shoppingpage.goToCartPage(driver);
		waitForPageToBeReady(driver);		
		Assert.assertTrue(cartpage.isCartPageEmpty(), "The Cart might not be empty. Remove all items first.");
		Assert.assertTrue(cartpage.isCartEmptyMsgCorrect(), "The empty cart message does not match the expectation.");
		System.out.println("The second assertion regarding empty cart is successfull.");
		
		
		// Als nächtes möchten wir einen Album Artikel in den leeren Warenkorb werfen und in den Cart wechseln.
		shoppingpage = cartpage.goToShoppingPage(driver);
		waitForPageToBeReady(driver);
		
		shoppingpage.addItemToCart_Robuster("Album");
		shoppingpage.addItemToCart_Robuster("Belt");
		shoppingpage.addItemToCart_Robuster("Beanie");
		WebElement lastAddedItem = shoppingpage.addItemToCart_Robuster("T-Shirt");
		
		cartpage = shoppingpage.viewCart(lastAddedItem);
				
		// Als nächstes möchten wir die Menge des zu kaufenden Elements inkrementieren.
		waitForPageToBeReady(driver);
		
		cartpage.incrementFirstCartItem();
		Assert.assertEquals(cartpage.getLblQtyFirstCartItem().getAttribute("value"), "2", "The number of album items in the cart is not 2.");
		
		cartpage.updateCart();
		
		//Das Update durch das Klicken auf den 'Update Cart'-Btn muss fertig sein, bevor die Automatisierung weitermacht
		waitForPageToBeReady(driver);
		
		String lblExpectedFirstItemSubtotal = "30,00";
		System.out.println("The expected subtotal price of the first cart item is: " + lblExpectedFirstItemSubtotal);
		
		String actualSubtotal = cartpage.calcSubtotal();
		
		System.out.println("The actual subtotal of the first item added item is: " + actualSubtotal);
		Assert.assertEquals(actualSubtotal, lblExpectedFirstItemSubtotal, "The expected total price does not match with the actual one");
		
		
//		waitController.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.wc-proceed-to-checkout a"))).click();
		CheckoutPage checkoutpage = cartpage.proceedToCheckout(driver);

		waitForPageToBeReady(driver);
		
		WebElement lblCheckoutHdline = checkoutpage.getLblHeadlineCheckout();
		String expectedCheckoutHdline = "Checkout Page";
		Assert.assertEquals(lblCheckoutHdline.getText().trim(), expectedCheckoutHdline, "The headline of the checkout page does not match the expectation.");
		
		checkoutpage.fillBillingDetails("Max", "Mustermann", "Germany", "Beliebiger Str. 1", "Musterstadt", "12345", "111111", "test@domain.de");

		waitForPageToBeReady(driver);
		OrderConfirmationPage ocp = checkoutpage.placeOrder();
//				
		//After confirming the order, you should check that a confirmation message is shown.
		WebElement lblOrderConfirmMsg = ocp.getLblOrderConfirmationMsg();
		String expectedConfirmMsg = "Order received";
		Assert.assertTrue(ocp.isOrderConfirmationPage());
		Assert.assertEquals(lblOrderConfirmMsg.getText(), expectedConfirmMsg, "The order confirmation message does not match the expected one.");
		
		Thread.sleep(4000);
		
		System.out.println("Happy path End!");
		driver.quit();
	}

	
	public static void waitForPageToBeReady(WebDriver driver) {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	  
	    wait.until(webDriver -> ((JavascriptExecutor) webDriver)
	            .executeScript("return document.readyState").equals("complete"));

	    wait.until(webDriver -> (Boolean) ((JavascriptExecutor) webDriver)
	            .executeScript("return (window.jQuery == null) || (jQuery.active === 0);"));
	}
}
