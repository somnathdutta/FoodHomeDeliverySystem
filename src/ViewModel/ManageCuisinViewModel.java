package ViewModel;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import Bean.ManageAlacarteItemBean;
import Bean.ManageCuisinBean;

public class ManageCuisinViewModel {

	private ManageCuisinBean manageCuisinBean = new ManageCuisinBean();
	
	private ArrayList<ManageCuisinBean> manageCuisinBeanList  = new ArrayList<ManageCuisinBean>();
	
	Session session = null;
	
	private Connection connection = null;
	
	private String userName = "";
	
	private boolean cuisineFileuploaded = false;
	
	private String cuisineImagefilePath;
	
	private AImage cuisineImage;
	
	private Boolean saveButtonVisibility = true;
	
	private Boolean updateButtonVisibility = false;
	
	PropertyFile propertyfile = new PropertyFile();	
	
	@AfterCompose
	public void initSetUp(@ContextParam(ContextType.VIEW) Component view)throws Exception{
		
		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		connection.setAutoCommit(true);
		
		loadAllCuisinList();
		
	}
	
	@GlobalCommand
	@NotifyChange("*")
	public void globalReload(){
		loadAllCuisinList();
		refresh();
	}
	
	/**
	 * This method is useful to load saved Cuisin item to the front page
	 */
	public void loadAllCuisinList(){
		if(manageCuisinBeanList.size()>0){
			manageCuisinBeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement =  null;
					ResultSet resultSet = null;
					try {
						preparedStatement = connection.prepareStatement(propertyfile.getPropValues("onLoadCuisinListSql"));
						resultSet = preparedStatement.executeQuery();
						
						while(resultSet.next()){
							
							ManageCuisinBean cuisinBean = new ManageCuisinBean();
							cuisinBean.cuisinId = resultSet.getInt("cuisin_id");
							cuisinBean.cuisinePicturePath = resultSet.getString("cuisine_image");
							//cuisinBean.cuisineImage = new AImage(cuisinBean.cuisinePicturePath);
							cuisinBean.cuisinName = resultSet.getString("cuisin_name");
							if( resultSet.getString("is_active").equalsIgnoreCase("Y")){
								cuisinBean.status = "Active";
							}else{
								cuisinBean.status = "Deactive";
							}
							manageCuisinBeanList.add(cuisinBean);
							
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
		if (validateFields()) {
			saveCuisinData();
		}
	}
	
	/**
	 * This method is useful to save the cuisin to database
	 */
	public void saveCuisinData(){
		String message ="";
		Boolean inserted=false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
				try {
						preparedStatement = connection.prepareStatement(propertyfile.getPropValues("saveCuisinSql"));
						
						preparedStatement.setString(1, manageCuisinBean.cuisinName);
						if(manageCuisinBean.cuisinePicturePath!=null){
							preparedStatement.setString(2, manageCuisinBean.cuisinePicturePath);
						}else{
							preparedStatement.setNull(2, Types.INTEGER);
						}
						
						if(manageCuisinBean.status.equalsIgnoreCase("Active")){
							preparedStatement.setString(3, "Y");
						}else{
							preparedStatement.setString(3, "N");
						}
						
						preparedStatement.setString(4, userName);
						preparedStatement.setString(5, userName);
						
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							message=resultSet.getString(1);
							inserted = true;
							System.out.println("Cuisin save message =" + message);
						}
						if (inserted) {
							Messagebox.show("Cuisin Saved Successfully...");
							loadAllCuisinList();
							refresh();
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
	
	@Command
	@NotifyChange("*")
	public void onClickEdit(@BindingParam("bean") ManageCuisinBean managecuisinbean){
		manageCuisinBean.cuisinId = managecuisinbean.cuisinId;
		manageCuisinBean.cuisinName = managecuisinbean.cuisinName;
		manageCuisinBean.cuisinePicturePath = managecuisinbean.cuisinePicturePath;
		try {
			cuisineImage = new AImage(manageCuisinBean.cuisinePicturePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		manageCuisinBean.status = managecuisinbean.status;
		updateButtonVisibility = true;
		saveButtonVisibility = false;
		/*try {
			SQL:{
				PreparedStatement preparedStatement = null;
				ResultSet resultSet =null;
				try {
					preparedStatement = connection.prepareStatement(propertyfile.getPropValues("editCuisinSql"));
					preparedStatement.setInt(1,managecuisinbean.cuisinId);
					manageCuisinBean.cuisinId = managecuisinbean.cuisinId;
					resultSet = preparedStatement.executeQuery();
					while(resultSet.next()){
						manageCuisinBean.cuisinId = resultSet.getInt("cuisin_id");
						manageCuisinBean.cuisinName = resultSet.getString("cuisin_name");
						
						if(resultSet.getString("is_active").equals("Y")){
							manageCuisinBean.status = "Active";
						}
						if(resultSet.getString("is_active").equals("N")){
							manageCuisinBean.status = "Deactive";
						}
						updateButtonVisibility = true;
						saveButtonVisibility = false;
					}	
				} catch (Exception e) {
					Messagebox.show("ERROR DUE TO::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					e.printStackTrace();
				}	
			}
		} catch (Exception e) {
			// TODO: handle exception
		}*/
	}
	
	@Command
	@NotifyChange("*")
	public void onClickUpdate(){
		String message="";
		Boolean updated= false;
		if(validateFields()){
			try {
				SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					try {
						preparedStatement = connection.prepareStatement(propertyfile.getPropValues("updateCuisinSql"));
						
						preparedStatement.setString(1, manageCuisinBean.cuisinName);
						if(manageCuisinBean.cuisinePicturePath!=null){
							preparedStatement.setString(2, manageCuisinBean.cuisinePicturePath);
						}else{
							preparedStatement.setNull(2, Types.INTEGER);
						}
						if(manageCuisinBean.status.equalsIgnoreCase("Active")){
							preparedStatement.setString(3, "Y");
						}
						else{
							preparedStatement.setString(3, "N");
						}
						preparedStatement.setString(4, userName);
						preparedStatement.setString(5, userName);
						preparedStatement.setInt(6,manageCuisinBean.cuisinId);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							message = resultSet.getString(1);
							updated = true;
							System.out.println(message);
						}
						if(updated){
							refresh();
							Messagebox.show("Cuisin Updated Successfully...");
							
							loadAllCuisinList();
							saveButtonVisibility=true;
							updateButtonVisibility=false;
						}
						
					} catch (Exception e) {
						Messagebox.show("ERROR DUE TO::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
	}
	
	@Command
	@NotifyChange("*")
	public void onClickDelete(@BindingParam("bean") final ManageCuisinBean bean){
		
		Messagebox.show("Are you sure to delete?", "Confirm Dialog", 
				Messagebox.OK |  Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
		    public void onEvent(Event evt) throws InterruptedException {
		        if (evt.getName().equals("onOK")) {
		         isDelete(bean);
		          BindUtils.postGlobalCommand(null, null, "globalReload", null);
		        } else if (evt.getName().equals("onIgnore")) {
		            Messagebox.show("Ignore Save", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
		        } else {
		           
		        }
		    }
		});
	}
	
	
	public void isDelete(ManageCuisinBean bean){
		boolean isDelete = false;
		try {
			SQL:{
					PreparedStatement preparedStatement =null;
					String sql = "UPDATE fapp_cuisins set is_delete = 'Y',is_active='N' where cuisin_id = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, bean.cuisinId);
						int count = preparedStatement.executeUpdate();
						if(count>0){
							isDelete = true;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(isDelete){
			Messagebox.show("Cuisine Deleted successfully!","Information",Messagebox.OK,Messagebox.INFORMATION);
		}else{
			Messagebox.show("Cuisine deletion failed !","ERROR",Messagebox.OK,Messagebox.ERROR);
		}
	}
	/**
	 * 
	 *Upload cuisine image
	 */
	@Command
	@NotifyChange("*")
	public void onUploadCuisineImage(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) throws IOException {

		UploadEvent upEvent = null;
		
		Object objUploadEvent = ctx.getTriggerEvent();
		
		if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
			upEvent = (UploadEvent) objUploadEvent;
		}

		if (upEvent != null) {

			Media media = upEvent.getMedia();
			
			String yearPath = "/"+"Images_Cuisineimages" + "/" +new SimpleDateFormat("dd-MMM-yyyy").format(new java.util.Date())+ "/";
						
			//String yearPath = "Images_Categoryimages" + "\\"+new SimpleDateFormat("dd-MMM-yyyy").format(new java.util.Date())+ "\\";
			
			String contextPath = Sessions.getCurrent().getWebApp().getServletContext().getContextPath();
			
			String imagespath = System.getProperty("catalina.home")+"/"+"webapps"+"/"+contextPath.replace("/", "")+"/"+"view"+"/"+"images";
	 		
			cuisineImagefilePath = imagespath;
			
			//categoryImagefilePath = "C:\\FooApp_Project_Images\\" + yearPath;
			cuisineImagefilePath = cuisineImagefilePath + yearPath;
		        
			File baseDir = new File(cuisineImagefilePath);
			if (!baseDir.exists()) {
				baseDir.mkdirs();
			}

			Files.copy(new File(cuisineImagefilePath + media.getName()),media.getStreamData());

			Messagebox.show("Image Sucessfully uploaded!");
			
			cuisineFileuploaded = true;
			cuisineImagefilePath = cuisineImagefilePath + media.getName();
		
			manageCuisinBean.cuisinePicturePath = cuisineImagefilePath;

			cuisineImage = (AImage) media;
		}
	}
	
	
	public Boolean validateFields(){
		if(manageCuisinBean.cuisinName!=null){
			if(manageCuisinBean.status!=null){
				return true;
			}else{
				Messagebox.show("Cuisin will active or not?");
				return false;
			}
		}else{
			Messagebox.show("Cuisin name required!");
			return false;
		}
	}
	
	public void refresh(){
		manageCuisinBean.cuisinName = null;
		manageCuisinBean.status = null;
		manageCuisinBean.cuisinId = null;
		manageCuisinBean.cuisineImage = null;
		cuisineImage= null;
		manageCuisinBean.cuisinePicturePath= null;
	}

	public ManageCuisinBean getManageCuisinBean() {
		return manageCuisinBean;
	}

	public void setManageCuisinBean(ManageCuisinBean manageCuisinBean) {
		this.manageCuisinBean = manageCuisinBean;
	}

	public ArrayList<ManageCuisinBean> getManageCuisinBeanList() {
		return manageCuisinBeanList;
	}

	public void setManageCuisinBeanList(
			ArrayList<ManageCuisinBean> manageCuisinBeanList) {
		this.manageCuisinBeanList = manageCuisinBeanList;
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

	public PropertyFile getPropertyfile() {
		return propertyfile;
	}

	public void setPropertyfile(PropertyFile propertyfile) {
		this.propertyfile = propertyfile;
	}


	public boolean isCuisineFileuploaded() {
		return cuisineFileuploaded;
	}


	public void setCuisineFileuploaded(boolean cuisineFileuploaded) {
		this.cuisineFileuploaded = cuisineFileuploaded;
	}


	public String getCuisineImagefilePath() {
		return cuisineImagefilePath;
	}


	public void setCuisineImagefilePath(String cuisineImagefilePath) {
		this.cuisineImagefilePath = cuisineImagefilePath;
	}


	public AImage getCuisineImage() {
		return cuisineImage;
	}


	public void setCuisineImage(AImage cuisineImage) {
		this.cuisineImage = cuisineImage;
	}
}
