package simplyTest.autoprojekt.entities;

import java.util.ArrayList;
import java.util.List;

public class Cart {

	private static List<Item> addedItemsInCart = new ArrayList<Item>();
	
	
	public static List<Item> getAddedItems() {
		return addedItemsInCart;
	}
	
	public static boolean addItem(Item item) {
		if(item != null) {
			addedItemsInCart.add(item);
			return true;
		}
		return false;
	}
		
}
