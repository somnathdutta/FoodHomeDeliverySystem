package Bean;

import java.sql.Date;

public class CompletedOrderBean {
	
	public String orderNo;
	
	public Integer orderId;

	public String deliveryAddress;
	
	public String contactNumber;
	
	public String orderBy;
	
	public String emailId;
	
	public String deliveryBoy;
	
	public String deliveryBoyPhone;
	
	public Date fromDeliveryDateSql;
	public java.util.Date fromDeliveryDateUtil;
	public String fromDeliveryDateStr;
	
	public Date toDeliveryDateSql;
	public java.util.Date toDeliveryDateUtil;
	public String toDeliveryDateStr;
	
	public Date deliveryDateSql;
	public java.util.Date deliveryDateUtil;
	public String deliveryDateStr;
	
	public String city;
	public String area;
	public Double totalPrice;
	public String orderStatus;
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getDeliveryAddress() {
		return deliveryAddress;
	}
	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getDeliveryBoy() {
		return deliveryBoy;
	}
	public void setDeliveryBoy(String deliveryBoy) {
		this.deliveryBoy = deliveryBoy;
	}
	public String getDeliveryBoyPhone() {
		return deliveryBoyPhone;
	}
	public void setDeliveryBoyPhone(String deliveryBoyPhone) {
		this.deliveryBoyPhone = deliveryBoyPhone;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Date getDeliveryDateSql() {
		return deliveryDateSql;
	}
	public void setDeliveryDateSql(Date deliveryDateSql) {
		this.deliveryDateSql = deliveryDateSql;
	}
	public java.util.Date getDeliveryDateUtil() {
		return deliveryDateUtil;
	}
	public void setDeliveryDateUtil(java.util.Date deliveryDateUtil) {
		this.deliveryDateUtil = deliveryDateUtil;
	}
	public String getDeliveryDateStr() {
		return deliveryDateStr;
	}
	public void setDeliveryDateStr(String deliveryDateStr) {
		this.deliveryDateStr = deliveryDateStr;
	}
	public Date getFromDeliveryDateSql() {
		return fromDeliveryDateSql;
	}
	public void setFromDeliveryDateSql(Date fromDeliveryDateSql) {
		this.fromDeliveryDateSql = fromDeliveryDateSql;
	}
	public java.util.Date getFromDeliveryDateUtil() {
		return fromDeliveryDateUtil;
	}
	public void setFromDeliveryDateUtil(java.util.Date fromDeliveryDateUtil) {
		this.fromDeliveryDateUtil = fromDeliveryDateUtil;
	}
	public String getFromDeliveryDateStr() {
		return fromDeliveryDateStr;
	}
	public void setFromDeliveryDateStr(String fromDeliveryDateStr) {
		this.fromDeliveryDateStr = fromDeliveryDateStr;
	}
	public Date getToDeliveryDateSql() {
		return toDeliveryDateSql;
	}
	public void setToDeliveryDateSql(Date toDeliveryDateSql) {
		this.toDeliveryDateSql = toDeliveryDateSql;
	}
	public java.util.Date getToDeliveryDateUtil() {
		return toDeliveryDateUtil;
	}
	public void setToDeliveryDateUtil(java.util.Date toDeliveryDateUtil) {
		this.toDeliveryDateUtil = toDeliveryDateUtil;
	}
	public String getToDeliveryDateStr() {
		return toDeliveryDateStr;
	}
	public void setToDeliveryDateStr(String toDeliveryDateStr) {
		this.toDeliveryDateStr = toDeliveryDateStr;
	}
	
	
}
