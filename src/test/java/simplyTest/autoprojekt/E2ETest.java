package simplyTest.autoprojekt;

import java.io.IOException;


import org.openqa.selenium.WebElement;

import org.testng.Assert;
import org.testng.annotations.Test;

import simplyTest.autoprojekt.entities.Cart;
import simplyTest.autoprojekt.entities.Item;
import simplyTest.autoprojekt.entities.Shop;
import simplyTest.autoprojekt.webAppPages.CartPage;
import simplyTest.autoprojekt.webAppPages.CheckoutPage;
import simplyTest.autoprojekt.webAppPages.OrderConfirmationPage;

public class E2ETest extends BaseForTests{

	
	@Test
	public void orderMultipleItemsTest() throws IOException, InterruptedException {
		extent.createTest("E2E Place Orders Test");
		String itemName1 = "Album";
		String itemName2 = "Belt";	
		String itemName3 = "T-Shirt";

		Item item1 = Shop.find(itemName1);
		Item item2 = Shop.find(itemName2);
		Item item3 = Shop.find(itemName3);
		
		shoppingPage.addItemToCart_Robuster(itemName1);
		Cart.addItem(item1);
		shoppingPage.addItemToCart_Robuster(itemName2);
		Cart.addItem(item2);
		WebElement lastAddedItem = shoppingPage.addItemToCart_Robuster(itemName3);
		Cart.addItem(item3);
		
		CartPage cartpage = shoppingPage.viewCart(lastAddedItem);
		cartpage.waitForPageToBeReady(driver);

		cartpage.incrementFirstCartItem();
		item1.setQuantity(Integer.parseInt(item1.getQuantity()) + 1);
		
		Assert.assertEquals(cartpage.getLblQtyFirstCartItem().getAttribute("value"), item1.getQuantity(),
				"The number of album items in the cart is not 2.");

		cartpage.updateCart();
		cartpage.waitForPageToBeReady(driver);

		String lblExpectedFirstItemSubtotal = item1.getSubtotal();
		System.out.println("The expected subtotal price of the first cart item after updating its quantity value is: " + lblExpectedFirstItemSubtotal);

		String actualSubtotal = cartpage.getLblSubtotalFirstCartItem().getText().substring(0, 5);
		System.out.println("The actual subtotal of the first item added item is: " + actualSubtotal);
		
		Assert.assertEquals(actualSubtotal, lblExpectedFirstItemSubtotal,
				"The expected total price does not match with the actual one");

		CheckoutPage checkoutpage = cartpage.proceedToCheckout(driver);
		checkoutpage.waitForPageToBeReady(driver);

		WebElement lblCheckoutHdline = checkoutpage.getLblHeadlineCheckout();
		String expectedCheckoutHdline = "Checkout Page";
		Assert.assertEquals(lblCheckoutHdline.getText().trim(), expectedCheckoutHdline,
				"The headline of the checkout page does not match the expectation.");

		checkoutpage.fillBillingDetails("Max", "Mustermann", "Germany", "Beliebiger Str. 1", "Musterstadt", "12345",
				"111111", "test@domain.de");

		checkoutpage.waitForPageToBeReady(driver);
		OrderConfirmationPage ocp = checkoutpage.placeOrder();

		WebElement lblOrderConfirmMsg = ocp.getLblOrderConfirmationMsg();
		String expectedConfirmMsg = "Order received";
		Assert.assertTrue(ocp.isOrderConfirmationPage());
		Assert.assertEquals(lblOrderConfirmMsg.getText(), expectedConfirmMsg,
				"The order confirmation message does not match the expected one.");	

		System.out.println("Happy path End!");	
		extent.flush();
	}

}
