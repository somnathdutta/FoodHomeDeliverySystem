package Bean;

import org.zkoss.image.AImage;

public class ManageCategoryBean {
	public String cuisinName;
	public Integer cuisinId;
	public String areaName;
	public Integer areaId;
	public String categoryName;
	public Integer categoryId;
	public String cityName;
	public Boolean citynamevisibility=true;
	public Integer cityId;
	public AImage categoryImage;
	public String categoryImagePath;
	public String categoryBannerImagePath;
	public AImage categoryBannerImage;
	public Boolean isActiveChecked=false;
	public String status;
	public Integer inStock;
	public Boolean chkCategory=false;
	public Integer qty = 0;
	public Double price;
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryImagePath() {
		return categoryImagePath;
	}
	public void setCategoryImagePath(String categoryImagePath) {
		this.categoryImagePath = categoryImagePath;
	}
	public String getCategoryBannerImagePath() {
		return categoryBannerImagePath;
	}
	public void setCategoryBannerImagePath(String categoryBannerImagePath) {
		this.categoryBannerImagePath = categoryBannerImagePath;
	}
	public Boolean getIsActiveChecked() {
		return isActiveChecked;
	}
	public void setIsActiveChecked(Boolean isActiveChecked) {
		this.isActiveChecked = isActiveChecked;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public AImage getCategoryImage() {
		return categoryImage;
	}
	public void setCategoryImage(AImage categoryImage) {
		this.categoryImage = categoryImage;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public Boolean getCitynamevisibility() {
		return citynamevisibility;
	}
	public void setCitynamevisibility(Boolean citynamevisibility) {
		this.citynamevisibility = citynamevisibility;
	}
	public AImage getCategoryBannerImage() {
		return categoryBannerImage;
	}
	public void setCategoryBannerImage(AImage categoryBannerImage) {
		this.categoryBannerImage = categoryBannerImage;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getInStock() {
		return inStock;
	}
	public void setInStock(Integer inStock) {
		this.inStock = inStock;
	}
	public Integer getCuisinId() {
		return cuisinId;
	}
	public void setCuisinId(Integer cuisinId) {
		this.cuisinId = cuisinId;
	}
	public String getCuisinName() {
		return cuisinName;
	}
	public void setCuisinName(String cuisinName) {
		this.cuisinName = cuisinName;
	}
	public Boolean getChkCategory() {
		return chkCategory;
	}
	public void setChkCategory(Boolean chkCategory) {
		this.chkCategory = chkCategory;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	
}
