package ViewModel;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.AMedia;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import com.sun.xml.internal.ws.developer.MemberSubmissionAddressing.Validation;

import service.LogisticsPaymentReportService;
import dao.LogisticsPaymentReportDAO;
import Bean.LogisticsPaymentReportBean;
import Bean.ManageDeliveryBoyBean;
import Bean.PaymentModeBean;

public class LogisticsPaymentReportViewModel {

	private LogisticsPaymentReportBean logisticsPaymentReportBean = new LogisticsPaymentReportBean();
	private ManageDeliveryBoyBean manageDeliveryBoyBean = new ManageDeliveryBoyBean();
	private PaymentModeBean paymentModeBean = new PaymentModeBean();
	
	private ArrayList<PaymentModeBean> paymentModeBeanList = new ArrayList<PaymentModeBean>();
	private ArrayList<LogisticsPaymentReportBean> logisticsPaymentReportBeanList = new ArrayList<LogisticsPaymentReportBean>();
	private ArrayList<ManageDeliveryBoyBean> manaDeliveryBoyBeanList = new ArrayList<ManageDeliveryBoyBean>();
	
	Session session = null;
	private Connection connection = null;
	private String userName = "";
	public Date startDate ,endDate;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		
		connection.setAutoCommit(true);
		
		paymentModeBeanList = LogisticsPaymentReportService.onLoadPaymentMode(connection);
		manaDeliveryBoyBeanList = LogisticsPaymentReportService.onLoadDriver(connection);
		logisticsPaymentReportBeanList = LogisticsPaymentReportService.onload(connection);
		System.out.println("zul page >> logisticPaymentReport.zul");
	}
	
	
	@Command
	@NotifyChange("*")
	public void onClickGenerateExcel(){

		 File f = null;
	      boolean bool = false;
	    if(logisticsPaymentReportBeanList.size()>0){
	    	try{
		         // create new file
		    	 String realPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
		  		
		  		String reportNamewithPath = realPath + "report.csv";
		  		System.out.println(reportNamewithPath);
		         //f = new File("C:/Users/somnathd/Desktop/report.csv");
		  		f = new File(reportNamewithPath);
		         // tries to create new file in the system
		         bool = f.createNewFile();
		         
		         // prints
		         System.out.println("File created: "+bool);
		         if(f.exists())
		         // deletes file from the system
		         f.delete();
		         System.out.println("delete() method is invoked");
		         // delete() is invoked
		         f.createNewFile();
		       
		         // tries to create new file in the system
		         bool = f.createNewFile();
		         
		         // print
		         System.out.println("File created: "+bool);
		         FileOutputStream fileOutputStream = new FileOutputStream(f);
		         OutputStreamWriter osw = new OutputStreamWriter(fileOutputStream);    
		            Writer w = new BufferedWriter(osw);
		           
		            w.write("DRIVER NAME,ORDER NO,DELIVERY DATE,PAYMENT MODE,AMOUNT\n");
		            for(int i=0;i<logisticsPaymentReportBeanList.size();i++){
		            	w.write(logisticsPaymentReportBeanList.get(i).getBikerName() +","+logisticsPaymentReportBeanList.get(i).getOrderNo()
		            			+","+logisticsPaymentReportBeanList.get(i).getDeliveryDateStr() +","+logisticsPaymentReportBeanList.get(i).getPaymentMode()
		            			+","+logisticsPaymentReportBeanList.get(i).getAmount()+"\n");
		            }
		            w.close();
		           // Desktop.getDesktop().open(f);
		            FileInputStream fis = new FileInputStream(new File(reportNamewithPath));
		    		
		    		byte[] ba1 = new byte[1024];

		    		int baLength;

		    		ByteArrayOutputStream bios = new ByteArrayOutputStream();

		    		try {

		    			try {

		    				while ((baLength = fis.read(ba1)) != -1) {

		    					bios.write(ba1, 0, baLength);

		    				}
		    			} catch (Exception e1) {

		    			} finally {

		    				fis.close();

		    			}

		    			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		    			String date = dateFormat.format(new Date());
		    			final AMedia amedia = new AMedia("paymey_mode_report--"+date, "csv", "application/csv", bios.toByteArray());

		    			Filedownload.save(amedia);
		    		}catch(Exception exception){
		    			
		    		}
		      }catch(Exception e){
		         e.printStackTrace();
		      }
	    }else{
	    	Messagebox.show("No DATA found !","Information",Messagebox.OK,Messagebox.EXCLAMATION);
	    }
	      
	
	}
	
	/*@Command
	@NotifyChange("*")
	public void onClickFind(){
		
		switch (key) {
		
		//only delivery boy
		case value:
			
			
			break;
		
		//only payment mode	
		case value:
			
			break;
			
		//only delivery date	
		case value:
			
			break;
		
		//only boy+mode 	
		case value:
			
			break;
			
		//only boy+date
		case value:

			break;

		//only mode+date	
		case value:

			break;

		//only boy+mode+date	
		case value:

			break;
		
		default:
			logisticsPaymentReportBeanList = LogisticsPaymentReportService.onload(connection);
			break;
		}
		
		
	}*/
	
	@Command
	@NotifyChange("*")
	public void onClickFind(){
		if(LogisticsPaymentReportService.validation(logisticsPaymentReportBean)){
			logisticsPaymentReportBeanList = LogisticsPaymentReportService.onloadDetailsWithDate(connection, logisticsPaymentReportBean);
			if(logisticsPaymentReportBeanList.size()==0){
				Messagebox.show("No DATA found !","Information",Messagebox.OK,Messagebox.EXCLAMATION);
			}
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickClear(){
		
		logisticsPaymentReportBean.setDeliveryFormDateUtil(null);
		logisticsPaymentReportBean.setDeliveryToDateUtil(null);
		
		manageDeliveryBoyBean.setName(null);
		manageDeliveryBoyBean.setDeliveryBoyId(null);
		manaDeliveryBoyBeanList = LogisticsPaymentReportService.onLoadDriver(connection);
		
		paymentModeBean.setPaymentModeId(null);
		paymentModeBean.setPaymentMode(null);
		paymentModeBeanList = LogisticsPaymentReportService.onLoadPaymentMode(connection);
		
		logisticsPaymentReportBeanList = LogisticsPaymentReportService.onload(connection);
		
		
		
	}
	
	
	

	public LogisticsPaymentReportBean getLogisticsPaymentReportBean() {
		return logisticsPaymentReportBean;
	}

	public void setLogisticsPaymentReportBean(
			LogisticsPaymentReportBean logisticsPaymentReportBean) {
		this.logisticsPaymentReportBean = logisticsPaymentReportBean;
	}

	public ArrayList<LogisticsPaymentReportBean> getLogisticsPaymentReportBeanList() {
		return logisticsPaymentReportBeanList;
	}

	public void setLogisticsPaymentReportBeanList(
			ArrayList<LogisticsPaymentReportBean> logisticsPaymentReportBeanList) {
		this.logisticsPaymentReportBeanList = logisticsPaymentReportBeanList;
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public ManageDeliveryBoyBean getManageDeliveryBoyBean() {
		return manageDeliveryBoyBean;
	}


	public void setManageDeliveryBoyBean(ManageDeliveryBoyBean manageDeliveryBoyBean) {
		this.manageDeliveryBoyBean = manageDeliveryBoyBean;
	}


	public ArrayList<ManageDeliveryBoyBean> getManaDeliveryBoyBeanList() {
		return manaDeliveryBoyBeanList;
	}


	public void setManaDeliveryBoyBeanList(
			ArrayList<ManageDeliveryBoyBean> manaDeliveryBoyBeanList) {
		this.manaDeliveryBoyBeanList = manaDeliveryBoyBeanList;
	}


	public PaymentModeBean getPaymentModeBean() {
		return paymentModeBean;
	}


	public void setPaymentModeBean(PaymentModeBean paymentModeBean) {
		this.paymentModeBean = paymentModeBean;
	}


	public ArrayList<PaymentModeBean> getPaymentModeBeanList() {
		return paymentModeBeanList;
	}


	public void setPaymentModeBeanList(
			ArrayList<PaymentModeBean> paymentModeBeanList) {
		this.paymentModeBeanList = paymentModeBeanList;
	}
	
	
}
