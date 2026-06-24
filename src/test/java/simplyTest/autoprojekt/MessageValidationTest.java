package simplyTest.autoprojekt;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import simplyTest.autoprojekt.webAppPages.CartPage;

public class MessageValidationTest extends BaseForTests{

	@Test
	public void cartEmptyTest() {
		extent.createTest("Test Cart Empty");
		System.out.println("-------------- Beginn of the 'Cart Empty Test' ---------------");
		Assert.assertTrue(shoppingPage.isShoppingPage(), "We expected the String Shop as headline");
		CartPage cartpage = shoppingPage.goToCartPage(driver);
		cartpage.waitForPageToBeReady(driver);
		Assert.assertTrue(cartpage.isCartPageEmpty(), "The Cart might not be empty. Remove all items first.");
		Assert.assertTrue(cartpage.isCartEmptyMsgCorrect(), "The empty cart message does not match the expectation.");
		System.out.println("The second assertion regarding empty cart is successfull.");
		System.out.println("-------------- End of the 'Cart Empty Test' ---------------");
		extent.flush();
	}
	
	@Test
	public void removeItemFromCartTest() {
		System.out.println("-------------- Beginn of the 'Remove Item from Cart Test' ---------------");
		String itemName = "T-Shirt";	
		extent.createTest("Test: Remove item '"+itemName+"'.");	
		
		shoppingPage.waitForPageToBeReady(driver);
		WebElement itemToAdd = shoppingPage.addItemToCart_Robuster(itemName);
		CartPage cartpage = shoppingPage.viewCart(itemToAdd);
		String itemRemovedMsg = cartpage.removeItem(itemName);
		System.out.println(itemRemovedMsg);		
		Assert.assertTrue(itemRemovedMsg.equalsIgnoreCase(cartpage.getItemRemovedMsg()), "The confirmation message after removing the item '"+itemName+ "' is not correct.");
		if(cartpage.isCartPageEmpty()) {
			Assert.assertTrue(cartpage.isCartPageEmpty(), "The Cart might not be empty. Remove all items first.");
			Assert.assertTrue(cartpage.isCartEmptyMsgCorrect(), "The empty cart message does not match the expectation.");
		}
		System.out.println("-------------- End of the 'Remove Item from Cart Test' ---------------");
		extent.flush();
	}
	
	@Test
	public void updateCartMsgTest() {
		System.out.println("-------------- Beginn of the 'Update Cart Message Test' ---------------");
		String itemName = "Sunglasses";
		extent.createTest("Test: Update an item '"+itemName+"' quantity in the cart.");
		
		WebElement lastAddedItem = shoppingPage.addItemToCart_Robuster(itemName);
		CartPage cartpage = shoppingPage.viewCart(lastAddedItem);
		cartpage.waitForPageToBeReady(driver);
		cartpage.incrementFirstCartItem();
		Assert.assertEquals(cartpage.getLblQtyFirstCartItem().getAttribute("value"), "2",
				"The number of album items in the cart is not 2.");
		System.out.println("-------------- End of the 'Update Cart Message Test' ---------------");
		extent.flush();
	}
}
