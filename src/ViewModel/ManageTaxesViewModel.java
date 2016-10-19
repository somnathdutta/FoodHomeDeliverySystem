package ViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.zkoss.bind.BindUtils;
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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;

import com.sun.crypto.provider.RSACipher;

import Bean.TaxesBean;

public class ManageTaxesViewModel {

	private TaxesBean taxesBean =  new TaxesBean();
	
	private ArrayList<TaxesBean> taxesBeanList = new ArrayList<TaxesBean>();
	
	Session session = null;
	
	private Connection connection = null;
	
	private String userName;
	
	private Integer roleId;
	
	private String roleName;
	
	public Boolean saveButtonVisibility = true;
	
	public Boolean updateButtonVisibility = false;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws SQLException{

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		
		roleId = (Integer) session.getAttribute("userRoleId");
		
		connection.setAutoCommit(true);
		
		onLoad();
		
		
	}

	@GlobalCommand
	@NotifyChange("*")
	public void globalReload(){
		onLoad();
		refresh();
	}
	
	
	public void onLoad(){
		System.out.println("onload called...");
		generateOrderNo();
		if(taxesBeanList.size()>0){
			taxesBeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql="SELECT tax_id, service_tax, vat_tax FROM fapp_tax WHERE is_delete='N'";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							TaxesBean taxesBean = new TaxesBean();
							taxesBean.taxId = resultSet.getInt("tax_id");
							taxesBean.serviceTax = resultSet.getDouble("service_tax");
							taxesBean.vatTax = resultSet.getDouble("vat_tax");
							taxesBeanList.add(taxesBean);
						}
					} catch (Exception e) {
						Messagebox.show("ERROR DUE TO :"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
	public void onClickSave(){
		if(validateFields()){
			saveTax();
		}
	}
	
	public void saveTax(){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "INSERT INTO fapp_tax("
				                +" service_tax, vat_tax)"
				                +" VALUES (?, ?)";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setDouble(1, taxesBean.serviceTax);
						preparedStatement.setDouble(2, taxesBean.vatTax);
						int count = preparedStatement.executeUpdate();
						if(count>0){
							System.out.println("Tax saved sucessfully!");
							refresh();
							onLoad();
							Messagebox.show("Tax saved sucessfully!");
							
						}
								
					} catch (Exception e) {
						Messagebox.show("ERROR DUE TO:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
	public void onClickUpdate(){
		if(validateFields()){
			updateTax();
		}
	}
	
	public void updateTax(){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "UPDATE fapp_tax"
				                +" SET service_tax=?, vat_tax=?"
				                +" WHERE tax_id = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setDouble(1, taxesBean.serviceTax);
						preparedStatement.setDouble(2, taxesBean.vatTax);
						preparedStatement.setInt(3, taxesBean.taxId);
						int count = preparedStatement.executeUpdate();
						if(count>0){
							System.out.println("Tax updated sucessfully!");
							updateButtonVisibility= false;
							saveButtonVisibility = true;
							refresh();
							Messagebox.show("Tax updated sucessfully!");
						}
								
					} catch (Exception e) {
						Messagebox.show("ERROR DUE TO:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
	public void onClickEdit(@BindingParam("bean")TaxesBean taxesbean){
		taxesBean.serviceTax = taxesbean.serviceTax;
		taxesBean.vatTax = taxesbean.vatTax;
		taxesBean.taxId = taxesbean.taxId;
		updateButtonVisibility = true;
		saveButtonVisibility = false;
	}
	
	@SuppressWarnings("unchecked")
	@Command
	@NotifyChange("*")
	public void onClickDelete(@BindingParam("bean") final TaxesBean taxesbean){
		Messagebox.show("Are you sure to delete?", "Confirm Dialog", 
				Messagebox.OK |  Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
		    public void onEvent(Event evt) throws InterruptedException {
		        if (evt.getName().equals("onOK")) {
		          deleteTax(taxesbean);
		          /*refresh();
				   onLoad();*/
		          BindUtils.postGlobalCommand(null, null, "globalReload", null);
				   Messagebox.show("Tax data deleted successfully!");
		        } else if (evt.getName().equals("onIgnore")) {
		            Messagebox.show("Ignore Save", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
		        } else {
		           
		        }
		    }
		});
	}
	
	public void deleteTax(TaxesBean taxesbean){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "UPDATE fapp_tax"
				                +" SET is_delete='Y'"
				                +" WHERE tax_id = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, taxesbean.taxId);
						int count = preparedStatement.executeUpdate();
						if(count>0){
							System.out.println("Tax deleted sucessfully!");
						}
								
					} catch (Exception e) {
						Messagebox.show("ERROR DUE TO:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
	
	public void refresh(){
		taxesBean.serviceTax = null;
		taxesBean.vatTax = null;
		taxesBean.taxId = null;
	}
	
	public void generateOrderNo(){
		String orderNumber = "";
		Integer serialorderid=0;
		//Connection connection = null; 
		try {
			// connection = DBConnection.createConnection();
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select max(tax_id) from fapp_tax";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							serialorderid = resultSet.getInt(1);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("serial---->"+serialorderid);
		orderNumber =  "ORDER/"+String.format("%06d", serialorderid+1);
		System.out.println("Order number->"+orderNumber);
		//System.out.println(orderNumber.substring(6)) ;
		//return orderNumber;
	}
	
	public Boolean validateFields(){
		if(taxesBean.serviceTax!=null){
			if(taxesBean.vatTax!=null){
				return true;
			}else{
				Messagebox.show("VAT Tax required!");
				return false;
			}
		}else{
			Messagebox.show("Service Tax required!");
			return false;
		}
	}
	
	public TaxesBean getTaxesBean() {
		return taxesBean;
	}

	public void setTaxesBean(TaxesBean taxesBean) {
		this.taxesBean = taxesBean;
	}

	public ArrayList<TaxesBean> getTaxesBeanList() {
		return taxesBeanList;
	}

	public void setTaxesBeanList(ArrayList<TaxesBean> taxesBeanList) {
		this.taxesBeanList = taxesBeanList;
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

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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
	
	
}
