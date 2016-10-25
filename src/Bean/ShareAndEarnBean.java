package Bean;

public class ShareAndEarnBean {

	private Integer shareAndEarnId;
	private String appMsg;
	private String invtMsg;
	private String imgUrl;
	private Double amnt;
	
	private boolean insertDivVis;
	
	public Integer getShareAndEarnId() {
		return shareAndEarnId;
	}
	public void setShareAndEarnId(Integer shareAndEarnId) {
		this.shareAndEarnId = shareAndEarnId;
	}
	public String getAppMsg() {
		return appMsg;
	}
	public void setAppMsg(String appMsg) {
		this.appMsg = appMsg;
	}
	public String getInvtMsg() {
		return invtMsg;
	}
	public void setInvtMsg(String invtMsg) {
		this.invtMsg = invtMsg;
	}
	public Double getAmnt() {
		return amnt;
	}
	public void setAmnt(Double amnt) {
		this.amnt = amnt;
	}
	public boolean isInsertDivVis() {
		return insertDivVis;
	}
	public void setInsertDivVis(boolean insertDivVis) {
		this.insertDivVis = insertDivVis;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
}
