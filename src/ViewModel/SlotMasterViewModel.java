package ViewModel;

import java.sql.Connection;
import java.util.ArrayList;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import com.google.android.gcm.server.Message;

import service.SlotMasterService;
import Bean.TimeSlot;


public class SlotMasterViewModel {

	TimeSlot timeSlotBean = new TimeSlot();
	ArrayList<TimeSlot> timeSlotBeanList = new ArrayList<TimeSlot>();
	
	Session session = null;
	private Connection connection = null;
	private String userName = "";
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		userName = (String) session.getAttribute("login");
		
		connection.setAutoCommit(true);
		
		System.out.println("zul page >> timeSlotMaster.zul");
		loadSlot();
	}
	
	public void loadSlot(){
		timeSlotBeanList = SlotMasterService.loadTimSlot(connection);
	}
	
	@Command
	@NotifyChange("*")
	public void onClickUpdate(@BindingParam("bean")TimeSlot bean){
		int i = 0;
		i = SlotMasterService.updatetimeSlot(connection, bean);
		if(i>0){
			Messagebox.show("Updated Successfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
		}
		
	}
	
	

	public TimeSlot getTimeSlotBean() {
		return timeSlotBean;
	}

	public void setTimeSlotBean(TimeSlot timeSlotBean) {
		this.timeSlotBean = timeSlotBean;
	}

	public ArrayList<TimeSlot> getTimeSlotBeanList() {
		return timeSlotBeanList;
	}

	public void setTimeSlotBeanList(ArrayList<TimeSlot> timeSlotBeanList) {
		this.timeSlotBeanList = timeSlotBeanList;
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
	
}
