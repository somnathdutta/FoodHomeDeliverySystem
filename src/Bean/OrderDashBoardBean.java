package Bean;

import java.sql.Date;

public class OrderDashBoardBean {

	public Integer orderId;
	public String orderNo,timeSlot,statusStyle,rowStyle="background-color: #ffffe6";
	public boolean orderNoVisibiliity = true;
	public boolean orderDateVisibility = true;
	public boolean orderByVisibility = true;
	public boolean contactNoVisibility = true;
	public boolean mealTypeVisibility = true;
	public boolean deliveryDateVisibility = true,
			orderReceived = false,orderNotified = false,
			orderDelivered = false,receiveVisibility = true,
			notifyVisibility= true,deliverVisibility = true,
			deliveryZoneVisibility = true, deliveryAddressVisibility = true,
			deliveryInstructionVisibility = true;
	public Date orderdate;
	public String orderDateValue;
	public String orderStatus;
	public String orderBy;
	public String mealType,itemCode,deliveryZone,deliveryAddress,deliveryInstruction;
	public Date deliveryDate;
	public String deliveryDateValue;
	public String contactNo;
	public String orderItem,itemDescription;
	public Integer quantity;
	public Double price;
	public String kitchenName;
	public String driverName;
	public String driverNumber;
	public String received;
	public String notified;
	public String rejected,picked;
	public String delivered;
	public String orderCreationTime;
	public boolean orderCreationTimeVis;
	public boolean orderAssignTimeVis;
	public boolean timeSlotVis;
	public Date orderAssignTime;
	
	public String orderAssignTimeValue;
	
	public Date acceptanceTime;
	
	public String acceptanceTimeValue;
	
	public Date notifyTime;
	
	public String notifyTimeValue;
	
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
	public Date getOrderdate() {
		return orderdate;
	}
	public void setOrderdate(Date orderdate) {
		this.orderdate = orderdate;
	}
	public String getOrderDateValue() {
		return orderDateValue;
	}
	public void setOrderDateValue(String orderDateValue) {
		this.orderDateValue = orderDateValue;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public String getMealType() {
		return mealType;
	}
	public void setMealType(String mealType) {
		this.mealType = mealType;
	}
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getDeliveryDateValue() {
		return deliveryDateValue;
	}
	public void setDeliveryDateValue(String deliveryDateValue) {
		this.deliveryDateValue = deliveryDateValue;
	}
	public String getContactNo() {
		return contactNo;
	}
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	public String getOrderItem() {
		return orderItem;
	}
	public void setOrderItem(String orderItem) {
		this.orderItem = orderItem;
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
	public String getKitchenName() {
		return kitchenName;
	}
	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getDriverNumber() {
		return driverNumber;
	}
	public void setDriverNumber(String driverNumber) {
		this.driverNumber = driverNumber;
	}
	public String getReceived() {
		return received;
	}
	public void setReceived(String received) {
		this.received = received;
	}
	public String getNotified() {
		return notified;
	}
	public void setNotified(String notified) {
		this.notified = notified;
	}
	public String getRejected() {
		return rejected;
	}
	public void setRejected(String rejected) {
		this.rejected = rejected;
	}
	public String getDelivered() {
		return delivered;
	}
	public void setDelivered(String delivered) {
		this.delivered = delivered;
	}
	public boolean isOrderNoVisibiliity() {
		return orderNoVisibiliity;
	}
	public void setOrderNoVisibiliity(boolean orderNoVisibiliity) {
		this.orderNoVisibiliity = orderNoVisibiliity;
	}
	public boolean isOrderDateVisibility() {
		return orderDateVisibility;
	}
	public void setOrderDateVisibility(boolean orderDateVisibility) {
		this.orderDateVisibility = orderDateVisibility;
	}
	public boolean isOrderByVisibility() {
		return orderByVisibility;
	}
	public void setOrderByVisibility(boolean orderByVisibility) {
		this.orderByVisibility = orderByVisibility;
	}
	public boolean isMealTypeVisibility() {
		return mealTypeVisibility;
	}
	public void setMealTypeVisibility(boolean mealTypeVisibility) {
		this.mealTypeVisibility = mealTypeVisibility;
	}
	public boolean isDeliveryDateVisibility() {
		return deliveryDateVisibility;
	}
	public void setDeliveryDateVisibility(boolean deliveryDateVisibility) {
		this.deliveryDateVisibility = deliveryDateVisibility;
	}
	public boolean isContactNoVisibility() {
		return contactNoVisibility;
	}
	public void setContactNoVisibility(boolean contactNoVisibility) {
		this.contactNoVisibility = contactNoVisibility;
	}
	public String getPicked() {
		return picked;
	}
	public void setPicked(String picked) {
		this.picked = picked;
	}
	public String getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public Date getOrderAssignTime() {
		return orderAssignTime;
	}
	public void setOrderAssignTime(Date orderAssignTime) {
		this.orderAssignTime = orderAssignTime;
	}
	public String getOrderAssignTimeValue() {
		return orderAssignTimeValue;
	}
	public void setOrderAssignTimeValue(String orderAssignTimeValue) {
		this.orderAssignTimeValue = orderAssignTimeValue;
	}
	public Date getAcceptanceTime() {
		return acceptanceTime;
	}
	public void setAcceptanceTime(Date acceptanceTime) {
		this.acceptanceTime = acceptanceTime;
	}
	public String getAcceptanceTimeValue() {
		return acceptanceTimeValue;
	}
	public void setAcceptanceTimeValue(String acceptanceTimeValue) {
		this.acceptanceTimeValue = acceptanceTimeValue;
	}
	public Date getNotifyTime() {
		return notifyTime;
	}
	public void setNotifyTime(Date notifyTime) {
		this.notifyTime = notifyTime;
	}
	public String getNotifyTimeValue() {
		return notifyTimeValue;
	}
	public void setNotifyTimeValue(String notifyTimeValue) {
		this.notifyTimeValue = notifyTimeValue;
	}
	public String getTimeSlot() {
		return timeSlot;
	}
	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}
	public boolean isOrderReceived() {
		return orderReceived;
	}
	public void setOrderReceived(boolean orderReceived) {
		this.orderReceived = orderReceived;
	}
	public boolean isOrderNotified() {
		return orderNotified;
	}
	public void setOrderNotified(boolean orderNotified) {
		this.orderNotified = orderNotified;
	}
	public boolean isOrderDelivered() {
		return orderDelivered;
	}
	public void setOrderDelivered(boolean orderDelivered) {
		this.orderDelivered = orderDelivered;
	}
	public boolean isReceiveVisibility() {
		return receiveVisibility;
	}
	public void setReceiveVisibility(boolean receiveVisibility) {
		this.receiveVisibility = receiveVisibility;
	}
	public boolean isNotifyVisibility() {
		return notifyVisibility;
	}
	public void setNotifyVisibility(boolean notifyVisibility) {
		this.notifyVisibility = notifyVisibility;
	}
	public boolean isDeliverVisibility() {
		return deliverVisibility;
	}
	public void setDeliverVisibility(boolean deliverVisibility) {
		this.deliverVisibility = deliverVisibility;
	}
	public String getOrderCreationTime() {
		return orderCreationTime;
	}
	public void setOrderCreationTime(String orderCreationTime) {
		this.orderCreationTime = orderCreationTime;
	}
	public boolean isOrderCreationTimeVis() {
		return orderCreationTimeVis;
	}
	public void setOrderCreationTimeVis(boolean orderCreationTimeVis) {
		this.orderCreationTimeVis = orderCreationTimeVis;
	}
	public boolean isOrderAssignTimeVis() {
		return orderAssignTimeVis;
	}
	public void setOrderAssignTimeVis(boolean orderAssignTimeVis) {
		this.orderAssignTimeVis = orderAssignTimeVis;
	}
	public boolean isTimeSlotVis() {
		return timeSlotVis;
	}
	public void setTimeSlotVis(boolean timeSlotVis) {
		this.timeSlotVis = timeSlotVis;
	}
	public String getDeliveryZone() {
		return deliveryZone;
	}
	public void setDeliveryZone(String deliveryZone) {
		this.deliveryZone = deliveryZone;
	}
	public String getDeliveryAddress() {
		return deliveryAddress;
	}
	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	public String getDeliveryInstruction() {
		return deliveryInstruction;
	}
	public void setDeliveryInstruction(String deliveryInstruction) {
		this.deliveryInstruction = deliveryInstruction;
	}
	public boolean isDeliveryZoneVisibility() {
		return deliveryZoneVisibility;
	}
	public void setDeliveryZoneVisibility(boolean deliveryZoneVisibility) {
		this.deliveryZoneVisibility = deliveryZoneVisibility;
	}
	public boolean isDeliveryAddressVisibility() {
		return deliveryAddressVisibility;
	}
	public void setDeliveryAddressVisibility(boolean deliveryAddressVisibility) {
		this.deliveryAddressVisibility = deliveryAddressVisibility;
	}
	public boolean isDeliveryInstructionVisibility() {
		return deliveryInstructionVisibility;
	}
	public void setDeliveryInstructionVisibility(
			boolean deliveryInstructionVisibility) {
		this.deliveryInstructionVisibility = deliveryInstructionVisibility;
	}
	public String getStatusStyle() {
		return statusStyle;
	}
	public void setStatusStyle(String statusStyle) {
		this.statusStyle = statusStyle;
	}
	public String getRowStyle() {
		return rowStyle;
	}
	public void setRowStyle(String rowStyle) {
		this.rowStyle = rowStyle;
	}
	
	
	
}
