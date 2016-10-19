package Bean;

import java.util.ArrayList;

public class ManageKitchens {

	public String cityName;
	
	public String areaName;
	
	public String address ;
	
	public String servingAreas;
	
	public String servingZipCodes;
	
	public String emailId;
	
	public String mobileNo;
	
	public String pincode;
	
	public int lunchStock,dinnerStock;
	
	public Double kitchenLongitude;
	
	public Double kitchenLatitude;
	
	public String kitchenName;
	
	public String kitchenUserName;
	
	public String password;
	
	public Integer areaId;
	
	public Integer kitchenId,cuisineId,categoryId;
	
	public String status;
	
	public Integer leadTime,capacity ; 
	
	public String cuisineName,categoryName;
	
	
	public ArrayList<ItemBean> itemBeanList = new ArrayList<ItemBean>();

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

	public String getKitchenName() {
		return kitchenName;
	}

	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public Integer getKitchenId() {
		return kitchenId;
	}

	public void setKitchenId(Integer kitchenId) {
		this.kitchenId = kitchenId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getKitchenLongitude() {
		return kitchenLongitude;
	}

	public void setKitchenLongitude(Double kitchenLongitude) {
		this.kitchenLongitude = kitchenLongitude;
	}

	public Double getKitchenLatitude() {
		return kitchenLatitude;
	}

	public void setKitchenLatitude(Double kitchenLatitude) {
		this.kitchenLatitude = kitchenLatitude;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getServingAreas() {
		return servingAreas;
	}

	public void setServingAreas(String servingAreas) {
		this.servingAreas = servingAreas;
	}

	public String getServingZipCodes() {
		return servingZipCodes;
	}

	public void setServingZipCodes(String servingZipCodes) {
		this.servingZipCodes = servingZipCodes;
	}

	public String getKitchenUserName() {
		return kitchenUserName;
	}

	public void setKitchenUserName(String kitchenUserName) {
		this.kitchenUserName = kitchenUserName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public Integer getLeadTime() {
		return leadTime;
	}

	public void setLeadTime(Integer leadTime) {
		this.leadTime = leadTime;
	}

	public ArrayList<ItemBean> getItemBeanList() {
		return itemBeanList;
	}

	public void setItemBeanList(ArrayList<ItemBean> itemBeanList) {
		this.itemBeanList = itemBeanList;
	}

	public Integer getCuisineId() {
		return cuisineId;
	}

	public void setCuisineId(Integer cuisineId) {
		this.cuisineId = cuisineId;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCuisineName() {
		return cuisineName;
	}

	public void setCuisineName(String cuisineName) {
		this.cuisineName = cuisineName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
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
	
	
}
