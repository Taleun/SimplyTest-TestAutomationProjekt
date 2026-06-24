package simplyTest.autoprojekt.entities;

public class Item {

	private static int itemCounter = 0;
	
	private String name;
	private String price;
	private String quantity;
	private String subtotal;
	
	public Item(String name, String price) {
		setName(name);
		setPrice(price);
		setQuantity("1");
		setSubtotal(price);
		
		itemCounter++;
	}
	
	public String getName() {
		return name;
	}
	
	private void setName(String name) {
		this.name = name;
	}
	
	public String getPrice() {
		return price;
	}
	
	private void setPrice(String price) {
		this.price = price;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
		updateSubtotal();
	}

	public void setQuantity(int quantity) {
		setQuantity(String.valueOf(quantity));
		updateSubtotal();
	}
	
	public String getSubtotal() {
		return subtotal;
	}

	private void setSubtotal(String subtotal) {
		this.subtotal = subtotal;
	}
	
	public static int getItemCounter() {
		return itemCounter;
	}
	
	public void incrementQty() {
		int actualQuantity = Integer.parseInt(quantity) + 1;
		double actualSubtotal = Double.parseDouble(subtotal) * actualQuantity;
		this.quantity = String.valueOf(actualQuantity);
		this.subtotal = String.format("%.2f", actualSubtotal);
	}
	
	private void updateSubtotal() {
		int actualQty = Integer.parseInt(this.quantity);
		double actualSubtotal = Double.parseDouble(this.price) * actualQty;
		this.subtotal = String.format("%.2f", actualSubtotal);
	}
	

	@Override
	public boolean equals(Object obj) {
		
		if(! (obj instanceof Item)) {
			return false;
		}		
		Item i = (Item) obj;
		return this.name.equalsIgnoreCase(i.getName()) ? true : false; 
	}
}
