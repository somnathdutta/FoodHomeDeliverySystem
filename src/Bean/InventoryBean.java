package Bean;

public class InventoryBean {
	
	
	private Integer kitchenInventoryId;
	private Integer packagingTypeId;
	private String packagingType;
	
	private Integer stock;
	private Integer sold;
	
	private Integer kitchenId;
	private String kitchenName;
	
	public Integer getPackagingTypeId() {
		return packagingTypeId;
	}
	public void setPackagingTypeId(Integer packagingTypeId) {
		this.packagingTypeId = packagingTypeId;
	}
	public String getPackagingType() {
		return packagingType;
	}
	public void setPackagingType(String packagingType) {
		this.packagingType = packagingType;
	}
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	public Integer getSold() {
		return sold;
	}
	public void setSold(Integer sold) {
		this.sold = sold;
	}
	public Integer getKitchenId() {
		return kitchenId;
	}
	public void setKitchenId(Integer kitchenId) {
		this.kitchenId = kitchenId;
	}
	public Integer getKitchenInventoryId() {
		return kitchenInventoryId;
	}
	public void setKitchenInventoryId(Integer kitchenInventoryId) {
		this.kitchenInventoryId = kitchenInventoryId;
	}
	public String getKitchenName() {
		return kitchenName;
	}
	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}

}
