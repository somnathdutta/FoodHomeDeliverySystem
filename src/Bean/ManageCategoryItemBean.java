package Bean;

import org.zkoss.image.AImage;

public class ManageCategoryItemBean {
		public String cityName;
		public String areaName;
		public Integer areaId;
		public String categoryName;
		public Integer categoryId;
		public String itemName;
		public Integer itemId;
		public String itemImageFilePath;
		public String itemBannerFilePath;
		public Double price;
		public Integer inStockQty;
		public String description;
		public AImage itemImage;
		public String status;
		public Boolean isActiveChecked=false;
		
		public Boolean citynamevisibility=true;
		
		public Boolean areanamevisibility=true;
		
		public Boolean categorynamevisibility=true;
		
		public Integer kitchenId ;
		public String kitchenName ; 
		public String cuisineName;
		
		/*public SubitemBean subitemBean = new SubitemBean();
		
		public ArrayList<SubitemBean> subitemBeanList = new ArrayList<SubitemBean>();*/
		
		public String getCityName() {
			return cityName;
		}
		public void setCityName(String cityName) {
			this.cityName = cityName;
		}
		public String getAreaName() {
			return areaName;
		}
		public void setAreaName(String areaName) {
			this.areaName = areaName;
		}
		public String getCategoryName() {
			return categoryName;
		}
		public void setCategoryName(String categoryName) {
			this.categoryName = categoryName;
		}
		public String getItemName() {
			return itemName;
		}
		public void setItemName(String itemName) {
			this.itemName = itemName;
		}
		public String getItemImageFilePath() {
			return itemImageFilePath;
		}
		public void setItemImageFilePath(String itemImageFilePath) {
			this.itemImageFilePath = itemImageFilePath;
		}
		public String getItemBannerFilePath() {
			return itemBannerFilePath;
		}
		public void setItemBannerFilePath(String itemBannerFilePath) {
			this.itemBannerFilePath = itemBannerFilePath;
		}
		public Double getPrice() {
			return price;
		}
		public void setPrice(Double price) {
			this.price = price;
		}
		public Integer getInStockQty() {
			return inStockQty;
		}
		public void setInStockQty(Integer inStockQty) {
			this.inStockQty = inStockQty;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public Boolean getIsActiveChecked() {
			return isActiveChecked;
		}
		public void setIsActiveChecked(Boolean isActiveChecked) {
			this.isActiveChecked = isActiveChecked;
		}
		public Integer getAreaId() {
			return areaId;
		}
		public void setAreaId(Integer areaId) {
			this.areaId = areaId;
		}
		public Integer getCategoryId() {
			return categoryId;
		}
		public void setCategoryId(Integer categoryId) {
			this.categoryId = categoryId;
		}
		public AImage getItemImage() {
			return itemImage;
		}
		public void setItemImage(AImage itemImage) {
			this.itemImage = itemImage;
		}
		public Integer getItemId() {
			return itemId;
		}
		public void setItemId(Integer itemId) {
			this.itemId = itemId;
		}
		/*public SubitemBean getSubitemBean() {
			return subitemBean;
		}
		public void setSubitemBean(SubitemBean subitemBean) {
			this.subitemBean = subitemBean;
		}
		public ArrayList<SubitemBean> getSubitemBeanList() {
			return subitemBeanList;
		}
		public void setSubitemBeanList(ArrayList<SubitemBean> subitemBeanList) {
			this.subitemBeanList = subitemBeanList;
		}
		*/
		public Boolean getCitynamevisibility() {
			return citynamevisibility;
		}
		public void setCitynamevisibility(Boolean citynamevisibility) {
			this.citynamevisibility = citynamevisibility;
		}
		public Boolean getAreanamevisibility() {
			return areanamevisibility;
		}
		public void setAreanamevisibility(Boolean areanamevisibility) {
			this.areanamevisibility = areanamevisibility;
		}
		public Boolean getCategorynamevisibility() {
			return categorynamevisibility;
		}
		public void setCategorynamevisibility(Boolean categorynamevisibility) {
			this.categorynamevisibility = categorynamevisibility;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public Integer getKitchenId() {
			return kitchenId;
		}
		public void setKitchenId(Integer kitchenId) {
			this.kitchenId = kitchenId;
		}
		public String getKitchenName() {
			return kitchenName;
		}
		public void setKitchenName(String kitchenName) {
			this.kitchenName = kitchenName;
		}
		public String getCuisineName() {
			return cuisineName;
		}
		public void setCuisineName(String cuisineName) {
			this.cuisineName = cuisineName;
		}
}
