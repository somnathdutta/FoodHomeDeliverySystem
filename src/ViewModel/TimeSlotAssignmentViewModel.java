package ViewModel;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;

import dao.TimeSlotDAO;
import Bean.ManageDeliveryBoyBean;
import Bean.TimeSlot;

public class TimeSlotAssignmentViewModel {

	public TimeSlot timeSlot = new TimeSlot();
	
	public ArrayList<TimeSlot> timeSlotList = new ArrayList<TimeSlot>();
	
	public ManageDeliveryBoyBean boyBean = new ManageDeliveryBoyBean();
	
	public ArrayList<ManageDeliveryBoyBean> boyList = new ArrayList<ManageDeliveryBoyBean>();
	
	Session session = null;
	
	private Connection connection = null;
	
	private String userName;
	
	private Integer roleId;
	
	private String roleName;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws SQLException{

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		
		roleId = (Integer) session.getAttribute("userRoleId");
		
		connection.setAutoCommit(true);
		
		boyList = TimeSlotDAO.fetchBikerList(connection);
		
		timeSlotList = TimeSlotDAO.fetchTimeSlotList(connection);
	}

	@Command
	@NotifyChange("*")
	public void onClickAssign(){
		/*System.out.println("boyBean.deliveryBoyUserId"+boyBean.deliveryBoyUserId);
		for(TimeSlot slot : timeSlotList){
			if(slot.checked)
				System.out.println("slot id "+slot.slotId);
		}*/
			
		TimeSlotDAO.saveTimeSlotForBiker(boyBean.deliveryBoyUserId, timeSlotList, connection);
		for(TimeSlot slot : timeSlotList){
			if(slot.checked){
				slot.checked = false;
			}
		}
		
	}
	
	public TimeSlot getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(TimeSlot timeSlot) {
		this.timeSlot = timeSlot;
	}

	public ArrayList<TimeSlot> getTimeSlotList() {
		return timeSlotList;
	}

	public void setTimeSlotList(ArrayList<TimeSlot> timeSlotList) {
		this.timeSlotList = timeSlotList;
	}

	public ManageDeliveryBoyBean getBoyBean() {
		return boyBean;
	}

	public void setBoyBean(ManageDeliveryBoyBean boyBean) {
		this.boyBean = boyBean;
	}

	public ArrayList<ManageDeliveryBoyBean> getBoyList() {
		return boyList;
	}

	public void setBoyList(ArrayList<ManageDeliveryBoyBean> boyList) {
		this.boyList = boyList;
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

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	
}
