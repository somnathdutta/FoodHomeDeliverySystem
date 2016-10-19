package Bean;

public class ManageAreaBean {
	public String areaName;
	public Integer areaId;
	public String cityName;
	public Integer cityId;
	public Boolean cityNameVisibility=true;
	public String status;
	public Boolean isActiveChecked=false;
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
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
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Boolean getCityNameVisibility() {
		return cityNameVisibility;
	}
	public void setCityNameVisibility(Boolean cityNameVisibility) {
		this.cityNameVisibility = cityNameVisibility;
	}
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	
}
