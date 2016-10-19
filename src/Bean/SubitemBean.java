package Bean;

import org.zkoss.image.AImage;

public class SubitemBean {
	public String subItemName;
	public String itemName;
	public Boolean itemVisibility=true;
	public Integer itemId;
	public String subItemImagePath;
	public AImage subItemImage;
	public String subItemdescription;
	public String getSubItemName() {
		return subItemName;
	}
	public void setSubItemName(String subItemName) {
		this.subItemName = subItemName;
	}
	public String getSubItemImagePath() {
		return subItemImagePath;
	}
	public void setSubItemImagePath(String subItemImagePath) {
		this.subItemImagePath = subItemImagePath;
	}
	public String getSubItemdescription() {
		return subItemdescription;
	}
	public void setSubItemdescription(String subItemdescription) {
		this.subItemdescription = subItemdescription;
	}
	public AImage getSubItemImage() {
		return subItemImage;
	}
	public void setSubItemImage(AImage subItemImage) {
		this.subItemImage = subItemImage;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Boolean getItemVisibility() {
		return itemVisibility;
	}
	public void setItemVisibility(Boolean itemVisibility) {
		this.itemVisibility = itemVisibility;
	}
	
}
