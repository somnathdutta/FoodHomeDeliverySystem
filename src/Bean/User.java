package Bean;

import java.sql.Timestamp;
import java.util.Date;

public class User {

	private String userName,emailId,mobileNo,startDateValue,endDateValue,creationDateValue,password;
	private Date startDate,endDate,creationDate;
	private int noOfOrders;
	private Timestamp registrationTime;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getStartDateValue() {
		return startDateValue;
	}
	public void setStartDateValue(String startDateValue) {
		this.startDateValue = startDateValue;
	}
	public String getEndDateValue() {
		return endDateValue;
	}
	public void setEndDateValue(String endDateValue) {
		this.endDateValue = endDateValue;
	}
	public String getCreationDateValue() {
		return creationDateValue;
	}
	public void setCreationDateValue(String creationDateValue) {
		this.creationDateValue = creationDateValue;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getNoOfOrders() {
		return noOfOrders;
	}
	public void setNoOfOrders(int noOfOrders) {
		this.noOfOrders = noOfOrders;
	}
	public Timestamp getRegistrationTime() {
		return registrationTime;
	}
	public void setRegistrationTime(Timestamp registrationTime) {
		this.registrationTime = registrationTime;
	}
	
	
}
