package Bean;

import java.util.Date;

public class ManageCouponsBean {
	public String couponCode;
	public Integer couponId;
	public Integer usesPerUser;
	public String discountType;
	public Integer discountTypeId;
	public Double discountAmount;
	public Date fromDate;
	public Date toDate;
	public Boolean isActiveChecked=false;
	public String status;
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public Integer getUsesPerUser() {
		return usesPerUser;
	}
	public void setUsesPerUser(Integer usesPerUser) {
		this.usesPerUser = usesPerUser;
	}
	public String getDiscountType() {
		return discountType;
	}
	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}
	public Double getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public Boolean getIsActiveChecked() {
		return isActiveChecked;
	}
	public void setIsActiveChecked(Boolean isActiveChecked) {
		this.isActiveChecked = isActiveChecked;
	}
	public Integer getDiscountTypeId() {
		return discountTypeId;
	}
	public void setDiscountTypeId(Integer discountTypeId) {
		this.discountTypeId = discountTypeId;
	}
	public Integer getCouponId() {
		return couponId;
	}
	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
