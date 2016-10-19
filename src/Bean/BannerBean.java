package Bean;

import java.util.ArrayList;

public class BannerBean {

	public String bannertTitle;
	public Integer bannerId,noOfUrls;
	ArrayList<String> urlList = new ArrayList<String>();
	public String getBannertTitle() {
		return bannertTitle;
	}
	public void setBannertTitle(String bannertTitle) {
		this.bannertTitle = bannertTitle;
	}
	public Integer getBannerId() {
		return bannerId;
	}
	public void setBannerId(Integer bannerId) {
		this.bannerId = bannerId;
	}
	public ArrayList<String> getUrlList() {
		return urlList;
	}
	public void setUrlList(ArrayList<String> urlList) {
		this.urlList = urlList;
	}
	public Integer getNoOfUrls() {
		return noOfUrls;
	}
	public void setNoOfUrls(Integer noOfUrls) {
		this.noOfUrls = noOfUrls;
	}
	
	
}
