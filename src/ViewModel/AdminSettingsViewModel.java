package ViewModel;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javafx.util.converter.DateStringConverter;

import org.apache.poi.hssf.record.aggregates.CFRecordsAggregate;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.sun.org.apache.regexp.internal.recompile;
import com.sun.xml.internal.ws.api.model.MEP;

import dao.LunchDinnerTimingsDAO;
import utility.DatetoStringConverter;
import Bean.AdminSettingsBean;
import Bean.ManageCategoryBean;
public class AdminSettingsViewModel {
	
	private AdminSettingsBean slotTimmingsBean = new AdminSettingsBean();
	private ArrayList<AdminSettingsBean> slotTimmingsBeanList = new ArrayList<AdminSettingsBean>();
	
	private AdminSettingsBean adminSettingsbean =  new AdminSettingsBean();
	
	private AdminSettingsBean lunchDinnerTimingsbean =  new AdminSettingsBean();
	
	
	public AdminSettingsBean ebLunchTimingsbean =  new AdminSettingsBean();
	
	public ArrayList<AdminSettingsBean> ebLunchTimingsbeanList = new ArrayList<AdminSettingsBean>();
	
	
	public AdminSettingsBean ebDinnerTimingsbean =  new AdminSettingsBean();
	
	public ArrayList<AdminSettingsBean> ebDinnerTimingsbeanList = new ArrayList<AdminSettingsBean>();
	
	
	private ArrayList<AdminSettingsBean> lunchDinnerTimingsbeanList = new ArrayList<AdminSettingsBean>();
	
	private ArrayList<AdminSettingsBean> normalSpecialTimingsbeanList = new ArrayList<AdminSettingsBean>();
	
	private ArrayList<AdminSettingsBean> creditbeanList = new ArrayList<AdminSettingsBean>();
	
	private AdminSettingsBean normalSpecialTimingsbean =  new AdminSettingsBean();
	
	private AdminSettingsBean creditbean =  new AdminSettingsBean();

	private String locationImageFilePath;
	
	private String homeScreenImagefilePath;
	
	Session session = null;
	
	private Connection connection = null;

	private boolean fileuploaded = false, saveDisability=false, updateDisability= true;
	
	private AImage locationPageBannerImage;
	
	private AImage homeScreenBannerImage;
	
	private ManageCategoryBean lunchCategoryBean = new ManageCategoryBean();
	
	private ArrayList<ManageCategoryBean> lunchCategoryList = new ArrayList<ManageCategoryBean>();
	
	private ManageCategoryBean dinnerCategoryBean = new ManageCategoryBean();
	
	private ArrayList<ManageCategoryBean> dinnerCategoryList = new ArrayList<ManageCategoryBean>();
	
	private boolean dinnerCategoryOpen = false;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		connection.setAutoCommit(true);
		
		//loadLunchCategories();
		
		ebLunchTimingsbean.earlyBirdLunchCategoryList = LunchDinnerTimingsDAO.loadLunchCategories(connection);
		
		ebDinnerTimingsbean.earlyBirdDinnerCategoryList = LunchDinnerTimingsDAO.loadDinnerCategories(connection);
		//loadDinnerCategories();
		
		loadSavedTimings();
		//lunchDinnerTimingsbeanList = LunchDinnerTimingsDAO.loadSavedTimings(connection);
		//loadSavedSpecialTimings();
		
		//loadSavedCredits();
		System.out.println("zul page >> lunchdinnertimings.zul");
		if(lunchDinnerTimingsbeanList.size()>1 || lunchDinnerTimingsbeanList.size()==1){
			saveDisability = true;
		}else{
			saveDisability = false;
		}
		
		slotTimmingsBeanList = timeSlotLoad();
		if(slotTimmingsBeanList.size()==1){
			slotTimmingsBean.slotTimeSaveDisabled = true;
			slotTimmingsBean.slotTimeUpdateDisabled = true;
		}else {
			slotTimmingsBean.slotTimeSaveDisabled = false;
			slotTimmingsBean.slotTimeUpdateDisabled = true;
		}
	}
	
	
	
	
	
	@Command
	@NotifyChange("*")
	public void onClickTab1(){
		loadSavedTimings();
	}
	
	@Command
	@NotifyChange("*")
	public void onClickTab2(){
		ebLunchTimingsbeanList = LunchDinnerTimingsDAO.loadSavedLunchTimings(connection);
	}
	
	@Command
	@NotifyChange("*")
	public void onClickTab3(){
		normalSpecialTimingsbeanList = LunchDinnerTimingsDAO.loadSavedDinnerTimings(connection);
	}
	
	@Command
	@NotifyChange("*")
	public void onClickTab4(){
		loadSavedCredits();
	}
	
	@Command
	@NotifyChange("*")
	public void onClickTab5(){
		
	}
	
	
	
	
	@Command
	@NotifyChange("*")
	public void onClickLunchItemDetails(@BindingParam("bean") AdminSettingsBean lunch){
		Window window = (Window) Executions.createComponents("itemdetails.zul", null, null);
		window.doModal();
	}
	
	@Command
	@NotifyChange("*")
	public void onClickDinnerItemDetails(@BindingParam("bean") AdminSettingsBean dinner){
		Window window = (Window) Executions.createComponents("itemdetails.zul", null, null);
		window.doModal();
	}
	
	@Command
	@NotifyChange("*")
	public void onClickSaveLD(){
		
		if(isLDValid()){
			lunchDinnerTimingsbean.lunchFromTimingsValue = DatetoStringConverter.convert24DateToString(lunchDinnerTimingsbean.lunchFromTimings);
			lunchDinnerTimingsbean.lunchToTimingsValue = DatetoStringConverter.convert24DateToString(lunchDinnerTimingsbean.lunchToTimings);
			lunchDinnerTimingsbean.dinnerFromTimingsValue = DatetoStringConverter.convert24DateToString(lunchDinnerTimingsbean.dinnerFromTimings);
			lunchDinnerTimingsbean.dinnerToTimingsValue = DatetoStringConverter.convert24DateToString(lunchDinnerTimingsbean.dinnerToTimings);
			
			LunchDinnerTimingsDAO.saveLD(lunchDinnerTimingsbean, connection);
			lunchDinnerTimingsbeanList = LunchDinnerTimingsDAO.loadSavedTimings(connection);
			if(lunchDinnerTimingsbeanList.size()>1 || lunchDinnerTimingsbeanList.size()==1){
				saveDisability = true;
			}else{
				saveDisability = false;
			}
			clearLDData();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickEditLD(@BindingParam("bean")AdminSettingsBean bean){
		updateDisability = false;
		
		lunchDinnerTimingsbean.setLunchFromTimings(DatetoStringConverter.convertStringTo24Date(bean.lunchFromTimingsValue));
		lunchDinnerTimingsbean.setLunchToTimings(DatetoStringConverter.convertStringTo24Date(bean.lunchToTimingsValue));
		
		
		lunchDinnerTimingsbean.setDinnerFromTimings(DatetoStringConverter.convertStringTo24Date(bean.dinnerFromTimingsValue));
		lunchDinnerTimingsbean.setDinnerToTimings(DatetoStringConverter.convertStringTo24Date(bean.dinnerToTimingsValue));
		
		lunchDinnerTimingsbean.setLunchDinnerTimingId(bean.getLunchDinnerTimingId());
		
		updateDisability = false;
	}
	
	@Command
	@NotifyChange("*")
	public void onClickUpdateLD(){
		if(isLDValid()){
			lunchDinnerTimingsbean.setLunchFromTimingsValue( DatetoStringConverter.convert24DateToString(lunchDinnerTimingsbean.getLunchFromTimings()));
			lunchDinnerTimingsbean.setLunchToTimingsValue(DatetoStringConverter.convert24DateToString(lunchDinnerTimingsbean.getLunchToTimings()));
			lunchDinnerTimingsbean.setDinnerFromTimingsValue(DatetoStringConverter.convert24DateToString(lunchDinnerTimingsbean.getDinnerFromTimings()));
			lunchDinnerTimingsbean.setDinnerToTimingsValue(DatetoStringConverter.convert24DateToString(lunchDinnerTimingsbean.getDinnerToTimings()));
			
			Messagebox.show("Lunch from "+lunchDinnerTimingsbean.lunchFromTimingsValue+" Lunch to:"+lunchDinnerTimingsbean.lunchToTimingsValue
					+"Dinner from "+lunchDinnerTimingsbean.dinnerFromTimingsValue+" Dinner to:"+lunchDinnerTimingsbean.dinnerToTimingsValue);
			LunchDinnerTimingsDAO.updateLD(lunchDinnerTimingsbean, connection);
			lunchDinnerTimingsbeanList = LunchDinnerTimingsDAO.loadSavedTimings(connection);
			updateDisability = true;
			clearLDData();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickSaveEBLunch(){
		if(LunchDinnerTimingsDAO.isEBLunchValid(ebLunchTimingsbean, connection)){
			 
			ebLunchTimingsbean.ebLunchFromTimingsValue = DatetoStringConverter.convertStringToDate( ebLunchTimingsbean.ebLunchFromTimings);
			ebLunchTimingsbean.ebLunchToTimingsValue = DatetoStringConverter.convertStringToDate( ebLunchTimingsbean.ebLunchToTimings);
			/**
			 * Save early bird lunch settings
			 */
			LunchDinnerTimingsDAO.saveEBLunch(connection, ebLunchTimingsbean);
			clearEbLunchDetails();
			ebLunchTimingsbeanList = LunchDinnerTimingsDAO.loadSavedLunchTimings(connection);
		}
	}
	
	
	@Command
	@NotifyChange("*")
	public void onClickSaveEBDinner(){
		if(LunchDinnerTimingsDAO.isEBDinnerValid(ebDinnerTimingsbean, connection)){
			 
			//ebLunchTimingsbean.ebLunchFromTimingsValue = DatetoStringConverter.convertStringToDate( ebLunchTimingsbean.ebDinnerFromTimings);
			//ebLunchTimingsbean.ebLunchToTimingsValue = DatetoStringConverter.convertStringToDate( ebLunchTimingsbean.ebDinnerToTimings);
			ebDinnerTimingsbean.dinnerFromTimingsValue = DatetoStringConverter.convertStringToDate(ebDinnerTimingsbean.dinnerFromTimings);
			ebDinnerTimingsbean.dinnerToTimingsValue = DatetoStringConverter.convertStringToDate(ebDinnerTimingsbean.dinnerToTimings);
			/**
			 * Save early bird dinner settings
			 */
			LunchDinnerTimingsDAO.saveEBDinner(connection, ebDinnerTimingsbean);
			clearEbDinnerDetails();
			normalSpecialTimingsbeanList = LunchDinnerTimingsDAO.loadSavedDinnerTimings(connection);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickSaveCredit(){
		if(isCreditValid()){
			LunchDinnerTimingsDAO.saveCredit(connection, creditbean);
		}
	}
	
	
	
	
	/*public void loadSavedSpecialLunchTimings(){
		if(ebLunchTimingsbeanList.size()>0){
			ebLunchTimingsbeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select early_bird_lunch_from,early_bird_lunch_to,early_bird_id from fapp_early_bird ";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							AdminSettingsBean lunchBean = new AdminSettingsBean();
							lunchBean.ebLunchFromTimingsValue = resultSet.getString("early_bird_lunch_from");
							lunchBean.ebLunchToTimingsValue = resultSet.getString("early_bird_lunch_to");
							lunchBean.ebLunchId = resultSet.getInt("early_bird_id");
							ebLunchTimingsbeanList.add(lunchBean);
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public ArrayList<ManageCategoryBean> loadEbLunchCategoryList(int ebLunchId){
		ArrayList<ManageCategoryBean> ebLunchCategoryList = new ArrayList<ManageCategoryBean>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select early_bird_category_id from fapp_early_bird_lunch_details where early_bird_id = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, ebLunchId);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							AdminSettingsBean ebLunchBean = new AdminSettingsBean();
							ebLunchBean.ebLunchBean.categoryId = resultSet.getInt("early_bird_category_id");
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ebLunchCategoryList;
	}*/
	
	
	
	
	
	
	/*public void saveEBLunch() {
		boolean savedLD = false, saveLunch=false;
		int earlyBirdId = 0;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "INSERT INTO fapp_early_bird(early_bird_lunch_from, early_bird_lunch_to) "
							   +" VALUES (?, ?)";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, ebLunchTimingsbean.ebLunchFromTimingsValue);
						preparedStatement.setString(2, ebLunchTimingsbean.ebLunchToTimingsValue);
						int update = preparedStatement.executeUpdate();
						if(update>0){
							savedLD = true;
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement != null){
							preparedStatement.close();
						}
					}
			}
		
		
			if(savedLD){
				SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select MAX(early_bird_id) from fapp_early_bird ";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							earlyBirdId = resultSet.getInt("early_bird_id");
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement != null){
							preparedStatement.close();
						}
					}
				}
			}
		
			if(earlyBirdId>0){
				SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "INSERT INTO fapp_early_bird_lunch_details(early_bird_id, early_bird_category_id) "
							   +" VALUES (?, ?)";
					try {
						preparedStatement = connection.prepareStatement(sql);
						for(ManageCategoryBean categoryBean : ebLunchTimingsbean.earlyBirdLunchCategoryList){
							if(categoryBean.chkCategory){
								preparedStatement.setInt(1, earlyBirdId);
								preparedStatement.setInt(2, categoryBean.categoryId);
								preparedStatement.addBatch();
							}
						}
						int[] countRows = preparedStatement.executeBatch();
						if(countRows.length>0){
							saveLunch = true;
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement != null){
							preparedStatement.close();
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(saveLunch){
			Messagebox.show("Early bird lunch Timings saved!","Successful Information",Messagebox.OK,Messagebox.INFORMATION);
			clearEbLunchDetails();
		}
	}*/

	/*public void saveCredit() {
		boolean savedLD = false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "INSERT INTO fapp_credits(sign_up_credit, order_credit) "
							   +" VALUES (?, ?)";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setDouble(1, creditbean.signUpCredit);
						preparedStatement.setDouble(2, creditbean.orderCredit);
						
						int update = preparedStatement.executeUpdate();
						if(update>0){
							savedLD = true;
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement != null){
							preparedStatement.close();
						}
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(savedLD){
			Messagebox.show("Credits saved!","Successful Information",Messagebox.OK,Messagebox.INFORMATION);
		}
	}*/
	
	@Command
	@NotifyChange("*")
	public void onClickSaveSettings(){
		Messagebox.show("Booking from time::"+adminSettingsbean.bookingFromTime+"\nBooking to time::"+adminSettingsbean.bookingToTime);
		if(adminSettingsbean.bookingFromTime==null){
			Messagebox.show("Booking from time can not be blank!!");
		}
		else if(adminSettingsbean.bookingToTime==null){
			Messagebox.show("Booking to time can not be blank!!");
		}
		else if(locationPageBannerImage==null){
			Messagebox.show("Location image Banner can not be blank!!");
		}
		else if(homeScreenBannerImage==null){
			Messagebox.show("Home screen image can not be blank!!");
		}
		else{
			saveSettingsData();
		}
	}
	
	public void saveSettingsData(){
		
	}
	
	@Command
	@NotifyChange("*")
	public void onClickEditCredit(@BindingParam("bean")AdminSettingsBean creditBean){
		creditbean.signUpCredit = creditBean.signUpCredit;
		creditbean.orderCredit = creditBean.orderCredit;
		creditbean.creditId = creditBean.creditId;
	}
	
	@Command
	@NotifyChange("*")
	public void onClickCreditClear(){
		creditbean.signUpCredit = 0.0;
		creditbean.orderCredit = 0.0;
	}
	
	public void clearData(){
		adminSettingsbean.bookingFromTime=null;
		adminSettingsbean.bookingToTime=null;
		adminSettingsbean.welcomePromotion="";
		locationPageBannerImage=null;
		homeScreenBannerImage=null;	
	}
	
	public void clearLDData(){
		lunchDinnerTimingsbean.lunchFromTimings = null;
		lunchDinnerTimingsbean.lunchToTimings = null;
		lunchDinnerTimingsbean.dinnerFromTimings = null;
		lunchDinnerTimingsbean.dinnerToTimings = null;
	}
	
	public void clearEbLunchDetails(){
		ebDinnerTimingsbean.ebLunchFromTimings = null;
		ebDinnerTimingsbean.ebLunchToTimings = null;
		dinnerCategoryOpen = false;
		for(ManageCategoryBean bean : ebDinnerTimingsbean.earlyBirdLunchCategoryList){
			if(bean.chkCategory){
				bean.chkCategory = false;
			}
		}
	}
	
	public void clearEbDinnerDetails(){
		ebDinnerTimingsbean.dinnerFromTimings = null;
		ebDinnerTimingsbean.dinnerToTimings = null;
		dinnerCategoryOpen = false;
		for(ManageCategoryBean bean : ebDinnerTimingsbean.earlyBirdDinnerCategoryList){
			if(bean.chkCategory){
				bean.chkCategory = false;
			}
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onUploadLocationImage(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) throws IOException {

		UploadEvent upEvent = null;
		
		Object objUploadEvent = ctx.getTriggerEvent();
		
		if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
			upEvent = (UploadEvent) objUploadEvent;
		}

		if (upEvent != null) {

			Media media = upEvent.getMedia();
			
			String yearPath = "Location_Page_Banners" + "\\"+new SimpleDateFormat("dd-MMM-yyyy").format(new java.util.Date())+ "\\";

			locationImageFilePath = "C:\\FooApp_Project_Images\\" + yearPath;
			File baseDir = new File(locationImageFilePath);
			if (!baseDir.exists()) {
				baseDir.mkdirs();
			}

			Files.copy(new File(locationImageFilePath + media.getName()),media.getStreamData());

			fileuploaded = true;
			locationImageFilePath = locationImageFilePath + media.getName();
		
			adminSettingsbean.locationBannerPicturePath = locationImageFilePath;

			locationPageBannerImage = (AImage) media;
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onUploadHomeScreenImage(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) throws IOException {

		UploadEvent upEvent = null;
		
		Object objUploadEvent = ctx.getTriggerEvent();
		
		if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
			upEvent = (UploadEvent) objUploadEvent;
		}

		if (upEvent != null) {

			Media media = upEvent.getMedia();
			
			String yearPath = "Home_Screen_Banners" + "\\"+new SimpleDateFormat("dd-MMM-yyyy").format(new java.util.Date())+ "\\";

			homeScreenImagefilePath = "C:\\Project_Images\\" + yearPath;
			File baseDir = new File(homeScreenImagefilePath);
			if (!baseDir.exists()) {
				baseDir.mkdirs();
			}

			Files.copy(new File(homeScreenImagefilePath + media.getName()),media.getStreamData());

			fileuploaded = true;
			homeScreenImagefilePath = homeScreenImagefilePath + media.getName();
		
			adminSettingsbean.homeScreenBannerPicturePath = homeScreenImagefilePath;

			homeScreenBannerImage = (AImage) media;
		}
	}
	
	
	public boolean isLDValid(){
		if(lunchDinnerTimingsbean.lunchFromTimings != null){
			if(lunchDinnerTimingsbean.lunchToTimings != null){
				if(lunchDinnerTimingsbean.dinnerFromTimings != null){
					if(lunchDinnerTimingsbean.dinnerToTimings != null ){
						return true;
					}else{
						Messagebox.show("Dinner range(TO) not given!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
						return false;
					}
				}else{
					Messagebox.show("Dinner range(FROM) not given!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
					return false;
				}
			}else{
				Messagebox.show("Lunch range(TO) not given!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
				return false;
			}
		}else{
			Messagebox.show("Lunch range(FROM) not given!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
			return false;
		}
	}
	
	public boolean isEBValid(){
		boolean isChked = false;
		for(ManageCategoryBean bean: lunchCategoryList){
			if(bean.chkCategory){
				isChked = true;
			}
		}		
		if(normalSpecialTimingsbean.ebLunchFromTimings != null){
			if(normalSpecialTimingsbean.ebDinnerToTimings != null ){
				if(isChked){
					return true;
				}else{
					Messagebox.show("Category not chosen!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
					return false;
				}
			}else{
				Messagebox.show("Lunch time(TO) not given!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
				return false;
			}
		}else{
			Messagebox.show("Lunch time(FROM) not given!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
			return false;
		}
	}
	
	public boolean isCreditValid(){
		if(creditbean.signUpCredit > 0){
			if(creditbean.orderCredit > 0){
				return true;
			}else{
				Messagebox.show("Order credit value required!","Alert!",Messagebox.OK,Messagebox.EXCLAMATION);
				return false;
			}
		}else{
			Messagebox.show("Sign up credit value required!","Alert!",Messagebox.OK,Messagebox.EXCLAMATION);
			return false;
		}
	}
	
	
	
	public void loadSavedTimings(){
		lunchDinnerTimingsbeanList = LunchDinnerTimingsDAO.loadSavedTimings(connection);
	}
	
	public void loadSavedCredits(){
		creditbeanList = LunchDinnerTimingsDAO.loadSavedCredits(connection);
	}
	
	@Command
	@NotifyChange("*")
	public void onClickSlotSave(){
		saveSlotTime();
	}
	
	@Command
	@NotifyChange("*")
	public void onClickSlotUpdate(){
	
		int i = 0;
		i = updateTimeSlot();
		if(i>0){
			Messagebox.show("Updated Successfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
			timeSlotClear();
			slotTimmingsBeanList = timeSlotLoad();
			if(slotTimmingsBeanList.size()==1){
				slotTimmingsBean.slotTimeSaveDisabled = true;
				slotTimmingsBean.slotTimeUpdateDisabled = true;
			}else {
				slotTimmingsBean.slotTimeSaveDisabled = false;
				slotTimmingsBean.slotTimeUpdateDisabled = true;
			}
		 }
	}

	@Command
	@NotifyChange("*")
	public void onClickSlotEdit(@BindingParam("bean")AdminSettingsBean bean){
		slotTimmingsBean.slotTimeUpdateDisabled = false;
		
		slotTimmingsBean.lunchSlotFromTime= DatetoStringConverter.convertStringTo24DateSlot(bean.lunchSlotFromTimeStr);
		slotTimmingsBean.lunchSlotToTime = DatetoStringConverter.convertStringTo24DateSlot(bean.lunchSlotToTimeStr);
		
		slotTimmingsBean.dinnerSlotFromTime = DatetoStringConverter.convertStringTo24DateSlot(bean.dinnerSlotFromTimeStr);
		slotTimmingsBean.dinnerSlotToTime = DatetoStringConverter.convertStringTo24DateSlot(bean.dinnerSlotToTimeStr);
		
		slotTimmingsBean.slotTimmingsId = bean.slotTimmingsId;
		slotTimmingsBean.slotTimeUpdateDisabled = false;
	}
	
	public void saveSlotTime(){
		try {
			PreparedStatement preparedStatement = null;
			String sql = "insert into fapp_slot_timings (lunch_from,lunch_to,dinner_from,dinner_to) values(?,?,?,?)";
			try {
				if(isSlotTimeNull()){
				
					slotTimmingsBean.lunchSlotFromTimeStr = DatetoStringConverter.convert24DateToStringSlot(slotTimmingsBean.lunchSlotFromTime);
					slotTimmingsBean.lunchSlotToTimeStr = DatetoStringConverter.convert24DateToStringSlot(slotTimmingsBean.lunchSlotToTime);
					slotTimmingsBean.dinnerSlotFromTimeStr = DatetoStringConverter.convert24DateToStringSlot(slotTimmingsBean.dinnerSlotFromTime);
					slotTimmingsBean.dinnerSlotToTimeStr = DatetoStringConverter.convert24DateToStringSlot(slotTimmingsBean.dinnerSlotToTime);
					
					preparedStatement = connection.prepareStatement(sql);
					
					preparedStatement= connection.prepareStatement(sql);
					preparedStatement.setString(1, slotTimmingsBean.lunchSlotFromTimeStr);
					preparedStatement.setString(2, slotTimmingsBean.lunchSlotToTimeStr);
					preparedStatement.setString(3, slotTimmingsBean.dinnerSlotFromTimeStr);
					preparedStatement.setString(4, slotTimmingsBean.dinnerSlotToTimeStr);
					
					int i = preparedStatement.executeUpdate();
					if(i>0){
						Messagebox.show("Saved Successfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
						timeSlotClear();
						slotTimmingsBeanList = timeSlotLoad();
						if(slotTimmingsBeanList.size()==1){
							slotTimmingsBean.slotTimeSaveDisabled = true;
							slotTimmingsBean.slotTimeUpdateDisabled = false;
						}else {
							slotTimmingsBean.slotTimeSaveDisabled = false;
							slotTimmingsBean.slotTimeUpdateDisabled = true;
						}
					}
				}else {
					Messagebox.show("Select All fields", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show(e.getMessage(), "Alert", Messagebox.OK, Messagebox.ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<AdminSettingsBean> timeSlotLoad(){
		ArrayList<AdminSettingsBean> list = new ArrayList<AdminSettingsBean>();
		if(list.size()>0){
			list.clear();
		}
		String sql= "select slot_timings_id,lunch_from,lunch_to,dinner_from,dinner_to from fapp_slot_timings ";
		try {
				PreparedStatement preparedStatement = null;
				preparedStatement = connection.prepareStatement(sql);
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					AdminSettingsBean bean = new AdminSettingsBean();
					
					bean.slotTimmingsId = resultSet.getInt("slot_timings_id");
					bean.lunchSlotFromTimeStr = resultSet.getString("lunch_from");
					bean.lunchSlotToTimeStr = resultSet.getString("lunch_to");
					bean.dinnerSlotFromTimeStr = resultSet.getString("dinner_from");
					bean.dinnerSlotToTimeStr = resultSet.getString("dinner_to");
					
					list.add(bean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public int updateTimeSlot(){
		int i = 0;
		PreparedStatement preparedStatement = null;
		String sql = "update fapp_slot_timings set lunch_from = ? ,lunch_to = ?,dinner_from = ?,dinner_to = ? where slot_timings_id = ? ";
		
		try {
			if(isSlotTimeNull()){
				
				slotTimmingsBean.lunchSlotFromTimeStr = DatetoStringConverter.convert24DateToStringSlot(slotTimmingsBean.lunchSlotFromTime);
				slotTimmingsBean.lunchSlotToTimeStr = DatetoStringConverter.convert24DateToStringSlot(slotTimmingsBean.lunchSlotToTime);
				slotTimmingsBean.dinnerSlotFromTimeStr = DatetoStringConverter.convert24DateToStringSlot(slotTimmingsBean.dinnerSlotFromTime);
				slotTimmingsBean.dinnerSlotToTimeStr = DatetoStringConverter.convert24DateToStringSlot(slotTimmingsBean.dinnerSlotToTime);
				
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, slotTimmingsBean.lunchSlotFromTimeStr);
				preparedStatement.setString(2, slotTimmingsBean.lunchSlotToTimeStr);
				preparedStatement.setString(3, slotTimmingsBean.dinnerSlotFromTimeStr);
				preparedStatement.setString(4, slotTimmingsBean.dinnerSlotToTimeStr);
				preparedStatement.setInt(5, slotTimmingsBean.slotTimmingsId);
			    
				i = preparedStatement.executeUpdate();
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
		
	}
	
	public boolean isSlotTimeNull(){
		
		if(slotTimmingsBean.lunchSlotFromTime !=null){
			
			if(slotTimmingsBean.lunchSlotToTime !=null){
				
				if(slotTimmingsBean.dinnerSlotFromTime !=null){
					
					if(slotTimmingsBean.dinnerSlotToTime !=null){
						
						return true;
					}else {
						
						Messagebox.show("Enter Dinner Slot End Time", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
						return false;
					}
				}else {
					
					Messagebox.show("Enter Dinner Slot Start Time", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
					return false;
				}
			}else {
				
				Messagebox.show("Enter Lunch Slot End Time", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
				return false;
			}
		}else {
			
			Messagebox.show("Enter Lunch Slot Start Time", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
			return false;
		}
		
	}

	
	public void timeSlotClear(){
		slotTimmingsBean.lunchSlotFromTime = null;
		slotTimmingsBean.lunchSlotToTime = null;
		slotTimmingsBean.dinnerSlotFromTime = null;
		slotTimmingsBean.dinnerSlotToTime = null;
		
		slotTimmingsBean.lunchSlotFromTimeStr = null;
		slotTimmingsBean.lunchSlotToTimeStr = null;
		slotTimmingsBean.dinnerSlotFromTimeStr = null;
		slotTimmingsBean.dinnerSlotToTimeStr = null;
		
	}
	
	
	
	/*public void loadSavedCredits(){
	if(creditbeanList.size()>0){
		creditbeanList.clear();
	}
	try {
		SQL:{
				PreparedStatement preparedStatement = null;
				ResultSet resultSet = null;
				String sql = "select sign_up_credit,order_credit,credits_id from fapp_credits order by credits_id desc limit 1";
				try {
					preparedStatement = connection.prepareStatement(sql);
					resultSet = preparedStatement.executeQuery();
					while (resultSet.next()) {
						AdminSettingsBean lunchDinnerBean = new AdminSettingsBean();
						lunchDinnerBean.signUpCredit = resultSet.getDouble("sign_up_credit");
						lunchDinnerBean.orderCredit = resultSet.getDouble("order_credit");
						creditbeanList.add(lunchDinnerBean);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
				}
		}
	} catch (Exception e) {
		// TODO: handle exception
	}
}*/
	
	/*public void loadSavedTimings(){
		if(lunchDinnerTimingsbeanList.size()>0){
			lunchDinnerTimingsbeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select lunch_from,lunch_to,dinner_from,dinner_to,timings_id from fapp_timings";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							AdminSettingsBean lunchDinnerBean = new AdminSettingsBean();
							lunchDinnerBean.lunchFromTimingsValue = resultSet.getString("lunch_from");
							lunchDinnerBean.lunchToTimingsValue = resultSet.getString("lunch_to");
							lunchDinnerBean.dinnerFromTimingsValue = resultSet.getString("dinner_from");
							lunchDinnerBean.dinnerToTimingsValue = resultSet.getString("dinner_to");
							lunchDinnerBean.lunchDinnerTimingId = resultSet.getInt("timings_id");
							lunchDinnerTimingsbeanList.add(lunchDinnerBean);
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}*/
	
	
	/*public void loadDinnerCategories(){
		if( ebDinnerTimingsbean.earlyBirdDinnerCategoryList.size()>0){
			ebDinnerTimingsbean.earlyBirdDinnerCategoryList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select category_id,category_name from food_category where category_price IS NULL ";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							ManageCategoryBean categoryBean = new ManageCategoryBean();
							categoryBean.categoryId = resultSet.getInt("category_id");
							categoryBean.categoryName = resultSet.getString("category_name");
							categoryBean.chkCategory = false;
							ebDinnerTimingsbean.earlyBirdDinnerCategoryList.add(categoryBean);		
						}
								
					} catch (Exception e) {
						e.printStackTrace();
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
	}*/
	
	
	/*public void loadLunchCategories(){
		if(ebLunchTimingsbean.earlyBirdLunchCategoryList.size()>0){
			ebLunchTimingsbean.earlyBirdLunchCategoryList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select category_id,category_name from food_category where category_price IS NULL ";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							ManageCategoryBean categoryBean = new ManageCategoryBean();
							categoryBean.categoryId = resultSet.getInt("category_id");
							categoryBean.categoryName = resultSet.getString("category_name");
							categoryBean.chkCategory = false;
							ebLunchTimingsbean.earlyBirdLunchCategoryList.add(categoryBean);		
						}
								
					} catch (Exception e) {
						e.printStackTrace();
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
	}*/
	
	
	
	
	/*public void saveLD() {
		boolean savedLD = false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "INSERT INTO fapp_timings(lunch_from, lunch_to, dinner_from, dinner_to) "
							   +" VALUES (?, ?, ?, ?)";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, lunchDinnerTimingsbean.lunchFromTimingsValue);
						preparedStatement.setString(2, lunchDinnerTimingsbean.lunchToTimingsValue);
						preparedStatement.setString(3, lunchDinnerTimingsbean.dinnerFromTimingsValue);
						preparedStatement.setString(4, lunchDinnerTimingsbean.dinnerToTimingsValue);
						int update = preparedStatement.executeUpdate();
						if(update>0){
							savedLD = true;
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement != null){
							preparedStatement.close();
						}
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(savedLD){
			Messagebox.show("Timings saved!","Successful Information",Messagebox.OK,Messagebox.INFORMATION);
		}
	}*/
	
	
	public AdminSettingsBean getAdminSettingsbean() {
		return adminSettingsbean;
	}

	public void setAdminSettingsbean(AdminSettingsBean adminSettingsbean) {
		this.adminSettingsbean = adminSettingsbean;
	}
	public boolean isFileuploaded() {
		return fileuploaded;
	}

	public void setFileuploaded(boolean fileuploaded) {
		this.fileuploaded = fileuploaded;
	}

	public AImage getLocationPageBannerImage() {
		return locationPageBannerImage;
	}

	public void setLocationPageBannerImage(AImage locationPageBannerImage) {
		this.locationPageBannerImage = locationPageBannerImage;
	}

	public AImage getHomeScreenBannerImage() {
		return homeScreenBannerImage;
	}

	public void setHomeScreenBannerImage(AImage homeScreenBannerImage) {
		this.homeScreenBannerImage = homeScreenBannerImage;
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


	public String getLocationImageFilePath() {
		return locationImageFilePath;
	}


	public void setLocationImageFilePath(String locationImageFilePath) {
		this.locationImageFilePath = locationImageFilePath;
	}


	public String getHomeScreenImagefilePath() {
		return homeScreenImagefilePath;
	}


	public void setHomeScreenImagefilePath(String homeScreenImagefilePath) {
		this.homeScreenImagefilePath = homeScreenImagefilePath;
	}

	public AdminSettingsBean getLunchDinnerTimingsbean() {
		return lunchDinnerTimingsbean;
	}

	public void setLunchDinnerTimingsbean(AdminSettingsBean lunchDinnerTimingsbean) {
		this.lunchDinnerTimingsbean = lunchDinnerTimingsbean;
	}

	public AdminSettingsBean getNormalSpecialTimingsbean() {
		return normalSpecialTimingsbean;
	}

	public void setNormalSpecialTimingsbean(
			AdminSettingsBean normalSpecialTimingsbean) {
		this.normalSpecialTimingsbean = normalSpecialTimingsbean;
	}

	public AdminSettingsBean getCreditbean() {
		return creditbean;
	}

	public void setCreditbean(AdminSettingsBean creditbean) {
		this.creditbean = creditbean;
	}


	public ArrayList<AdminSettingsBean> getLunchDinnerTimingsbeanList() {
		return lunchDinnerTimingsbeanList;
	}


	public void setLunchDinnerTimingsbeanList(
			ArrayList<AdminSettingsBean> lunchDinnerTimingsbeanList) {
		this.lunchDinnerTimingsbeanList = lunchDinnerTimingsbeanList;
	}


	public ArrayList<AdminSettingsBean> getNormalSpecialTimingsbeanList() {
		return normalSpecialTimingsbeanList;
	}


	public void setNormalSpecialTimingsbeanList(
			ArrayList<AdminSettingsBean> normalSpecialTimingsbeanList) {
		this.normalSpecialTimingsbeanList = normalSpecialTimingsbeanList;
	}


	public ArrayList<AdminSettingsBean> getCreditbeanList() {
		return creditbeanList;
	}


	public void setCreditbeanList(ArrayList<AdminSettingsBean> creditbeanList) {
		this.creditbeanList = creditbeanList;
	}

	public ManageCategoryBean getLunchCategoryBean() {
		return lunchCategoryBean;
	}


	public void setLunchCategoryBean(ManageCategoryBean lunchCategoryBean) {
		this.lunchCategoryBean = lunchCategoryBean;
	}


	public ArrayList<ManageCategoryBean> getLunchCategoryList() {
		return lunchCategoryList;
	}


	public void setLunchCategoryList(ArrayList<ManageCategoryBean> lunchCategoryList) {
		this.lunchCategoryList = lunchCategoryList;
	}


	public ManageCategoryBean getDinnerCategoryBean() {
		return dinnerCategoryBean;
	}


	public void setDinnerCategoryBean(ManageCategoryBean dinnerCategoryBean) {
		this.dinnerCategoryBean = dinnerCategoryBean;
	}


	public ArrayList<ManageCategoryBean> getDinnerCategoryList() {
		return dinnerCategoryList;
	}


	public void setDinnerCategoryList(
			ArrayList<ManageCategoryBean> dinnerCategoryList) {
		this.dinnerCategoryList = dinnerCategoryList;
	}


	public AdminSettingsBean getEbLunchTimingsbean() {
		return ebLunchTimingsbean;
	}


	public void setEbLunchTimingsbean(AdminSettingsBean ebLunchTimingsbean) {
		this.ebLunchTimingsbean = ebLunchTimingsbean;
	}


	public AdminSettingsBean getEbDinnerTimingsbean() {
		return ebDinnerTimingsbean;
	}


	public void setEbDinnerTimingsbean(AdminSettingsBean ebDinnerTimingsbean) {
		this.ebDinnerTimingsbean = ebDinnerTimingsbean;
	}


	public ArrayList<AdminSettingsBean> getEbLunchTimingsbeanList() {
		return ebLunchTimingsbeanList;
	}


	public void setEbLunchTimingsbeanList(
			ArrayList<AdminSettingsBean> ebLunchTimingsbeanList) {
		this.ebLunchTimingsbeanList = ebLunchTimingsbeanList;
	}


	public ArrayList<AdminSettingsBean> getEbDinnerTimingsbeanList() {
		return ebDinnerTimingsbeanList;
	}


	public void setEbDinnerTimingsbeanList(
			ArrayList<AdminSettingsBean> ebDinnerTimingsbeanList) {
		this.ebDinnerTimingsbeanList = ebDinnerTimingsbeanList;
	}





	public boolean isDinnerCategoryOpen() {
		return dinnerCategoryOpen;
	}





	public void setDinnerCategoryOpen(boolean dinnerCategoryOpen) {
		this.dinnerCategoryOpen = dinnerCategoryOpen;
	}





	public boolean isSaveDisability() {
		return saveDisability;
	}





	public void setSaveDisability(boolean saveDisability) {
		this.saveDisability = saveDisability;
	}





	public boolean isUpdateDisability() {
		return updateDisability;
	}





	public void setUpdateDisability(boolean updateDisability) {
		this.updateDisability = updateDisability;
	}





	public AdminSettingsBean getSlotTimmingsBean() {
		return slotTimmingsBean;
	}





	public void setSlotTimmingsBean(AdminSettingsBean slotTimmingsBean) {
		this.slotTimmingsBean = slotTimmingsBean;
	}





	public ArrayList<AdminSettingsBean> getSlotTimmingsBeanList() {
		return slotTimmingsBeanList;
	}





	public void setSlotTimmingsBeanList(
			ArrayList<AdminSettingsBean> slotTimmingsBeanList) {
		this.slotTimmingsBeanList = slotTimmingsBeanList;
	}

	
	
}
