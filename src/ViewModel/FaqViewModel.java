package ViewModel;

import java.sql.Connection;
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
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import service.FaqService;
import dao.FaqDAO;
import Bean.FaqBean;

public class FaqViewModel {

	private FaqBean faqBean = new FaqBean();
	
	private ArrayList<FaqBean> faqBeanList = new ArrayList<FaqBean>();
	
	Session session = null;
	
	private Connection connection = null;
	
	private String userName;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws SQLException{

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		
		loadAllFaqs();
	}
	
	/**
	 * Faq saving 
	 * @param faqBean
	 */
	@Command
	@NotifyChange("*")
	public void onClickSaveFaq(){
		if(FaqService.isValidFields(faqBean))
		saveFaq();
	}
	
	/**
	 * Updating faq
	 * @param faqBean
	 */
	@Command
	@NotifyChange("*")
	public void onClickUpdateFaq(@BindingParam("bean")FaqBean faqBean){
		updateFaq(faqBean);
	}
	
	
	/**
	 * Deleting faq
	 * @param faqBean
	 */
	@SuppressWarnings("unchecked")
	@Command
	@NotifyChange("*")
	public void onClickDeleteFaq(@BindingParam("bean")FaqBean faqBean){
		Messagebox.show("Are you sure to delete this FAQ?", "Confirm Dialog", 
				Messagebox.OK |  Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
		    public void onEvent(Event evt) throws InterruptedException {
		        if (evt.getName().equals("onOK")) {
		         deleteFaq(faqBean);
		          BindUtils.postGlobalCommand(null, null, "globalReload", null);
				   //Messagebox.show("Faq deleted successfully!","Delete Information",Messagebox.OK,Messagebox.INFORMATION);
		        } else if (evt.getName().equals("onIgnore")) {
		            Messagebox.show("Ignore Delete", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
		        } else {
		           
		        }
		    }
		});
	}
	
	/**
	 * Global function for bindutils
	 */
	@GlobalCommand
	@NotifyChange("*")
	public void globalReload(){
		loadAllFaqs();
	}
	
	/**
	 * Load all saved faqs
	 * @param connection
	 */
	public void loadAllFaqs(){
		faqBeanList = FaqDAO.fetchAllFaqs(connection);
	}
	
	@Command
	@NotifyChange("*")
	public void onClickShowFaq(){
		loadAllFaqs();
	}
	
	/**
	 * Save new faq
	 * @param connection
	 * @param faqBean
	 */
	public void saveFaq(){
		faqBean.setUserName(userName);
		FaqDAO.saveNewFaq(connection, faqBean);
		FaqService.clear(faqBean);
	}
	
	
	/**
	 * Update faq
	 * @param connection
	 * @param faqBean
	 */
	public void updateFaq(FaqBean faqBean){
		faqBean.setUserName(userName);
		FaqDAO.updateNewFaq(connection, faqBean);
	}
		
	
	/**
	 * delete faq
	 * @param connection
	 * @param faqBean
	 */
	public void deleteFaq(FaqBean faqBean){
		faqBean.setUserName(userName);
		FaqDAO.deleteFaq(connection, faqBean);
	}
	
	/////////////////////////////////////////// GETTER SETTER ///////////////////////////////////////////////////////
	public FaqBean getFaqBean() {
		return faqBean;
	}

	public void setFaqBean(FaqBean faqBean) {
		this.faqBean = faqBean;
	}

	public ArrayList<FaqBean> getFaqBeanList() {
		return faqBeanList;
	}

	public void setFaqBeanList(ArrayList<FaqBean> faqBeanList) {
		this.faqBeanList = faqBeanList;
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
	
	
}
