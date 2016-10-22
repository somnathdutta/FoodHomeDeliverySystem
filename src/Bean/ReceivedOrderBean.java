package Bean;

import java.sql.Date;

import org.zkoss.image.AImage;

public class ReceivedOrderBean {
	public Integer orderId;
	public String orderNo;
	public String orderBy;
	public String contactNumber;
	public String emailId;
	public String deliveryAddress;
	public String cuisineName;
	public String categoryName;
	public Integer quantity;
	public Double price;
	public String status;
	public String city;
	public String area;
	public String itemName,itemDescription;
	public Boolean itemVisibility=true;
	public String subItemName;
	public AImage subItemImage;
	public String subItemImagePath;
	public String deliveryBoyName;
	public Integer deliveryBoyId;
	public Integer statusId;
	public String orderStatus;
	public Date orderedDate ;
	public String orderDateValue;
	
	public java.util.Date deliveryDate; 
	public Date deliveryDateSql ;
	public String deliveryDateValue;
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getSubItemName() {
		return subItemName;
	}
	public void setSubItemName(String subItemName) {
		this.subItemName = subItemName;
	}
	public AImage getSubItemImage() {
		return subItemImage;
	}
	public void setSubItemImage(AImage subItemImage) {
		this.subItemImage = subItemImage;
	}
	public String getSubItemImagePath() {
		return subItemImagePath;
	}
	public void setSubItemImagePath(String subItemImagePath) {
		this.subItemImagePath = subItemImagePath;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getDeliveryBoyName() {
		return deliveryBoyName;
	}
	public void setDeliveryBoyName(String deliveryBoyName) {
		this.deliveryBoyName = deliveryBoyName;
	}
	public Integer getDeliveryBoyId() {
		return deliveryBoyId;
	}
	public void setDeliveryBoyId(Integer deliveryBoyId) {
		this.deliveryBoyId = deliveryBoyId;
	}
	public Boolean getItemVisibility() {
		return itemVisibility;
	}
	public void setItemVisibility(Boolean itemVisibility) {
		this.itemVisibility = itemVisibility;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Integer getStatusId() {
		return statusId;
	}
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
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
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public Date getOrderedDate() {
		return orderedDate;
	}
	public void setOrderedDate(Date orderedDate) {
		this.orderedDate = orderedDate;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
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
	public String getDeliveryAddress() {
		return deliveryAddress;
	}
	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	public String getOrderDateValue() {
		return orderDateValue;
	}
	public void setOrderDateValue(String orderDateValue) {
		this.orderDateValue = orderDateValue;
	}
	public String getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	public java.util.Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(java.util.Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public Date getDeliveryDateSql() {
		return deliveryDateSql;
	}
	public void setDeliveryDateSql(Date deliveryDateSql) {
		this.deliveryDateSql = deliveryDateSql;
	}
	public String getDeliveryDateValue() {
		return deliveryDateValue;
	}
	public void setDeliveryDateValue(String deliveryDateValue) {
		this.deliveryDateValue = deliveryDateValue;
	}
	
	
}
