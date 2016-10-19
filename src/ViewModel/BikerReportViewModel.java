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

import dao.BikerTrackReportDAO;
import Bean.BikerTrack;

public class BikerReportViewModel {

	ArrayList<BikerTrack> bikerTrackReportList = new ArrayList<BikerTrack>();
	
	Session session = null;
	
	private Connection connection = null;
	
	private String userName = "";
	
	public Date startDate ,endDate;
	
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
		bikerTrackReportList = BikerTrackReportDAO.fetchBikerTrackReport(connection,null,null);
	}

	@Command
	@NotifyChange("*")
	public void onClickFind(){
		findDataBetweenDateRange();
	}
	
	@Command
	@NotifyChange("*")
	public void onClickClear(){
		clear();
	}
	
	@Command
	@NotifyChange("*")
	public void onClickGenerate(){
		generateExcel();
	}
	
	public void findDataBetweenDateRange(){
		bikerTrackReportList = BikerTrackReportDAO.fetchBikerTrackReport(connection,startDate,endDate);
	}
	
	public void clear(){
		startDate = null;
		endDate = null;
		onLoadQuery();
	}
	
	public void generateExcel(){
		BikerTrackReportDAO.generateExcel(bikerTrackReportList);
	}
	
	public ArrayList<BikerTrack> getBikerTrackReportList() {
		return bikerTrackReportList;
	}

	public void setBikerTrackReportList(ArrayList<BikerTrack> bikerTrackReportList) {
		this.bikerTrackReportList = bikerTrackReportList;
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
	
}
