package ViewModel;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import Bean.ManageCategoryItemBean;
import Bean.SubitemBean;

public class SubitemViewModel {
	
	private SubitemBean subitemBean = new SubitemBean();
	
	private ArrayList<SubitemBean> subitembeanlist = new ArrayList<SubitemBean>();
	
	public String itemName= "";
	
	private ArrayList<String> itemNameList = new ArrayList<String>();
	
	Session session = null;
	
	private Connection connection = null;
	
	private String userName = "";
		
	private AImage subItemImage;
	
	private String subItemImagefilePath;
	
	private boolean subItemImageFileuploaded = false;
	
	PropertyFile propertyfile = new PropertyFile();
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		
		connection.setAutoCommit(true);
		
		onLoaditemNameList();
		loadAllItemsInGrid();
	}
	
	/**
	 * 
	 * To load all item list 
	 */
	public void onLoaditemNameList(){
		if(itemNameList!=null){
			itemNameList.clear();
		}	
		try {
				sql1:{
						PreparedStatement preparedStatement=null;
						try {
								preparedStatement=connection.prepareStatement(propertyfile.getPropValues("onLoadItemListSql"));
								
								ResultSet rs=preparedStatement.executeQuery();
								
								while(rs.next()){
									
									itemNameList.add(rs.getString("item_name"));	
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
	 * 
	 * Load all sub item w.r.t item list in the grid view "vw_subitem_data"
	 */
	public void loadAllItemsInGrid(){
		
		if(subitembeanlist!=null){
			subitembeanlist.clear();
		}	
		try {
				sql1:{
						PreparedStatement preparedStatement=null;
						try {
								preparedStatement=connection.prepareStatement(propertyfile.getPropValues("onLoadAllItemsListSql"));
								
								ResultSet rs=preparedStatement.executeQuery();
								String itemName = null;
								while(rs.next()){
									SubitemBean subitembean = new SubitemBean();
									subitembean.itemName = rs.getString("item_name");
									String tepmItemName = subitembean.itemName;
									if(subitembean.itemName.equals(itemName)){
										subitembean.itemName="";
										subitembean.itemVisibility = false;
									}
									subitembean.subItemName = rs.getString("sub_item_name");
									subitembean.subItemImagePath = rs.getString("sub_item_image");
									subitembean.subItemImage = new AImage(subitembean.subItemImagePath);
									subitembean.subItemdescription = rs.getString("sub_item_description");
									itemName=tepmItemName;
									subitembeanlist.add(subitembean);
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
	 * 
	 * selection of item from combobox 	
	 */
	@Command
	@NotifyChange("*")
	public void onSelectItemName(){
		try {
			sql1:{
					PreparedStatement preparedStatement=null;
					try {
							preparedStatement=connection.prepareStatement(propertyfile.getPropValues("onSelectItemNameSql"));
							preparedStatement.setString(1, itemName);
							ResultSet resultSet=preparedStatement.executeQuery();
							
							while(resultSet.next()){
								subitemBean.itemId = resultSet.getInt("item_id");								
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
	 * 
	 * Uploading subitem image method
	 */
	@Command
	@NotifyChange("*")
	public void onUploadSubitemImage(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) throws IOException {

		UploadEvent upEvent = null;
		
		Object objUploadEvent = ctx.getTriggerEvent();
		
		if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
			upEvent = (UploadEvent) objUploadEvent;
		}

		if (upEvent != null) {

			Media media = upEvent.getMedia();
			
			String yearPath = "Images_SubItem_images" + "\\"+new SimpleDateFormat("dd-MMM-yyyy").format(new java.util.Date())+ "\\";

			subItemImagefilePath = "C:\\FooApp_Project_Images\\" + yearPath;
			File baseDir = new File(subItemImagefilePath);
			if (!baseDir.exists()) {
				baseDir.mkdirs();
			}

			Files.copy(new File(subItemImagefilePath + media.getName()),media.getStreamData());

			subItemImageFileuploaded = true;
			subItemImagefilePath = subItemImagefilePath + media.getName();
		
			subitemBean.subItemImagePath = subItemImagefilePath;

			subItemImage = (AImage) media;
		}
	}
	
	/**
	 * 
	 * on onclick save method
	 * 
	 */
	@Command
	@NotifyChange("*")
	public void onClickSaveSubItem(){
		if(validateFields()){
			saveSubItemData();
		}
	}
	
	/**
	 * 
	 * Saving sub item data to the database
	 * 
	 */
	public void saveSubItemData(){
		String message = "";
		Boolean inserted=false;
		try {
			SQL:{
				 PreparedStatement preparedStatement= null;
				 ResultSet resultSet = null;
				 try {
					preparedStatement = connection.prepareStatement(propertyfile.getPropValues("saveSubItemSql"));
					preparedStatement.setInt(1, subitemBean.itemId);
					preparedStatement.setString(2, subitemBean.subItemName);
					preparedStatement.setString(3, subitemBean.subItemImagePath);
					preparedStatement.setString(4, subitemBean.subItemdescription);
					preparedStatement.setString(5, userName);
					preparedStatement.setString(6, userName);
					
					resultSet =  preparedStatement.executeQuery();
					if(resultSet.next()){
						message=resultSet.getString(1);
						inserted=true;
						System.out.println("Sub item Data::"+message);
					}
					if(inserted){
						Messagebox.show("Sub item data saved successfully!!");
						clearData();
						loadAllItemsInGrid();
					}
				}catch (Exception e) {
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
	
	/**
	 * 
	 * clearing data
	 * 
	 */
	public void clearData(){
		subitemBean.subItemName="";
		subitemBean.subItemdescription = "";
		subItemImage = null;
		itemName ="";
	}
	
	/**
	 * 
	 * Method to validate the field
	 */
	public Boolean validateFields(){
		if(subitemBean.itemId!=null){
			if(subitemBean.subItemName!=null){
				if(subitemBean.subItemdescription!=null){
					return true;
				}else{
					Messagebox.show("Sub item description required");
					return false;
				}				
			}else{
				Messagebox.show("Sub item name required");
				return false;
			}
		}else{
			Messagebox.show("Item name required");
			return false;
		}
	}
	/*@Command
	@NotifyChange("*")
	public void onClickAddRow(){
		
		for(int i=0;i<1;i++){
			SubitemBean subitemBean =  new SubitemBean();
			subitemBeanList.add(subitemBean);
		}
	}*/
	public SubitemBean getSubitemBean() {
		return subitemBean;
	}

	public void setSubitemBean(SubitemBean subitemBean) {
		this.subitemBean = subitemBean;
	}
	
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
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

	public boolean isSubItemImageFileuploaded() {
		return subItemImageFileuploaded;
	}

	public void setSubItemImageFileuploaded(boolean subItemImageFileuploaded) {
		this.subItemImageFileuploaded = subItemImageFileuploaded;
	}

	public AImage getSubItemImage() {
		return subItemImage;
	}

	public void setSubItemImage(AImage subItemImage) {
		this.subItemImage = subItemImage;
	}

	public String getSubItemImagefilePath() {
		return subItemImagefilePath;
	}

	public void setSubItemImagefilePath(String subItemImagefilePath) {
		this.subItemImagefilePath = subItemImagefilePath;
	}

	public ArrayList<String> getItemNameList() {
		return itemNameList;
	}

	public void setItemNameList(ArrayList<String> itemNameList) {
		this.itemNameList = itemNameList;
	}

	public ArrayList<SubitemBean> getSubitembeanlist() {
		return subitembeanlist;
	}

	public void setSubitembeanlist(ArrayList<SubitemBean> subitembeanlist) {
		this.subitembeanlist = subitembeanlist;
	}
	
}
