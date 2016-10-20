package Bean;

import java.util.ArrayList;

public class ManageDeliveryBoyBean {
	
	public String logisticsName;
	
	public Integer logisticsId ;
	
	public String kitchenName;
	
	public Integer kitchenId ;
	public String vehicleRegNo;
	public String cityName;
	public Integer cityId;
	public String areaName;
	public Integer areaId;
	public String name;
	public String phoneNo;
	public String address;
	public Boolean isActiveChecked=false;
	public Integer deliveryBoyId;
	
	public String deliveryBoyUserId,userName;
	
	public String password;
	
	public String status,isPickJiBoy;
	public String boyStatus;
	public int boyStatusId;
	public String orderAssigned;
	public Integer orderId;
	public String deliveryDateTime;
	public Integer totalDelivery;
	public Boolean activeVisibility=true;
	public Boolean suspendVisibility=true;
	
	public BoyStatus boyStatusBean = new BoyStatus();
	public ArrayList<BoyStatus> boyStatusList = new ArrayList<BoyStatus>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Boolean getIsActiveChecked() {
		return isActiveChecked;
	}
	public void setIsActiveChecked(Boolean isActiveChecked) {
		this.isActiveChecked = isActiveChecked;
	}
	public Integer getDeliveryBoyId() {
		return deliveryBoyId;
	}
	public void setDeliveryBoyId(Integer deliveryBoyId) {
		this.deliveryBoyId = deliveryBoyId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
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
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getDeliveryDateTime() {
		return deliveryDateTime;
	}
	public void setDeliveryDateTime(String deliveryDateTime) {
		this.deliveryDateTime = deliveryDateTime;
	}
	public String getBoyStatus() {
		return boyStatus;
	}
	public void setBoyStatus(String boyStatus) {
		this.boyStatus = boyStatus;
	}
	public String getOrderAssigned() {
		return orderAssigned;
	}
	public void setOrderAssigned(String orderAssigned) {
		this.orderAssigned = orderAssigned;
	}
	public Integer getTotalDelivery() {
		return totalDelivery;
	}
	public void setTotalDelivery(Integer totalDelivery) {
		this.totalDelivery = totalDelivery;
	}
	public Boolean getActiveVisibility() {
		return activeVisibility;
	}
	public void setActiveVisibility(Boolean activeVisibility) {
		this.activeVisibility = activeVisibility;
	}
	public Boolean getSuspendVisibility() {
		return suspendVisibility;
	}
	public void setSuspendVisibility(Boolean suspendVisibility) {
		this.suspendVisibility = suspendVisibility;
	}
	public String getLogisticsName() {
		return logisticsName;
	}
	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}
	public Integer getLogisticsId() {
		return logisticsId;
	}
	public void setLogisticsId(Integer logisticsId) {
		this.logisticsId = logisticsId;
	}
	public String getDeliveryBoyUserId() {
		return deliveryBoyUserId;
	}
	public void setDeliveryBoyUserId(String deliveryBoyUserId) {
		this.deliveryBoyUserId = deliveryBoyUserId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
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
	public String getVehicleRegNo() {
		return vehicleRegNo;
	}
	public void setVehicleRegNo(String vehicleRegNo) {
		this.vehicleRegNo = vehicleRegNo;
	}
	public int getBoyStatusId() {
		return boyStatusId;
	}
	public void setBoyStatusId(int boyStatusId) {
		this.boyStatusId = boyStatusId;
	}
	public BoyStatus getBoyStatusBean() {
		return boyStatusBean;
	}
	public void setBoyStatusBean(BoyStatus boyStatusBean) {
		this.boyStatusBean = boyStatusBean;
	}
	public ArrayList<BoyStatus> getBoyStatusList() {
		return boyStatusList;
	}
	public void setBoyStatusList(ArrayList<BoyStatus> boyStatusList) {
		this.boyStatusList = boyStatusList;
	}
	public String getIsPickJiBoy() {
		return isPickJiBoy;
	}
	public void setIsPickJiBoy(String isPickJiBoy) {
		this.isPickJiBoy = isPickJiBoy;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
