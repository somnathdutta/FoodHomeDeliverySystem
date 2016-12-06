package Bean;

import java.sql.Timestamp;

public class Query {

	private int queryID;
	private String queryName,status, userName, userEmailId, userMessage,replyMessage;
	private Timestamp queryTime;
	private boolean isClosed = false;
	
	public int getQueryID() {
		return queryID;
	}
	public void setQueryID(int queryID) {
		this.queryID = queryID;
	}
	public String getQueryName() {
		return queryName;
	}
	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public boolean isClosed() {
		return isClosed;
	}
	public void setClosed(boolean isClosed) {
		this.isClosed = isClosed;
	}
	public String getUserEmailId() {
		return userEmailId;
	}
	public void setUserEmailId(String userEmailId) {
		this.userEmailId = userEmailId;
	}
	public String getReplyMessage() {
		return replyMessage;
	}
	public void setReplyMessage(String replyMessage) {
		this.replyMessage = replyMessage;
	}
	public String getUserMessage() {
		return userMessage;
	}
	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}
	public Timestamp getQueryTime() {
		return queryTime;
	}
	public void setQueryTime(Timestamp queryTime) {
		this.queryTime = queryTime;
	}
}
