package Bean;

import java.util.Date;

public class Ordertimings {

	private int mealId,orderTimingId;
	private String mealType,lunchFromTimingsValue,lunchToTimingsValue,dinnerFromTimingsValue,dinnerToTimingsValue;
	private Date lunchFromTimings,lunchToTimings;
	private Date dinnerFromTimings,dinnerToTimings;
	private boolean saveDisability = false,updateDisability=false;
	
	public int getMealId() {
		return mealId;
	}
	public void setMealId(int mealId) {
		this.mealId = mealId;
	}
	public String getMealType() {
		return mealType;
	}
	public void setMealType(String mealType) {
		this.mealType = mealType;
	}
	public Date getLunchFromTimings() {
		return lunchFromTimings;
	}
	public void setLunchFromTimings(Date lunchFromTimings) {
		this.lunchFromTimings = lunchFromTimings;
	}
	public Date getLunchToTimings() {
		return lunchToTimings;
	}
	public void setLunchToTimings(Date lunchToTimings) {
		this.lunchToTimings = lunchToTimings;
	}
	public Date getDinnerFromTimings() {
		return dinnerFromTimings;
	}
	public void setDinnerFromTimings(Date dinnerFromTimings) {
		this.dinnerFromTimings = dinnerFromTimings;
	}
	public Date getDinnerToTimings() {
		return dinnerToTimings;
	}
	public void setDinnerToTimings(Date dinnerToTimings) {
		this.dinnerToTimings = dinnerToTimings;
	}
	public boolean isSaveDisability() {
		return saveDisability;
	}
	public void setSaveDisability(boolean saveDisability) {
		this.saveDisability = saveDisability;
	}
	public boolean isUpdateDisability() {
		return updateDisability;
	}
	public void setUpdateDisability(boolean updateDisability) {
		this.updateDisability = updateDisability;
	}
	public int getOrderTimingId() {
		return orderTimingId;
	}
	public void setOrderTimingId(int orderTimingId) {
		this.orderTimingId = orderTimingId;
	}
	
	public String getLunchToTimingsValue() {
		return lunchToTimingsValue;
	}
	public void setLunchToTimingsValue(String lunchToTimingsValue) {
		this.lunchToTimingsValue = lunchToTimingsValue;
	}
	public String getDinnerFromTimingsValue() {
		return dinnerFromTimingsValue;
	}
	public void setDinnerFromTimingsValue(String dinnerFromTimingsValue) {
		this.dinnerFromTimingsValue = dinnerFromTimingsValue;
	}
	public String getDinnerToTimingsValue() {
		return dinnerToTimingsValue;
	}
	public void setDinnerToTimingsValue(String dinnerToTimingsValue) {
		this.dinnerToTimingsValue = dinnerToTimingsValue;
	}
	public String getLunchFromTimingsValue() {
		return lunchFromTimingsValue;
	}
	public void setLunchFromTimingsValue(String lunchFromTimingsValue) {
		this.lunchFromTimingsValue = lunchFromTimingsValue;
	}
	
}
