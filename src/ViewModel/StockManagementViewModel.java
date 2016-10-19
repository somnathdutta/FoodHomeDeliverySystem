package ViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Pre;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import com.itextpdf.text.log.SysoCounter;

import dao.StockDAO;
import Bean.StockCategoryBean;

public class StockManagementViewModel {

	private StockCategoryBean stockCategoryBean = new StockCategoryBean();
	
	public ArrayList<StockCategoryBean> stockCategoryBeanList =  new ArrayList<StockCategoryBean>();
	
	public ArrayList<String> cuisineList =  new ArrayList<String>();

	public ArrayList<String> kitchenList =  new ArrayList<String>();
	
	public String kitchenName ="";
	
	Session session = null;
	
	private Connection connection = null;
	
	private String userName = "";
	
	private Integer roleId = 0;
	
	public Boolean kitchenDivVisibility = false;
	
	PropertyFile propertyfile = new PropertyFile(); 
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		
		roleId = (Integer) session.getAttribute("userRoleId");
		
		if(roleId==1){
			kitchenDivVisibility = true;
			onLoadKitchenList();
		}else{
			stockCategoryBean.kitchenName = userName;
			loadAllStocks(userName);
		}
		
		connection.setAutoCommit(true);
		
		
		//onLoadCuisineList();		
	}
	
	public void loadAllStocks(String userName){
		stockCategoryBeanList = StockDAO.fetchStockOfKitchen(userName, connection);
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
	
	@Command
	@NotifyChange("*")
	public void onSelectKitchen(){
		stockCategoryBeanList.clear();
		stockCategoryBean.cuisineName="";
		userName = kitchenName;
		stockCategoryBean.kitchenName = kitchenName;
		//onLoadCuisineList();
		loadAllStocks(kitchenName);
	}
	
	/**
	 * 
	 * Load all cuisine list
	 */
	public void onLoadCuisineList(){
		if(cuisineList.size()>0){
			cuisineList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sqlKitchen = "SELECT "
										+" fc.cuisin_name " 
										+" FROM fapp_cuisins fc, "
										+" fapp_kitchen_details fkd "
										+" WHERE fc.cuisin_id = fkd.cuisin_id "
										+" AND fkd.kitchen_id = (SELECT kitchen_id FROM fapp_kitchen WHERE kitchen_name = ?)";
					try {
						preparedStatement = connection.prepareStatement(sqlKitchen);
						preparedStatement.setString(1,userName);
						resultSet =  preparedStatement.executeQuery();
						while (resultSet.next()) {
							cuisineList.add(resultSet.getString(1));
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
	public void onSelectCuisine(){
		if(stockCategoryBeanList.size()>0){
			stockCategoryBeanList.clear();
		}
		try {
			SQL:{
				PreparedStatement preparedStatement = null;
				ResultSet resultSet = null;	
				String sql ="select "
							+" fks.kitchen_category_id,"
							+" fc.category_name,"
							+" fks.category_stock, "
							+" fks.lunch_stock,fks.dinner_stock,"
							+" fks.cost_price "
							+" from fapp_kitchen_stock fks,"
							+" food_category fc"
							+" where "
							+" fks.kitchen_category_id = fc.category_id"
							+" and kitchen_cuisine_id = (SELECT cuisin_id FROM fapp_cuisins WHERE cuisin_name=?)"
							+" and kitchen_id=(SELECT kitchen_id FROM fapp_kitchen WHERE kitchen_name=?)";
				try {
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, stockCategoryBean.cuisineName);
					preparedStatement.setString(2, userName);
					resultSet = preparedStatement.executeQuery();
					while(resultSet.next()){
						StockCategoryBean categoryBean = new StockCategoryBean();
						categoryBean.categoryName = resultSet.getString("category_name");
						categoryBean.categoryId = resultSet.getInt("kitchen_category_id");
						categoryBean.stock = resultSet.getInt("category_stock");
						categoryBean.lunchStock = resultSet.getInt("lunch_stock");
						categoryBean.dinnerStock = resultSet.getInt("dinner_stock");
						categoryBean.costPrice = resultSet.getDouble("cost_price");
						stockCategoryBeanList.add(categoryBean);
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
	
	public Integer getKitchenId(){
		Integer kitchenId =0 ;
		try {
			SQL:{
				PreparedStatement preparedStatement = null;
				ResultSet resultSet = null;	
				String sql ="SELECT kitchen_id FROM fapp_kitchen WHERE kitchen_name = ?";
				try {
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, userName);
					resultSet = preparedStatement.executeQuery();
					if(resultSet.next()){
						kitchenId = resultSet.getInt(1);
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
		return kitchenId;
	}
	
	
	@Command
	@NotifyChange("*")
	public void onClickEdit(@BindingParam("bean")StockCategoryBean stockbean){
		stockbean.stockDisability = false;
		stockbean.editVisibility = false;
		System.out.println(roleId);
		if(roleId==1)
		stockbean.priceDisability = false;
		else
			stockbean.priceDisability = true;
		stockbean.updateVisibility = true;
	}
	
	@Command
	@NotifyChange("*")
	public void onClickUpdate(@BindingParam("bean")StockCategoryBean stockbean){
		StockDAO.updateStock(stockbean, connection);
		/* try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "UPDATE fapp_kitchen_stock SET category_stock=? ,lunch_stock =?,dinner_stock = ?, cost_price = ? WHERE"
							   + " kitchen_cuisine_id = (SELECT cuisin_id FROM fapp_cuisins WHERE cuisin_name=?)"
							   + " AND kitchen_category_id = ?  AND"
							   + " kitchen_id=(SELECT kitchen_id FROM fapp_kitchen WHERE kitchen_name=?) ";
					try {
						preparedStatement  = connection.prepareStatement(sql);
						if(stockbean.stock!=null){
							preparedStatement.setInt(1, stockbean.stock);
						}else{
							preparedStatement.setInt(1, 0);	
						}
						
						if(stockbean.lunchStock!=null){
							preparedStatement.setInt(2, stockbean.lunchStock);
						}else{
							preparedStatement.setInt(2, 0);
						}
						
						if(stockbean.dinnerStock!=null){
							preparedStatement.setInt(3, stockbean.dinnerStock);
						}else{
							preparedStatement.setInt(3, 0);
						}
						
						if(stockbean.costPrice!=null){
							preparedStatement.setDouble(4, stockbean.costPrice);
						}else{
							preparedStatement.setDouble(4, 0.0);
						}
						preparedStatement.setString(5, stockCategoryBean.cuisineName );
						preparedStatement.setInt(6, stockbean.categoryId);
						preparedStatement.setString(7, userName);
						int update = preparedStatement.executeUpdate();
						if(update>0){
							Messagebox.show("Stock updated!");
							stockbean.stockDisability = true;
							stockbean.editVisibility = true;
							stockbean.priceDisability = true;
							stockbean.updateVisibility = false;
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
		} */
	}
	
	@Command
	@NotifyChange("*")
	public void onClickClose(){
		if(userName.equalsIgnoreCase("admin")){
			Messagebox.show("Please choose kitchen to close for a day!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
		}else{
		
		Messagebox.show("It will cause not showing your items in the app for today only. \nAre you sure to close kitchen for today?", "Confirm Dialog", 
				Messagebox.OK |  Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
		    public void onEvent(Event evt) throws InterruptedException {
		        if (evt.getName().equals("onOK")) {
		        	System.out.println("Kitchen---->"+kitchenName+" kitchen id--->"+userName);
		        	if( setInactive(kitchenName) ){
		        		 Messagebox.show("Kitchen closed for today successfully!");
		        	}
		          /*refresh();
				   onLoad();*/
		         // BindUtils.postGlobalCommand(null, null, "globalReload", null);
				 //  Messagebox.show("Tax data deleted successfully!");
		        } else if (evt.getName().equals("onIgnore")) {
		            Messagebox.show("Ignore Save", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
		        } else {
		           
		        }
		    }
		});
		
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickOpen(){
		if(userName.equalsIgnoreCase("admin")){
			Messagebox.show("Please choose kitchen to close for a day!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);	
		}
		else if(isClosed(kitchenName)){
			Messagebox.show("Already closed!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
		}else{
			Messagebox.show("Already  open!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
		}
	}
	
	public boolean setInactive(String kitchenName){
		boolean isInActive = false;
		try {
			SQL:{
				  PreparedStatement preparedStatement = null;
				  String sql = "UPDATE fapp_kitchen_stock set category_stock = 0,lunch_stock = 0, dinner_stock = 0, is_active='N' where kitchen_id = "
				  		+ "(select kitchen_id from fapp_kitchen where kitchen_name = ?)";
				  try {
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, kitchenName);
					int update = preparedStatement.executeUpdate();
					if(update>0){
						isInActive = true;
					}
				} catch (Exception e) {
					Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
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
		return isInActive;
	}
	
	public boolean isClosed(String kitchenName){
		String status = "";
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null ;
					String sql = "select distinct is_active from fapp_kitchen_stock where kitchen_id = "
							+ "(select kitchen_id from fapp_kitchen where kitchen_name = ?)";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, kitchenName);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							status = resultSet.getString("is_active");
						}
					} catch (Exception e) {
						Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
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
		if(status.equalsIgnoreCase("N")){
			return true;
		}	
		else {
			return false;
		}
	}
	
	public boolean setIsActive(String kitchenname){
		boolean stock= false,lunch=false,dinner=false,status = false;
		try {
			SQLMainStock:{
				PreparedStatement preparedStatement = null;
				String sql = "update fapp_kitchen_stock set category_stock = stock_updation.stock from "
					+ " stock_updation where fapp_kitchen_stock.kitchen_id=stock_updation.kitchen_id and fapp_kitchen_stock.kitchen_id="
					+ "(select kitchen_id from fapp_kitchen where kitchen_name = ?)";
				try {
					preparedStatement= connection.prepareStatement(sql);
					preparedStatement.setString(1, kitchenname);
					int count = preparedStatement.executeUpdate();
					if(count>0){
						stock = true;
					}
				} catch (Exception e) {
					Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
					e.printStackTrace();
				}finally{
					if(preparedStatement!=null){
						preparedStatement.close();
					}
				}
			}
			SQLLunchStock:{
				PreparedStatement preparedStatement = null;
				String sql = "update fapp_kitchen_stock set lunch_stock = stock_updation.lunch_stock from "
					+ " stock_updation where fapp_kitchen_stock.kitchen_id=stock_updation.kitchen_id and fapp_kitchen_stock.kitchen_id="
					+ "(select kitchen_id from fapp_kitchen where kitchen_name = ?)";
				try {
					preparedStatement= connection.prepareStatement(sql);
					preparedStatement.setString(1, kitchenname);
					int count = preparedStatement.executeUpdate();
					if(count>0){
						lunch = true;
					}
				} catch (Exception e) {
					Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
					e.printStackTrace();
				}finally{
					if(preparedStatement!=null){
						preparedStatement.close();
					}
				}
			}
			SQLDinnerStock:{
				PreparedStatement preparedStatement = null;
				String sql = "update fapp_kitchen_stock set dinner_stock = stock_updation.dinner_stock from "
					+ " stock_updation where fapp_kitchen_stock.kitchen_id=stock_updation.kitchen_id and fapp_kitchen_stock.kitchen_id="
					+ "(select kitchen_id from fapp_kitchen where kitchen_name = ?)";
				try {
					preparedStatement= connection.prepareStatement(sql);
					preparedStatement.setString(1, kitchenname);
					int count = preparedStatement.executeUpdate();
					if(count>0){
						dinner = true;
					}
				} catch (Exception e) {
					Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
					e.printStackTrace();
				}finally{
					if(preparedStatement!=null){
						preparedStatement.close();
					}
				}
			}
			SQLActive:{
				PreparedStatement preparedStatement = null;
				String sql = "update fapp_kitchen_stock set is_active ='Y' where kitchen_id="
					+ "(select kitchen_id from fapp_kitchen where kitchen_name = ?)";
				try {
					preparedStatement= connection.prepareStatement(sql);
					preparedStatement.setString(1, kitchenname);
					int count = preparedStatement.executeUpdate();
					if(count>0){
						status = true;
					}
				} catch (Exception e) {
					Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
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
		if(stock && lunch && dinner && status){
			return true;
		}else {
			return false;
		}
	}
	
	
	public StockCategoryBean getStockCategoryBean() {
		return stockCategoryBean;
	}

	public void setStockCategoryBean(StockCategoryBean stockCategoryBean) {
		this.stockCategoryBean = stockCategoryBean;
	}

	public ArrayList<StockCategoryBean> getStockCategoryBeanList() {
		return stockCategoryBeanList;
	}

	public void setStockCategoryBeanList(
			ArrayList<StockCategoryBean> stockCategoryBeanList) {
		this.stockCategoryBeanList = stockCategoryBeanList;
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


	public PropertyFile getPropertyfile() {
		return propertyfile;
	}


	public void setPropertyfile(PropertyFile propertyfile) {
		this.propertyfile = propertyfile;
	}

	public ArrayList<String> getCuisineList() {
		return cuisineList;
	}

	public void setCuisineList(ArrayList<String> cuisineList) {
		this.cuisineList = cuisineList;
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



	public Integer getRoleId() {
		return roleId;
	}



	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}



	public Boolean getKitchenDivVisibility() {
		return kitchenDivVisibility;
	}



	public void setKitchenDivVisibility(Boolean kitchenDivVisibility) {
		this.kitchenDivVisibility = kitchenDivVisibility;
	}

	
	
}
