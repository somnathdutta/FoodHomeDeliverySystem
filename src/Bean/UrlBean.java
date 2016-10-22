package Bean;

public class UrlBean {
	public String urlName;
	public Integer urlId;
	public String bannerName;
	public Integer bannerId;
	public boolean divVis;
	public String activeStatus;
	
	public boolean checkedUrl = false;
	public UrlBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UrlBean(String urlName, Integer urlId) {
		super();
		this.urlName = urlName;
		this.urlId = urlId;
	}

	public String getUrlName() {
		return urlName;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}

	public Integer getUrlId() {
		return urlId;
	}

	public void setUrlId(Integer urlId) {
		this.urlId = urlId;
	}

	public boolean isCheckedUrl() {
		return checkedUrl;
	}

	public void setCheckedUrl(boolean checkedUrl) {
		this.checkedUrl = checkedUrl;
	}

	public String getBannerName() {
		return bannerName;
	}

	public void setBannerName(String bannerName) {
		this.bannerName = bannerName;
	}

	public Integer getBannerId() {
		return bannerId;
	}

	public void setBannerId(Integer bannerId) {
		this.bannerId = bannerId;
	}

	public boolean isDivVis() {
		return divVis;
	}

	public void setDivVis(boolean divVis) {
		this.divVis = divVis;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}
	
}
