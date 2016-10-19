package Bean;

public class UrlBean {
	public String urlName;
	public Integer urlId;
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
	
}
