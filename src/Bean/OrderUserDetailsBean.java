package Bean;

import java.util.*;

public class OrderUserDetailsBean {
	public Integer orderId;
	public String name;
	public String emailId;
	public String contactNumber;
	public String deliveryAddress;
	public String deliveryZone;
	public String deliveryPincode;
	public String instruction;
	public Date orderedDate ;
	public String orderedDateValue;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public Date getOrderedDate() {
		return orderedDate;
	}
	public void setOrderedDate(Date orderedDate) {
		this.orderedDate = orderedDate;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getDeliveryAddress() {
		return deliveryAddress;
	}
	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	public String getDeliveryZone() {
		return deliveryZone;
	}
	public void setDeliveryZone(String deliveryZone) {
		this.deliveryZone = deliveryZone;
	}
	public String getDeliveryPincode() {
		return deliveryPincode;
	}
	public void setDeliveryPincode(String deliveryPincode) {
		this.deliveryPincode = deliveryPincode;
	}
	public String getInstruction() {
		return instruction;
	}
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
	public String getOrderedDateValue() {
		return orderedDateValue;
	}
	public void setOrderedDateValue(String orderedDateValue) {
		this.orderedDateValue = orderedDateValue;
	}
	
}
