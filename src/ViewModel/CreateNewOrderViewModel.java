package ViewModel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import dao.CreateOrderDAO;
import Bean.DayBean;
import Bean.ManageCuisinBean;
import Bean.MealBean;
import Bean.RoadRunnrBean;

public class CreateNewOrderViewModel {

	private RoadRunnrBean loactionBean = new RoadRunnrBean();
	
	private ArrayList<RoadRunnrBean> locationBeanList = new ArrayList<RoadRunnrBean>();
	
	private DayBean dayBean = new DayBean();
	
	private ArrayList<DayBean> dayBeanList = new ArrayList<DayBean>();
	
	private MealBean mealBean = new MealBean();
	
	private ArrayList<MealBean> mealBeanList = new ArrayList<MealBean>();
	
	private ManageCuisinBean cuisinBean = new ManageCuisinBean();
	
	private ArrayList<ManageCuisinBean> cuisinBeanList = new ArrayList<ManageCuisinBean>();
	
	Session session = null;
	
	private Connection connection = null;
	
	private String userName;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws SQLException{

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		
		loadAllLocationList();
	}
	
	public void loadAllLocationList(){
		locationBeanList = CreateOrderDAO.fetchActiveLocationList(connection);
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectLocationName(){
		if(dayBeanList.size() >0){
			dayBeanList.clear();
		}
		dayBean.setDay(null);
		mealBean.setTypeOfMeal(null);
		mealBeanList.clear();
		cuisinBean.setCuisinName(null);
		cuisinBeanList.clear();
		DayBean todayBean = new DayBean();
		todayBean.setDay("TODAY");
		
		DayBean tomorrowBean = new DayBean();
		tomorrowBean.setDay("TOMORROW");
		
		dayBeanList.add(todayBean);
		dayBeanList.add(tomorrowBean);
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectDayName(){
		if(mealBeanList.size() > 0){
			mealBeanList.clear();
		}
		mealBean.setTypeOfMeal(null);
		MealBean mealLunchBean = new MealBean();
		mealLunchBean.setTypeOfMeal("LUNCH");
		
		MealBean mealDinnerBean = new MealBean();
		mealDinnerBean.setTypeOfMeal("DINNER");

		mealBeanList.add(mealLunchBean);
		mealBeanList.add(mealDinnerBean);
		
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectTypeName(){
		if(cuisinBeanList.size() > 0){
			cuisinBeanList.clear();
		}
		
		cuisinBeanList = CreateOrderDAO.onloadCuisinList(connection);
	}
	
	@Command
	@NotifyChange("*")
	public void onClickCreate() throws JSONException{
		System.out.println("Api calling...");
		JSONObject callJson = callApi();
		try {
			if(callJson.get("status").equals("200")){
				
				/*JSONArray jsonArray = new JSONArray();
				jsonArray = callJson.getJSONArray("faqList");
				for(int i=0;i<jsonArray.length();i++){
					JSONObject jsonOb = jsonArray.getJSONObject(i);
					System.out.println(jsonOb.toString());
					System.out.println("ques: "+jsonOb.getString("faqQuestion"));
					FaqBean faqBean = new FaqBean();
					faqBean.setFaqQuestion(jsonOb.getString("faqQuestion"));
					faqBean.setFaqAnswer(jsonOb.getString("faqAnswer"));
					faqBeanList.add(faqBean);
				}*/
			}else{
				Messagebox.show("Calling failed");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public JSONObject callApi() throws JSONException{
		JSONObject jsonObject = new JSONObject();
		DefaultHttpClient client = new DefaultHttpClient();
		List<NameValuePair> urlParameters = createInputParam();
		String line = "";
		try {
   	    	String url ="http://appsquad.cloudapp.net:8080/RESTfulExample/rest/category/fetchcuisine";
   			HttpPost post = new HttpPost(url.trim());
   			/*List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
   			urlParameters.add(new BasicNameValuePair("pincode", "700156"));
   			urlParameters.add(new BasicNameValuePair("deliveryday", "TODAY"));*/
   			post.setEntity(new UrlEncodedFormEntity(urlParameters));

   			//StringEntity input = new StringEntity(shipMent.toString());
   			post.addHeader("Content-Type", "application/json");
   			
   			//post.setEntity(input);
   			HttpResponse response = client.execute(post);
   		      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));   
   		      while ((line = rd.readLine()) != null) {
   		    	 // System.out.println("Line - - >"+line);
   		    	jsonObject = new JSONObject(line);
   		      }
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println(jsonObject);
		return jsonObject;
	}

	public List<NameValuePair> createInputParam() throws JSONException{
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("pincode", "700156"));
			urlParameters.add(new BasicNameValuePair("deliveryday", "TODAY"));
		return urlParameters;
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

	public RoadRunnrBean getLoactionBean() {
		return loactionBean;
	}

	public void setLoactionBean(RoadRunnrBean loactionBean) {
		this.loactionBean = loactionBean;
	}

	public ArrayList<RoadRunnrBean> getLocationBeanList() {
		return locationBeanList;
	}

	public void setLocationBeanList(ArrayList<RoadRunnrBean> locationBeanList) {
		this.locationBeanList = locationBeanList;
	}

	public DayBean getDayBean() {
		return dayBean;
	}

	public void setDayBean(DayBean dayBean) {
		this.dayBean = dayBean;
	}

	public ArrayList<DayBean> getDayBeanList() {
		return dayBeanList;
	}

	public void setDayBeanList(ArrayList<DayBean> dayBeanList) {
		this.dayBeanList = dayBeanList;
	}

	public MealBean getMealBean() {
		return mealBean;
	}

	public void setMealBean(MealBean mealBean) {
		this.mealBean = mealBean;
	}

	public ArrayList<MealBean> getMealBeanList() {
		return mealBeanList;
	}

	public void setMealBeanList(ArrayList<MealBean> mealBeanList) {
		this.mealBeanList = mealBeanList;
	}

	public ManageCuisinBean getCuisinBean() {
		return cuisinBean;
	}

	public void setCuisinBean(ManageCuisinBean cuisinBean) {
		this.cuisinBean = cuisinBean;
	}

	public ArrayList<ManageCuisinBean> getCuisinBeanList() {
		return cuisinBeanList;
	}

	public void setCuisinBeanList(ArrayList<ManageCuisinBean> cuisinBeanList) {
		this.cuisinBeanList = cuisinBeanList;
	}
}
