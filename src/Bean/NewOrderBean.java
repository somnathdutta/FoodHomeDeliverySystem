package Bean;

import java.sql.Date;

public class NewOrderBean {
	public Integer orderId;
	public String orderNo;
	public Integer rejectedKitchenId;
	public String rejectedKitchenName;
	public String rejectionReason;
	public String kitchenName ; 
	public Integer kitchenId ;
	public Boolean kitchenNameVisisbility ;
	public Boolean orderIdVisibility = true;
	public String cuisineName;
	public String categoryName;
	public Integer quantity;
	public Double price;
	public String city;
	public String area;
	public String orderBy;
	public Double totalPrice;
	public String status;
	public Integer statusId;
	public Boolean statusVisisbility = true;
	public Boolean detailsVisibility = true;
	public Boolean receiveVisibility = true;
	public Boolean receiveDisability = false;
	public Boolean rejectVisibility = true;
	public Date orderDate;
	

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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Boolean getOrderIdVisibility() {
		return orderIdVisibility;
	}
	public void setOrderIdVisibility(Boolean orderIdVisibility) {
		this.orderIdVisibility = orderIdVisibility;
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
	public Boolean getDetailsVisibility() {
		return detailsVisibility;
	}
	public void setDetailsVisibility(Boolean detailsVisibility) {
		this.detailsVisibility = detailsVisibility;
	}
	public Boolean getReceiveVisibility() {
		return receiveVisibility;
	}
	public void setReceiveVisibility(Boolean receiveVisibility) {
		this.receiveVisibility = receiveVisibility;
	}
	public Boolean getRejectVisibility() {
		return rejectVisibility;
	}
	public void setRejectVisibility(Boolean rejectVisibility) {
		this.rejectVisibility = rejectVisibility;
	}
	public Boolean getStatusVisisbility() {
		return statusVisisbility;
	}
	public void setStatusVisisbility(Boolean statusVisisbility) {
		this.statusVisisbility = statusVisisbility;
	}
	public Integer getKitchenId() {
		return kitchenId;
	}
	public void setKitchenId(Integer kitchenId) {
		this.kitchenId = kitchenId;
	}
	public Boolean getReceiveDisability() {
		return receiveDisability;
	}
	public void setReceiveDisability(Boolean receiveDisability) {
		this.receiveDisability = receiveDisability;
	}
	public Integer getStatusId() {
		return statusId;
	}
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	public String getKitchenName() {
		return kitchenName;
	}
	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}
	public Boolean getKitchenNameVisisbility() {
		return kitchenNameVisisbility;
	}
	public void setKitchenNameVisisbility(Boolean kitchenNameVisisbility) {
		this.kitchenNameVisisbility = kitchenNameVisisbility;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Integer getRejectedKitchenId() {
		return rejectedKitchenId;
	}
	public void setRejectedKitchenId(Integer rejectedKitchenId) {
		this.rejectedKitchenId = rejectedKitchenId;
	}
	public String getRejectedKitchenName() {
		return rejectedKitchenName;
	}
	public void setRejectedKitchenName(String rejectedKitchenName) {
		this.rejectedKitchenName = rejectedKitchenName;
	}
	public String getRejectionReason() {
		return rejectionReason;
	}
	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	
}
