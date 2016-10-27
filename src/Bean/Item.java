package Bean;

public class Item {

	private String itemName,itemCode,itemDescription,cusineName,categoryName,status,dayName,itemTypeName,setName;
	private int cuisineId,categoryId,qty,lunchStock,dinnerStock;
	private double price;
	private boolean bikerAvailabilityForLunch = false,bikerAvailabilityForDinner=false;
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	public String getCusineName() {
		return cusineName;
	}
	public void setCusineName(String cusineName) {
		this.cusineName = cusineName;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDayName() {
		return dayName;
	}
	public void setDayName(String dayName) {
		this.dayName = dayName;
	}
	public String getItemTypeName() {
		return itemTypeName;
	}
	public void setItemTypeName(String itemTypeName) {
		this.itemTypeName = itemTypeName;
	}
	public String getSetName() {
		return setName;
	}
	public void setSetName(String setName) {
		this.setName = setName;
	}
	public int getCuisineId() {
		return cuisineId;
	}
	public void setCuisineId(int cuisineId) {
		this.cuisineId = cuisineId;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public int getLunchStock() {
		return lunchStock;
	}
	public void setLunchStock(int lunchStock) {
		this.lunchStock = lunchStock;
	}
	public int getDinnerStock() {
		return dinnerStock;
	}
	public void setDinnerStock(int dinnerStock) {
		this.dinnerStock = dinnerStock;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public boolean isBikerAvailabilityForLunch() {
		return bikerAvailabilityForLunch;
	}
	public void setBikerAvailabilityForLunch(boolean bikerAvailabilityForLunch) {
		this.bikerAvailabilityForLunch = bikerAvailabilityForLunch;
	}
	public boolean isBikerAvailabilityForDinner() {
		return bikerAvailabilityForDinner;
	}
	public void setBikerAvailabilityForDinner(boolean bikerAvailabilityForDinner) {
		this.bikerAvailabilityForDinner = bikerAvailabilityForDinner;
	}
	
}
