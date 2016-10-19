package Bean;

public class ManageCityBean {
	public String cityName;
	public Integer cityId;
	public Boolean isActiveChecked=false;
	public String status;
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public Boolean getIsActiveChecked() {
		return isActiveChecked;
	}
	public void setIsActiveChecked(Boolean isActiveChecked) {
		this.isActiveChecked = isActiveChecked;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	
}
