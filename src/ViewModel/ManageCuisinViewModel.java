package ViewModel;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.ws.rs.Path;

import org.apache.commons.io.FilenameUtils;
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
	private Boolean updateCancelButtonVisibility = false;
	
	private Boolean saveImageVisibility = true;
	private Boolean updateImageVisibility = false;
	private Boolean otherUrlImageVisibility = false;
	
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
		manageCuisinBean.setProgressBarValue(10);
		if (validateFields()) {
			manageCuisinBean.setProgressBarVisible(true);
			manageCuisinBean.setUploadingDisable(true);
			manageCuisinBean.setProgressBarValue(20);
			saveCuisinData();
		}
	}
	
	/**
	 * This method is useful to save the cuisin to database
	 */
	

	private boolean uploadImageToFolder(String dirName,String fileName){
		try {
			 manageCuisinBean.setProgressBarValue(80);
			 if(manageCuisinBean.getImageMedia() != null){
				 Files.copy(new File(dirName+File.separator + fileName),manageCuisinBean.getImageMedia().getStreamData());
				 System.out.println("Image uploaded!...");
			 }
			
			 manageCuisinBean.setProgressBarValue(90);
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	} // end uploadImageToFolder()
	
	
	private boolean updateImageToFolder(String oldFullPathName,String dirName,String fileName){
		try {
			
			URL resourceUrl = this.getClass().getResource("/");
			String filePath = resourceUrl.getFile();
			String serverRootDir = new File(new File(filePath).getParent()).getParent();
			
			String pullPath = "view";
			String[] dir = oldFullPathName.split("/");
			for(String token: dir){
				pullPath = pullPath+File.separator+token;
			}
			File file = new File(serverRootDir+File.separator+pullPath);
			if(file.exists()){
				file.delete();
				System.out.println("Image deleted!...");
			}
			Files.copy(new File(dirName+File.separator + fileName),manageCuisinBean.getImageMedia().getStreamData());
			 System.out.println("Image updated!...");
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	} // end updateImageToFolder()
	
	
	private void deleteUoloadeImage(String oldFullPathName){
		try {
			URL resourceUrl = this.getClass().getResource("/");
			String filePath = resourceUrl.getFile();
			String serverRootDir = new File(new File(filePath).getParent()).getParent();
			
			String pullPath = "view";
			String[] dir = oldFullPathName.split("/");
			for(String token: dir){
				pullPath = pullPath+File.separator+token;
			}
			File file = new File(serverRootDir+File.separator+pullPath);
			if(file.exists()){
				file.delete();
				System.out.println("Image deleted!...");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // end deleteUoloadeImage()
	
	
	public void saveCuisinData(){
		String fileName =null;
		String uploadImageDir = "";
		String uploadImagePath = "";
		String message ="";
		Boolean inserted=false;
		try {
			MAKE_FILE_PATH:{
			if(manageCuisinBean.getImageMedia() != null){
				URL resourceUrl = this.getClass().getResource("/");
				String filePath = resourceUrl.getFile();
				String serverRootDir = new File(new File(filePath).getParent()).getParent();

				String currentDate = new SimpleDateFormat("dd-MMM-yyyy").format(new java.util.Date()).toString();
				
				String uploadPath = serverRootDir.toString()+File.separator+"uploads"+"/"+"Cuising_Image"+ File.separator+currentDate;
				manageCuisinBean.setProgressBarValue(70);
				File fileDir = new File(uploadPath);
				 if(!fileDir.exists()){
					 fileDir.mkdirs();
					 System.out.println("New Directory Created..");
				 }
				
				String cuisingName = manageCuisinBean.cuisinName;
				cuisingName = cuisingName.replaceAll("\\s", "");
				String rand = UUID.randomUUID().toString().substring(0, 30);
				String generateName = cuisingName+"_"+rand;
			    fileName = generateName.toUpperCase()+".png";
				
			    uploadImageDir = serverRootDir.toString()+File.separator+"view"+File.separator+"uploads"+"/"+"Cuising_Image"+ File.separator+currentDate;
				
			    uploadImagePath ="uploads"+"/"+"Cuising_Image"+"/"+currentDate+"/"+fileName;
				manageCuisinBean.setProgressBarValue(30);
			   }

		    }
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
				try {
						preparedStatement = connection.prepareStatement(propertyfile.getPropValues("saveCuisinSql"));
						
						preparedStatement.setString(1, manageCuisinBean.cuisinName);
						if(manageCuisinBean.getImageMedia() != null && manageCuisinBean.cuisinePicturePath != null){
							 preparedStatement.setString(2, uploadImagePath);
						}else {
							if(manageCuisinBean.getImageMedia() != null){
								preparedStatement.setString(2, uploadImagePath);
							}else if(manageCuisinBean.cuisinePicturePath != null) {
								preparedStatement.setString(2, manageCuisinBean.cuisinePicturePath);
							}else{
								preparedStatement.setNull(2, Types.VARCHAR);
							}
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
							manageCuisinBean.setProgressBarValue(50);
							if(uploadImageToFolder(uploadImageDir,fileName)){
								manageCuisinBean.setProgressBarValue(100);
								manageCuisinBean.setProgressBarVisible(false);
								manageCuisinBean.setUploadingDisable(false);
								manageCuisinBean.setImageMedia(null);
								Messagebox.show("Cuisin Saved Successfully...");
								loadAllCuisinList();
								refresh();
							}
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
		
		saveImageVisibility = false;
		updateImageVisibility = true;
		
		updateButtonVisibility = true;
		updateCancelButtonVisibility = true;
		saveButtonVisibility = false;
		
		manageCuisinBean.cuisinId = managecuisinbean.cuisinId;
		manageCuisinBean.cuisinName = managecuisinbean.cuisinName;
		manageCuisinBean.cuisineUpdatePicturePath = managecuisinbean.cuisinePicturePath;
		manageCuisinBean.oldUploadedPath = managecuisinbean.cuisinePicturePath;
		if(!(managecuisinbean.cuisinePicturePath != null && managecuisinbean.cuisinePicturePath.startsWith("uploads/"))){
			manageCuisinBean.cuisinePicturePath = managecuisinbean.cuisinePicturePath;
		}else {
			manageCuisinBean.cuisinePicturePath = null;
		}
		//manageCuisinBean.cuisinePicturePath = managecuisinbean.cuisinePicturePath;
/*		try {
			cuisineImage = new AImage(manageCuisinBean.cuisinePicturePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		manageCuisinBean.status = managecuisinbean.status;
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
	public void onClickUpdateCancel(){
		
		updateButtonVisibility = false;
		updateCancelButtonVisibility = false;
		saveButtonVisibility = true;
		
		saveImageVisibility = true;
		updateImageVisibility = false;
		
		manageCuisinBean.cuisinName = null;
		manageCuisinBean.cuisinePicturePath = null;
		manageCuisinBean.status = null;
		manageCuisinBean.setImageMedia(null);
		cuisineImage = null;
		
	} // end onClickUpdateCancel()
	
	
	
	@Command
	@NotifyChange("*")
	public void onClickUpdate(){
		String fileName = null;
		String uploadImageDir = null;
		String uploadImagePath = null;
		String message="";
		Boolean updated= false;
		if(validateFields()){
			try {
				UPDATE_IMG:{
					URL resourceUrl = this.getClass().getResource("/");
					String filePath = resourceUrl.getFile();
					String serverRootDir = new File(new File(filePath).getParent()).getParent();

					String currentDate = new SimpleDateFormat("dd-MMM-yyyy").format(new java.util.Date()).toString();
					
					String uploadPath = serverRootDir.toString()+File.separator+"view"+File.separator+"uploads"+File.separator+"Cuising_Image"+ File.separator+currentDate;
					File fileDir = new File(uploadPath);
					 if(!fileDir.exists()){
						 fileDir.mkdirs();
						 System.out.println("New Directory Created..");
					 }
					
					String cuisingName = manageCuisinBean.cuisinName;
					cuisingName = cuisingName.replaceAll("\\s", "");
					String rand = UUID.randomUUID().toString().substring(0, 30);
					String generateName = cuisingName+"_"+rand;
				    fileName = generateName.toUpperCase()+".png";
					
				    uploadImageDir = fileDir.toString();
					
				    uploadImagePath ="uploads"+"/"+"Cuising_Image"+"/"+currentDate+"/"+fileName;

			    }
				SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					try {
						preparedStatement = connection.prepareStatement(propertyfile.getPropValues("updateCuisinSql"));
						
						preparedStatement.setString(1, manageCuisinBean.cuisinName);
						if(manageCuisinBean.cuisinePicturePath!=null){
							if(manageCuisinBean.oldUploadedPath.startsWith("uploads/")){
								deleteUoloadeImage(manageCuisinBean.oldUploadedPath);
							}
							preparedStatement.setString(2, manageCuisinBean.cuisinePicturePath);
						}else{
							if(manageCuisinBean.getImageMedia() != null){
								if(manageCuisinBean.cuisineUpdatePicturePath != null){
									if(updateImageToFolder(manageCuisinBean.cuisineUpdatePicturePath,uploadImageDir,fileName)){
										preparedStatement.setString(2, uploadImagePath);
									}
								}else {
									 if(uploadImageToFolder(uploadImageDir,fileName)){
										 preparedStatement.setString(2, uploadImagePath);
					                  }
								}
							}else {
								if(manageCuisinBean.cuisineUpdatePicturePath != null && manageCuisinBean.cuisineUpdatePicturePath.startsWith("uploads/")){
									preparedStatement.setString(2, manageCuisinBean.cuisineUpdatePicturePath);
								}else {
									preparedStatement.setNull(2, Types.VARCHAR);
								}
							}
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
							updateCancelButtonVisibility = false;
							
							saveImageVisibility = true;
							updateImageVisibility = false;
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
	
	@SuppressWarnings("unchecked")
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
	
	
	
	@Command
	@NotifyChange("*")
	public void onOkOnlineImage(){
		
		if(manageCuisinBean.cuisinePicturePath == null){
			Messagebox.show("PLEASE GIVE FIRST ONE LINE URL!!","ALERT:",Messagebox.OK,Messagebox.QUESTION);
		}else {
			saveImageVisibility = false;
			updateImageVisibility = true;
			
			manageCuisinBean.setImageMedia(null);
			manageCuisinBean.cuisineUpdatePicturePath = manageCuisinBean.cuisinePicturePath;
		}
		
	} // end onOkOnlineImage()
	
	
	
	/**
	 * 
	 *Upload cuisine image
	 */
	@Command
	@NotifyChange("*")
	public void onUploadCuisineImage(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) throws IOException {

		saveImageVisibility = true;
		updateImageVisibility = false;

		manageCuisinBean.cuisinePicturePath = null;
		
		manageCuisinBean.setProgressBarVisible(false);
		
		UploadEvent upEvent = null;
		
		Object objUploadEvent = ctx.getTriggerEvent();
		
		if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
			upEvent = (UploadEvent) objUploadEvent;
		}

		if (upEvent != null) {

			Media media = upEvent.getMedia();
			manageCuisinBean.setImageMedia((AImage) media);
			
			
			cuisineImage = (AImage) media;
			
/*			String yearPath = "/"+"Images_Cuisineimages" + "/" +new SimpleDateFormat("dd-MMM-yyyy").format(new java.util.Date())+ "/";
						
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

			
			cuisineFileuploaded = true;
			cuisineImagefilePath = cuisineImagefilePath + media.getName();
		
			manageCuisinBean.cuisinePicturePath = cuisineImagefilePath;

			cuisineImage = (AImage) media;*/
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

	public Boolean getUpdateCancelButtonVisibility() {
		return updateCancelButtonVisibility;
	}

	public void setUpdateCancelButtonVisibility(Boolean updateCancelButtonVisibility) {
		this.updateCancelButtonVisibility = updateCancelButtonVisibility;
	}

	public Boolean getSaveImageVisibility() {
		return saveImageVisibility;
	}

	public void setSaveImageVisibility(Boolean saveImageVisibility) {
		this.saveImageVisibility = saveImageVisibility;
	}

	public Boolean getUpdateImageVisibility() {
		return updateImageVisibility;
	}

	public void setUpdateImageVisibility(Boolean updateImageVisibility) {
		this.updateImageVisibility = updateImageVisibility;
	}

	public Boolean getOtherUrlImageVisibility() {
		return otherUrlImageVisibility;
	}

	public void setOtherUrlImageVisibility(Boolean otherUrlImageVisibility) {
		this.otherUrlImageVisibility = otherUrlImageVisibility;
	}
}
