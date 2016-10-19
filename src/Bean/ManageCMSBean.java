package Bean;

import org.zkoss.image.AImage;

public class ManageCMSBean {
	public String cityName;
	public String areaName;
	public Integer cityId;
	public Integer areaId;
	public String pageTitle;
	public String pageContent;
	public String pageBanner;
	public AImage bannerImage;
	public String pageBannerPicturePath;
	public Boolean showInAppChecked=false;
	public String status;
	public Integer pageId;
	public String inAppMenu;
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public String getPageContent() {
		return pageContent;
	}
	public void setPageContent(String pageContent) {
		this.pageContent = pageContent;
	}
	public Boolean getShowInAppChecked() {
		return showInAppChecked;
	}
	public void setShowInAppChecked(Boolean showInAppChecked) {
		this.showInAppChecked = showInAppChecked;
	}
	public String getPageBanner() {
		return pageBanner;
	}
	public void setPageBanner(String pageBanner) {
		this.pageBanner = pageBanner;
	}
	public String getPageBannerPicturePath() {
		return pageBannerPicturePath;
	}
	public void setPageBannerPicturePath(String pageBannerPicturePath) {
		this.pageBannerPicturePath = pageBannerPicturePath;
	}
	public Integer getPageId() {
		return pageId;
	}
	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}
	public String getInAppMenu() {
		return inAppMenu;
	}
	public void setInAppMenu(String inAppMenu) {
		this.inAppMenu = inAppMenu;
	}
	public AImage getBannerImage() {
		return bannerImage;
	}
	public void setBannerImage(AImage bannerImage) {
		this.bannerImage = bannerImage;
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
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
