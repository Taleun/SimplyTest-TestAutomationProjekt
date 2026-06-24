package simplyTest.autoprojekt;

import java.util.Set;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import simplyTest.autoprojekt.entities.Cart;
import simplyTest.autoprojekt.entities.Item;
import simplyTest.autoprojekt.entities.Shop;
import simplyTest.autoprojekt.webAppPages.CartPage;

public class AddItemTest extends BaseForTests{

	Set<Item> shopArticles = Shop.getShopItems();
	
	@Test (dataProvider = "externDataDrivenGetItemNames")
	public void addAnItem(String itemName) {
				
		extent.createTest("Test: Add an item '"+itemName+"' to the cart." );
		
		Item foundItem = Shop.find(itemName);
		
		WebElement addedItem = shoppingPage.addItemToCart_Robuster(itemName);
		Cart.addItem(foundItem);
		CartPage cartpage = shoppingPage.viewCart(addedItem);
		cartpage.waitForPageToBeReady(driver);
		
		Assert.assertNotNull(cartpage.getLblCartHdline(), "The reference of the cartpage headline is null.");
		Assert.assertEquals(cartpage.getLblNameFirstCartItem().getText(), foundItem.getName(), "The displayed name of the added item does not match the expected.");
		Assert.assertEquals(cartpage.getLblUniquePriceFirstCartItem().getText().substring(0, 5).replace(',', '.'), foundItem.getPrice(), "The displayed price of the added item does not match the expected.");
		Assert.assertEquals(cartpage.getLblQtyFirstCartItem().getAttribute("value"), foundItem.getQuantity(), "The displayed quantity ordered does not match the expected.");
		Assert.assertEquals(cartpage.getLblSubtotalFirstCartItem().getText().substring(0, 5).replace(',', '.'), foundItem.getSubtotal(), "The displayed subtotal of the added item does not match the expected.");
		
		extent.flush();
	}
}
