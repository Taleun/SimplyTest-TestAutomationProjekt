package simplyTest.autoprojekt.webAppPages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
//import org.testng.Assert;


public class ShoppingPage extends MenuHeaders {

	
	
	public ShoppingPage (WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
//	WebElement lblShopHeadline = driver.findElement(By.xpath("//div[@id='content']//main[@id='main']/header/h1"));
	
	@FindBy(xpath = "//div[@id='content']//main[@id='main']/header/h1")
	WebElement lblShopHeadline;	
		
	@FindBy(xpath = "//div[@class='storefront-sorting'][2]//a[@class='next page-numbers']")
	WebElement btnNextPage;
	
	
	public ShoppingPage loadShoppingPage() {
		driver.get("https://autoprojekt.simplytest.de/");
		return this;
	}
	
	public boolean isShoppingPage() {
		return lblShopHeadline.getText().trim().equalsIgnoreCase("Shop");
	}

	public boolean nextPageExists() {
		return btnNextPage.isDisplayed();
	}
	
	public ShoppingPage goToNextPage(WebDriver driver) {
		if(nextPageExists()) {
			btnNextPage.click();
			return new ShoppingPage(driver);
		}
		return this;
	}
	
	
	public CartPage goToCartPage(WebDriver driver) {
		btnCartMenu.click();
		return new CartPage(driver);		
	}
	
	/*
	public void addAlbumToCart_NotRobust() {
		WebElement btnAddAlbumToCart = driver.findElement(By.xpath("//main[@id='main']/ul/li[1]/a[2]"));
		btnAddAlbumToCart.click();
	}
	*/
	
	public WebElement addItemToCart_Robuster(String searchedItemName) {
		boolean isItemFound = false;
		WebElement addGesuchteItem = null;
		String itemName = null;
		List<WebElement> itemList;
		boolean continueSearching = nextPageExists();
			do {
				// Das ist die Liste der Artikeln auf die aktuelle Shopping Seite.
				itemList = driver.findElements(By.xpath("//main[@id='main']/ul/li"));

				for (WebElement item : itemList) {
					itemName = item.findElement(By.tagName("h2")).getText().trim();
					System.out.println("Item name: " + itemName);
					if (itemName.equalsIgnoreCase(searchedItemName)) {
						addGesuchteItem = item.findElement(By.xpath(".//a[text()='Add to cart']"));
						System.out.println("The Add-To-Cart button of the searched item has been found.");
						addGesuchteItem.click();
						isItemFound = true;
						continueSearching = false;
						System.out.println("The item '" + searchedItemName + "' has been added.");
						break; // Nachdem den richtige Artikel abgearbeitet wurde, sollte man die Schleife
								// verlassen.
					}
				}
				
				if (! isItemFound && continueSearching) {
					this.goToNextPage(driver);
				}
			} while (!isItemFound && continueSearching);
			
		if(! isItemFound) {
			throw new NoSuchElementException("The searched element " + searchedItemName + " could not be found.");
		}
		return addGesuchteItem;
	}
	
	public CartPage viewCart (WebElement addedItem) {
		waitForPageToBeReady(driver);
		if(addedItem != null) {
			WebElement btnViewCart = addedItem.findElement(By.xpath("./following-sibling::a"));
			btnViewCart.click();
			return new CartPage(driver);
		}
		return null; //Eigentlich soll hier eine Exception rausfliegen.
	}
	
}
