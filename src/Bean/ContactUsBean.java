package Bean;

public class ContactUsBean {
	
	private Integer contactusId;
	private String contNo,contactMessage;
	
	private boolean insertDivVis;

	public Integer getContactusId() {
		return contactusId;
	}

	public void setContactusId(Integer contactusId) {
		this.contactusId = contactusId;
	}

	public String getContNo() {
		return contNo;
	}

	public void setContNo(String contNo) {
		this.contNo = contNo;
	}

	public boolean isInsertDivVis() {
		return insertDivVis;
	}

	public void setInsertDivVis(boolean insertDivVis) {
		this.insertDivVis = insertDivVis;
	}

	public String getContactMessage() {
		return contactMessage;
	}

	public void setContactMessage(String contactMessage) {
		this.contactMessage = contactMessage;
	}

}
