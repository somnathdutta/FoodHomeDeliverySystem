package ViewModel;

import java.sql.Connection;
import java.util.ArrayList;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;
import service.KitchenStockUpdateService;
import dao.KitchenStockUpdateDao;
import Bean.KitchenStockUpdateViewBean;

public class KitchenStockUpdateViewModel {
	private Connection connection = null;
	private Session sessions = null;
	private String userName = "";
	
	private ArrayList<KitchenStockUpdateViewBean> kitchenStockList = null;
	private ArrayList<KitchenStockUpdateViewBean> kitchenNameList = null;
	
	private KitchenStockUpdateViewBean kitchenBean = new KitchenStockUpdateViewBean();
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {
		Selectors.wireComponents(view, this, false);
		sessions= Sessions.getCurrent();
		connection=(Connection) sessions.getAttribute("sessionConnection");
		userName = (String) sessions.getAttribute("login");
		kitchenNameList = KitchenStockUpdateDao.loadKitchenName(connection);
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectKitchenName(){
		System.out.println("KITCHEN NAME :"+kitchenBean.getKitchenName());
		kitchenBean.setKitchenStockDivVisibility(true);
		kitchenStockList = KitchenStockUpdateService.fetchKitchenStockDetails(connection, kitchenBean.getKitchenName());
	}
	
	@Command
	@NotifyChange("*")
	public void onClickUpdateButton(@BindingParam("bean") KitchenStockUpdateViewBean bean){
		System.out.println("STOCK UPDATETION ID IS :"+bean.getStockUpdationId());
		boolean flag = false;
		if(bean.getLunchStock()!=null){
			if(bean.getDinnerStock()!=null){
				flag = KitchenStockUpdateService.updateKitchenStockDetails(connection, bean.getStockUpdationId(), bean.getLunchStock(), bean.getDinnerStock());				
				if(flag){
					System.out.println("updated data.");
				}
			}else{
				Messagebox.show("Dinner Stock Can't Be Blank!", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			}
		}else{
			Messagebox.show("Lunch Stock Can't Be Blank!", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
		}
	}
	
	/**********************************************************************GETTER AND SETTER METHOD ******************************************************/
	
	public Connection getConnection() {
		return connection;
	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	public Session getSessions() {
		return sessions;
	}
	public void setSessions(Session sessions) {
		this.sessions = sessions;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public ArrayList<KitchenStockUpdateViewBean> getKitchenStockList() {
		return kitchenStockList;
	}
	public void setKitchenStockList(ArrayList<KitchenStockUpdateViewBean> kitchenStockList) {
		this.kitchenStockList = kitchenStockList;
	}
	public ArrayList<KitchenStockUpdateViewBean> getKitchenNameList() {
		return kitchenNameList;
	}
	public void setKitchenNameList(
			ArrayList<KitchenStockUpdateViewBean> kitchenNameList) {
		this.kitchenNameList = kitchenNameList;
	}
	public KitchenStockUpdateViewBean getKitchenBean() {
		return kitchenBean;
	}
	public void setKitchenBean(KitchenStockUpdateViewBean kitchenBean) {
		this.kitchenBean = kitchenBean;
	}
}
