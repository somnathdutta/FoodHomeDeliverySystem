package Bean;

public class KitchenStockUpdateViewBean {
    private String kitchenName;
    private Integer kitchenId;
    private String itemTypeName;
    private Integer itemTypeId;
    private Integer lunchStock;
    private Integer dinnerStock;
    private Integer stockUpdationId;
    
    private boolean kitchenStockDivVisibility = false;
    
    /*****************************************************************************************************************/
    
	public String getKitchenName() {
		return kitchenName;
	}
	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}
	public Integer getKitchenId() {
		return kitchenId;
	}
	public void setKitchenId(Integer kitchenId) {
		this.kitchenId = kitchenId;
	}
	public String getItemTypeName() {
		return itemTypeName;
	}
	public void setItemTypeName(String itemTypeName) {
		this.itemTypeName = itemTypeName;
	}
	public Integer getItemTypeId() {
		return itemTypeId;
	}
	public void setItemTypeId(Integer itemTypeId) {
		this.itemTypeId = itemTypeId;
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
	public boolean isKitchenStockDivVisibility() {
		return kitchenStockDivVisibility;
	}
	public void setKitchenStockDivVisibility(boolean kitchenStockDivVisibility) {
		this.kitchenStockDivVisibility = kitchenStockDivVisibility;
	}
	public Integer getStockUpdationId() {
		return stockUpdationId;
	}
	public void setStockUpdationId(Integer stockUpdationId) {
		this.stockUpdationId = stockUpdationId;
	}
   
}
