package ViewModel;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import dao.UserDAO;
import Bean.User;

public class UserViewModel {
	User user = new User();

	ArrayList<User> userList = new ArrayList<User>();

	Session session = null;

	private Connection connection = null;

	private String userName, mobileNo ;

	private Date startDate, endDate;

	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);

		session= Sessions.getCurrent();

		connection=(Connection) session.getAttribute("sessionConnection");

		userName = (String) session.getAttribute("login");

		connection.setAutoCommit(true);

		onLoadQuery();

	}

	public void onLoadQuery(){
		userList = UserDAO.showUsers(connection);
		System.out.println("user list::"+userList.size());

	}

	@Command
	@NotifyChange("*")
	public void onClickRefresh(){
		clear();
	}
	
	@Command
	@NotifyChange("*")
	public void onChangeMobileNo(){
		userList = UserDAO.showSpecificUser(connection, mobileNo);
	}
	
	@Command
	@NotifyChange("*")
	public void onClickFind(){
		if(startDate!=null && endDate!=null){
			userList = UserDAO.showUsersBetweenDates(connection, startDate, endDate);
		}
		else if(mobileNo!=null){
			userList = UserDAO.showSpecificUser(connection, mobileNo);
		}else{
			Messagebox.show("No search value given!","Information",Messagebox.OK,Messagebox.EXCLAMATION);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickClear(){
		clear();
		onLoadQuery();
	}
	
	@Command
	@NotifyChange("*")
	public void onClickGenerate(){
		UserDAO.generateExcel(userList);
	}
	
	public void clear(){
		startDate = null;
		endDate = null;
		mobileNo = null;
		userList = UserDAO.showUsers(connection);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ArrayList<User> getUserList() {
		return userList;
	}

	public void setUserList(ArrayList<User> userList) {
		this.userList = userList;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
}
