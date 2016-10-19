package ViewModel;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
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
import Bean.VendorMisBean;

public class DelayInProductionViewModel {

	Session session = null;
	
	private Connection connection = null;
	
	private String username = "";
	
	private Integer roleId = 0;

	private Boolean averageRadio = false;
	
	private Boolean highestRadio = false;
	
	public ArrayList<String> kitchenList =  new ArrayList<String>();
	
	public String kitchenName = null;
	
	private DelayProductionBean delayProductionBean = new DelayProductionBean();
	
	private ArrayList<DelayProductionBean> delayInProductionBeanList = new ArrayList<DelayProductionBean>();
	
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
		
		onLoadKitchenList();
		/*onLoadQuery();*/
	}

	/**
	 * 
	 * Load all kitchen list
	 */
	public void onLoadKitchenList(){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sqlKitchen = "SELECT kitchen_name FROM fapp_kitchen WHERE is_active = 'Y' ";
										
					try {
						preparedStatement = connection.prepareStatement(sqlKitchen);
						resultSet =  preparedStatement.executeQuery();
						while (resultSet.next()) {
							kitchenList.add(resultSet.getString(1));
						}
					} catch (Exception e) {
						Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
						e.printStackTrace();
					} finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}		
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void onLoadQuery(Boolean avg, Boolean high) throws ParseException{
		if(delayInProductionBeanList.size()>0){
			delayInProductionBeanList.clear();
		}
		System.out.println("Avg->"+avg+" High->"+high);
		if(avg || high){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					/*String sql = "SELECT distinct kitchen_name,order_no,order_date,category_name,qty,order_assign_date,received_time,notified_time, "
					  +" (notified_time - order_assign_date)::text AS delay_in_delivery, "
					  +"(received_time- order_assign_date)::text AS delay_in_receive "
												+"  from vw_received_order_details ";*/
					String sql = "SELECT distinct kitchen_name,order_id,order_no,order_date,item_name,qty,order_assign_date,received_time,notified_time, "
							  +" (notified_time - order_assign_date)::text AS delay_in_delivery, "
							  +"(received_time- order_assign_date)::text AS delay_in_receive "
														+"  from vw_dip order by order_id desc";
					/*String sql1="SELECT distinct kitchen_name,order_no,order_date,category_name,qty,order_assign_date,received_time,notified_time, "
					  +" (notified_time - order_assign_date)::text AS delay_in_delivery, "
					  +"(received_time- order_assign_date)::text AS delay_in_receive "
												+"  from vw_received_order_details order by delay_in_receive DESC limit 5";*/
					String sql1="SELECT distinct kitchen_name,order_id,order_no,order_date,item_name,qty,order_assign_date,received_time,notified_time, "
							  +" (notified_time - order_assign_date)::text AS delay_in_delivery, "
							  +"(received_time- order_assign_date)::text AS delay_in_receive "
														+"  from vw_dip order by delay_in_receive DESC limit 5";
					try {
						if(avg){
							preparedStatement = connection.prepareStatement(sql);
						}else if(high){
							preparedStatement = connection.prepareStatement(sql1);
						}
						//preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							DelayProductionBean bean = new DelayProductionBean();
							String orderDate="",reformattedOrderDate="";
							SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
							SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
							
							orderDate = resultSet.getString("order_date");
							java.util.Date stdate = null;
							try {
								reformattedOrderDate = myFormat.format(fromUser.parse(orderDate));
								//System.out.println("refor--"+reformattedOrderDate);
								 stdate = myFormat.parse(reformattedOrderDate);
								// System.out.println("st1="+stdate);
								 
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
							
							bean.orderDateValue =  reformattedOrderDate;
							bean.kitchenName = resultSet.getString("kitchen_name");
							bean.orderNo = resultSet.getString("order_no");
							bean.categoryName = resultSet.getString("item_name");
							bean.quantity = resultSet.getInt("qty");
							bean.orderAssignTime = resultSet.getTimestamp("order_assign_date");
							/*DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");*/
							DateFormat dateFormat = new SimpleDateFormat(" HH:mm:ss");
					        bean.orderAssignTimeValue = dateFormat.format(bean.orderAssignTime);
					       
						    bean.acceptanceTime = resultSet.getTimestamp("received_time");
						    bean.acceptanceTimeValue = dateFormat.format(bean.acceptanceTime);
						    
							bean.notifyTime = resultSet.getTimestamp("notified_time");
							bean.notifyTimeValue = dateFormat.format(bean.notifyTime);
							
							//String delay = resultSet.getString("delay_in_receive");
							//System.out.println("delay-"+delay);
							//bean.delayInReceive = dateFormat.format(delay);
							bean.delayInReceive = resultSet.getString("delay_in_receive");
							int minutes =toMins(bean.delayInReceive);
							bean.delayInMinutes = minutes;
							//System.out.println("Miutes-->"+minutes);
							bean.delayInDelivery = resultSet.getString("delay_in_delivery");
							
							delayInProductionBeanList.add(bean);
						}
					} catch (Exception e) {
						// TODO: handle exception
						Messagebox.show("ERROR Due to :"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		}else{
			Messagebox.show("Please choose one option !","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
		}
	}
	
	private int toMins(String str) {
	    /*String[] hourMin = s.split(":");
	    int hour = Integer.parseInt(hourMin[0]);
	    int mins = Integer.parseInt(hourMin[1]);
	    int hoursInMins = hour * 60;*/
		boolean space = false;int hour,mins,hoursInMins;
		if(str.contains(" ")){
			space = true;
		}
		if(space){
			String[] splitted = str.split(" ");
			//System.out.println(splitted[2]);
			String[] hourMin = splitted[2].split(":");
		    hour = Integer.parseInt(hourMin[0]);
		    mins = Integer.parseInt(hourMin[1]);
		    hoursInMins = hour * 60;
		    //System.out.println("Min->"+hoursInMins);
		}else{
			String[] hourMin = str.split(":");
		    hour = Integer.parseInt(hourMin[0]);
		    mins = Integer.parseInt(hourMin[1]);
		    hoursInMins = hour * 60;
		    //System.out.println("Min else part->"+hoursInMins);
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
		onLoadQuery(averageRadio, highestRadio);
	}
	
	@Command
	@NotifyChange("*")
	public void onCheckSecondRadio() throws ParseException{
		if(averageRadio){
			averageRadio = false;
		}
		startDate = null;endDate=null;kitchenName= null;
		System.out.println("When second checked first-"+averageRadio+" second-"+highestRadio);
		onLoadQuery(averageRadio, highestRadio);
	}

	@Command
	@NotifyChange("*")
	public void onClickFind(){
		System.out.println("kitchn-->"+kitchenName+" stdate-->"+startDate+" end date->"+endDate);
		if(kitchenName==null && startDate==null && endDate==null){
			Messagebox.show("Search value required!","Information",Messagebox.OK,Messagebox.EXCLAMATION);
		}else{
			//DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		//	String startStrDate = dateFormat.format(startDate);
		//	String endStrDate = dateFormat.format(endDate);
     //   System.out.println("Start Date converted to String: " + startStrDate);
      //  System.out.println("End Date converted to String: " + endStrDate);
			 if(kitchenName!=null && startDate==null && endDate!=null){
				Messagebox.show("Start date required!","Information",Messagebox.OK,Messagebox.EXCLAMATION);
				
			}else if(kitchenName!=null && startDate!=null && endDate==null){
				Messagebox.show("End date required!","Information",Messagebox.OK,Messagebox.EXCLAMATION);
			}else if(kitchenName==null && startDate!=null && endDate==null){
				Messagebox.show("Please give either vendor name or date range!","Information",Messagebox.OK,Messagebox.EXCLAMATION);
			}else if(kitchenName==null && startDate==null && endDate!=null){
				Messagebox.show("Please give either vendor name or date range!","Information",Messagebox.OK,Messagebox.EXCLAMATION);
			}else{
				findByVendor(averageRadio, highestRadio,kitchenName);
			}
			
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickClear(){
		if(delayInProductionBeanList.size()>0){
			delayInProductionBeanList.clear();
		}
		if(startDate!=null){
			startDate = null;
		}
		if(endDate!=null){
			endDate = null;
		}
		if(kitchenName!=null){
			kitchenName = null;
		}
		
		try {
			onLoadQuery(averageRadio, highestRadio);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickGenerate(){
		 File f = null;
	      boolean bool = false;
	    if(delayInProductionBeanList.size()>0){
	    	try{
		         // create new file
		    	 String realPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
		  		
		  		String reportNamewithPath = realPath + "delayinproductionreport.csv";
		  		System.out.println(reportNamewithPath);
		         //f = new File("C:/Users/somnathd/Desktop/report.csv");
		  		f = new File(reportNamewithPath);
		         // tries to create new file in the system
		         bool = f.createNewFile();
		         
		         // prints
		         System.out.println("File created: "+bool);
		         if(f.exists())
		         // deletes file from the system
		         f.delete();
		         System.out.println("delete() method is invoked");
		         // delete() is invoked
		         f.createNewFile();
		       
		         // tries to create new file in the system
		         bool = f.createNewFile();
		         
		         // print
		         System.out.println("File created: "+bool);
		         FileOutputStream fileOutputStream = new FileOutputStream(f);
		         OutputStreamWriter osw = new OutputStreamWriter(fileOutputStream);    
		            Writer w = new BufferedWriter(osw);
		            w.write("ORDER DATE,VENDOR NAME,ORDER NO,ITEM NAME,QUANTITY,ASSIGN TIME,ACCEPT TIME,"
		            		+ "FINISH TIME,DELAY IN MINUTES\n");
		            for(int i=0;i<delayInProductionBeanList.size();i++){
		            	w.write(delayInProductionBeanList.get(i).orderDateValue+","+delayInProductionBeanList.get(i).kitchenName
		            			+","+delayInProductionBeanList.get(i).orderNo+","+delayInProductionBeanList.get(i).categoryName
		            			+","+delayInProductionBeanList.get(i).quantity+","+delayInProductionBeanList.get(i).orderAssignTimeValue
		            			+","+delayInProductionBeanList.get(i).acceptanceTimeValue+","+delayInProductionBeanList.get(i).notifyTimeValue
		            			+","+delayInProductionBeanList.get(i).delayInMinutes+"\n");
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
		    			final AMedia amedia = new AMedia("delayinproductionreport", "csv", "application/csv", bios.toByteArray());
		    			Filedownload.save(amedia);
		    		}catch(Exception exception){		
		    		}
		      }catch(Exception e){
		         e.printStackTrace();
		      }
	    }else{
	    	Messagebox.show("No DATA found !","Information",Messagebox.OK,Messagebox.EXCLAMATION);
	    }     
	}
	
	private void findByVendor(Boolean avg , Boolean high,String kitchenName){
		if(delayInProductionBeanList.size()>0){
			delayInProductionBeanList.clear();
		}
		System.out.println("Avg->"+avg+" High->"+high+" kitchen:: "+kitchenName);
		if(avg || high){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
				
					String sqlAvgWithKitchen = "SELECT distinct kitchen_name,order_no,order_date,item_name,qty,order_assign_date,received_time,notified_time, "
					  +" (notified_time - order_assign_date)::text AS delay_in_delivery, "
					  +"(received_time- order_assign_date)::text AS delay_in_receive "
					  +"  from vw_dip where kitchen_name = ? ";
					
					String sqlAvgWithDates = "SELECT distinct kitchen_name,order_no,order_date,item_name,qty,order_assign_date,received_time,notified_time, "
					  +" (notified_time - order_assign_date)::text AS delay_in_delivery, "
					  +"(received_time- order_assign_date)::text AS delay_in_receive "
					  +"  from vw_dip where order_date>=? and order_date<=? ";
					
					String sqlAvgWithBoth = "SELECT distinct kitchen_name,order_no,order_date,item_name,qty,order_assign_date,received_time,notified_time, "
					  +" (notified_time - order_assign_date)::text AS delay_in_delivery, "
					  +"(received_time- order_assign_date)::text AS delay_in_receive "
					  +"  from vw_dip where kitchen_name = ? AND order_date>=? and order_date<=?";
					
					String sqlHighWithKitchen="SELECT * FROM vw_highest_delay_production where kitchen_name = ?";
					
					String sqlHighWithDates="SELECT * FROM vw_highest_delay_production WHERE order_date>= ? AND order_date <= ?";
					
					String sqlHighWithBoth="SELECT  * FROM vw_highest_delay_production where kitchen_name = ? AND order_date>= ? AND order_date <= ?";
					/*String sqlHighWithBoth="SELECT distinct kitchen_name,order_no,order_date,category_name,qty,order_assign_date,received_time,notified_time, "
					  +" (notified_time - order_assign_date)::text AS delay_in_delivery, "
					  +"(received_time- order_assign_date)::text AS delay_in_receive "
					  +"  from vw_received_order_details order by delay_in_receive DESC limit 5 where kitchen_name = ? AND order_date>= ? AND order_date <= ?";*/
					
					try {
						if(avg){
							if(kitchenName!=null && startDate==null && endDate==null){
								preparedStatement = connection.prepareStatement(sqlAvgWithKitchen);
								preparedStatement.setString(1, kitchenName);
								System.out.println("with kitchen name. . .");
								System.out.println(preparedStatement);
							}else if(kitchenName==null && startDate!=null && endDate!=null){
								preparedStatement = connection.prepareStatement(sqlAvgWithDates);
								preparedStatement.setDate(1, new java.sql.Date(startDate.getTime()) );
								preparedStatement.setDate(2, new java.sql.Date(endDate.getTime()) );
								System.out.println("with dates. . .");
							}
							else{
								System.out.println("else part  . . . .");
								preparedStatement = connection.prepareStatement(sqlAvgWithBoth);
								preparedStatement.setString(1, kitchenName);
								preparedStatement.setDate(2, new java.sql.Date(startDate.getTime()) );
								preparedStatement.setDate(3, new java.sql.Date(endDate.getTime()) );
								System.out.println("with both . . .");
							}
							
							
						}else if(high){
							if(kitchenName!=null && startDate==null && endDate==null){
								preparedStatement = connection.prepareStatement(sqlHighWithKitchen);
								preparedStatement.setString(1, kitchenName);
								System.out.println("with kitchen name. . .");
							}else if(kitchenName==null && startDate!=null && endDate!=null){
								preparedStatement = connection.prepareStatement(sqlHighWithDates);
								preparedStatement.setDate(1, new java.sql.Date(startDate.getTime()) );
								preparedStatement.setDate(2, new java.sql.Date(endDate.getTime()) );
								System.out.println("with dates. . .");
							}
							else{
								System.out.println("else part  . . . .");
								preparedStatement = connection.prepareStatement(sqlHighWithBoth);
								preparedStatement.setString(1, kitchenName);
								preparedStatement.setDate(2, new java.sql.Date(startDate.getTime()) );
								preparedStatement.setDate(3, new java.sql.Date(endDate.getTime()) );
								System.out.println("with both . . .");
							}
						}
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							DelayProductionBean bean = new DelayProductionBean();
							String orderDate="",reformattedOrderDate="";
							SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
							SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
							
							orderDate = resultSet.getString("order_date");
							java.util.Date stdate = null;
							try {
								reformattedOrderDate = myFormat.format(fromUser.parse(orderDate));
								//System.out.println("refor--"+reformattedOrderDate);
								 stdate = myFormat.parse(reformattedOrderDate);
								// System.out.println("st1="+stdate);
								 
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
							
							bean.orderDateValue =  reformattedOrderDate;
							bean.kitchenName = resultSet.getString("kitchen_name");
							bean.orderNo = resultSet.getString("order_no");
							bean.categoryName = resultSet.getString("item_name");
							bean.quantity = resultSet.getInt("qty");
							bean.orderAssignTime = resultSet.getTimestamp("order_assign_date");
							/*DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");*/
							DateFormat dateFormat = new SimpleDateFormat(" HH:mm:ss");
					        bean.orderAssignTimeValue = dateFormat.format(bean.orderAssignTime);
					       
						    bean.acceptanceTime = resultSet.getTimestamp("received_time");
						    bean.acceptanceTimeValue = dateFormat.format(bean.acceptanceTime);
						    
							bean.notifyTime = resultSet.getTimestamp("notified_time");
							bean.notifyTimeValue = dateFormat.format(bean.notifyTime);
							
							//String delay = resultSet.getString("delay_in_receive");
							//System.out.println("delay-"+delay);
							//bean.delayInReceive = dateFormat.format(delay);
							bean.delayInReceive = resultSet.getString("delay_in_receive");
							int minutes =toMins(bean.delayInReceive);
							//System.out.println("Miutes-->"+minutes);
							bean.delayInMinutes = minutes;
							bean.delayInDelivery = resultSet.getString("delay_in_delivery");
							
							delayInProductionBeanList.add(bean);
						}
					} catch (Exception e) {
						// TODO: handle exception
						Messagebox.show("ERROR Due to :"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		}else{
			Messagebox.show("Please choose one option !","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
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

	public ArrayList<String> getKitchenList() {
		return kitchenList;
	}

	public void setKitchenList(ArrayList<String> kitchenList) {
		this.kitchenList = kitchenList;
	}

	public String getKitchenName() {
		return kitchenName;
	}

	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}

	public DelayProductionBean getDelayProductionBean() {
		return delayProductionBean;
	}

	public void setDelayProductionBean(DelayProductionBean delayProductionBean) {
		this.delayProductionBean = delayProductionBean;
	}

	public ArrayList<DelayProductionBean> getDelayInProductionBeanList() {
		return delayInProductionBeanList;
	}

	public void setDelayInProductionBeanList(
			ArrayList<DelayProductionBean> delayInProductionBeanList) {
		this.delayInProductionBeanList = delayInProductionBeanList;
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
