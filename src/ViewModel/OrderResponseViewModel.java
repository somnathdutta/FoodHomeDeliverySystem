package ViewModel;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.AMedia;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import Bean.DelayProductionBean;
import Bean.OrderResponseBean;

public class OrderResponseViewModel {

	Session session = null;
	
	private Connection connection = null;
	
	private String username = "";
	
	private Integer roleId = 0;

	private Boolean averageRadio = false;
	
	private Boolean highestRadio = false;
	
	public String kitchenName = null;
	
	private OrderResponseBean orderResponseBean = new OrderResponseBean();
	
	private ArrayList<OrderResponseBean> orderResponseBeanList = new ArrayList<OrderResponseBean>();
	
	private Date startDate;
	
	private Date endDate;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		username = (String) session.getAttribute("login");
		
		roleId = (Integer) session.getAttribute("userRoleId");
		
		connection.setAutoCommit(true);
		
	}
	
	public void onLoadQuery(Boolean avg, Boolean high, Boolean dategiven){
		if(orderResponseBeanList.size()>0){
			orderResponseBeanList.clear();
		}
		System.out.println("Avg ->"+avg+" high->"+high+" dategiven - >"+dategiven);
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql ="select * from vw_response_delay";
					
					String sql1 ="select * from vw_response_delay order by delay_in_response DESC limit 5";
					
					String sqlAvgWithDate = "select * from vw_response_delay where order_date>= ? and order_date<= ?";
					
					String sqlTopWithDate = "select * from vw_response_delay where order_date>= ? and order_date<= ?"
										+ " order by delay_in_response DESC limit 5";
					try {
						if(avg && dategiven==false){
							preparedStatement = connection.prepareStatement(sql);
						}else if(avg && dategiven){
							preparedStatement = connection.prepareStatement(sqlAvgWithDate);
							preparedStatement.setDate(1, new java.sql.Date(startDate.getTime()) );
							preparedStatement.setDate(2, new java.sql.Date(endDate.getTime()) );
						}else if(high && dategiven){
							preparedStatement = connection.prepareStatement(sqlTopWithDate);
							preparedStatement.setDate(1, new java.sql.Date(startDate.getTime()) );
							preparedStatement.setDate(2, new java.sql.Date(endDate.getTime()) );
						}else if(high){
							preparedStatement = connection.prepareStatement(sql1);
						}
						System.out.println(preparedStatement);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							OrderResponseBean orderResponseBean = new OrderResponseBean();
							orderResponseBean.orderNo = resultSet.getString("order_no");
							String orderDate="",reformattedOrderDate="";
							SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
							SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
							DateFormat dateFormat = new SimpleDateFormat(" HH:mm:ss");
							orderDate = resultSet.getString("order_date");
							java.util.Date stdate = null;
							try {
								 reformattedOrderDate = myFormat.format(fromUser.parse(orderDate));
								 stdate = myFormat.parse(reformattedOrderDate); 
							} catch (Exception e) {
								e.printStackTrace();
							}
							orderResponseBean.orderDateValue =  reformattedOrderDate;
							orderResponseBean.kitchenName = resultSet.getString("kitchen_name");
							if( resultSet.getTimestamp("notified_time") !=null){
								orderResponseBean.notifyTime = resultSet.getTimestamp("notified_time");
								orderResponseBean.notifyTimeValue = dateFormat.format(orderResponseBean.notifyTime );
							}
							if( resultSet.getTimestamp("driver_arrival_time") !=null){
								orderResponseBean.arrivalTime = resultSet.getTimestamp("driver_arrival_time");
								orderResponseBean.arrivalTimeValue = dateFormat.format(orderResponseBean.arrivalTime );
							}
							
							/*orderResponseBean.orderResponseTime = resultSet.getTimestamp("driver_pickup_time");
							orderResponseBean.orderResponseTimeValue = dateFormat.format(orderResponseBean.orderResponseTime);*/
							if(resultSet.getTimestamp("driver_pickup_time")!=null){
								orderResponseBean.orderResponseTime = resultSet.getTimestamp("driver_pickup_time");
								orderResponseBean.orderResponseTimeValue = dateFormat.format(orderResponseBean.orderResponseTime);
							}else{
								orderResponseBean.orderResponseTimeValue = "NA";
							}
							if(resultSet.getString("delay_in_pickup")!=null){
								orderResponseBean.delayInResponse = resultSet.getString("delay_in_pickup");
								int minutes =toMins(orderResponseBean.delayInResponse);
								orderResponseBean.delayInMinutes = minutes;
								orderResponseBean.delayByBoy = orderResponseBean.delayInResponse;
							}else{
								orderResponseBean.delayInMinutes = 0;
							}
							orderResponseBeanList.add(orderResponseBean);
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("ERROR Due to :"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
						if(resultSet!=null){
							resultSet.close();
						}
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private int toMins(String str) {
	    boolean space = false;int hour,mins,hoursInMins;
		if(str.contains(" ")){
			space = true;
		}
		if(space){
			String[] splitted = str.split(" ");
			String[] hourMin = splitted[2].split(":");
		    hour = Integer.parseInt(hourMin[0]);
		    mins = Integer.parseInt(hourMin[1]);
		    hoursInMins = hour * 60;
		}else{
			String[] hourMin = str.split(":");
		    hour = Integer.parseInt(hourMin[0]);
		    mins = Integer.parseInt(hourMin[1]);
		    hoursInMins = hour * 60;
		}
	    return hoursInMins + mins;
	}
	
	@Command
	@NotifyChange("*")
	public void onCheckFirstRadio() throws ParseException{
		if(highestRadio){
			highestRadio = false;
		}
		startDate = null;endDate=null;kitchenName= null;
		System.out.println("When first checked first-"+averageRadio+" second-"+highestRadio);
		onLoadQuery(averageRadio, highestRadio , false);
	}
	
	@Command
	@NotifyChange("*")
	public void onCheckSecondRadio() throws ParseException{
		if(averageRadio){
			averageRadio = false;
		}
		startDate = null;endDate=null;kitchenName= null;
		System.out.println("When second checked first-"+averageRadio+" second-"+highestRadio);
		onLoadQuery(averageRadio, highestRadio , false);
	}

	@Command
	@NotifyChange("*")
	public void onClickFind(){
		if(averageRadio==false && highestRadio==false){
			Messagebox.show("Please choose one of the options","Information",Messagebox.OK,Messagebox.EXCLAMATION);
		}else if(startDate==null && endDate==null){
			Messagebox.show("Date range required!","Information",Messagebox.OK,Messagebox.EXCLAMATION);
		}else if(startDate==null && endDate!=null){
			Messagebox.show("Start date required!","Information",Messagebox.OK,Messagebox.EXCLAMATION);
		}else if(startDate!=null && endDate==null){
			Messagebox.show("End date required!","Information",Messagebox.OK,Messagebox.EXCLAMATION);
		}else{
			System.out.println("OK");
			boolean dateGiven = true;
			onLoadQuery(averageRadio, highestRadio, dateGiven);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickClear(){
		if(orderResponseBeanList.size()>0){
			orderResponseBeanList.clear();
		}
		if(startDate!=null){
			startDate = null;
		}
		if(endDate!=null){
			endDate = null;
		}
		onLoadQuery(averageRadio, highestRadio, false);
	}
	
	@Command
	@NotifyChange("*")
	public void onClickGenerate(){
		 File f = null;
	      boolean bool = false;
	    if(orderResponseBeanList.size()>0){
	    	try{
		         // create new file
		    	 String realPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
		    	 String reportNamewithPath = null;
		  		if(averageRadio){
		  			reportNamewithPath = realPath + "averageorderresponsereport.csv";
		  		}else{
		  			reportNamewithPath = realPath + "highestorderresponsereport.csv";
		  		}
		  		
		  		System.out.println(reportNamewithPath);
		  		f = new File(reportNamewithPath); 
		  		bool = f.createNewFile();
		        //f = new File("C:/Users/somnathd/Desktop/report.csv");
		  		
		  		 // tries to create new file in the system
		         if(f.exists()){
		         // deletes file from the system
		         bool = f.delete();
		         System.out.println("File deleted: "+bool);
		         // delete() is invoked
		         bool = f.createNewFile();
		         System.out.println("File created: "+bool);
		         }
		         
		         FileOutputStream fileOutputStream = new FileOutputStream(f);
		         OutputStreamWriter osw = new OutputStreamWriter(fileOutputStream);    
		            Writer w = new BufferedWriter(osw);
		            w.write("ORDER DATE,VENDOR NAME,ORDER NO,NOTIFICATION TIME,ARRIVAL TIME,PICKUP TIME,DELAY\n");
		            for(int i=0;i<orderResponseBeanList.size();i++){
		            	w.write(orderResponseBeanList.get(i).orderDateValue+","+orderResponseBeanList.get(i).kitchenName
		            			+","+orderResponseBeanList.get(i).orderNo+","+orderResponseBeanList.get(i).notifyTimeValue
		            			+","+orderResponseBeanList.get(i).arrivalTimeValue
		            			+","+orderResponseBeanList.get(i).orderResponseTimeValue+","+orderResponseBeanList.get(i).delayInMinutes+"\n");
		            }
		            w.close();
		           Desktop.getDesktop().open(f);
		           
		            FileInputStream fis = new FileInputStream(new File(reportNamewithPath));
		    		byte[] ba1 = new byte[1024];
		    		int baLength;
		    		ByteArrayOutputStream bios = new ByteArrayOutputStream();
		    		try {
		    			try {
		    				while ((baLength = fis.read(ba1)) != -1) {
		    					bios.write(ba1, 0, baLength);
		    				}
		    			} catch (Exception e1) {
		    			} finally {
		    				fis.close();
		    			}
		    			if(averageRadio){
		    				final AMedia amedia = new AMedia("averageorderresponsereport", "csv", "application/csv", bios.toByteArray());
			    			Filedownload.save(amedia);
		    			}else{
		    				final AMedia amedia = new AMedia("highestorderresponsereport", "csv", "application/csv", bios.toByteArray());
			    			Filedownload.save(amedia);
		    			}
		    			
		    		}catch(Exception exception){		
		    		}
		      }catch(Exception e){
		         e.printStackTrace();
		      }
	    }else{
	    	Messagebox.show("No DATA found !","Information",Messagebox.OK,Messagebox.EXCLAMATION);
	    }     
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Boolean getAverageRadio() {
		return averageRadio;
	}

	public void setAverageRadio(Boolean averageRadio) {
		this.averageRadio = averageRadio;
	}

	public Boolean getHighestRadio() {
		return highestRadio;
	}

	public void setHighestRadio(Boolean highestRadio) {
		this.highestRadio = highestRadio;
	}

	public String getKitchenName() {
		return kitchenName;
	}

	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}

	public OrderResponseBean getOrderResponseBean() {
		return orderResponseBean;
	}

	public void setOrderResponseBean(OrderResponseBean orderResponseBean) {
		this.orderResponseBean = orderResponseBean;
	}

	public ArrayList<OrderResponseBean> getOrderResponseBeanList() {
		return orderResponseBeanList;
	}

	public void setOrderResponseBeanList(
			ArrayList<OrderResponseBean> orderResponseBeanList) {
		this.orderResponseBeanList = orderResponseBeanList;
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
