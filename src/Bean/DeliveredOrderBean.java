package Bean;

import java.util.Date;

import org.zkoss.image.AImage;

public class DeliveredOrderBean {
	
	public Integer orderId;

	public String deliveryAddress;
	
	public String contactNumber;
	
	public String orderBy;
	
	public String emailId;
	
	public String deliveryBoy;
	
	public String deliveryBoyPhone;
	
	private String city;
	private String area;
	private Double totalPrice;
	private String itemName;
	private Boolean itemVisibility=true;
	private String subItemName;
	private AImage subItemImage;
	private String subItemImagePath;
	private AImage podImage;
	private String podPicturePath;
	public String orderStatus;
	private Date uploadingProofdate;
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
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Boolean getItemVisibility() {
		return itemVisibility;
	}
	public void setItemVisibility(Boolean itemVisibility) {
		this.itemVisibility = itemVisibility;
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
	public AImage getPodImage() {
		return podImage;
	}
	public void setPodImage(AImage podImage) {
		this.podImage = podImage;
	}
	public String getPodPicturePath() {
		return podPicturePath;
	}
	public void setPodPicturePath(String podPicturePath) {
		this.podPicturePath = podPicturePath;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Date getUploadingProofdate() {
		return uploadingProofdate;
	}
	public void setUploadingProofdate(Date uploadingProofdate) {
		this.uploadingProofdate = uploadingProofdate;
	}
	public String getDeliveryBoy() {
		return deliveryBoy;
	}
	public void setDeliveryBoy(String deliveryBoy) {
		this.deliveryBoy = deliveryBoy;
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
	public String getDeliveryBoyPhone() {
		return deliveryBoyPhone;
	}
	public void setDeliveryBoyPhone(String deliveryBoyPhone) {
		this.deliveryBoyPhone = deliveryBoyPhone;
	}
	
	
	
	
}
