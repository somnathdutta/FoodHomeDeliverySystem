package Bean;

import java.util.Date;

public class OrderRejectionBean {

	public Integer orderId;
	
	public String orderNo ; 
	
	public Integer kitchenId;
	
	public String rejectReason;
	
	public Date rejectiondate = new java.util.Date() ;
	
	public Date orderDate ;
	
	public String orderDateValue;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public Date getRejectiondate() {
		return rejectiondate;
	}

	public void setRejectiondate(Date rejectiondate) {
		this.rejectiondate = rejectiondate;
	}

	public Integer getKitchenId() {
		return kitchenId;
	}

	public void setKitchenId(Integer kitchenId) {
		this.kitchenId = kitchenId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	
}
