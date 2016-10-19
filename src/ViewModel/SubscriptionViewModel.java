package ViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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

import sun.text.normalizer.UBiDiProps;
import Bean.ManageCuisinBean;
import Bean.MealBean;
import Bean.OrderItemDetailsBean;
import Bean.SubscriptionBean;

public class SubscriptionViewModel {

	Session session = null;
	
	private Connection connection = null;
	
	private String username;
	
	private Integer roleId;
	
	private SubscriptionBean subscriptionBean = new SubscriptionBean();
	
	private ArrayList<SubscriptionBean> subscriptionBeanList = new ArrayList<SubscriptionBean>();
	
	private Boolean mondayButtonVisibility = true;
	
	private Boolean cuisineGridVisibility = false;
	
	private Boolean dayVisibility = true;
	
	private ArrayList<String> cityList= new ArrayList<String>();
	
	private ArrayList<String> areaList= new ArrayList<String>();
	
	PropertyFile propertyfile = new PropertyFile();
	
	private Boolean contactFocus = true;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		connection=(Connection) session.getAttribute("sessionConnection");
		username = (String) session.getAttribute("login");
		roleId = (Integer) session.getAttribute("userRoleId");
		connection.setAutoCommit(true);
		
		onLoadCityList();
	
	}
	
	
	@Command
	@NotifyChange("*")
	public void onChangeContactNo(){
		if(subscriptionBean.contactNo.matches("[0-9]") && subscriptionBean.contactNo.length()==10 ){
			
		}else{
			Messagebox.show("Invalid contact no.!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
			contactFocus =false;
			
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Command
	@NotifyChange("*")
	public void onClickCreateSubscription() throws ParseException{
		
		if(validateFields() ){
			if(subscriptionBean.endDate.before(subscriptionBean.startDate)){
				Messagebox.show("End date must be after start date!");
			}else{
				int truecount=0;
				for(int count=0;count<7;count++){
					if(subscriptionBean.dayVisibilityArray[count]==true){
						truecount++;
					}
				}
				if(truecount<7){
					for(int count=0;count<7;count++){
						subscriptionBean.dayVisibilityArray[count]=true;
					}
					
				}
			
			/*if(subscriptionBean.endDate.before(subscriptionBean.startDate)){
				Messagebox.show("End date must be after start date!");
			}else{*/
				
				Date stdate = new SimpleDateFormat("yyyy-M-d").parse(subscriptionBean.startDate.toString());
				String startDay = new SimpleDateFormat("EEEE",Locale.ENGLISH).format(stdate);
				int stPos = 0 ,enPos= 0;
				
				Date endate = new SimpleDateFormat("yyyy-M-d").parse(subscriptionBean.endDate.toString());
				String endDay = new SimpleDateFormat("EEEE",Locale.ENGLISH).format(endate);
				
				int nodays= getNoOfDaysBetween(stdate, endate);
				
				for(int c = 0 ;c<7;c++){
					if(startDay.equalsIgnoreCase(subscriptionBean.dayArray[c])){
						stPos = c;
					}
					if(endDay.equalsIgnoreCase(subscriptionBean.dayArray[c])){
						enPos = c;
					}
				}
				System.out.println("strt pos-"+stPos+" end pos-"+enPos+" no of days-"+nodays);
		
					if(enPos < stPos && nodays<8){
						System.out.println("enPos < stPos && nodays<8");
						for(int j=stPos;j<7;j++){
							subscriptionBean.dayVisibilityArray[j] = false;
							System.out.println(	subscriptionBean.dayArray[j]);
						}
						
						for(int j=0;j<=enPos;j++){
							subscriptionBean.dayVisibilityArray[j]= false;
							if(j==enPos){
								break;
							}
						}
					
					}else if(enPos > stPos && nodays<7){
						System.out.println("enPos > stPos && nodays<7");
						for(int j=stPos;j<=enPos;j++){
							subscriptionBean.dayVisibilityArray[j]= false;
							if(j==enPos){
								break;
							}
						}
					}else if(enPos > stPos && nodays>7){
						System.out.println("enPos > stPos && nodays>7");
						for(int count=0;count<7;count++){
							subscriptionBean.dayVisibilityArray[count]=false;
						}
					}else if(stPos > enPos && nodays>7){
						System.out.println("stPos > enPos && nodays>7");
						for(int count=0;count<7;count++){
							subscriptionBean.dayVisibilityArray[count]=false;
						}
					}else{
						if(stPos==enPos && subscriptionBean.startDate.before(subscriptionBean.endDate)){
							System.out.println("stPos==enPos && subscriptionBean.startDate.before(subscriptionBean.endDate)");
							for(int count=0;count<7;count++){
								subscriptionBean.dayVisibilityArray[count]=false;
							}
						}else{
							System.out.println("else");
							subscriptionBean.dayVisibilityArray[enPos]=false;
						}
						
					}
			//}
			
			/********************************/
			subscriptionBean.subscriptionNo = generateSubcriptionNo();
			
			//subscriptionBean.subscriptionId = generateSubscriptionId();
			insertSubscriptionData();
			
			subscriptionBean.subscriptionId = getGeneratedKeys();
			
			System.out.println("sub id-->"+subscriptionBean.subscriptionId);
			
			//cuisineGridVisibility = true;
			
			Messagebox.show("Now select day and meal type!","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
			
			subscriptionBean.createSubscriptionDisability = true;
			
			dayVisibility = true;
			}
		}
	}
	
	private int getNoOfDaysBetween(Date stDate,Date enDate){
		long diff = enDate.getTime() - stDate.getTime();
		long dayl = diff/(24*60*60*1000);
		int day = (int)dayl;
		return day+1;
		
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
    		mealBean.orderItemDetailsBeanList = fetchCategoryList();
    		subscriptionBean.saveMealDisability = false;
    		subscriptionBean.mealBean.dayName = mealBean.dayName;
    		subscriptionBean.mealBean.typeOfMeal = mealBean.typeOfMeal;
    		subscriptionBean.mealBean.price = mealBean.price;
    		cuisineGridVisibility = true;
    		subscriptionBean.mealBeanList.add(mealBean);
    		
    	}
		if(dayWithType!=null){
			cuisineGridVisibility =true;
			//loadCuisineList();
			//subscriptionBean.price = 0d;
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickDay(@BindingParam("string")String dayName ){
		if(subscriptionBean.subscriptionId!=null){
			Map<String, String> dayData = new HashMap<String, String>();
			dayData.put("day", dayName);
			Window win = (Window) Executions.createComponents("choseType.zul", null, dayData);
			
			win.doModal();
		}else{
			Messagebox.show("First create subscription !","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
		}
		
		
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
	
	
	public void loadCuisineList(){
		if(subscriptionBean.mealBean.cuisineBeanList.size()>0)
			subscriptionBean.mealBean.cuisineBeanList.clear();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					//String sql = "SELECT cuisin_id,cuisin_name from fapp_cuisins where is_delete = 'N'";
					String sql =" select distinct fkd.cuisin_id,"
	    				   	+" fc.cuisin_name,fc.cuisine_image"
		    				+" from fapp_kitchen_details fkd "
		    				+" join sa_area sa "
		    				+" on sa.area_id = fkd.area_id "
		    				+" join "
		    				+" fapp_cuisins fc "
		    				+" on fkd.cuisin_id = fc.cuisin_id "
		    				+"  where fkd.area_id =  "
		    				+" (select area_id from sa_area where area_name ILIKE ? and city_id = "
		    				+" (select city_id from sa_city where city_name ILIKE ?))";
					
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, subscriptionBean.areaName);
						preparedStatement.setString(2, subscriptionBean.cityName);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							ManageCuisinBean bean = new ManageCuisinBean();
							bean.cuisinId = resultSet.getInt("cuisin_id");
							bean.cuisinName = resultSet.getString("cuisin_name");
							subscriptionBean.mealBean.cuisineBeanList.add(bean);
						}
					} catch (Exception e) {
						// TODO: handle exception
						Messagebox.show("Error Due To:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
	
	@Command
	@NotifyChange("*")
	public void onSelectAreaName(){
		
		fetchCategoryList();
		subscriptionBean.areaId = getAreaId(subscriptionBean.areaName);
		System.out.println(subscriptionBean.areaName+ +subscriptionBean.areaId);
		if(subscriptionBean.createSubscriptionDisability){
			subscriptionBean.saveMealDisability = false;
		}

	}
	
	private Integer getAreaId(String areaName){
		Integer areaId = 0;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT area_id FROM sa_area WHERE area_name = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, areaName);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							areaId = resultSet.getInt("area_id");
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
		return areaId;
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
    
    private String getKitchenIDListFromLocation(){
    	String kitchenIds = "";
    	ArrayList<Integer> kitchenIdList = new ArrayList<Integer>();
		try {
			 SQLKITCHENLIST:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select distinct fkd.kitchen_id "
								+" from fapp_kitchen_details fkd "
								+" join sa_area sa "
								+" on fkd.area_id =  sa.area_id " 
								+" join fapp_kitchen fk "
								+" on fk.kitchen_id =  fkd.kitchen_id "
								+" where sa.area_id = "
								+" (select area_id from sa_area where area_name ILIKE ? and city_id = "
			    				+" (select city_id from sa_city where city_name ILIKE ?))"
								+" and fkd.cuisin_id = (select cuisin_id from fapp_cuisins where cuisin_name = ?) "
								+" and fk.is_active = 'Y'";
					 try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, subscriptionBean.areaName);
						preparedStatement.setString(2, subscriptionBean.cityName);
						preparedStatement.setString(3, subscriptionBean.mealBean.cuisinBean.cuisinName );
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							kitchenIdList.add(resultSet.getInt("kitchen_id"));
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
		StringBuilder kitchenIdListBuilder = new StringBuilder();
		String temp = kitchenIdList.toString();
		String fb = temp.replace("[", "(");
		String bb = fb.replace("]", ")");
		kitchenIdListBuilder.append(bb);
		kitchenIds = kitchenIdListBuilder.toString();
		//System.out.println("KitchenIds -->"+kitchenIds);
		return kitchenIds;
    }
    
    
    @Command
    @NotifyChange("*")
    public void onClickSaveMeal(){
    	
    	saveMealWithDay();
    	Integer subscriptionMealId = getGeneratedKeysForMeal();
    	saveMealItems(subscriptionMealId);
    	subscriptionBean.saveMealDisability = true;
    	subscriptionBean.mealBean.orderItemDetailsBeanList.clear();
    }
	
   
    @Command
    @NotifyChange("*")
    public void onClickFinalSave(){
    	System.out.println(subscriptionBean.price);
    	if(subscriptionBean.subscriptionNo!=null){
    		updatePrice();
    		refresh();
    		cuisineGridVisibility = false;
    		subscriptionBean.saveMealDisability = true ;
    		dayVisibility = false;
    	}
    	else
    		Messagebox.show("Please Fill details.","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
    }
    
    private void insertSubscriptionData(){
    	Integer subscriptionId = null;
    	try {
				SQL:{
			    		PreparedStatement preparedStatement = null;
						ResultSet resultSet = null;
						String sql = "INSERT INTO fapp_subscription( "
						           +" subscription_no, subscribed_by, "
						           +" user_mail_id, contact_number, flat_no, "
						           +" street_name, landmark, pincode,area_id,start_date,end_date)"
						           +" VALUES ( ?, ?, ?, ?,  ?, ?, ?, ?,?,?,?)";
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
							preparedStatement.setInt(9, subscriptionBean.areaId);
							preparedStatement.setDate(10, subscriptionBean.startDate);
							preparedStatement.setDate(11, subscriptionBean.endDate);
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
					String sql = "SELECT MAX(subscription_id) FROM fapp_subscription";
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
					           +" subscription_meal_id, cuisine_id, "
					           +" category_id, quantity, meal_price, kitchen_id)"
					           +" VALUES (?, ?, ?, ?, ?, ?)";
    				try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, subscriptionMealId);
						for(OrderItemDetailsBean bean : subscriptionBean.mealBean.orderItemDetailsBeanList){
				    		if(bean.quantity!=0){
				    			preparedStatement.setInt(2, bean.cuisineId);
				    			preparedStatement.setInt(3, bean.categoryId);
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
				    	  
				    	  updatePrice();
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
							Messagebox.show("Subscribed sucessfully!");
							//refresh();
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
		if (subscriptionBean.cityName != null) {
			if (subscriptionBean.areaName != null) {
				if (subscriptionBean.startDate != null) {
					if (subscriptionBean.endDate != null) {
						if (subscriptionBean.contactName != null) {
						
							if (subscriptionBean.emailId != null) {
								if (subscriptionBean.contactNo != null) {
									if (subscriptionBean.flatNo != null) {
										if (subscriptionBean.streetName != null) {
											if (subscriptionBean.landMark != null) {
												if (subscriptionBean.pincode != null) {
													return true;
												} else {
													Messagebox
															.show("Pincode required!");
													return false;
												}
											} else {
												Messagebox
														.show("Landmark required!");
												return false;
											}
										} else {
											Messagebox
													.show("Street name required!");
											return false;
										}
									} else {
										Messagebox.show("Flat no required!");
										return false;
									}
								} else {
									Messagebox.show("Contact No required!");
									return false;
								}
							} else {
								Messagebox.show("EmailID required!");
								
								return false;
							}
							
							
						} else {
							Messagebox.show("Contact Name required!");
							return false;
						}
					} else {
						Messagebox.show("End Date required!");
						return false;
					}
				} else {
					Messagebox.show("Start Date required!");
					return false;
				}
			} else {
				Messagebox.show("Area Name required!");
				return false;
			}
		} else {
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

	public Boolean getDayVisibility() {
		return dayVisibility;
	}

	public void setDayVisibility(Boolean dayVisibility) {
		this.dayVisibility = dayVisibility;
	}

	public void setContactFocus(Boolean contactFocus) {
		this.contactFocus = contactFocus;
	}


	public Boolean getContactFocus() {
		return contactFocus;
	}


}
