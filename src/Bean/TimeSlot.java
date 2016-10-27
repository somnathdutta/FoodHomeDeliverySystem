package Bean;

public class TimeSlot {

	public int slotId;
	public String timeSlot;
	public String status;
	public boolean checked = false;
	
	private String lunchStatus;
	private String dinnerStatus;
	
	public int getSlotId() {
		return slotId;
	}
	public void setSlotId(int slotId) {
		this.slotId = slotId;
	}
	public String getTimeSlot() {
		return timeSlot;
	}
	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public String getLunchStatus() {
		return lunchStatus;
	}
	public void setLunchStatus(String lunchStatus) {
		this.lunchStatus = lunchStatus;
	}
	public String getDinnerStatus() {
		return dinnerStatus;
	}
	public void setDinnerStatus(String dinnerStatus) {
		this.dinnerStatus = dinnerStatus;
	}
	
	
}
