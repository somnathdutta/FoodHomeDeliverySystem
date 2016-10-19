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

import com.sun.org.apache.regexp.internal.recompile;

import dao.FoodFeedbackDAO;
import Bean.FeedBackFoodBean;
import Bean.StarBean;

public class FeedbackOnFoodViewModel {

	Session session = null;
	
	private Connection connection = null;
	
	private String username = "";
	
	private Integer roleId = 0;
	
	public ArrayList<String> kitchenList =  new ArrayList<String>();
	
	public String kitchenName = null;
	
	public FeedBackFoodBean feedBackFoodBean = new FeedBackFoodBean();
	
	public FeedBackFoodBean feedBackStars = new FeedBackFoodBean();
	
	public ArrayList<FeedBackFoodBean> feedBackStarsList = new ArrayList<FeedBackFoodBean>();
	
	public ArrayList<FeedBackFoodBean> feedBackFoodBeanList = new ArrayList<FeedBackFoodBean>();
	
	private Date startDate,startDatePer;
	
	private Date endDate,endDatePer;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		username = (String) session.getAttribute("login");
		
		roleId = (Integer) session.getAttribute("userRoleId");
		
		connection.setAutoCommit(true);
		
		loadAllData();
		
		loadStardata();
		//onLoadKitchenList();
		
		//onLoadQuery();
	}
	
	/**
	 * Load all data initially
	 */
	public void loadAllData(){
		feedBackFoodBeanList = FoodFeedbackDAO.getAllFeedBacks(connection, null, null);
	}
	
	/**
	 * Find data from date range
	 */
	@Command
	@NotifyChange("*")
	public void onClickFind(){
		if(startDate!=null && endDate!=null){
			feedBackFoodBeanList = FoodFeedbackDAO.getAllFeedBacks(connection, startDate, endDate);
		}else{
			Messagebox.show("Search value required!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
		}
	}
	
	/**
	 * Generating CSV code
	 */
	@Command
	@NotifyChange("*")
	public void onClickGenerate(){
		 FoodFeedbackDAO.generateCSV(feedBackFoodBeanList);
	}
	
	/**
	 * Clear data and load initial data
	 */
	@Command
	@NotifyChange("*")
	public void onClickClear(){
		if(feedBackFoodBeanList.size()>0){
			feedBackFoodBeanList.clear();
		}
		if(startDate!=null){
			startDate = null;
		}
		if(endDate!=null){
			endDate = null;
		}
		loadAllData();
	}
	
	/**
	 * Load all data in the first tab 
	 */
	public void loadStardata(){
		
		feedBackStarsList = FoodFeedbackDAO.setAllStars(connection);
	}
	
	/**
	 * Find data from date range
	 */
	@Command
	@NotifyChange("*")
	public void onClickFindData(){
		if(startDatePer!=null && endDatePer!=null){
			feedBackStarsList = FoodFeedbackDAO.setAllStarsWithinRange(connection, startDatePer, endDatePer);
		}else{
			Messagebox.show("Date range required!","Information",Messagebox.OK,Messagebox.EXCLAMATION);
		}
	}
	
	/**
	 * Generating CSV code
	 */
	@Command
	@NotifyChange("*")
	public void onClickGenerateData(){
		FoodFeedbackDAO.generateCSVForStars(feedBackStarsList);
	}
	
	/**
	 * Clear data and load initial data
	 */
	@Command
	@NotifyChange("*")
	public void onClickClearData(){
		startDatePer = null;
		endDatePer = null;
		loadStardata();
	}
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  * * * * * * * * * * * * * * * * * * * * * ** * *  * * ************/	
	
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

	public void onLoadQuery(){
		if(feedBackFoodBeanList.size()>0){
			feedBackFoodBeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null; 
					String sql = "select * from vw_feedback_on_food";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next() ) {
							FeedBackFoodBean feedbackFoodBean = new FeedBackFoodBean();
							feedbackFoodBean.orderNo = resultSet.getString("order_no");
							feedbackFoodBean.kitchenName = resultSet.getString("kitchen_name");
							feedbackFoodBean.categoryName = resultSet.getString("category_name");
							feedbackFoodBean.quantity = resultSet.getInt("qty");
							String orderDate="",reformattedOrderDate="";
							SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
							SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");		
							orderDate = resultSet.getString("order_date");
							java.util.Date stdate = null;
							try {
								reformattedOrderDate = myFormat.format(fromUser.parse(orderDate));
								 stdate = myFormat.parse(reformattedOrderDate);
								 
							} catch (Exception e) {
								e.printStackTrace();
							}	
							feedbackFoodBean.orderDateValue =  reformattedOrderDate;
							ArrayList<String> negList = new ArrayList<String>();
							ArrayList<String> ratinglist = new ArrayList<String>();
							if(resultSet.getString("taste")!=null){
								ratinglist.add(resultSet.getString("taste"));
								if(resultSet.getString("taste").equalsIgnoreCase("NOT OK")){
									negList.add("TASTE");
								}
							}
							if(resultSet.getString("hotness")!=null){
								ratinglist.add(resultSet.getString("hotness"));
								if(resultSet.getString("hotness").equalsIgnoreCase("NOT OK")){
									negList.add("HOTNESS");
								}
							}
							if(resultSet.getString("portion")!=null){
								ratinglist.add(resultSet.getString("portion"));
								if(resultSet.getString("portion").equalsIgnoreCase("NOT OK")){
									negList.add("PORTION") ;
								}
							}
							if(resultSet.getString("timely_delivered")!=null){
								ratinglist.add(resultSet.getString("timely_delivered"));
								if(resultSet.getString("timely_delivered").equalsIgnoreCase("NOT OK")){
									negList.add("TIMELY DELIVERED") ;
								}
							}
							if(resultSet.getString("packing")!=null){
								ratinglist.add(resultSet.getString("packing"));
								if(resultSet.getString("packing").equalsIgnoreCase("NOT OK")){
									negList.add("PACKAGING");
								}
							}
							StringBuilder negativeFieldBuilder = new StringBuilder();
							String temp = negList.toString();
							String fb = temp.replace("[", "");
							String bb = fb.replace("]", "");
							String cm = bb.replace(",", ";");
							negativeFieldBuilder.append(cm);
							feedbackFoodBean.negativeFields = negativeFieldBuilder.toString();
							
							int countOK =0,countNOK=0;
							for(int i =0 ;i<ratinglist.size();i++){
								if(ratinglist.get(i).contains("NOT OK")){
									countNOK++;
								}else{
									countOK ++;
								}
							}
							int num = countOK + (-5*(countNOK));
							String ratings = String.valueOf(num)+"/"+"5";
							feedbackFoodBean.rating = ratings;
							feedBackFoodBeanList.add(feedbackFoodBean)	;	
						}
					} catch (Exception e) {
						e.printStackTrace();
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
	}
	
	/*@Command
	@NotifyChange("*")
	public void onClickFind(){
		if(startDate!=null && endDate!=null){
			feedBackFoodBeanList = FoodFeedbackDAO.getAllFeedBacks(connection, startDate, endDate);
		}else{
			Messagebox.show("Search value required!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
		}
		System.out.println("kitchn-->"+kitchenName+" stdate-->"+startDate+" end date->"+endDate);
		if(kitchenName==null && startDate==null && endDate==null){
			Messagebox.show("Search value required!","Information",Messagebox.OK,Messagebox.EXCLAMATION);
		}else{
		 if(kitchenName!=null && startDate==null && endDate!=null){
				Messagebox.show("Start date required!","Information",Messagebox.OK,Messagebox.EXCLAMATION);
				
			}else if(kitchenName!=null && startDate!=null && endDate==null){
				Messagebox.show("End date required!","Information",Messagebox.OK,Messagebox.EXCLAMATION);
			}else if(kitchenName==null && startDate!=null && endDate==null){
				Messagebox.show("Please give either vendor name or date range!","Information",Messagebox.OK,Messagebox.EXCLAMATION);
			}else if(kitchenName==null && startDate==null && endDate!=null){
				Messagebox.show("Please give either vendor name or date range!","Information",Messagebox.OK,Messagebox.EXCLAMATION);
			}else{
				//onFindQuery();
				System.out.println("Hi.  . . . . ");
				onFindQuery(kitchenName, startDate, endDate);
			}
			
		}
	}*/
	
	public void onFindQuery(String kitchenName, Date stDate, Date enDate){
		if(feedBackFoodBeanList.size()>0){
			feedBackFoodBeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null; 
					String sqlKitchen = "SELECT * FROM vw_feedback_on_food where kitchen_name = ?";
					String sqlKitchenDate = "SELECT * FROM vw_feedback_on_food where kitchen_name = ? and "
							+ " order_date>= ? and order_date<= ?";
					String sqlDate = "SELECT * FROM vw_feedback_on_food where "
							+ " order_date>= ? and order_date<= ?";
					try {	
						if(kitchenName!=null && stDate==null & enDate==null){
							preparedStatement = connection.prepareStatement(sqlKitchen);
							preparedStatement.setString(1, kitchenName);
							resultSet = preparedStatement.executeQuery();
						}else if(kitchenName!=null && stDate!=null & enDate!=null){
							preparedStatement = connection.prepareStatement(sqlKitchenDate);
							preparedStatement.setString(1, kitchenName);
							preparedStatement.setDate(2, new java.sql.Date(stDate.getTime()));
							preparedStatement.setDate(3, new java.sql.Date(enDate.getTime()));
							resultSet = preparedStatement.executeQuery();
						}else if(kitchenName==null && stDate!=null & enDate!=null){
							preparedStatement = connection.prepareStatement(sqlDate);
							preparedStatement.setDate(1, new java.sql.Date(stDate.getTime()));
							preparedStatement.setDate(2, new java.sql.Date(enDate.getTime()));
							resultSet = preparedStatement.executeQuery();
						}
						
						while (resultSet.next() ) {
							FeedBackFoodBean feedbackFoodBean = new FeedBackFoodBean();
							feedbackFoodBean.orderNo = resultSet.getString("order_no");
							feedbackFoodBean.kitchenName = resultSet.getString("kitchen_name");
							feedbackFoodBean.categoryName = resultSet.getString("category_name");
							feedbackFoodBean.quantity = resultSet.getInt("qty");
							String orderDate="",reformattedOrderDate="";
							SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
							SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");		
							orderDate = resultSet.getString("order_date");
							java.util.Date stdate = null;
							try {
								reformattedOrderDate = myFormat.format(fromUser.parse(orderDate));
								 stdate = myFormat.parse(reformattedOrderDate);
								 
							} catch (Exception e) {
								e.printStackTrace();
							}	
							feedbackFoodBean.orderDateValue =  reformattedOrderDate;
							ArrayList<String> negList = new ArrayList<String>();
							if(resultSet.getString("taste")!=null){
								if(resultSet.getString("taste").equalsIgnoreCase("NOT OK")){
									negList.add("TASTE");
								}
							}
							if(resultSet.getString("hotness")!=null){
								if(resultSet.getString("hotness").equalsIgnoreCase("NOT OK")){
									negList.add("HOTNESS");
								}
							}
							if(resultSet.getString("portion")!=null){
								if(resultSet.getString("portion").equalsIgnoreCase("NOT OK")){
									negList.add("PORTION") ;
								}
							}
							if(resultSet.getString("timely_delivered")!=null){
								if(resultSet.getString("timely_delivered").equalsIgnoreCase("NOT OK")){
									negList.add("TIMELY DELIVERED") ;
								}
							}
							if(resultSet.getString("packing")!=null){
								if(resultSet.getString("packing").equalsIgnoreCase("NOT OK")){
									negList.add("PACKAGING");
								}
							}
							StringBuilder negativeFieldBuilder = new StringBuilder();
							String temp = negList.toString();
							String fb = temp.replace("[", "");
							String bb = fb.replace("]", "");
							String cm = bb.replace(",", ";");
							negativeFieldBuilder.append(cm);
							feedbackFoodBean.negativeFields = negativeFieldBuilder.toString();
							
							ArrayList<String> ratinglist = new ArrayList<String>();
							
							ratinglist.add(resultSet.getString("taste"));
							ratinglist.add(resultSet.getString("hotness"));
							ratinglist.add(resultSet.getString("portion"));
							ratinglist.add(resultSet.getString("timely_delivered"));
							ratinglist.add(resultSet.getString("packing"));
							int countOK =0,countNOK=0;
							for(int i =0 ;i<ratinglist.size();i++){
								if(ratinglist.get(i).contains("NOT OK")){
									countNOK++;
								}else{
									countOK ++;
								}
							}
							int num = countOK + (-5*(countNOK));
							String ratings = String.valueOf(num)+"/"+"5";
							feedbackFoodBean.rating = ratings;
							feedBackFoodBeanList.add(feedbackFoodBean)	;	
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
	}
	
	
	
	
	
	
	public String getNegativeFields(String orderNo){
		String negativeFields =  null;
		ArrayList<String> negList = new ArrayList<String>();
	   try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select * from fapp_order_feedback where order_id = "
							+ "(select order_id from fapp_orders where order_no = ? )";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, orderNo);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							if(resultSet.getString("taste")!=null){
								if(resultSet.getString("taste").equalsIgnoreCase("NOT OK")){
									negList.add("TASTE");
								}
							}
							if(resultSet.getString("hotness")!=null){
								if(resultSet.getString("hotness").equalsIgnoreCase("NOT OK")){
									negList.add("HOTNESS");
								}
							}
							if(resultSet.getString("portion")!=null){
								if(resultSet.getString("portion").equalsIgnoreCase("NOT OK")){
									negList.add("PORTION") ;
								}
							}
							if(resultSet.getString("timely_delivered")!=null){
								if(resultSet.getString("timely_delivered").equalsIgnoreCase("NOT OK")){
									negList.add("TIMELY DELIVERED") ;
								}
							}
							if(resultSet.getString("packing")!=null){
								if(resultSet.getString("packing").equalsIgnoreCase("NOT OK")){
									negList.add("PACKAGING");
								}
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		StringBuilder negativeFieldBuilder = new StringBuilder();
		String temp = negList.toString();
		String fb = temp.replace("[", "");
		String bb = fb.replace("]", "");
		String cm = bb.replace(",", "  ");
		negativeFieldBuilder.append(cm);
		negativeFields = negativeFieldBuilder.toString();
		return negativeFields;
	}
	
	public String setRatings(String orderNo){
		ArrayList<String> ratinglist = new ArrayList<String>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select taste,hotness,portion,timely_delivered,packing from fapp_order_feedback where order_id = "
							+ "(select order_id from fapp_orders where order_no = ? ) and overeall_rating IS NOT NULL";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, orderNo);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							ratinglist.add(resultSet.getString("taste"));
							ratinglist.add(resultSet.getString("hotness"));
							ratinglist.add(resultSet.getString("portion"));
							ratinglist.add(resultSet.getString("timely_delivered"));
							ratinglist.add(resultSet.getString("packing"));
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Error due to:"+e,"ERROR",Messagebox.OK,Messagebox.ERROR);
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		int countOK =0,countNOK=0;
		for(int i =0 ;i<ratinglist.size();i++){
			if(ratinglist.get(i).contains("NOT OK")){
				countNOK++;
			}else{
				countOK ++;
			}
		}
		int num = countOK + (-5*(countNOK));
		String ratings = String.valueOf(num)+"/"+"5";
		return ratings;
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

	public FeedBackFoodBean getFeedBackFoodBean() {
		return feedBackFoodBean;
	}

	public void setFeedBackFoodBean(FeedBackFoodBean feedBackFoodBean) {
		this.feedBackFoodBean = feedBackFoodBean;
	}

	public ArrayList<FeedBackFoodBean> getFeedBackFoodBeanList() {
		return feedBackFoodBeanList;
	}

	public void setFeedBackFoodBeanList(
			ArrayList<FeedBackFoodBean> feedBackFoodBeanList) {
		this.feedBackFoodBeanList = feedBackFoodBeanList;
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

	public Date getStartDatePer() {
		return startDatePer;
	}

	public void setStartDatePer(Date startDatePer) {
		this.startDatePer = startDatePer;
	}

	public Date getEndDatePer() {
		return endDatePer;
	}

	public void setEndDatePer(Date endDatePer) {
		this.endDatePer = endDatePer;
	}

	public FeedBackFoodBean getFeedBackStars() {
		return feedBackStars;
	}

	public void setFeedBackStars(FeedBackFoodBean feedBackStars) {
		this.feedBackStars = feedBackStars;
	}

	public ArrayList<FeedBackFoodBean> getFeedBackStarsList() {
		return feedBackStarsList;
	}

	public void setFeedBackStarsList(ArrayList<FeedBackFoodBean> feedBackStarsList) {
		this.feedBackStarsList = feedBackStarsList;
	}
	
}
