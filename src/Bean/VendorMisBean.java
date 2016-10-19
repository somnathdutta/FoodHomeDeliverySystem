package Bean;

import java.util.Date;

public class VendorMisBean {

	public String kitchenName;
	
	public String orderNo;
	
	public Date orderAssignTime;
	
	public Date acceptanceTime;
	
	public Date notifyTime;
	
	public String orderAssignTimeValue;
	
	public String acceptanceTimeValue;
	
	public String notifyTimeValue;
	
	public String delayInReceive;
	
	public String delayInDelivery;

	public String getKitchenName() {
		return kitchenName;
	}

	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderAssignTimeValue() {
		return orderAssignTimeValue;
	}

	public void setOrderAssignTimeValue(String orderAssignTimeValue) {
		this.orderAssignTimeValue = orderAssignTimeValue;
	}

	public String getAcceptanceTimeValue() {
		return acceptanceTimeValue;
	}

	public void setAcceptanceTimeValue(String acceptanceTimeValue) {
		this.acceptanceTimeValue = acceptanceTimeValue;
	}

	public String getNotifyTimeValue() {
		return notifyTimeValue;
	}

	public void setNotifyTimeValue(String notifyTimeValue) {
		this.notifyTimeValue = notifyTimeValue;
	}

	public void setOrderAssignTime(Date orderAssignTime) {
		this.orderAssignTime = orderAssignTime;
	}

	public void setAcceptanceTime(Date acceptanceTime) {
		this.acceptanceTime = acceptanceTime;
	}

	public void setNotifyTime(Date notifyTime) {
		this.notifyTime = notifyTime;
	}

	public Date getOrderAssignTime() {
		return orderAssignTime;
	}

	public Date getAcceptanceTime() {
		return acceptanceTime;
	}

	public Date getNotifyTime() {
		return notifyTime;
	}

	public String getDelayInReceive() {
		return delayInReceive;
	}

	public void setDelayInReceive(String delayInReceive) {
		this.delayInReceive = delayInReceive;
	}

	public String getDelayInDelivery() {
		return delayInDelivery;
	}

	public void setDelayInDelivery(String delayInDelivery) {
		this.delayInDelivery = delayInDelivery;
	}
	
	
}
