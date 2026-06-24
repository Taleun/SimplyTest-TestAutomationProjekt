package simplyTest.autoprojekt;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class StandAloneTest {

	public static void main(String[] args) throws InterruptedException {
		
		// Einige Konfigurationseinstellungen sollen mit ChromeOptions Objekte eingestellt werden, bevor die Webanwendung aufgerufen wird.
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--incognito", "--start-maximized");
		options.setImplicitWaitTimeout(Duration.ofSeconds(7));
		
		WebDriver driver = new ChromeDriver(options);
		driver.get("https://autoprojekt.simplytest.de/");
	
		// Als erstes möchten wir prüfen, ob man nach dem Aufruf auf die Homepage der Onlineshop-Anwendung gelandet ist.
		WebElement lblShopHeadline = driver.findElement(By.xpath("//div[@id='content']//main[@id='main']/header/h1"));
		String lblShopHeadlineText = lblShopHeadline.getText();
		Assert.assertEquals(lblShopHeadlineText, "Shop", "We expected the String Shop as headline");
		System.out.println("The first assertion is successfull. The shop headline is correct.");
		
		// Als nächstes möchten prüfen, dass der Warenkorb leer ist, bevor man selbst einkaufen kann.
		WebElement btnCartMenu = driver.findElement(By.xpath("//header[@id='masthead']//ul[@class='nav-menu']//a[text()='Cart']"));
		btnCartMenu.click();
		
		WebElement lblEmptyCart = driver.findElement(By.cssSelector("div.cart-empty")); 
		String lblEmptyCartText = lblEmptyCart.getText().trim();
		String expectedEmptyCartText = "Your cart is currently empty.";
		boolean isEmptyCartTextCorrect = lblEmptyCartText.equalsIgnoreCase(expectedEmptyCartText);
		Assert.assertTrue(isEmptyCartTextCorrect, "The Cart might not be empty. Remove all items first.");
		System.out.println("The second assertion regarding empty cart is successfull.");
		
		// Als nächtes möchten wir einen Album Artikel in den leeren Warenkorb werfen und in den Cart wechseln.
		WebElement btnHomeMenu = driver.findElement(By.xpath("//header[@id='masthead']//ul[@class='nav-menu']//a[text()='Home']"));
		Assert.assertNotNull(btnHomeMenu, "The home menu button could not be found.");
		btnHomeMenu.click();
		
		// Erste Methode: Sehr einfach, schnelle Implementierung aber hohe Wartbarkeitsaufwand und langfristig nicht am Stabilsten 
		
		WebElement btnAddAlbumToCart = driver.findElement(By.xpath("//main[@id='main']/ul/li[1]/a[2]"));
		Assert.assertNotNull(btnAddAlbumToCart, "The button for adding an album to cart could not be found.");
		btnAddAlbumToCart.click();
		/*
		WebElement btnViewAlbumCart = driver.findElement(By.xpath("//main[@id='main']/ul/li[1]/a[2]/following-sibling::a"));
		Assert.assertNotNull(btnViewAlbumCart, "The button 'View Cart' for the Album item could not be found.");
		btnViewAlbumCart.click();
		*/
		
		// Zweite Methode: Implementierungsaufwand höher aber Wartbarkeitsaufwand niedriger. Langfristig stabiler
		
		List<WebElement> itemList = driver.findElements(By.xpath("//main[@id='main']/ul/li"));
		WebElement gesuchteItem = null;
		String gesuchteItemName = "Beanie";
		String itemName = null;
		for (WebElement item : itemList) {
			itemName = item.findElement(By.tagName("h2")).getText().trim();
			System.out.println("Item name: " + itemName);
			if(itemName.equalsIgnoreCase(gesuchteItemName)) {
				gesuchteItem = item.findElement(By.xpath(".//a[text()='Add to cart']"));
				Assert.assertNotNull(gesuchteItem, "The Add-to-chart button for the " + gesuchteItemName + " item could not be found.");
				gesuchteItem.click();
				//Anschließend soll direkt in den Warenkorb gewechselt werden
				/*
				btnViewCart = item.findElement(By.xpath(".//a[@title='View cart']"));
				Assert.assertNotNull(btnViewCart, "The view cart button could not be found.");
				btnViewCart.click();
				*/
				break; // Nachdem den richtige Artikel abgearbeitet wurde, sollte man die Schleife verlassen. 
			}
		}		
		
		
		// Dritte Methode using Java Stream API: Algorithmisch identisch zur zweiten, aber performanter. Geeignet bei großen Datenmengen bzw. wo die Ausführzeit wichtig ist.
		driver.findElements(By.xpath("//main[@id='main']/ul/li")).stream()
				.filter(i -> i.findElement(By.tagName("h2")).getText().trim().equalsIgnoreCase("Belt")).findFirst()
				.ifPresent(item -> {
					item.findElement(By.xpath(".//a[text()='Add to cart']")).click();
					item.findElement(By.xpath(".//a[@title='View cart']")).click();
				});
		
		
		
		// Als nächstes möchten wir die Menge des zu kaufenden Elements inkrementieren.
		WebElement editBoxQty = driver.findElement(By.cssSelector("div.quantity input"));
		editBoxQty.sendKeys(Keys.ARROW_UP); //Erhöhe Qty auf 2
		String albumQty = editBoxQty.getAttribute("value");
		Assert.assertEquals(albumQty, "2", "The number of album items in the cart is not 2.");
		
		WebElement btnUpdateCart = driver.findElement(By.xpath("//button[text()='Update cart']"));
		btnUpdateCart.click();
		
		//Das Update durch das Klicken auf den 'Update Cart'-Btn muss fertig sein, bevor die Automatisierung weitermacht
		WebElement lblConfirmUpdateMsg = driver.findElement(By.xpath("//div[@role='alert']"));
		WebDriverWait waitController = new WebDriverWait(driver, Duration.ofSeconds(7));
		waitController.until(
				ExpectedConditions.attributeToBe(lblConfirmUpdateMsg, "innerText", lblConfirmUpdateMsg.getText()));
		
		String lblAlbumSubtotal = driver.findElement(By.xpath("//td[@class='product-subtotal']/span/bdi")).getText().substring(0, 5);
		System.out.println("The subtotal price is: " + lblAlbumSubtotal);
		String lblAlbumPrice = driver.findElement(By.xpath("//td[@class='product-price']/span/bdi")).getText().substring(0, 5).replace(',', '.');
		System.out.println("The price of a single album item is: " + lblAlbumPrice);
		String lblAlbumQty = driver.findElement(By.xpath("//td[@class='product-quantity']/div/input")).getAttribute("value");
		System.out.println("The number of album items added is: " + lblAlbumQty);
		double actualSubtotal = Double.parseDouble(lblAlbumPrice) * Integer.parseInt(lblAlbumQty);
		System.out.println(actualSubtotal);
		String actualSubtotalText = String.format("%.2f", actualSubtotal);
		System.out.println("The expected subtotal of the added album items is: " + actualSubtotalText);
		Assert.assertEquals(actualSubtotalText, lblAlbumSubtotal, "The expected total price does not match with the actual one");
		
		
//		waitController.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.wc-proceed-to-checkout a"))).click();
		WebElement btnCheckout = waitController.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.wc-proceed-to-checkout a")));
		System.out.println(btnCheckout.getText());
//		new Actions(driver).scrollByAmount(0, 1500).build().perform();
//		new Actions(driver).scrollByAmount(0, 2500).perform();
		waitForPageToBeReady(driver);
		btnCheckout.click();

		
		
		WebElement lblCheckoutHdline = driver.findElement(By.xpath("//header/h1"));
		waitController.until(ExpectedConditions.visibilityOf(lblCheckoutHdline));
		
		WebElement inputFirstName = driver.findElement(By.id("billing_first_name"));
		inputFirstName.sendKeys("Max");
		
		WebElement inputLastName = driver.findElement(By.id("billing_last_name"));
		inputLastName.sendKeys("Mustermann");
		
		WebElement selectCountryArrow = driver.findElement(By.id("select2-billing_country-container"));
		selectCountryArrow.click();		
		WebElement inputCountry = driver.findElement(By.cssSelector("input.select2-search__field"));
		inputCountry.sendKeys("Ger");
		List<WebElement> resultListCountry = driver.findElements(By.cssSelector("ul#select2-billing_country-results li"));
		/*
		for(WebElement country : resultListCountry) {
			if(country.getText().equalsIgnoreCase("Germany")) {
				country.click();
				break;
			}
		}
		*/
		resultListCountry.stream().filter(c -> c.getText().equalsIgnoreCase("Germany")).findFirst().orElse(null).click();
		
		WebElement inputAddress = driver.findElement(By.id("billing_address_1"));
		inputAddress.sendKeys("Beliebige Str. 1");
		
		WebElement inputPostcode = driver.findElement(By.id("billing_postcode"));
		inputPostcode.sendKeys("11111");
		
		WebElement inputCity = driver.findElement(By.id("billing_city"));
		inputCity.sendKeys("Musterstadt");
		
		WebElement inputPhoneNumber = driver.findElement(By.id("billing_phone"));
		inputPhoneNumber.sendKeys("111111111");
		
		WebElement inputEmail = driver.findElement(By.id("billing_email"));
		inputEmail.sendKeys("test@gmx.de");
		
		WebElement inputComment = driver.findElement(By.id("order_comments"));
		inputComment.sendKeys("It was a pleasure. ;)");
		
		// Bestätige deine Bestellung
//		waitController.until(ExpectedConditions.elementToBeClickable(By.id("place_order"))).click();
//		Thread.sleep(Duration.ofSeconds(4));
		waitForPageToBeReady(driver);
		driver.findElement(By.id("place_order")).click();
//		new Actions(driver).click(placeOrderBtn);
		
		
		//After confirming the order, you should check that a confirmation message is shown.
		WebElement lblOrderConfirmMsg = driver.findElement(By.xpath("//h1[text()='Order received']"));
		String expectedConfirmMsg = "Order received";
		Assert.assertEquals(lblOrderConfirmMsg.getText(), expectedConfirmMsg, "The order confirmation message does not match the expected one.");
		
		Thread.sleep(4000);
		
		System.out.println("Happy End!");
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
