package simplyTest.autoprojekt.entities;

import java.util.HashSet;
import java.util.Set;

public class Shop {

	private static Set<Item> shopItems = new HashSet<Item>();
	
	public static Set<Item> getShopItems(){
		return shopItems;
	}
	
	public static void instantiateShopItems() {
		if(shopItems.isEmpty())
		{
			Item album = new Item("Album", "15.00");
			Item beanie = new Item("Beanie", "18.00");
			Item beanieWithLogo = new Item("Beanie with Logo", "18.00");
			Item belt = new Item("Belt", "55.00");
			Item cap = new Item("Cap", "16.00");
	//		Item hoodie = new Item("Hoodie", "45.00"); TODO: This item can be created once a color and the presence of the logo have been selected.
			Item hoodieWithLogo = new Item("Hoodie with Logo", "45.00");
			Item hoodieWithZipper = new Item("Hoodie with Zipper", "45.00");
	//		Item logoCollection = new Item("Logo Collection", "45,00"); TODO: This item is a collection of 3 items which must first be specified before the whole collection can be added to Cart.
			Item longSleeveTee = new Item("Long Sleeve Tee", "25.00");
			Item polo = new Item("Polo", "20.00");
	//		Item single = new Item("Single", "3.00"); TODO: This item on sale. Therefore it owns 2 prices. For manipulating it the sale behavior must be firstly processed.
			Item sunglasses = new Item("Sunglasses", "90.00");
			Item tshirt = new Item("T-Shirt", "18.00");
			Item tshirtWithLogo = new Item("T-Shirt with Logo", "18.00");
	//		Item vneckTshirt = new Item("V-Neck T-Shirt", "15.00"); TODO: This item can be created once a Color and the presence of the Size have been selected.
			
			shopItems.add(album);
			shopItems.add(beanie);
			shopItems.add(beanieWithLogo);
			shopItems.add(belt);
			shopItems.add(cap);
			shopItems.add(hoodieWithLogo);
			shopItems.add(hoodieWithZipper);
			shopItems.add(longSleeveTee);
			shopItems.add(polo);
			shopItems.add(sunglasses);
			shopItems.add(tshirt);
			shopItems.add(tshirtWithLogo);
			
			System.out.println(Item.getItemCounter() + " shop items have been instantiated and exist on the shop page.");
			System.out.println("The set of shop articles contains: " + shopItems.size() + " added items.");
		}
		else {
			System.out.println("The Shop has been already instanciated.");
		}
	}
	
	public static Item find (String itemName) {
		Item result = null;
		for(Item item : shopItems) {
			if(item.getName().equalsIgnoreCase(itemName)) {
				result = item;
				break;
			}
		}
		return result;
	}
}
