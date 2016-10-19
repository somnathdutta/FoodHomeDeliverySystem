package ViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import Bean.ManageCategoryBean;
import Bean.ManageDealBean;
import Bean.ManageKitchens;

public class CategoryDeleteViewModel {

	private ManageCategoryBean manageCategoryBEAN = null;
	
	private ManageKitchens manageKitchenBEAN = new ManageKitchens();
	
	private Connection connection = null;
	
	private Session sessions = null;
	
	private String userName ;
	
	@Wire("#winCategoryDelete")
	private Window winCategoryDelete;
	
	PropertyFile propertyfile = new PropertyFile();
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("parentCategoryObject") ManageCategoryBean manageCategorybean,
			@ExecutionArgParam("parentKitchenObject") ManageKitchens manageKitchenbean)
			throws Exception {

		Selectors.wireComponents(view, this, false);
		
		sessions = Sessions.getCurrent();
		
		connection = (Connection) sessions.getAttribute("sessionConnection");
		
		connection.setAutoCommit(true);
		
		userName = (String) sessions.getAttribute("login");
		
		if(manageCategorybean != null){
			manageCategoryBEAN = manageCategorybean;
		}
				
		if(manageKitchenbean != null){
			manageKitchenBEAN =  manageKitchenbean;
		}
			
	}
	
	@Command
	@NotifyChange("*")
	public void onClickYes(){
		
		Integer noOfUpdate = 0;
		
		if(manageCategoryBEAN != null){
			try {
				SQL:{
						PreparedStatement preparedStatement = null;
						try {
							
							preparedStatement = connection.prepareStatement(propertyfile.getPropValues("deleteCategorySql"));
					
							preparedStatement.setInt(1, manageCategoryBEAN.categoryId);
							
							noOfUpdate = preparedStatement.executeUpdate();
							
						} catch (Exception e) {
							Messagebox.show("ERROR DUE TO::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
							e.printStackTrace();
						}finally{
							if(preparedStatement != null){
								preparedStatement.close();
							}
						}
				}
			
				if(noOfUpdate>0){
					
					Messagebox.show("Category data Deleted Successfully...");
					
					BindUtils.postGlobalCommand(null, null, "globalReload", null);
					winCategoryDelete.detach();
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(manageKitchenBEAN != null){
			try {
				SQL:{
						PreparedStatement preparedStatement = null;
						try {
							
							preparedStatement = connection.prepareStatement(propertyfile.getPropValues("deleteKitchenSql"));
					
							preparedStatement.setInt(1, manageKitchenBEAN.kitchenId);
							
							noOfUpdate = preparedStatement.executeUpdate();
							
						} catch (Exception e) {
							Messagebox.show("ERROR DUE TO::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
							e.printStackTrace();
						}finally{
							if(preparedStatement != null){
								preparedStatement.close();
							}
						}
				}
			
				if(noOfUpdate>0){
					
					Messagebox.show("Kitchen data Deleted Successfully...");
					
					BindUtils.postGlobalCommand(null, null, "globalReload", null);
					winCategoryDelete.detach();
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	@Command
	@NotifyChange("*")
	public void onClickNo(){
		
		BindUtils.postGlobalCommand(null, null, "globalReload", null);
		winCategoryDelete.detach();
	}
}
