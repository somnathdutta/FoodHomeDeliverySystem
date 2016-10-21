package Bean;

import java.util.ArrayList;

public class BannerBean {

	public String bannertTitle;
	
	public Integer bannerId,noOfUrls;
	
	private boolean urlGridVis;
	
	public ArrayList<UrlBean> urlBeanList = new ArrayList<UrlBean>();
	
	public UrlBean ub = new UrlBean();
	
	public ArrayList<String> urlList = new ArrayList<String>();
	
	private ArrayList<BannerBean> bannerImageList;
	
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
	public ArrayList<BannerBean> getBannerImageList() {
		return bannerImageList;
	}
	public void setBannerImageList(ArrayList<BannerBean> bannerImageList) {
		this.bannerImageList = bannerImageList;
	}
	public ArrayList<UrlBean> getUrlBeanList() {
		return urlBeanList;
	}
	public void setUrlBeanList(ArrayList<UrlBean> urlBeanList) {
		this.urlBeanList = urlBeanList;
	}
	public UrlBean getUb() {
		return ub;
	}
	public void setUb(UrlBean ub) {
		this.ub = ub;
	}
	public boolean isUrlGridVis() {
		return urlGridVis;
	}
	public void setUrlGridVis(boolean urlGridVis) {
		this.urlGridVis = urlGridVis;
	}
	
	
}
