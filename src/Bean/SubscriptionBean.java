package Bean;

import java.sql.Date;
import java.util.ArrayList;

public class SubscriptionBean {

	public String subscriptionNo;
	
	public Integer subscriptionId ;
	
	public String contactName;
	
	public String emailId;
	
	public String contactNo;
	
	public String flatNo ;
	
	public String streetName;
	
	public String landMark;
	
	public String pincode;
	
	public String deliveryAddress;
	
	public Date startDate;
	
	public String startDateValue;
	
	public Date endDate;
	
	public String endDateValue;
	
	public MealBean mealBean =  new MealBean();
	
	public ArrayList<MealBean> mealBeanList = new ArrayList<MealBean>();
	
	public String day;
	
	public String cityName;
	
	public String areaName = null;
	
	public Integer areaId ;
	
	public String[] dayArray = {"MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY","SUNDAY"};
	
	public Boolean[] dayVisibilityArray = {true,true,true,true,true,true,true};
	
	public String mealType;
	
	public Double price = 0d;
	
	public Double totalPrice = 0d;
	
	public Boolean createSubscriptionDisability =  false;

	public Boolean saveMealDisability =  true;
	
	public String cuisineName;
	
	public String categoryName;
	
	public Integer quantity;
	
	public Integer subscriptionMealDetailId;
	
	public String kitchenName;
	
	public Boolean detailsVisibility = true;
	
	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getMealType() {
		return mealType;
	}

	public void setMealType(String mealType) {
		this.mealType = mealType;
	}

	public String[] getDayArray() {
		return dayArray;
	}

	public void setDayArray(String[] dayArray) {
		this.dayArray = dayArray;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

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

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public MealBean getMealBean() {
		return mealBean;
	}

	public void setMealBean(MealBean mealBean) {
		this.mealBean = mealBean;
	}

	public ArrayList<MealBean> getMealBeanList() {
		return mealBeanList;
	}

	public void setMealBeanList(ArrayList<MealBean> mealBeanList) {
		this.mealBeanList = mealBeanList;
	}

	public String getFlatNo() {
		return flatNo;
	}

	public void setFlatNo(String flatNo) {
		this.flatNo = flatNo;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getLandMark() {
		return landMark;
	}

	public void setLandMark(String landMark) {
		this.landMark = landMark;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getSubscriptionNo() {
		return subscriptionNo;
	}

	public void setSubscriptionNo(String subscriptionNo) {
		this.subscriptionNo = subscriptionNo;
	}

	public Integer getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(Integer subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public Boolean getCreateSubscriptionDisability() {
		return createSubscriptionDisability;
	}

	public void setCreateSubscriptionDisability(Boolean createSubscriptionDisability) {
		this.createSubscriptionDisability = createSubscriptionDisability;
	}

	public Boolean getSaveMealDisability() {
		return saveMealDisability;
	}

	public void setSaveMealDisability(Boolean saveMealDisability) {
		this.saveMealDisability = saveMealDisability;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	
	public void setDayVisibilityArray(Boolean[] dayVisibilityArray) {
		this.dayVisibilityArray = dayVisibilityArray;
	}

	public Boolean[] getDayVisibilityArray() {
		return dayVisibilityArray;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getStartDateValue() {
		return startDateValue;
	}

	public void setStartDateValue(String startDateValue) {
		this.startDateValue = startDateValue;
	}

	public String getEndDateValue() {
		return endDateValue;
	}

	public void setEndDateValue(String endDateValue) {
		this.endDateValue = endDateValue;
	}

	public Integer getSubscriptionMealDetailId() {
		return subscriptionMealDetailId;
	}

	public void setSubscriptionMealDetailId(Integer subscriptionMealDetailId) {
		this.subscriptionMealDetailId = subscriptionMealDetailId;
	}

	public String getKitchenName() {
		return kitchenName;
	}

	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}

	public Boolean getDetailsVisibility() {
		return detailsVisibility;
	}

	public void setDetailsVisibility(Boolean detailsVisibility) {
		this.detailsVisibility = detailsVisibility;
	}
	
}
