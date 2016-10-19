package ViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import Bean.ManageCuisinBean;
import Bean.MealBean;
import Bean.OrderItemDetailsBean;
import Bean.SubscriptionBean;

public class SubscriptionListViewModel {
	
Session session = null;
	
	private Connection connection = null;
	
	private String username;
	
	private Integer roleId;
	
	private SubscriptionBean subscriptionBean = new SubscriptionBean();
	
	private ArrayList<SubscriptionBean> subscriptionBeanList = new ArrayList<SubscriptionBean>();
	
	private Boolean mondayButtonVisibility = true;
	
	private Boolean cuisineGridVisibility = false;
	
	private ArrayList<String> cityList= new ArrayList<String>();
	
	private ArrayList<String> areaList= new ArrayList<String>();
	
	PropertyFile propertyfile = new PropertyFile();
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		connection=(Connection) session.getAttribute("sessionConnection");
		username = (String) session.getAttribute("login");
		roleId = (Integer) session.getAttribute("userRoleId");
		connection.setAutoCommit(true);
		
		onLoadCityList();
		loadSubscribedOrders();
	}
	
	private void loadSubscribedOrders(){
		if(subscriptionBeanList.size()>0){
			subscriptionBeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					/*String sql = "SELECT distinct subscription_no,subscription_id,user_mail_id,contact_number,subscribed_by,area_id,"
							+ " area_name,city_name,price,start_date,end_date FROM vw_subscribed_orders"
							+ "  order by subscription_no";*/
					String sql = "SELECT distinct subscription_no,subscription_id,user_mail_id,contact_number,subscribed_by,"
							+ " price,start_date,end_date,delivery_address FROM vw_subscribed_order_list"
							+ "  order by subscription_no";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							SubscriptionBean bean = new SubscriptionBean();
							bean.subscriptionNo = resultSet.getString("subscription_no");
							bean.subscriptionId = resultSet.getInt("subscription_id");
							bean.contactName = resultSet.getString("subscribed_by");
							bean.emailId = resultSet.getString("user_mail_id");
							bean.contactNo = resultSet.getString("contact_number");
							//bean.areaId = resultSet.getInt("area_id");
							//bean.areaName = resultSet.getString("area_name");
							//bean.cityName = resultSet.getString("city_name");
							bean.price = resultSet.getDouble("price");
							bean.startDate = resultSet.getDate("start_date");
							if(resultSet.getString("delivery_address")!=null){
								bean.deliveryAddress = resultSet.getString("delivery_address");
							}else{
								bean.deliveryAddress = " ";
							}
							
							SimpleDateFormat simpDate = new SimpleDateFormat("dd-MM-yyyy");
							bean.startDateValue = simpDate.format(bean.startDate);
							bean.endDate = resultSet.getDate("end_date");
							bean.endDateValue =simpDate.format(bean.endDate);
							subscriptionBeanList.add(bean);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
	
	
	@GlobalCommand
	@NotifyChange("*")
	public void onOkData(@BindingParam("day")String dayWithType){
		
		String[] dayWType = dayWithType.split("\\$");
    	for(int i=0;i<(dayWType.length-1);i++){
    		MealBean mealBean = new MealBean();
    		mealBean.dayName =  dayWType[0];
    		mealBean.typeOfMeal = dayWType[1];
    		
			mealBean.price = subscriptionBean.price;
    		//mealBean.orderItemDetailsBeanList = fetchCategoryList();
    		subscriptionBean.saveMealDisability = false;
    		subscriptionBean.mealBean.dayName = mealBean.dayName;
    		subscriptionBean.mealBean.typeOfMeal = mealBean.typeOfMeal;
    		subscriptionBean.mealBean.price = mealBean.price;
    		Integer listSize = fetchDataWithDayAndMealType(subscriptionBean.subscriptionNo,subscriptionBean.mealBean.dayName, 
    				subscriptionBean.mealBean.typeOfMeal );
    		if(listSize==0){
    			subscriptionBean.mealBean.orderItemDetailsBeanList = fetchCategoryList();
    			System.out.println("fetch category list called . . .");
    			for(OrderItemDetailsBean oBean: subscriptionBean.mealBean.orderItemDetailsBeanList){
    				System.out.println("cuisine-"+oBean.cuisineName+oBean.cuisineId);
    				System.out.println("category -"+oBean.categoryName+oBean.categoryId);
    				System.out.println("quantity-"+oBean.quantity);
    				
    			}
    		}
    		subscriptionBean.mealBeanList.add(mealBean);
    		
    	}
		
	}
	
	public ArrayList<OrderItemDetailsBean>  fetchCategoryList() {
    	ArrayList<OrderItemDetailsBean> orderItemDetailsBeanList = new ArrayList<OrderItemDetailsBean>();
    	
    	try {
			SQL:{
				PreparedStatement preparedStatement = null;
				ResultSet resultSet = null;
				String sql = "SELECT * FROM vw_category_from_kitchen_details where area_id = "
						   + " (select area_id from sa_area where area_name ILIKE ? and city_id = "
			    		   + " (select city_id from sa_city where city_name ILIKE ?))";
				try {
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, subscriptionBean.areaName);
					preparedStatement.setString(2, subscriptionBean.cityName);
					resultSet = preparedStatement.executeQuery();
					while (resultSet.next()) {
						OrderItemDetailsBean orderItemDetailsBean = new OrderItemDetailsBean();
						orderItemDetailsBean.kitchenId = resultSet.getInt("kitchen_id");
						orderItemDetailsBean.cuisineId = resultSet.getInt("kitchen_cuisine_id");
						orderItemDetailsBean.cuisineName = resultSet.getString("cuisine_name");
						orderItemDetailsBean.categoryId = resultSet.getInt("category_id");
						orderItemDetailsBean.categoryName = resultSet.getString("category_name");
						orderItemDetailsBean.price = resultSet.getDouble("cost_price");
						orderItemDetailsBean.stock = resultSet.getInt("category_stock");
						
						orderItemDetailsBeanList.add(orderItemDetailsBean);
						subscriptionBean.mealBean.orderItemDetailsBeanList = orderItemDetailsBeanList;
					}
				} catch (Exception e) {
					e.printStackTrace();
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
    	return orderItemDetailsBeanList;
    }
	
	@Command
	@NotifyChange("*")
	public void onClickDay(@BindingParam("string")String dayName ){
		
		Map<String, String> dayData = new HashMap<String, String>();
		dayData.put("day", dayName);
		Window win = (Window) Executions.createComponents("choseType.zul", null, dayData);
		
		win.doModal();
		
	}

	/**
	 * onLoadCityList method is used to load all the city name list from database
	 * 
	 */
	public void onLoadCityList(){	
		if(cityList!=null){
			cityList.clear();
		}
		try {
				sql1:{
						PreparedStatement preparedStatement=null;
						try {
								preparedStatement=connection.prepareStatement(propertyfile.getPropValues("sqlcitylist"));
								
								ResultSet rs=preparedStatement.executeQuery();
								
								while(rs.next()){
									cityList.add(rs.getString(1));	
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
			e.printStackTrace();
		}
	}
	
	/**
	 * onSelectcityName method is used to load the area name by selecting the city name from dropdown
	 * 
	 */
	@Command
	@NotifyChange("*")
	public void onSelectcityName(){
		if(areaList.size()>0){
			areaList.clear();
		}
		try {
			sql1:{
					PreparedStatement preparedStatement=null;
					try {
							preparedStatement=connection.prepareStatement(propertyfile.getPropValues("areaIdWRTcityNameSql"));
							preparedStatement.setString(1, subscriptionBean.cityName);
							ResultSet resultSet=preparedStatement.executeQuery();
							
							while(resultSet.next()){
								areaList.add(resultSet.getString("area_name"));		
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
			e.printStackTrace();
			}
	}
	
	
	
	@Command
	@NotifyChange("*")
	public void onSelectAreaName(){
		
		fetchCategoryList();
		if(subscriptionBean.createSubscriptionDisability){
			subscriptionBean.saveMealDisability = false;
		}

	}
	
	@Command
	@NotifyChange("*")
	public void onClickAdd(@BindingParam("bean")OrderItemDetailsBean bean){
		bean.quantity ++ ;
		subscriptionBean.price = subscriptionBean.price + bean.price;
		
	}
	
	@Command
	@NotifyChange("*")
	public void onClickLess(@BindingParam("bean")OrderItemDetailsBean bean){
		if(bean.quantity!=0){
			bean.quantity-- ;
			subscriptionBean.price = (subscriptionBean.price - bean.price );
		}
	}
	
	
	
	@GlobalCommand
	public void globalReload(){
		cuisineGridVisibility = true;
		Messagebox.show("Now select day and meal type!","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
		
	}
	
    
    private Integer fetchDataWithDayAndMealType(String subNo, String day, String mealType){
    	if(subscriptionBean.mealBean.orderItemDetailsBeanList.size()>0){
    		subscriptionBean.mealBean.orderItemDetailsBeanList.clear();
    	}
    	try {
			 SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select * from vw_subscribed_orders "
							   +" where "
							   +" subscription_no = ? and "
							   +" day_name = ? " 
							   +" and meal_type = ? ";
					 try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, subNo);
						preparedStatement.setString(2, day);
						preparedStatement.setString(3, mealType);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							OrderItemDetailsBean detailsBean = new OrderItemDetailsBean();
							detailsBean.cuisineName = resultSet.getString("cuisine_name");
							detailsBean.categoryName = resultSet.getString("category_name");
							detailsBean.price = resultSet.getDouble("meal_price");
							detailsBean.quantity = resultSet.getInt("quantity");
							detailsBean.subscriptionMealDetailId = resultSet.getInt("subscription_meal_detail_id");
							subscriptionBean.mealBean.orderItemDetailsBeanList.add(detailsBean);
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
    	
    	System.out.println("fetch category list called . . .");
		for(OrderItemDetailsBean oBean: subscriptionBean.mealBean.orderItemDetailsBeanList){
			System.out.println("cuisine-"+oBean.cuisineName+oBean.cuisineId);
			System.out.println("category -"+oBean.categoryName+oBean.categoryId);
			System.out.println("quantity-"+oBean.quantity);
			
		}
    	
    	return subscriptionBean.mealBean.orderItemDetailsBeanList.size();
    }
    
    
    @Command
    @NotifyChange("*")
    public void onClickUpdateAndChooseMeal(){
    	
    	if(updateMealData()){
    		Messagebox.show("Meal Updated!","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);	
    	}else{
    		saveMealWithDay();
    		Integer subscriptionMealId = getGeneratedKeysForMeal();
        	if(saveMealItems(subscriptionMealId))
    		Messagebox.show("New meal updated in your subscription!","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
    	}
    	
    }
	
    private Boolean updateMealData(){
    	 Boolean updated = false;
    	try {
			SQL:{
    				PreparedStatement preparedStatement = null;
    				String sql = "UPDATE fapp_subscription_meals_details SET quantity = ? WHERE subscription_meal_detail_id = ?";
    				try {
						preparedStatement = connection.prepareStatement(sql);
						for(OrderItemDetailsBean bean : subscriptionBean.mealBean.orderItemDetailsBeanList){
				    		if(bean.subscriptionMealDetailId==null){
				    			break;
				    		}
							preparedStatement.setInt(1, bean.quantity);
				    		preparedStatement.setInt(2, bean.subscriptionMealDetailId);
				    		preparedStatement.addBatch();
				    	}
						int [] count = preparedStatement.executeBatch();
				    	   
				    	   for(Integer integer : count){
				    		   updated = true;
				    	   }
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						Messagebox.show("Updation failed due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
    		}
		} catch (Exception e) {
			// TODO: handle exception
		}
    	return updated;
    }
    
    @Command
    @NotifyChange("*")
    public void onClickFind(){
    	if(subscriptionBeanList.size()>0){
    		subscriptionBeanList.clear();
    	}
    	try {
			SQL:{
    				PreparedStatement preparedStatement = null;
    				ResultSet resultSet = null;
    				/*String sql = "SELECT distinct subscription_no,subscription_id,user_mail_id,contact_number,subscribed_by,subscription_date,area_id,"
							+" area_name,city_name,price,start_date,end_date "
							+"	FROM vw_subscribed_orders "
							+"	where subscription_no ILIKE ? "
							//+"	and user_mail_id ILIKE ? "
							+"	and contact_number ILIKE ? "
							//+" and start_date LIKE ? "
							//+" and end_date LIKE ? "
							//+"	and subscribed_by ILIKE ? "
							+"	order by subscription_no ";*/
    				String sql = "SELECT distinct subscription_no,subscription_id,user_mail_id,contact_number,subscribed_by,"
							+ " price,start_date,end_date,delivery_address FROM vw_subscribed_order_list where subscription_no ILIKE ?"
							+ " and contact_number ILIKE ?"
							+ "  order by subscription_no";
    				try {
						preparedStatement = connection.prepareStatement(sql);
						if(subscriptionBean.subscriptionNo!=null && subscriptionBean.subscriptionNo.trim().length()>0)
							preparedStatement.setString(1, (subscriptionBean.subscriptionNo+"%"));
						else
							preparedStatement.setString(1, "%%");
						
						/*if(subscriptionBean.emailId!=null && subscriptionBean.emailId.trim().length()>0)
							preparedStatement.setString(2, (subscriptionBean.emailId+"%"));
						else
							preparedStatement.setString(2, "%%");*/
						
						if(subscriptionBean.contactNo!=null && subscriptionBean.contactNo.trim().length()>0)
							preparedStatement.setString(2, (subscriptionBean.contactNo+"%"));
						else
							preparedStatement.setString(2, "%%");
						
						/*if(subscriptionBean.startDate!=null )
							preparedStatement.setDate(3, (subscriptionBean.startDate));
						else
							preparedStatement.setNull(3, Types.NULL);
						
						if(subscriptionBean.contactName!=null && subscriptionBean.contactName.trim().length()>0)
							preparedStatement.setString(4, (subscriptionBean.contactName+"%"));
						else
							preparedStatement.setString(4, "%%");
						if(subscriptionBean.endDate!=null )
							preparedStatement.setDate(4, (subscriptionBean.endDate));
						else
							preparedStatement.setNull(4, Types.NULL);*/
						
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							SubscriptionBean bean = new SubscriptionBean();
							bean.subscriptionNo = resultSet.getString("subscription_no");
							bean.subscriptionId = resultSet.getInt("subscription_id");
							bean.contactName = resultSet.getString("subscribed_by");
							bean.emailId = resultSet.getString("user_mail_id");
							bean.contactNo = resultSet.getString("contact_number");
							//bean.areaId = resultSet.getInt("area_id");
							//bean.areaName = resultSet.getString("area_name");
							//bean.cityName = resultSet.getString("city_name");
							bean.price = resultSet.getDouble("price");
							bean.startDate = resultSet.getDate("start_date");
							if(resultSet.getString("delivery_address")!=null){
								bean.deliveryAddress = resultSet.getString("delivery_address");
							}else{
								bean.deliveryAddress = " ";
							}
							SimpleDateFormat simpDate = new SimpleDateFormat("dd-MM-yyyy");
							bean.startDateValue = simpDate.format(bean.startDate);
							bean.endDate = resultSet.getDate("end_date");
							bean.endDateValue =simpDate.format(bean.endDate);
							subscriptionBeanList.add(bean);
							
						}
						
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
    		}
		} catch (Exception e) {
			// TODO: handle exception
		}
    	/*System.out.println(subscriptionBeanList.size());
    	if(subscriptionBeanList.size()==1){
    		cuisineGridVisibility= true;
    	}*/
    }
    
    @Command
    @NotifyChange("*")
    public void onClickClear(){
    	subscriptionBean.emailId = null;
    	subscriptionBean.contactName = null;
    	subscriptionBean.contactNo = null;
    	subscriptionBean.subscriptionNo = null;
    	cuisineGridVisibility = false;
    	loadSubscribedOrders();
    	
    }
    
    @Command
    @NotifyChange("*")
    public void onClickEdit(@BindingParam("bean")SubscriptionBean subscriptionbean){
    	cuisineGridVisibility = true;
    	subscriptionBean.subscriptionNo = subscriptionbean.subscriptionNo;
    	subscriptionBean.subscriptionId = subscriptionbean.subscriptionId;
    	subscriptionBean.areaName = subscriptionbean.areaName;
    	subscriptionBean.cityName = subscriptionbean.cityName;
    	subscriptionBean.price = subscriptionbean.price;
    	System.out.println(subscriptionBean.price);
    }
   
    @Command
    @NotifyChange("*")
    public void onClickDetails(@BindingParam("bean")SubscriptionBean subscriptionbean){
    	if(subscriptionbean.subscriptionId!=null){	
			
			Map<String, Object> parentMap = new HashMap<String, Object>();
			
			parentMap.put("parentOrderObject", subscriptionbean);
			
			System.out.println("sub id-"+subscriptionbean.subscriptionId);
			
			Window win = (Window) Executions.createComponents("subscribedOrders.zul", null, parentMap);
			
			win.doModal();
			
		}else{
			Messagebox.show("Data not available!","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
		}
    }
    
    @Command
    @NotifyChange("*")
    public void onClickFinalUpdate(){
    	System.out.println(subscriptionBean.price);
    	if(subscriptionBean.subscriptionNo!=null){
    		updatePrice();
    		cuisineGridVisibility = false;
    		loadSubscribedOrders();
    	}
    	else
    		Messagebox.show("Please Fill details.","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
    }
    
    private void generateSubscriptionId(){
    	Integer subscriptionId = null;
    	try {
				SQL:{
			    		PreparedStatement preparedStatement = null;
						ResultSet resultSet = null;
						String sql = "INSERT INTO fapp_subscription( "
						           +" subscription_no, subscribed_by, "
						           +" user_mail_id, contact_number, flat_no, "
						           +" street_name, landmark, pincode)"
						           +" VALUES ( ?, ?, ?, ?,  ?, ?, ?, ?)";
						try {
							preparedStatement = connection.prepareStatement(sql);
							preparedStatement.setString(1, subscriptionBean.subscriptionNo);
							preparedStatement.setString(2, subscriptionBean.contactName);
							preparedStatement.setString(3, subscriptionBean.emailId);
							preparedStatement.setString(4, subscriptionBean.contactNo);
							preparedStatement.setString(5, subscriptionBean.flatNo);
							preparedStatement.setString(6, subscriptionBean.streetName);
							preparedStatement.setString(7, subscriptionBean.landMark);
							preparedStatement.setString(8, subscriptionBean.pincode);
							int count = preparedStatement.executeUpdate();
							if(count>0){
								 subscriptionId = getGeneratedKeys();
								System.out.println("Subscription created and Id is =====> "+subscriptionId);
								//subscriptionBean.subscriptionId = subscriptionId;
							}
						} catch (Exception e) {
							Messagebox.show("Details saving faied due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
							e.printStackTrace();
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
    
    private Integer getGeneratedKeys(){
    	Integer id =  null;
    	try {
			SQL:{
    				PreparedStatement preparedStatement = null;
    				ResultSet resultSet = null;
    				String sql = "SELECT max(subscription_id)AS id from fapp_subscription ";
    				try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet =  preparedStatement.executeQuery();
						if(resultSet.next()){
							id = resultSet.getInt("id");
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
    		}
		} catch (Exception e) {
			// TODO: handle exception
		}
    	return id;
    }
    
    private String generateSubcriptionNo(){
		String subscriptionNumber = "";
		Integer serialorderid=0;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT Count(subscription_id) FROM fapp_subscription";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							serialorderid = resultSet.getInt(1);
						}
					} catch (Exception e) {
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
		subscriptionNumber =  "SUBS/"+String.format("%06d", serialorderid+1);
		System.out.println("Subscription number->"+subscriptionNumber);
		subscriptionBean.subscriptionNo = subscriptionNumber;
		return subscriptionNumber;
	}
    
    private Integer saveMealWithDay(){
    	Integer insertedId = null  ;
    	try {
			SQL:{
    				PreparedStatement preparedStatement = null;
    				ResultSet resultSet = null;
    				String sql = "INSERT INTO fapp_subscription_meals( "
					           +" subscription_no, subscription_id, day_name,  meal_type) "
					           +" VALUES ( ?, ?, ?, ?);";
    				try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, subscriptionBean.subscriptionNo);
						preparedStatement.setInt(2, subscriptionBean.subscriptionId);
						preparedStatement.setString(3, subscriptionBean.mealBean.dayName);
						preparedStatement.setString(4, subscriptionBean.mealBean.typeOfMeal);
						
						preparedStatement.execute();
						resultSet = preparedStatement.getGeneratedKeys();
						if(resultSet.next()){
							insertedId = getGeneratedKeysForMeal();
							System.out.println("Meal Id created and Id is =====> "+insertedId);
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Saving failed due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
	    	}
		} catch (Exception e) {
			// TODO: handle exception
		}
    	return insertedId;
    }
    
    private Integer getGeneratedKeysForMeal(){
    	Integer id =  null;
    	try {
			SQL:{
    				PreparedStatement preparedStatement = null;
    				ResultSet resultSet = null;
    				String sql = "SELECT max(subscription_meal_id)AS id from fapp_subscription_meals ";
    				try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet =  preparedStatement.executeQuery();
						if(resultSet.next()){
							id = resultSet.getInt("id");
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
    		}
		} catch (Exception e) {
			// TODO: handle exception
		}
    	return id;
    }
    
    private Boolean saveMealItems(Integer subscriptionMealId){
    	Boolean inserted = false;
    	try {
			SQL:{
    				PreparedStatement preparedStatement = null;
    				String sql = "INSERT INTO fapp_subscription_meals_details( "
					           +" subscription_meal_id, cuisine_name, "
					           +" category_name, quantity, meal_price, kitchen_id)"
					           +" VALUES (?, ?, ?, ?, ?, ?)";
    				try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, subscriptionMealId);
						for(OrderItemDetailsBean bean : subscriptionBean.mealBean.orderItemDetailsBeanList){
				    		if(bean.quantity!=0){
				    			preparedStatement.setString(2, bean.cuisineName);
				    			preparedStatement.setString(3, bean.categoryName);
				    			preparedStatement.setInt(4, bean.quantity);
				    			preparedStatement.setDouble(5, bean.price);
				    			preparedStatement.setInt(6, bean.kitchenId);
				    			preparedStatement.addBatch();
				    		}
						}
						int [] count = preparedStatement.executeBatch();
				    	   
				    	   for(Integer integer : count){
				    		   inserted = true;
				    	   }
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Saving failed due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}
    			}
		} catch (Exception e) {
			// TODO: handle exception
		}
    	return inserted;
    }
    
    private void updatePrice(){
    	try {
			SQL:{
    				PreparedStatement preparedStatement = null;
    				String sql = "UPDATE fapp_subscription set price = ? where subscription_id = ?";
    				try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setDouble(1, subscriptionBean.price);
						preparedStatement.setInt(2, subscriptionBean.subscriptionId);
						int count = preparedStatement.executeUpdate();
						if(count>0){
							System.out.println("Subscribed sucessfully!");
							Messagebox.show("Subscription updated!");
							refresh();
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						Messagebox.show("Save failed due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
    
    private Boolean validateFields(){
    	if(subscriptionBean.cityName!=null){
    		if(subscriptionBean.areaName!=null){
    			if(subscriptionBean.contactName!=null){
    				if(subscriptionBean.emailId!=null){
    					if(subscriptionBean.contactNo!=null){
    						if(subscriptionBean.flatNo!=null){
    							if(subscriptionBean.streetName!=null){
    								if(subscriptionBean.landMark!=null){
    									if(subscriptionBean.pincode!=null){
    										return true;
    									}else{
    										Messagebox.show("Pincode required!");
    										return false;
    									}
    								}else{
										Messagebox.show("Landmark required!");
										return false;
									}
    							}else{
									Messagebox.show("Street name required!");
									return false;
								}
    						}else{
								Messagebox.show("Flat no required!");
								return false;
							}
    					}else{
							Messagebox.show("Contact No required!");
							return false;
						}
    				}else{
						Messagebox.show("EmailID required!");
						return false;
					}
    			}else{
					Messagebox.show("Contact Name required!");
					return false;
				}
    		}else{
				Messagebox.show("Area Name required!");
				return false;
			}
    	}else{
			Messagebox.show("City Name required!");
			return false;
		}
    }
   
    private void refresh(){
    	subscriptionBean.cityName = null;
    	subscriptionBean.areaName = null;
    	subscriptionBean.contactName = null;
    	subscriptionBean.contactNo = null;
    	subscriptionBean.emailId = null;
    	subscriptionBean.flatNo = null;
    	subscriptionBean.streetName = null;
    	subscriptionBean.landMark = null;
    	subscriptionBean.pincode = null;
    	subscriptionBean.subscriptionId = null;
    	subscriptionBean.subscriptionNo = null;
    	subscriptionBean.price = 0d;
    	subscriptionBean.mealBean.dayName = null;
    	subscriptionBean.mealBean.typeOfMeal = null;
    	subscriptionBean.mealBeanList.clear();
    	subscriptionBean.createSubscriptionDisability = false;
    }
    
    /***********************************************Getter and Setters********************************************************************************/
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

	public Boolean getMondayButtonVisibility() {
		return mondayButtonVisibility;
	}

	public void setMondayButtonVisibility(Boolean mondayButtonVisibility) {
		this.mondayButtonVisibility = mondayButtonVisibility;
	}

	public SubscriptionBean getSubscriptionBean() {
		return subscriptionBean;
	}

	public void setSubscriptionBean(SubscriptionBean subscriptionBean) {
		this.subscriptionBean = subscriptionBean;
	}

	public Boolean getCuisineGridVisibility() {
		return cuisineGridVisibility;
	}


	public void setCuisineGridVisibility(Boolean cuisineGridVisibility) {
		this.cuisineGridVisibility = cuisineGridVisibility;
	}


	public ArrayList<String> getCityList() {
		return cityList;
	}


	public void setCityList(ArrayList<String> cityList) {
		this.cityList = cityList;
	}


	public ArrayList<String> getAreaList() {
		return areaList;
	}


	public void setAreaList(ArrayList<String> areaList) {
		this.areaList = areaList;
	}


	public PropertyFile getPropertyfile() {
		return propertyfile;
	}


	public void setPropertyfile(PropertyFile propertyfile) {
		this.propertyfile = propertyfile;
	}


	public ArrayList<SubscriptionBean> getSubscriptionBeanList() {
		return subscriptionBeanList;
	}


	public void setSubscriptionBeanList(
			ArrayList<SubscriptionBean> subscriptionBeanList) {
		this.subscriptionBeanList = subscriptionBeanList;
	}


}
