package Bean;

import java.sql.Date;

import org.zkoss.image.AImage;

public class ManageDealBean {
	public String cityName;
	public Integer cityId;
	public Integer areaId;
	public Integer dealId;
	public String areaName;
	public String title;
	public String dealBannerPicturePath;
	public Date fromDate;
	public Date toDate;
	public Boolean isActiveChecked=false;
	public AImage databaseBannerImage;
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDealBannerPicturePath() {
		return dealBannerPicturePath;
	}
	public void setDealBannerPicturePath(String dealBannerPicturePath) {
		this.dealBannerPicturePath = dealBannerPicturePath;
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
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public Integer getDealId() {
		return dealId;
	}
	public void setDealId(Integer dealId) {
		this.dealId = dealId;
	}
	public AImage getDatabaseBannerImage() {
		return databaseBannerImage;
	}
	public void setDatabaseBannerImage(AImage databaseBannerImage) {
		this.databaseBannerImage = databaseBannerImage;
	}
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	
}
