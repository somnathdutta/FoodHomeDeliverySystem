package Bean;

import java.util.Date;

public class LogisticsPaymentReportBean {

	private String orderNo;
	
	private Integer bikerId;
	private String bikerName;
	
	private String paymentMode;
	private Double amount;
	
	private String deliveryFormDateStr;
	private Date deliveryFormDateUtil;
	private Date deliveryFormDateSql;
	
	private String deliveryToDateStr;
	private Date deliveryToDateUtil;
	private Date deliveryToDateSql;
	
	private String deliveryDateStr;
	private Date deliveryDateUtil;
	private Date deliveryDateSql;
	
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getBikerName() {
		return bikerName;
	}
	public void setBikerName(String bikerName) {
		this.bikerName = bikerName;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getDeliveryFormDateStr() {
		return deliveryFormDateStr;
	}
	public void setDeliveryFormDateStr(String deliveryFormDateStr) {
		this.deliveryFormDateStr = deliveryFormDateStr;
	}
	public Date getDeliveryFormDateUtil() {
		return deliveryFormDateUtil;
	}
	public void setDeliveryFormDateUtil(Date deliveryFormDateUtil) {
		this.deliveryFormDateUtil = deliveryFormDateUtil;
	}
	public Date getDeliveryFormDateSql() {
		return deliveryFormDateSql;
	}
	public void setDeliveryFormDateSql(Date deliveryFormDateSql) {
		this.deliveryFormDateSql = deliveryFormDateSql;
	}
	public String getDeliveryToDateStr() {
		return deliveryToDateStr;
	}
	public void setDeliveryToDateStr(String deliveryToDateStr) {
		this.deliveryToDateStr = deliveryToDateStr;
	}
	public Date getDeliveryToDateUtil() {
		return deliveryToDateUtil;
	}
	public void setDeliveryToDateUtil(Date deliveryToDateUtil) {
		this.deliveryToDateUtil = deliveryToDateUtil;
	}
	public Date getDeliveryToDateSql() {
		return deliveryToDateSql;
	}
	public void setDeliveryToDateSql(Date deliveryToDateSql) {
		this.deliveryToDateSql = deliveryToDateSql;
	}
	public String getDeliveryDateStr() {
		return deliveryDateStr;
	}
	public void setDeliveryDateStr(String deliveryDateStr) {
		this.deliveryDateStr = deliveryDateStr;
	}
	public Date getDeliveryDateUtil() {
		return deliveryDateUtil;
	}
	public void setDeliveryDateUtil(Date deliveryDateUtil) {
		this.deliveryDateUtil = deliveryDateUtil;
	}
	public Date getDeliveryDateSql() {
		return deliveryDateSql;
	}
	public void setDeliveryDateSql(Date deliveryDateSql) {
		this.deliveryDateSql = deliveryDateSql;
	}
	public Integer getBikerId() {
		return bikerId;
	}
	public void setBikerId(Integer bikerId) {
		this.bikerId = bikerId;
	}
	
	
}
