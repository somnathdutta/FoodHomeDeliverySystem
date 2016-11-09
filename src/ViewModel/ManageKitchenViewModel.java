package ViewModel;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Pre;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import dao.ManageKitchenDAO;
import Bean.ItemBean;
import Bean.ManageAlacarteItemBean;
import Bean.ManageCategoryBean;
import Bean.ManageCuisinBean;
import Bean.ManageKitchens;

public class ManageKitchenViewModel {

	private ManageKitchens manageKitchensBean = new ManageKitchens();
	
	private ArrayList<ManageKitchens> manageKitchenBeanList = new ArrayList<ManageKitchens>();
	
	private ManageKitchens kitchenBean = new ManageKitchens();
	
	private ArrayList<ManageKitchens> kitchenBeanList = new ArrayList<ManageKitchens>();
	
	private ArrayList<ManageKitchens> lunchDinnerkitchenBeanList = new ArrayList<ManageKitchens>();;

	ManageKitchens lunchDinnerBean = new ManageKitchens();
	
	private ArrayList<ManageKitchens> lunchDinnerDetailsBeanList = new ArrayList<ManageKitchens>();
	private ManageKitchens lunchDinnerDetailsBean = new ManageKitchens();
	
	ArrayList<ItemBean> kitchenItemList = new ArrayList<ItemBean>();
	
	private ManageCuisinBean cuisinBEAN = new ManageCuisinBean();
	
	private ManageCategoryBean categoryBean = new ManageCategoryBean();
	
	private ArrayList<ManageCuisinBean> cuisinBeanList =  new ArrayList<ManageCuisinBean>();

	private ArrayList<ManageCategoryBean> categoryBeanList = new ArrayList<ManageCategoryBean>();
	
	Session session = null;
	
	private Connection connection = null;
	
	private String userName = "";
	
	private ArrayList<String> discountTypeList= new ArrayList<String>();
	
	private Boolean saveButtonVisibility = true;
	
	private Boolean updateButtonVisibility = false;
	
	private ArrayList<String> cityList= new ArrayList<String>();
	
	private ArrayList<String> areaList= new ArrayList<String>();
	
	private String gridWidth = "30%";
	
	PropertyFile propertyfile = new PropertyFile();
	
	@AfterCompose
	public void initSetUp(@ContextParam(ContextType.VIEW) Component view)throws Exception{
		
		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		connection.setAutoCommit(true);
		
		System.out.println("zul page >> manageKitchen.zul");
		lunchDinnerkitchenBeanList = ManageKitchenDAO.fetchKitchens(connection);
		loadAllKitchenList();
		onLoadCityList();
		loadCuisinList();
	}
	
	
	/**
	 * 
	 * GlobalReload is used to reload the grid when child window is detach
	 */
	@GlobalCommand
	@NotifyChange("*")
	public void globalReload(){
		loadAllKitchenList();
		refresh();
		updateButtonVisibility = false;
		saveButtonVisibility = true;
	}
	
	/**
	 * This method is useful to load saved kitchens to the front page
	 */
	public void loadAllKitchenList(){
		if(manageKitchenBeanList.size()>0){
			manageKitchenBeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement =  null;
					ResultSet resultSet = null;
					try {
						preparedStatement = connection.prepareStatement(propertyfile.getPropValues("onLoadKitchenSql"));
						resultSet = preparedStatement.executeQuery();
						
						while(resultSet.next()){
							
							ManageKitchens manageKitchens = new ManageKitchens();
							manageKitchens.cityName = resultSet.getString("city_name");
							manageKitchens.areaName = resultSet.getString("area_name");
							manageKitchens.kitchenUserName = resultSet.getString("kitchen_user_name");
							manageKitchens.areaId = resultSet.getInt("area_id");
							manageKitchens.address = resultSet.getString("address");
							manageKitchens.pincode = resultSet.getString("pincode");
							manageKitchens.kitchenName = resultSet.getString("kitchen_name");
							manageKitchens.kitchenId = resultSet.getInt("kitchen_id");
							manageKitchens.password = resultSet.getString("kitchen_password");
							manageKitchens.kitchenLatitude = resultSet.getDouble("latitude");
							manageKitchens.kitchenLongitude = resultSet.getDouble("longitude");
							//manageKitchens.emailId = resultSet.getString("email_id");
							manageKitchens.mobileNo = resultSet.getString("mobile_no");
							manageKitchens.servingAreas = resultSet.getString("serving_areas");
							manageKitchens.servingZipCodes = resultSet.getString("serving_zipcodes");
							if( resultSet.getString("is_active").equalsIgnoreCase("Y")){
								manageKitchens.status = "Active";
							}else{
								manageKitchens.status = "Deactive";
							}
							manageKitchens.leadTime = resultSet.getInt("lead_time");
							manageKitchens.emailId = resultSet.getString("email_id");
							manageKitchens.lunchStock = resultSet.getInt("lunch_stock");
							manageKitchens.dinnerStock = resultSet.getInt("dinner_stock");
									
							manageKitchenBeanList.add(manageKitchens);
							
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
	
	
	@Command
	@NotifyChange("*")
	public void onClickLoadItem(){
		
		kitchenBeanList = ManageKitchenDAO.fetchKitchens(connection);
		
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectKitchen(){
		kitchenItemList =  ManageKitchenDAO.fetchKitchenItems(kitchenBean.kitchenId, connection);
	}
	
	
	@Command
	@NotifyChange("*")
	public void onSelectStatus(@BindingParam("bean")ItemBean bean){
		if(bean.categoryId == 78 || bean.categoryId == 79){
			if(ManageKitchenDAO.updateItem(connection, bean.status, kitchenBean.kitchenId, bean.itemCode, true)){
				kitchenBeanList = ManageKitchenDAO.fetchKitchens(connection);
			}
		}else{
			if(ManageKitchenDAO.updateItem(connection, bean.status, kitchenBean.kitchenId, bean.itemCode, false)){
				kitchenBeanList = ManageKitchenDAO.fetchKitchens(connection);
			}
		}
		
	}
	
	/*@Command
	@NotifyChange("*")
	public void onClickUpdate(@BindingParam("bean")ItemBean bean){
		if(ManageKitchenDAO.updateItem(connection, bean.status, kitchenBean.kitchenId, bean.itemCode)){
			kitchenBeanList = ManageKitchenDAO.fetchKitchens(connection);
		}
	}*/
	
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
		manageKitchensBean.areaName="";
		try {
			sql1:{
					PreparedStatement preparedStatement=null;
					try {
							preparedStatement=connection.prepareStatement(propertyfile.getPropValues("areaIdWRTcityNameSql"));
							preparedStatement.setString(1, manageKitchensBean.cityName);
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
		try {
			sql1:{
					PreparedStatement preparedStatement=null;
					try {
							preparedStatement=connection.prepareStatement(propertyfile.getPropValues("areaIdWRTareaNameSql"));
							preparedStatement.setString(1, manageKitchensBean.areaName);
							ResultSet resultSet=preparedStatement.executeQuery();
							
							while(resultSet.next()){
								manageKitchensBean.areaId = resultSet.getInt("area_id");	
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
	public void onSelectCuisineName(@BindingParam("bean") ManageCuisinBean manageCuisineBean){
		
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectCategoryeName(@BindingParam("bean") ManageCategoryBean manageCategoryBean){
		
	}
	
	public void loadCuisinList(){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT * FROM fapp_cuisins WHERE is_delete='N' AND is_active='Y' order by cuisin_id";
				try {
					preparedStatement = connection.prepareStatement(sql);
					resultSet = preparedStatement.executeQuery();
					while (resultSet.next()) {
						ManageCuisinBean bean = new ManageCuisinBean();
						bean.cuisinId = resultSet.getInt("cuisin_id");
						bean.cuisinName = resultSet.getString("cuisin_name");
						cuisinBeanList.add(bean);
						
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
	
	
	@Command
	@NotifyChange("*")
	public void onClickSave(){
		
		if(validateFields()){
			saveKitchenData();
			refresh();
			loadAllKitchenList();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickClear(){
		refresh();
	}
	
	public void saveKitchenData(){
		String message ="";
		Boolean inserted=false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					try {
						preparedStatement = connection.prepareStatement(propertyfile.getPropValues("saveKitchenSql"));
						preparedStatement.setString(1, manageKitchensBean.kitchenName);
						preparedStatement.setString(2, manageKitchensBean.password);
						preparedStatement.setInt(3, manageKitchensBean.areaId);
						preparedStatement.setString(4, manageKitchensBean.address);
						preparedStatement.setString(5, manageKitchensBean.pincode);
						//preparedStatement.setDouble(6, manageKitchensBean.kitchenLatitude);
						//preparedStatement.setDouble(7, manageKitchensBean.kitchenLongitude);
						preparedStatement.setDouble(6, 0.0);
						preparedStatement.setDouble(7, 0.0);
						preparedStatement.setString(8, manageKitchensBean.servingAreas);
						preparedStatement.setString(9, manageKitchensBean.servingZipCodes);
						//preparedStatement.setString(10, manageKitchensBean.emailId);
						preparedStatement.setString(10, manageKitchensBean.mobileNo);
						preparedStatement.setString(11, manageKitchensBean.kitchenUserName);
						if(manageKitchensBean.status.equalsIgnoreCase("Active")){
							preparedStatement.setString(12, "Y");
						}else{
							preparedStatement.setString(12, "N");
						}
						preparedStatement.setString(13, userName);
						preparedStatement.setString(14, userName);
						preparedStatement.setInt(15, Integer.valueOf(manageKitchensBean.leadTime));
						preparedStatement.setString(16, manageKitchensBean.emailId);
						preparedStatement.setInt(17, manageKitchensBean.lunchStock);
						preparedStatement.setInt(18, manageKitchensBean.dinnerStock);
						System.out.println(preparedStatement);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							message=resultSet.getString(1);
							inserted = true;
							System.out.println("Kitchen save message =" + message);
						}
						if (inserted) {
							Messagebox.show("Kitchen Saved Successfully...");
							int kitchenId = getGeneratedKitchenId();
							System.out.println("Kitchen created and Id is =====> "+kitchenId);
							saveKitchendetailsData(kitchenId);
							refresh();
							loadAllKitchenList();
						}
					} catch (Exception e) {
						Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
						e.printStackTrace();
						}finally{
							if(preparedStatement != null){
							preparedStatement.close();
							}
						 }
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	public void saveKitchendetailsData(Integer kitchenId){
		try {
			
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql="INSERT INTO fapp_kitchen_details( "
						+" kitchen_id, cuisin_id, area_id) "
						+" VALUES (?, ?, ?)";
					
					try {
						preparedStatement = connection.prepareStatement(sql);
						for(ManageCuisinBean cuisinBean :  cuisinBeanList){
							if(cuisinBean.chkCuisin){
								preparedStatement.setInt(1, kitchenId);
								preparedStatement.setInt(2, cuisinBean.cuisinId);
								preparedStatement.setInt(3, manageKitchensBean.areaId);
								preparedStatement.addBatch();
							}
						}
						int [] count = preparedStatement.executeBatch();
			    	   
			    	   for(Integer integer : count){
			    		   System.out.println(integer);    
			    	   }
					} catch (Exception e) {
						Messagebox.show("Error Due to: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();// TODO: handle exception
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
			}
			/*SQL:{
					PreparedStatement preparedStatement = null;
					String sql="INSERT INTO fapp_kitchen_details( "
								+" kitchen_id, cuisin_id, area_id) "
								+" VALUES (?, ?, ?)";
					String sql="INSERT INTO fapp_kitchen_stock( "
							+" kitchen_cuisine_id, kitchen_category_id, category_stock, kitchen_id) "
							+" VALUES (?, ?, 0, ?)";
					
					try {
						
						preparedStatement = connection.prepareStatement(sql);
						
						for(ManageCuisinBean cuisinBean :  cuisinBeanList){
							if(cuisinBean.chkCuisin){
								for(ManageCategoryBean bean : cuisinBean.categoryBeanList){
									if(bean.chkCategory){
										System.out.println(cuisinBean.cuisinId);
										System.out.println("Category Id->"+bean.categoryId);
										preparedStatement.setInt(1, cuisinBean.cuisinId);
										preparedStatement.setInt(2, bean.categoryId);
										preparedStatement.setInt(3, kitchenId);
										preparedStatement.addBatch();
									}
								}
									
							}
						}
						
						for(ManageCuisinBean cuisinBean :  cuisinBeanList){
							if(cuisinBean.chkCuisin){
								preparedStatement.setInt(1, getKitchenId());
								preparedStatement.setInt(2, cuisinBean.cuisinId);
								preparedStatement.setInt(3, manageKitchensBean.areaId);
								preparedStatement.addBatch();
							}
							
						}
						int [] count = preparedStatement.executeBatch();
				    	   
				    	   for(Integer integer : count){
				    		   
				    		   System.out.println(integer);
				    		      
				    	   }
						
					} catch (Exception e) {
						Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
						e.printStackTrace();
					}finally{
						if(preparedStatement != null){
							preparedStatement.close();
						}
						if(connection!=null){
							connection.close();
						}
					}
			}*/
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private Integer getKitchenId(){
		Integer kitchenId = 0;
		try {
			SQL:{
				PreparedStatement preparedStatement = null;
				ResultSet resultSet = null;
				String sql = "SELECT kitchen_id from fapp_kitchen where kitchen_name=?";
				try {
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, manageKitchensBean.kitchenName);
					resultSet = preparedStatement.executeQuery();
					if(resultSet.next()){
						kitchenId =  resultSet.getInt(1);
					}
				} catch (Exception e) {
					Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
					e.printStackTrace();
				}finally{
					if(preparedStatement != null){
						preparedStatement.close();
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return kitchenId;
	}
	
	public Integer getGeneratedKitchenId(){
		Integer kitchenId = 0;
		
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select max(kitchen_id)AS kitchen_id from fapp_kitchen";
					
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							kitchenId = resultSet.getInt("kitchen_id");
						}
					} catch (Exception e) {
						Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
						e.printStackTrace();
						}finally{
							if(preparedStatement != null){
							preparedStatement.close();
							}
						 }
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return kitchenId;
	}
	
	@Command
	@NotifyChange("*")
	public void onCheckCuisineName(@BindingParam("bean")ManageCuisinBean bean){
		//System.out.println("bean.chkCuisin->"+bean.chkCuisin);
		if(bean.chkCuisin){
		//	System.out.println("bean.chkCuisin->"+bean.cuisinId);
		//	loadCategoryList(bean);
		 cuisinBEAN.cuisinId = bean.cuisinId;
		}else{
			bean.categoryListVisibility= false;	
		}
	}
	
	public ArrayList<ManageCategoryBean> loadCategoryList(ManageCuisinBean bean){
		if(bean.categoryBeanList.size()>0){
			bean.categoryBeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT * FROM food_category WHERE cuisine_id = ? AND is_delete='N' AND is_active='Y'";
				try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, bean.cuisinId);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							ManageCategoryBean manageCategoryBean = new ManageCategoryBean();
							manageCategoryBean.categoryId = resultSet.getInt("category_id");
							manageCategoryBean.categoryName= resultSet.getString("category_name");
							
							bean.categoryBeanList.add(manageCategoryBean);
						}
						bean.categoryListVisibility = true;
				
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
		return bean.categoryBeanList;
	}
	
	@Command
	@NotifyChange("*")
	public void onCheckCategoryName(@BindingParam("bean")ManageCategoryBean bean){
		//System.out.println("bean.chkcategory->"+bean.chkCategory);
		if(bean.chkCategory){
		//	System.out.println("bean.chkcategory->"+bean.categoryId);
			
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onOKkitchenName(){
		if(isKitchenNameExists()){
			Messagebox.show("Kitchen name already exists!","ALERT",Messagebox.OK,Messagebox.INFORMATION);
			manageKitchensBean.kitchenName=null;
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onOKkitchenUserName(){
		if(isKitchenUserNameExists()){
			Messagebox.show("Kitchen user name already exists!","ALERT",Messagebox.OK,Messagebox.INFORMATION);
			manageKitchensBean.kitchenUserName=null;
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onOKkitchenMobileNo(){
		if(isKitchenMobileNoExists()){
			Messagebox.show("Kitchen mobile number already exists!","ALERT",Messagebox.OK,Messagebox.INFORMATION);
			manageKitchensBean.mobileNo=null;
		}
	}
	
	private Boolean isKitchenNameExists(){
		Boolean notExists = false;
		ArrayList<String> kitchenList = new ArrayList<String>();
		try {
			SQL:{
				PreparedStatement preparedStatement = null;
				ResultSet resultSet = null;
				String sql = "SELECT kitchen_name FROM fapp_kitchen";
				try {
					preparedStatement = connection.prepareStatement(sql);
					resultSet = preparedStatement.executeQuery();
					while (resultSet.next()) {
						kitchenList.add(resultSet.getString(1));
					}
				} catch (Exception e) {
					Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
					e.printStackTrace();
				}finally{
					if(preparedStatement != null){
						preparedStatement.close();
					}
				}
				if(kitchenList.size()>0){
					for(String kitchen : kitchenList){
						if(kitchen.equals(manageKitchensBean.kitchenName)){	
							notExists = true;	
						}	
					}
				}else{
					notExists = true;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return notExists;
	}
	
	private Boolean isKitchenUserNameExists(){
		Boolean notExists = false;
		ArrayList<String> kitchenUserNameList = new ArrayList<String>();
		try {
			SQL:{
				PreparedStatement preparedStatement = null;
				ResultSet resultSet = null;
				String sql = "SELECT kitchen_user_name FROM fapp_kitchen";
				try {
					preparedStatement = connection.prepareStatement(sql);
					resultSet = preparedStatement.executeQuery();
					while (resultSet.next()) {
						kitchenUserNameList.add(resultSet.getString(1));
					}
				} catch (Exception e) {
					Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
					e.printStackTrace();
				}finally{
					if(preparedStatement != null){
						preparedStatement.close();
					}
				}
				if(kitchenUserNameList.size()>0){
					for(String kitchen : kitchenUserNameList){
						if(kitchen!=null){
						if(kitchen.equals(manageKitchensBean.kitchenUserName)){	
							notExists = true;	
						}
						}
					}
				}else{
					notExists = true;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println(notExists);
		return notExists;
	}
	
	private Boolean isKitchenMobileNoExists(){
		Boolean notExists = false;
		ArrayList<String> kitchenMobileNoList = new ArrayList<String>();
		try {
			SQL:{
				PreparedStatement preparedStatement = null;
				ResultSet resultSet = null;
				String sql = "SELECT mobile_no FROM fapp_kitchen";
				try {
					preparedStatement = connection.prepareStatement(sql);
					resultSet = preparedStatement.executeQuery();
					while (resultSet.next()) {
						kitchenMobileNoList.add(resultSet.getString(1));
					}
				} catch (Exception e) {
					Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
					e.printStackTrace();
				}finally{
					if(preparedStatement != null){
						preparedStatement.close();
					}
				}
				if(kitchenMobileNoList.size()>0){
					for(String kitchen : kitchenMobileNoList){
						if(kitchen!=null){
						if(kitchen.equals(manageKitchensBean.mobileNo)){	
							notExists = true;	
						}
						}
					}
				}else{
					notExists = true;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return notExists;
	}
	
	@Command
	@NotifyChange("*")
    public void onClickEdit(@BindingParam("bean")ManageKitchens manageKitchens){
		manageKitchensBean.cityName = manageKitchens.cityName;
		manageKitchensBean.kitchenUserName = manageKitchens.kitchenUserName;
		manageKitchensBean.areaId = manageKitchens.areaId;
		manageKitchensBean.areaName = manageKitchens.areaName;
		manageKitchensBean.address = manageKitchens.address;
		manageKitchensBean.pincode = manageKitchens.pincode;
		manageKitchensBean.kitchenName = manageKitchens.kitchenName;
		manageKitchensBean.kitchenId = manageKitchens.kitchenId;
		manageKitchensBean.password = manageKitchens.password;
		manageKitchensBean.kitchenLatitude = manageKitchens.kitchenLatitude;
		manageKitchensBean.kitchenLongitude = manageKitchens.kitchenLongitude;
		manageKitchensBean.servingAreas = manageKitchens.servingAreas;
		manageKitchensBean.servingZipCodes = manageKitchens.servingZipCodes;
		//manageKitchensBean.emailId = manageKitchens.emailId;
		manageKitchensBean.mobileNo = manageKitchens.mobileNo;
		manageKitchensBean.status = manageKitchens.status;
		manageKitchensBean.leadTime = manageKitchens.leadTime;
		manageKitchensBean.emailId = manageKitchens.emailId;
		manageKitchensBean.lunchStock = manageKitchens.lunchStock;
		manageKitchensBean.dinnerStock = manageKitchens.dinnerStock;
		saveButtonVisibility = false;
		updateButtonVisibility = true;
		//uncheck all cuisines category visibility false
		for(int j=0;j<cuisinBeanList.size();j++){
			cuisinBeanList.get(j).chkCuisin = false;
			cuisinBeanList.get(j).categoryListVisibility= false;
		}
		System.out.println("Kitchen Id------------> "+manageKitchensBean.kitchenId);
		ArrayList<ManageCuisinBean> manageCuisinBeanList =  getCuisineList(manageKitchens.kitchenId);
		
		for(ManageCuisinBean bean : cuisinBeanList){
			for(int i=0;i<manageCuisinBeanList.size() ; i++){
				
				if(bean.cuisinName.equals(manageCuisinBeanList.get(i).cuisinName)){
					System.out.println("Edit cuisine name- - - - - ->"+bean.cuisinName+" cuisine id- - >"+bean.cuisinId);
					bean.chkCuisin = true;
					bean.categoryListVisibility=true;
					
					ArrayList<ManageCategoryBean> categoryBeans = getCategoryList(manageKitchens.kitchenId, bean.cuisinId);
					
					for(ManageCategoryBean categoryBean : loadCategoryList(bean)){
						for(int j=0; j < categoryBeans.size();j++){
							if(categoryBean.categoryName.equals(categoryBeans.get(j).categoryName)){
								System.out.println("Edit category name-->"+categoryBean.categoryName+" category id- - >"+categoryBean.categoryId);
								categoryBean.chkCategory = true;
							}
						}
					}
					break;
				}
				
			}
		}
		
	}
	
	
	@Command
	@NotifyChange("*")
	public void onClickupdate(){
		//System.out.println("Kitchen  - -  >"+manageKitchensBean.kitchenId);
		
		
		for(ManageCuisinBean bean : cuisinBeanList){
			System.out.println(" - - - - - - - - - - -");
			if(bean.chkCuisin){
				if(isCusineServedByKitchen(bean.cuisinId, manageKitchensBean.kitchenId) ){
					System.out.println("Cuisine id "+bean.cuisinId+" served");
				}else{
					System.out.println("Cuisine id "+bean.cuisinId+" not served");
					// add code for new cuisine insertion
					if(insertNewCuisineToKitchen(manageKitchensBean.kitchenId, bean.cuisinId, manageKitchensBean.areaId) ){
						Messagebox.show("New cuisine "+bean.cuisinName+" is added !");
					}
					
				}
				
				/*for(ManageCategoryBean mBean :bean.categoryBeanList){
					if(mBean.chkCategory){
						if( isCategoryServedByKitchen(mBean.categoryId, bean.cuisinId, manageKitchensBean.kitchenId)){
							System.out.println("Category id "+mBean.categoryId+" served");
						}else{
							System.out.println("Category id "+mBean.categoryId+" not served");
							// add code here for new category insertion
							insertNewCategoryToKitchen(manageKitchensBean.kitchenId, bean.cuisinId, mBean.categoryId);
							Messagebox.show("Insert new category with cusine id-"+bean.cuisinId+" and category id-"+mBean.categoryId
									+" and kitchenid-"+manageKitchensBean.kitchenId);
						}
					}else{
						System.out.println("Unchecked category - >"+mBean.categoryName+mBean.categoryId);
						if(isCategoryServedByKitchen(mBean.categoryId, bean.cuisinId, manageKitchensBean.kitchenId)){
							System.out.println("category id"+mBean.categoryId+" was in Db now it is unchecked for deletion!");
							//add code for the deletion of cuisines and respective categories 
							Messagebox.show("Delete category with category id-"+mBean.categoryId);
							deleteCategoryFromKitchen(manageKitchensBean.kitchenId, bean.cuisinId, mBean.categoryId);
						}
					}
				}*/
			}else{
				System.out.println("Unchecked cuisines - >"+bean.cuisinName+bean.cuisinId);
				if(isCusineServedByKitchen(bean.cuisinId, manageKitchensBean.kitchenId)){
					System.out.println("Cuicine id"+bean.cuisinId+" was in Db now it is unchecked for deletion!");
					//add code for the deletion of cuisines and respective categories 
					Messagebox.show("Cuisine deleted!");
					deleteCuisineFromKitchen(manageKitchensBean.kitchenId, bean.cuisinId);
				}
			}
			
		}
		
		if(validateFields()){
			if(upDateKitchenData()){
				//ManageKitchenDAO.updateStock(manageKitchensBean, connection);
				
				if(manageKitchensBean.status.equalsIgnoreCase("Deactive")){
					ManageKitchenDAO.updateDeactiveKitchenStock(connection, manageKitchensBean.kitchenId);
				}
				refresh();
				loadAllKitchenList();
				saveButtonVisibility=true;
				updateButtonVisibility=false;
			}
		}
	}
	
	//check whether a cuisine served by specific kitchen or not
	private Boolean isCusineServedByKitchen(Integer cuisineId , Integer KitchenId){
		Boolean isCuisineServed = false;
		ArrayList<Integer> idList =  new ArrayList<Integer>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "Select cuisin_id from fapp_kitchen_details where kitchen_id = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, KitchenId);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							idList.add(resultSet.getInt("cuisin_id"));
						}
					} catch (Exception e) {
						// TODO: handle exception
						Messagebox.show("Error due to:"+e.getMessage(),"Information",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(idList.contains(cuisineId)){
			isCuisineServed = true;
		}else{
			isCuisineServed = false;
		}
		return isCuisineServed;
	}
	
	private Boolean isCategoryServedByKitchen(Integer categoryId, Integer cuisineId , Integer KitchenId){
		Boolean isCategoryServed = false;
		ArrayList<Integer> idList =  new ArrayList<Integer>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "Select kitchen_category_id from fapp_kitchen_stock where kitchen_id = ? and kitchen_cuisine_id = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, KitchenId);
						preparedStatement.setInt(2, cuisineId);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							idList.add(resultSet.getInt("kitchen_category_id"));
						}
					} catch (Exception e) {
						// TODO: handle exception
						Messagebox.show("Error due to:"+e.getMessage(),"Information",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(idList.contains(categoryId)){
			isCategoryServed = true;
		}else{
			isCategoryServed = false;
		}
		return isCategoryServed;
	}
	
	private Boolean insertNewCuisineToKitchen(Integer kitchenId, Integer cuisineId, Integer areaId){
		Boolean inserted = false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "INSERT INTO fapp_kitchen_details(kitchen_id, cuisin_id, area_id) "
				               +" VALUES ( ?, ?, ?)";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, kitchenId);
						preparedStatement.setInt(2, cuisineId);
						preparedStatement.setInt(3, areaId);
						int count = preparedStatement.executeUpdate();
						if(count>0){
							inserted = true; 
						}
					} catch (Exception e) {
						// TODO: handle exception
						Messagebox.show("Error due to :"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return inserted;
	}
	
	private Boolean insertNewCategoryToKitchen(Integer kitchenId, Integer cuisineId, Integer categoryId){
		Boolean inserted = false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "INSERT INTO fapp_kitchen_stock(kitchen_id, kitchen_cuisine_id, kitchen_category_id) "
				               +" VALUES ( ?, ?, ?)";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, kitchenId);
						preparedStatement.setInt(2, cuisineId);
						preparedStatement.setInt(3, categoryId);
						int count = preparedStatement.executeUpdate();
						if(count>0){
							inserted = true; 
						}
					} catch (Exception e) {
						// TODO: handle exception
						Messagebox.show("Error due to :"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return inserted;
	}

	private Boolean deleteCuisineFromKitchen(Integer kitchenId, Integer cuisineId){
		Boolean isdeleted = false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
				//	String sql = "UPDATE fapp_kitchen_details SET is_delete = 'Y' where kitchen_id = ? AND cuisin_id = ?";
					String sql = "DELETE FROM fapp_kitchen_details where kitchen_id = ? AND cuisin_id = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, kitchenId);
						preparedStatement.setInt(2, cuisineId);
						int count = preparedStatement.executeUpdate();
						if(count>0){
							isdeleted = true; 
						}
					} catch (Exception e) {
						// TODO: handle exception
						Messagebox.show("Error due to :"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
			}
		
		SQL:{
				PreparedStatement preparedStatement = null;
				//String sql = "UPDATE fapp_kitchen_stock SET is_delete = 'Y' where kitchen_id = ? AND kitchen_cuisine_id = ?";
				String sql = "DELETE FROM fapp_kitchen_stock where kitchen_id = ? AND kitchen_cuisine_id = ?";
				try {
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setInt(1, kitchenId);
					preparedStatement.setInt(2, cuisineId);
					int count = preparedStatement.executeUpdate();
					if(count>0){
						isdeleted = true; 
					}
				} catch (Exception e) {
					// TODO: handle exception
					Messagebox.show("Error due to :"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
				}finally{
					if(preparedStatement!=null){
						preparedStatement.close();
					}
				}
		}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return isdeleted;
	}
	
	private Boolean deleteCategoryFromKitchen(Integer kitchenId, Integer cuisineId, Integer categoryId){
		Boolean isDeleted = false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "UPDATE fapp_kitchen_stock SET is_delete = 'Y' where kitchen_id = ? AND "
							   + " kitchen_cuisine_id = ? AND kitchen_category_id=?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, kitchenId);
						preparedStatement.setInt(2, cuisineId);
						preparedStatement.setInt(3, categoryId);
						int count = preparedStatement.executeUpdate();
						if(count>0){
							isDeleted = true; 
						}
					} catch (Exception e) {
						// TODO: handle exception
						Messagebox.show("Error due to :"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return isDeleted;
	}
	
	public ArrayList<ManageCuisinBean> getCuisineList(Integer kitchenId){
		ArrayList<ManageCuisinBean> manageCuisinBeanList = new ArrayList<ManageCuisinBean>();
		if(manageCuisinBeanList.size()>0){
			manageCuisinBeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					/*String sql = "select distinct fks.kitchen_cuisine_id,"
								+" fc.cuisin_name"
								+" from fapp_kitchen_stock fks"
								+" join fapp_cuisins fc"
								+" on fks.kitchen_cuisine_id = fc.cuisin_id"
								+" where fks.kitchen_id = ?";*/
					String sql ="select fkd.cuisin_id,(select cuisin_name from fapp_cuisins where cuisin_id =fkd.cuisin_id)AS cuisin_name"
								+ " from fapp_kitchen_details fkd where kitchen_id=? ";
				try {
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setInt(1, kitchenId);
					resultSet = preparedStatement.executeQuery();
					while (resultSet.next()) {
						ManageCuisinBean bean = new ManageCuisinBean();
						if(bean.chkCuisin){
						bean.chkCuisin = false;
						}
						//bean.categoryListVisibility = false;
						bean.cuisinId = resultSet.getInt("cuisin_id");
						bean.cuisinName = resultSet.getString("cuisin_name");
						manageCuisinBeanList.add(bean);
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
		return manageCuisinBeanList;
	}
	
	public ArrayList<ManageCategoryBean> getCategoryList(Integer kitchenId , Integer cuisineId){
		ArrayList<ManageCategoryBean> manageCategoryBeanList = new ArrayList<ManageCategoryBean>();
		if(manageCategoryBeanList.size()>0)
			manageCategoryBeanList.clear();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select distinct fks.kitchen_category_id,"
								+" fc.category_name"
								+" from fapp_kitchen_stock fks"
								+" join food_category fc"
								+" on fks.kitchen_category_id = fc.category_id"
								+" where fks.kitchen_id = ?"
								+" and fks.kitchen_cuisine_id=?";
					
				try {
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setInt(1, kitchenId);
					preparedStatement.setInt(2, cuisineId);
					resultSet = preparedStatement.executeQuery();
					while (resultSet.next()) {
						ManageCategoryBean manageCategoryBean = new ManageCategoryBean();
						manageCategoryBean.categoryId = resultSet.getInt("kitchen_category_id");
						manageCategoryBean.categoryName= resultSet.getString("category_name");
						manageCategoryBeanList.add(manageCategoryBean);
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
		return manageCategoryBeanList;
	}
	
	private Boolean upDateKitchenData(){
		Boolean updated = false;
		String message = "";
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					try {
						preparedStatement =  connection.prepareStatement(propertyfile.getPropValues("updateKitchenSql"));
						preparedStatement.setString(1, manageKitchensBean.kitchenName);
						preparedStatement.setString(2, manageKitchensBean.password);
						preparedStatement.setInt(3, manageKitchensBean.areaId);
						preparedStatement.setString(4, manageKitchensBean.address);
						preparedStatement.setString(5, manageKitchensBean.pincode);
						preparedStatement.setDouble(6, manageKitchensBean.kitchenLatitude);
						preparedStatement.setDouble(7, manageKitchensBean.kitchenLongitude);
						preparedStatement.setString(8, manageKitchensBean.servingAreas);
						preparedStatement.setString(9, manageKitchensBean.servingZipCodes);
						//preparedStatement.setString(10, manageKitchensBean.emailId);
						preparedStatement.setString(10, manageKitchensBean.mobileNo);
						preparedStatement.setString(11, manageKitchensBean.kitchenUserName);
						if(manageKitchensBean.status.equalsIgnoreCase("Active")){
							preparedStatement.setString(12, "Y");
						}else{
							preparedStatement.setString(12, "N");
						}
						preparedStatement.setString(13, userName);
						preparedStatement.setString(14, userName);
						preparedStatement.setInt(15, manageKitchensBean.leadTime);
						preparedStatement.setString(16, manageKitchensBean.emailId);
						preparedStatement.setInt(17, manageKitchensBean.lunchStock);
						preparedStatement.setInt(18, manageKitchensBean.dinnerStock);
						preparedStatement.setInt(19, manageKitchensBean.kitchenId);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							message=resultSet.getString(1);
							updated = true;
							System.out.println(message);
						}
						if(updated){
							Messagebox.show("Data updated!","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
							
						}
					} catch (Exception e) {
						// TODO: handle exception
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
		return updated;
	}
	
	
	@Command
	@NotifyChange("*")
    public void onChangePincode(){
		/*String[] latlong = null;
		try {
			 latlong = getLatLongPositions(manageKitchensBean.address+","+manageKitchensBean.pincode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		manageKitchensBean.kitchenLatitude = Double.parseDouble(latlong[0]);
		manageKitchensBean.kitchenLongitude = Double.parseDouble(latlong[1]);*/
	}

	@Command
	@NotifyChange("*")
    public void	onClickDelete(@BindingParam("bean")ManageKitchens manageKitchens){
		
		Map<String, Object> parentMap = new HashMap<String, Object>();
		
		parentMap.put("parentKitchenObject", manageKitchens);
		
		Window win = (Window) Executions.createComponents("categoryDeleteConfirmationMessageBox.zul", null, parentMap);
		
		win.doModal();
	}
	
	
	public static String[] getLatLongPositions(String address) throws Exception
    {
      int responseCode = 0;
      String api = "http://maps.googleapis.com/maps/api/geocode/xml?address=" + URLEncoder.encode(address, "UTF-8") + "&sensor=true";
      URL url = new URL(api);
      HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
      httpConnection.connect();
      responseCode = httpConnection.getResponseCode();
      if(responseCode == 200)
      {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();;
        Document document = builder.parse(httpConnection.getInputStream());
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr = xpath.compile("/GeocodeResponse/status");
        String status = (String)expr.evaluate(document, XPathConstants.STRING);
        if(status.equals("OK"))
        {
           expr = xpath.compile("//geometry/location/lat");
           String latitude = (String)expr.evaluate(document, XPathConstants.STRING);
           expr = xpath.compile("//geometry/location/lng");
           String longitude = (String)expr.evaluate(document, XPathConstants.STRING);
           return new String[] {latitude, longitude};
        }
        else
        {
           throw new Exception("Error from the API - response status: "+status);
        }
      }
      return null;
    }
	
	private Boolean validateFields(){
		
		if(manageKitchensBean.cityName!=null){
			
			if(manageKitchensBean.areaId!=null){
				
				if(manageKitchensBean.address!=null){
				
				  if(manageKitchensBean.pincode!=null){	
			    	
					 /* if(manageKitchensBean.kitchenLatitude!=null){
					
						  if(manageKitchensBean.kitchenLongitude!=null){*/
						
							  if(manageKitchensBean.kitchenName!=null){
							
								 /* if(manageKitchensBean.kitchenUserName !=null){*/
								  
									  if(manageKitchensBean.mobileNo!=null){
							
									  if(manageKitchensBean.password!=null){
								
										  if(manageKitchensBean.status!=null){
											  return true;
										  }else{
											  Messagebox.show("Kitchen will active or not?");
											  return false;
										  }
									  }else{
										  Messagebox.show("Password required!");
										  return false;
									  }
								  }else{
									  Messagebox.show("Mobile No.ssssss required for app!");
									  return false;
								  }
								 /* }else{
									  Messagebox.show("User name required for app!");
									  return false;
								  }*/
							  }else{
								  Messagebox.show("Kitchen name required!");
								  return false;
							  }
						 /* }else{
							Messagebox.show("Longitude required!");
							return false;
						}
					  }else{
						Messagebox.show("Latitude required!");
						return false;
					}*/
				  }else{
						Messagebox.show("Pincode required!");
						return false;
					}	
				}else{
					Messagebox.show("Address required!");
					return false;
				}
				}else{
						Messagebox.show("Area name required!");
						return false;
					}
			}else{
				Messagebox.show("City name required!");
				return false;
			}
	}
			
	
	public void refresh(){
		manageKitchensBean.cityName = null;
		manageKitchensBean.areaId = null;
		manageKitchensBean.areaName = null;
		manageKitchensBean.address = null;
		manageKitchensBean.pincode = null;
		manageKitchensBean.kitchenName = null;
		manageKitchensBean.password = null;
		manageKitchensBean.status = null;
		manageKitchensBean.kitchenLatitude = null;
		manageKitchensBean.kitchenLongitude = null;
		manageKitchensBean.mobileNo = null;
		manageKitchensBean.kitchenUserName = null;
		manageKitchensBean.servingAreas = null;
		manageKitchensBean.servingZipCodes = null;
		manageKitchensBean.leadTime =null;
		manageKitchensBean.lunchStock = 0;
		manageKitchensBean.dinnerStock = 0;
		manageKitchensBean.emailId = null;
		for(ManageCuisinBean cuisinBean :  cuisinBeanList){
			if(cuisinBean.chkCuisin){
				/*for(ManageCategoryBean bean : cuisinBean.categoryBeanList){
					if(bean.chkCategory){
						cuisinBean.chkCuisin = false;
						bean.chkCategory = false;
						cuisinBean.categoryListVisibility = false;
					}
				}*/
				cuisinBean.chkCuisin = false;	
			}else{
				cuisinBean.categoryListVisibility = false;
			}
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectLunchDinnerKitchen(){
		lunchDinnerDetailsBeanList = ManageKitchenDAO.loadlunchDinnerDetails(connection, lunchDinnerBean.kitchenId);
		
		if(lunchDinnerDetailsBeanList.size() ==0){
			Messagebox.show("No Category Found!", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickClearLunchDinner(){
		lunchDinnerDetailsBeanList.clear();
		lunchDinnerBean.kitchenName = null;
		lunchDinnerkitchenBeanList = ManageKitchenDAO.fetchKitchens(connection);
	}
	
	@Command
	@NotifyChange("*")
	public void onClickUpdate(@BindingParam("bean") ManageKitchens bean){
		int i = 0;
		i = ManageKitchenDAO.upDateCategory(connection, bean);
		if(i>0){
			lunchDinnerDetailsBeanList = ManageKitchenDAO.loadlunchDinnerDetails(connection, lunchDinnerBean.kitchenId);
			Messagebox.show("Updated Successfully", "Inforation", Messagebox.OK, Messagebox.INFORMATION);
		}
	}
	
	
	public ManageKitchens getManageKitchensBean() {
		return manageKitchensBean;
	}

	public void setManageKitchensBean(ManageKitchens manageKitchensBean) {
		this.manageKitchensBean = manageKitchensBean;
	}

	public ArrayList<ManageKitchens> getManageKitchenBeanList() {
		return manageKitchenBeanList;
	}

	public void setManageKitchenBeanList(
			ArrayList<ManageKitchens> manageKitchenBeanList) {
		this.manageKitchenBeanList = manageKitchenBeanList;
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


	public ArrayList<String> getDiscountTypeList() {
		return discountTypeList;
	}


	public void setDiscountTypeList(ArrayList<String> discountTypeList) {
		this.discountTypeList = discountTypeList;
	}


	public Boolean getSaveButtonVisibility() {
		return saveButtonVisibility;
	}


	public void setSaveButtonVisibility(Boolean saveButtonVisibility) {
		this.saveButtonVisibility = saveButtonVisibility;
	}


	public Boolean getUpdateButtonVisibility() {
		return updateButtonVisibility;
	}


	public void setUpdateButtonVisibility(Boolean updateButtonVisibility) {
		this.updateButtonVisibility = updateButtonVisibility;
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


	public ArrayList<ManageCuisinBean> getCuisinBeanList() {
		return cuisinBeanList;
	}


	public void setCuisinBeanList(ArrayList<ManageCuisinBean> cuisinBeanList) {
		this.cuisinBeanList = cuisinBeanList;
	}



	public ManageCategoryBean getCategoryBean() {
		return categoryBean;
	}


	public void setCategoryBean(ManageCategoryBean categoryBean) {
		this.categoryBean = categoryBean;
	}


	public ArrayList<ManageCategoryBean> getCategoryBeanList() {
		return categoryBeanList;
	}


	public void setCategoryBeanList(ArrayList<ManageCategoryBean> categoryBeanList) {
		this.categoryBeanList = categoryBeanList;
	}


	public String getGridWidth() {
		return gridWidth;
	}


	public void setGridWidth(String gridWidth) {
		this.gridWidth = gridWidth;
	}


	public ManageCuisinBean getCuisinBEAN() {
		return cuisinBEAN;
	}


	public void setCuisinBEAN(ManageCuisinBean cuisinBEAN) {
		this.cuisinBEAN = cuisinBEAN;
	}


	public ArrayList<ManageKitchens> getKitchenBeanList() {
		return kitchenBeanList;
	}


	public void setKitchenBeanList(ArrayList<ManageKitchens> kitchenBeanList) {
		this.kitchenBeanList = kitchenBeanList;
	}


	public ManageKitchens getKitchenBean() {
		return kitchenBean;
	}


	public void setKitchenBean(ManageKitchens kitchenBean) {
		this.kitchenBean = kitchenBean;
	}


	public ArrayList<ItemBean> getKitchenItemList() {
		return kitchenItemList;
	}


	public void setKitchenItemList(ArrayList<ItemBean> kitchenItemList) {
		this.kitchenItemList = kitchenItemList;
	}


	public ArrayList<ManageKitchens> getLunchDinnerkitchenBeanList() {
		return lunchDinnerkitchenBeanList;
	}


	public void setLunchDinnerkitchenBeanList(
			ArrayList<ManageKitchens> lunchDinnerkitchenBeanList) {
		this.lunchDinnerkitchenBeanList = lunchDinnerkitchenBeanList;
	}


	public ManageKitchens getLunchDinnerBean() {
		return lunchDinnerBean;
	}


	public void setLunchDinnerBean(ManageKitchens lunchDinnerBean) {
		this.lunchDinnerBean = lunchDinnerBean;
	}


	public ArrayList<ManageKitchens> getLunchDinnerDetailsBeanList() {
		return lunchDinnerDetailsBeanList;
	}


	public void setLunchDinnerDetailsBeanList(
			ArrayList<ManageKitchens> lunchDinnerDetailsBeanList) {
		this.lunchDinnerDetailsBeanList = lunchDinnerDetailsBeanList;
	}


	public ManageKitchens getLunchDinnerDetailsBean() {
		return lunchDinnerDetailsBean;
	}


	public void setLunchDinnerDetailsBean(ManageKitchens lunchDinnerDetailsBean) {
		this.lunchDinnerDetailsBean = lunchDinnerDetailsBean;
	}
	
	
}
