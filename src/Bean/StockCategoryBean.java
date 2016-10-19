package Bean;

public class StockCategoryBean {

	public String cuisineName;
	
	public String categoryName,itemName,itemCode,kitchenName;
	
	public Integer categoryId;
	
	public Integer stock;
	
	public Integer lunchStock;
	
	public Integer dinnerStock;
	
	public Double costPrice;
	
	public Boolean priceDisability = true;
	
	public Boolean stockDisability = true;
	
	public Boolean editVisibility = true;
	
	public Boolean updateVisibility = false;
	

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public String getCuisineName() {
		return cuisineName;
	}

	public void setCuisineName(String cuisineName) {
		this.cuisineName = cuisineName;
	}

	public Boolean getStockDisability() {
		return stockDisability;
	}

	public void setStockDisability(Boolean stockDisability) {
		this.stockDisability = stockDisability;
	}

	public Boolean getEditVisibility() {
		return editVisibility;
	}

	public void setEditVisibility(Boolean editVisibility) {
		this.editVisibility = editVisibility;
	}

	public Boolean getUpdateVisibility() {
		return updateVisibility;
	}

	public void setUpdateVisibility(Boolean updateVisibility) {
		this.updateVisibility = updateVisibility;
	}

	public Double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}

	public Boolean getPriceDisability() {
		return priceDisability;
	}

	public void setPriceDisability(Boolean priceDisability) {
		this.priceDisability = priceDisability;
	}

	public Integer getLunchStock() {
		return lunchStock;
	}

	public void setLunchStock(Integer lunchStock) {
		this.lunchStock = lunchStock;
	}

	public Integer getDinnerStock() {
		return dinnerStock;
	}

	public void setDinnerStock(Integer dinnerStock) {
		this.dinnerStock = dinnerStock;
	}

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

	public String getKitchenName() {
		return kitchenName;
	}

	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}
	
	
}
