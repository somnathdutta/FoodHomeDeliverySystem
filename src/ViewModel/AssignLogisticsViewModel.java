package ViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Pre;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

import Bean.AssignLogisticsBean;

public class AssignLogisticsViewModel {

	private AssignLogisticsBean assignLogisticsBean = new AssignLogisticsBean();
	
	private ArrayList<AssignLogisticsBean> assignLogisticsBeanList = new ArrayList<AssignLogisticsBean>();
	
	Session session = null;
	
	private Connection connection = null;
	
	private ArrayList<String> logisticsList = new ArrayList<String>();

	private String username;
	
	private Integer roleId;
	
	PropertyFile propertyfile = new PropertyFile();
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		connection=(Connection) session.getAttribute("sessionConnection");
		username = (String) session.getAttribute("login");
		roleId = (Integer) session.getAttribute("userRoleId");
		connection.setAutoCommit(true);
		
		fetchLogisticsList();
		
		onLoadQuery();
		reload();
	}
	
	@Command
	@NotifyChange("*")
	public void refresh(){
		onLoadQuery();
	}
	
	public void reload(){
		for(int i=0;i<1;i++){
			AssignLogisticsBean bean = new AssignLogisticsBean();
			//bean.logisticsList = loadLogistics();
			assignLogisticsBeanList.add(bean);
		}
	}
	
	
	public void fetchLogisticsList(){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sqlAdmin = "SELECT logistics_name FROM fapp_logistics ";
					String sqlOthers = "SELECT logistics_name FROM fapp_logistics "
						    + "WHERE kitchen_id=(SELECT kitchen_id FROM fapp_kitchen WHERE kitchen_name=?)";
					try {
						if(roleId==1){
							preparedStatement = connection.prepareStatement(sqlAdmin);
						}else{
							preparedStatement = connection.prepareStatement(sqlOthers);
							preparedStatement.setString(1, username);
						}
						
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							logisticsList.add(resultSet.getString(1));
						}
					}  catch (Exception e) {
						Messagebox.show("ERROR DUE TO ::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
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
	
	
	
	public void onLoadQuery(){
		if(assignLogisticsBeanList.size()>0){
			assignLogisticsBeanList.clear();
		}
		
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					//String sqlAdmin = "SELECT * FROM vw_assign_logistics" ;
					String sqlAdmin = "SELECT * FROM vw_kitchen_received_order_list";
					//String sqlOthers = "SELECT * FROM vw_assign_logistics WHERE kitchen_id=(SELECT kitchen_id FROM fapp_kitchen WHERE kitchen_name=?)" ;
					String sqlOthers = "SELECT * FROM vw_kitchen_received_order_list WHERE kitchen_id=(SELECT kitchen_id FROM fapp_kitchen WHERE kitchen_name=?)";
					try {
						if(roleId==1){
							preparedStatement = connection.prepareStatement(sqlAdmin);
						}else{
							preparedStatement = connection.prepareStatement(sqlOthers);
							preparedStatement.setString(1, username);
						}
						
						resultSet = preparedStatement.executeQuery();
						String orderno = null ;
						while (resultSet.next()) {
							AssignLogisticsBean assignLogisticsbean = new AssignLogisticsBean();
							assignLogisticsbean.orderId = resultSet.getInt("order_id");
							assignLogisticsbean.orderNo =  resultSet.getString("order_no");
							String tempOrderNo = assignLogisticsbean.orderNo;
							if(assignLogisticsbean.orderNo.equals(orderno)){
								assignLogisticsbean.orderNo = null;
								assignLogisticsbean.orderIdVisibility = false;
							}
							assignLogisticsbean.orderNo = resultSet.getString("order_no");
							assignLogisticsbean.orderBy = resultSet.getString("order_by");
							assignLogisticsbean.cuisineName = resultSet.getString("cuisin_name");
							assignLogisticsbean.categoryName = resultSet.getString("category_name");
							assignLogisticsbean.quantity = resultSet.getInt("qty");
							assignLogisticsbean.price = resultSet.getDouble("total_price");
							assignLogisticsbean.status = resultSet.getString("order_status_name");
							assignLogisticsbean.contactNumber = resultSet.getString("contact_number");
							assignLogisticsbean.emailId = resultSet.getString("user_mail_id");
							String flatNo = resultSet.getString("flat_no");
							String streetName = resultSet.getString("street_name");
							String landmark = resultSet.getString("landmark");
							String deliveryLocation = resultSet.getString("delivery_location");
							String city = resultSet.getString("city");
							String pincode = resultSet.getString("pincode");
							assignLogisticsbean.deliveryAddress = flatNo+", "+streetName+", "+landmark+", "+deliveryLocation+", "+city+", "+pincode;	
							assignLogisticsbean.orderedDate = resultSet.getDate("order_date");
							assignLogisticsbean.orderStatus = resultSet.getString("order_status_name");
							orderno =  tempOrderNo;
							assignLogisticsBeanList.add(assignLogisticsbean);
						}
					} catch (Exception e) {
						Messagebox.show("ERROR DUE TO ::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
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
	public void onSelectLogistics(@BindingParam("bean") AssignLogisticsBean logisticsBean){
		try {
			SQL:{
				PreparedStatement preparedStatement = null;
				ResultSet resultSet = null;
				String sql ="SELECT logistics_id FROM fapp_logistics WHERE logistics_name=?";
				try {
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, logisticsBean.logisticsName);
					resultSet = preparedStatement.executeQuery();
					while (resultSet.next()) {
						logisticsBean.logisticsId = resultSet.getInt("logistics_id");
						System.out.println("Logistics id--"+logisticsBean.logisticsId);
					}
				} catch (Exception e) {
					Messagebox.show("ERROR DUE TO ::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					e.printStackTrace();
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
	public void onClickNotifyOrder(@BindingParam("bean") AssignLogisticsBean assignlogisticsbean) throws ParseException, org.json.simple.parser.ParseException, JSONException{
		Boolean orderNotified = false;
		JSONObject responseJsonObject = new JSONObject();
		if(assignlogisticsbean.orderId!=null){
			//if(logisticsBean.logisticsId!=null){
				/*try {
		    		System.out.println("onassign logistics::"+logisticsBean.logisticsId+" ord id::"+logisticsBean.orderId);
					SQL:{
		    				PreparedStatement preparedStatement= null;
		    				String sql = "UPDATE fapp_orders SET order_status_id=3 WHERE  order_id=?";
		    				try {
								preparedStatement =connection.prepareStatement(sql);
								preparedStatement.setInt(1, logisticsBean.orderId);
								int count = preparedStatement.executeUpdate();
		    					if(count > 0){
		    						System.out.println("logistics "+logisticsBean.logisticsId+" is assigned to order id:"+logisticsBean.orderId);
		    						//assignLogistic(logisticsBean);
		    						Messagebox.show("Logistics notified!","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
		    						onLoadQuery();
		    					}
							} catch (Exception e) {
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
			
			 orderNotified = false;
			
			if(roleId!=1){
				orderNotified = notifyOrder(assignlogisticsbean.orderNo, username);
				onLoadQuery();
				if(isAllKitchenNotified(assignlogisticsbean.orderNo)){
					onLoadQuery();
					if(isOrderNotified(assignlogisticsbean.orderNo)){
						Messagebox.show("Logistics Notified !");
						onLoadQuery();
					}else{
						Messagebox.show("Logistics Notified failed!");
					}
					
				}
			}
			}else{
				Messagebox.show("Logistics required!","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
			}*/
			responseJsonObject  = createShipmentByPost( createRoadRunnrJsonForAPI(assignlogisticsbean.orderNo, username) );
			System.out.println("Response json- - - > > "+responseJsonObject.toString());
			JSONObject status = responseJsonObject.getJSONObject("status");
			String code = status.getString("code");
			if(code.equals("706")){
				Messagebox.show(status.toString(),"Information",Messagebox.OK,Messagebox.INFORMATION);
			}else if(code.equals("200")){
				System.out.println("200 status code");
				/*String externalOrderID = responseJsonObject.getString("order_id");
				if( saveExterOrderId(externalOrderID, orderNo, kitchenName) ){
					System.out.println("External order id "+externalOrderID+" is saved!");
				}*/
				orderNotified = notifyOrder(assignlogisticsbean.orderNo, username);
				if(isAllKitchenNotified(assignlogisticsbean.orderNo)){
					if( orderNotified(assignlogisticsbean.orderNo) ){
						Messagebox.show("Notified successfully!","Information",Messagebox.OK,Messagebox.INFORMATION);
						onLoadQuery();
					}
				}
			//	notifiedJsonObject.put("status", responseJsonObject);
			}else{
				Messagebox.show(status.toString(),"Information",Messagebox.OK,Messagebox.INFORMATION);
			}
		}else{
			Messagebox.show("Data not available!","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private org.json.simple.JSONObject createRoadRunnrJsonForAPI(String orderNo , String kitchenName) throws JSONException{
		JSONObject kitchenDetails = new JSONObject();
		JSONObject dropDetailsJson = new JSONObject();
		JSONObject orderDetailsJson = new JSONObject();
		String createdDate = "";
		try {
			SQLKitchenPICKUP:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select * from vw_kitchens_details where kitchen_name = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, kitchenName);
						resultSet = preparedStatement.executeQuery();
						if (resultSet.next()) {
							//kitchenDetails.put("name", resultSet.getString("kitchen_name") );
							kitchenDetails.put("name", "EazeLyf" );
							kitchenDetails.put("phone_no", resultSet.getString("mobile_no") );
							kitchenDetails.put("email", resultSet.getString("email_id"));
							kitchenDetails.put("type", "merchant");
							kitchenDetails.put("external_id", orderNo);
							JSONObject geoJsonValue = new JSONObject();
							geoJsonValue.put("latitude", resultSet.getString("latitude"));
							geoJsonValue.put("longitude", resultSet.getString("longitude"));
							//geoJsonValue.put("latitude", "12.935322");
							//geoJsonValue.put("longitude", "77.618754");
							JSONObject cityJsonValue = new JSONObject();
							cityJsonValue.put("name", toCamelCase(resultSet.getString("city_name")));
							//cityJsonValue.put("name", "BANGALORE");//HARD CODED NEED TO BE CHANGE
							JSONObject localityJsonValue = new JSONObject();
							localityJsonValue.put("name", toCamelCase(resultSet.getString("area_name")));
							//localityJsonValue.put("name", "KORAMANGALA");//HARD CODED NEED TO BE CHANGE
							JSONObject sublocalityJsonValue = new JSONObject();
							//sublocalityJsonValue.put("name", "8TqwewqH sMAIN 3RD CR3OSS NEAR POST OFFICE SARASWATHIPURAM MYSORE");
							sublocalityJsonValue.put("name", resultSet.getString("address"));
							JSONObject fulladdress = new JSONObject();
							fulladdress.put("address", resultSet.getString("address"));
							//fulladdress.put("address","8TqwewqH sMAIN 3RD CR3OSS NEAR POST OFFICE SARASWATHIPURAM MYSORE");
							//fulladdress.put("locality", localityJsonValue);
							fulladdress.put("city", cityJsonValue);
							//fulladdress.put("sub_locality", sublocalityJsonValue);
							fulladdress.put("geo", geoJsonValue);
							
							kitchenDetails.put("full_address",fulladdress);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
			}
			
			SQLUserDROP:{
				PreparedStatement preparedStatement = null;
				ResultSet resultSet = null;
				String sql = "select * from vw_orders_delivery_address where order_no = ?";
				try {
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, orderNo);
					resultSet = preparedStatement.executeQuery();
					if (resultSet.next()) {
						dropDetailsJson.put("name", resultSet.getString("order_by") );
						dropDetailsJson.put("phone_no", resultSet.getString("contact_number") );
						dropDetailsJson.put("email", resultSet.getString("user_mail_id"));
						dropDetailsJson.put("type", "customer");
						dropDetailsJson.put("external_id", resultSet.getString("user_details_id"));
				//		String[] latlong = new String[2];
					//		latlong =	getLatLongPositions(resultSet.getString("delivery_address")+","+resultSet.getString("pincode")+","+resultSet.getString("city"));
				//		JSONObject geoJsonValue = new JSONObject();
				//		geoJsonValue.put("latitude", latlong[0]);
				//		geoJsonValue.put("longitude", latlong[1]);
						/*geoJsonValue.put("latitude", "12.935322");
						geoJsonValue.put("longitude", "77.618754");*/
						JSONObject cityJsonValue = new JSONObject();
						//String cityName = resultSet.getString("city");
						//cityJsonValue.put("name", resultSet.getString("city"));
						cityJsonValue.put("name", "Kolkata");//HARD CODED NEED TO BE CHANGE
						//cityJsonValue.put("name", "BANGALORE");
						JSONObject localityJsonValue = new JSONObject();
						if( resultSet.getString("delivery_zone").equalsIgnoreCase("New Town")){
							localityJsonValue.put("name", "New Town(kolkata)");
						}else if(resultSet.getString("delivery_zone").equalsIgnoreCase("Salt Lake")){
							localityJsonValue.put("name", "Salt Lake City");
						}else{
							localityJsonValue.put("name",toCamelCase(resultSet.getString("delivery_zone")) );
						}
						
						//localityJsonValue.put("name", "Kora mangala");//HARD CODED NEED TO BE CHANGE
						JSONObject sublocalityJsonValue = new JSONObject();
						sublocalityJsonValue.put("name", resultSet.getString("delivery_address"));
						//sublocalityJsonValue.put("name", "1st Block");
						JSONObject fulladdress = new JSONObject();
						fulladdress.put("address", resultSet.getString("delivery_address"));
						//fulladdress.put("address","345,221st street,kormangala");
						fulladdress.put("locality", localityJsonValue);
						//fulladdress.put("sub_locality", sublocalityJsonValue);
						fulladdress.put("city", cityJsonValue);
						//fulladdress.put("geo", geoJsonValue);
						dropDetailsJson.put("full_address",fulladdress);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					if(preparedStatement!=null){
						preparedStatement.close();
					}
				}
			}
			SQLorderDetails:{
				PreparedStatement preparedStatement = null;
				ResultSet resultSet = null;
				String sql = "select order_no,sum(total_price)AS total_price,order_date from vw_order_item_details_list "
						+ "where order_no =? and kitchen_name = ? group by order_no,order_date ";
				try {
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, orderNo);
					preparedStatement.setString(2, kitchenName);
					resultSet = preparedStatement.executeQuery();
					if (resultSet.next()) {
						orderDetailsJson.put("order_id", resultSet.getString("order_no") );
						orderDetailsJson.put("order_value", resultSet.getString("total_price") );
						orderDetailsJson.put("amount_to_be_collected", resultSet.getString("total_price") );
						orderDetailsJson.put("amount_to_be_paid", resultSet.getString("total_price") );
						orderDetailsJson.put("note","Fragile");
						JSONObject ordertypejson = new JSONObject();
						ordertypejson.put("name", "CashOnDelivery");
						//orderDetailsJson.put("order_type", resultSet.getString("payment_name"));
						orderDetailsJson.put("order_type", ordertypejson);
						createdDate =  resultSet.getString("order_date");
						//orderDetailsJson.put("order_items", getOrderitemdetails(orderNo));
						orderDetailsJson.put("order_items", getOrderitemdetailsWithKitchen(orderNo, kitchenName));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					if(preparedStatement!=null){
						preparedStatement.close();
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		org.json.simple.JSONObject shipmentJSON = new org.json.simple.JSONObject();
		JSONObject pickupjson = new JSONObject();
		pickupjson.put("user", kitchenDetails);
		JSONObject dropjson = new JSONObject();
		dropjson.put("user", dropDetailsJson);
		shipmentJSON.put("pickup", pickupjson);
		shipmentJSON.put("drop", dropjson);
		shipmentJSON.put("order_details", orderDetailsJson);
		shipmentJSON.put("created_at",createdDate);
		shipmentJSON.put("callback_url","http://appsquad.cloudapp.net:8080/RESTfulExample/rest/category/trackstatus?id="+kitchenName+"&orderno="+orderNo);
		System.out.println("CALL BACK URL-"+shipmentJSON.get("callback_url"));
		System.out.println("Shipment - "+shipmentJSON.toString());
		return shipmentJSON;
	}
	
	public JSONObject getItemName(Integer categoryId){
    	//JSONObject categoryName = new JSONObject();
    	PreparedStatement preparedStatement = null;
    	ResultSet resultSet = null;
    	JSONObject itemName =new JSONObject();
    	try {
			SQL:{
    			String sql = "SELECT category_name FROM food_category WHERE category_id=?";
    				try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, categoryId);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							itemName.put("name", resultSet.getString("category_name"));
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}	
    		}
		} catch (Exception e) {
			
		}	
    	return itemName;
    }
	
	 public JSONArray getOrderitemdetailsWithKitchen(String orderNo, String kitchenName){
	    	JSONArray itemsDetailArray = new JSONArray();
	    	PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			try {
			
				SQL:{
						String cuisineSql="select category_id , qty, category_price " 
							  +" from fapp_order_item_details "
							  +" where order_id = "
							  +"(SELECT order_id FROM fapp_orders WHERE order_no = ?)"
							  +" and kitchen_id = "
							  +" (SELECT kitchen_id FROM fapp_kitchen WHERE  kitchen_name = ? )";
					try {
							preparedStatement = connection.prepareStatement(cuisineSql);
							preparedStatement.setString(1, orderNo);
							preparedStatement.setString(2, kitchenName);
							resultSet = preparedStatement.executeQuery();
							while (resultSet.next()) {
								JSONObject itemobject = new JSONObject();
								itemobject.put("quantity", resultSet.getInt("qty"));
								itemobject.put("price", resultSet.getDouble("category_price"));
								itemobject.put("item", getItemName(resultSet.getInt("category_id")));
								itemsDetailArray.put(itemobject);
							}
					} catch (Exception e) {
						e.printStackTrace();
					} finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}		
				}
			} catch (Exception e) {
			
			}
	    	return itemsDetailArray;
	    }
	
	
	public String[] getLatLongPositions(String address) throws Exception
    {
      int responseCode = 0;
      String api = "http://maps.googleapis.com/maps/api/geocode/xml?address=" + URLEncoder.encode(address, "UTF-8") + "&sensor=true";
      URL url = new URL(api);
      HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
      httpConnection.connect();
      responseCode = httpConnection.getResponseCode();
      if(responseCode == 200)
      {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();;
        Document document = builder.parse(httpConnection.getInputStream());
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr = xpath.compile("/GeocodeResponse/status");
        String status = (String)expr.evaluate(document, XPathConstants.STRING);
        if(status.equals("OK"))
        {
           expr = xpath.compile("//geometry/location/lat");
           String latitude = (String)expr.evaluate(document, XPathConstants.STRING);
           expr = xpath.compile("//geometry/location/lng");
           String longitude = (String)expr.evaluate(document, XPathConstants.STRING);
           return new String[] {latitude, longitude};
        }
        else
        {
        	 System.out.println(status+" from given address!");
          // throw new Exception("Error from the API - response status: "+status);
        }
      }
      return null;
    }
	
	
	private JSONObject createShipmentByPost(org.json.simple.JSONObject shipMent) throws ParseException, org.json.simple.parser.ParseException{
		JSONObject jObject = new JSONObject();
		//String ship = shipMent.toJSONString();
		String line = "";
	    JSONParser parser = new JSONParser();
   	    JSONObject newJObject = null;
   	    System.out.println("shipment object-->"+shipMent.toJSONString());
		try{
			DefaultHttpClient client = new DefaultHttpClient();
			/* TESTING URL */
			//String url="https://apitest.roadrunnr.in/v1/orders/ship";
			/* LIVE URL */
			String url="http://roadrunnr.in/v1/orders/ship";
			HttpPost post = new HttpPost(url);
			StringEntity input = new StringEntity(shipMent.toJSONString());
			post.addHeader("Content-Type", "application/json");
			post.addHeader("Authorization" , generateAuthToken());
			//post.addHeader("Authorization" , "Token T9T7JGvdrVKf9S00PJSPi9cYxIWvZPpitlowV5PA");
			post.setEntity(input);
			System.out.println("StringEntity - - ->"+input);
			HttpResponse response = client.execute(post);
		      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));   
		      while ((line = rd.readLine()) != null) {
		    	  System.out.println("Line - - >"+line);
		    	  
			  		/*newJObject = (JSONObject) parser.parse(line);*/
		    	  newJObject = new JSONObject(line);
		      }
		}catch(UnsupportedEncodingException e){
			
		}catch(JSONException e){
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newJObject;  
	}
	
	public  String generateAuthToken() throws org.json.simple.parser.ParseException, JSONException{
		String authToken = "";
		//String[] apiDetails = getApiDetails();
		String[] apiDetails= new String[2];
		//apiDetails[0]="kkwJrVg65WdIRjMT6c6v7kRXHtwK8xHqCIlgpBLb";
		//apiDetails[1]="vwLw3bNHTMe9hTy0LxSjiHt9bSRkq81eTfyzIzXu";
		apiDetails = getApiDetails();
		try {
			  /*URL url = new URL(" https://apitest.roadrunnr.in/oauth/token?grant_type=client_credentials&client_"
				 +"id="+apiDetails[0]+"&client_secret="+apiDetails[1]);
			URL url = new URL(" https://apitest.roadrunnr.in/oauth/token?grant_type=client_credentials&client_"
					 +"id=kkwJrVg65WdIRjMT6c6v7kRXHtwK8xHqCIlgpBLb&client_secret=vwLw3bNHTMe9hTy0LxSjiHt9bSRkq81eTfyzIzXu");*/
			/* LIVE URL */
			//10j30V08qQFfc2E1DD8ujSTPmSXSEOzrft9VmyoS  yBKKMvMxHU4HrehbtkoOxRJkDnGo20CcCe8dFmwQ
			String urlstr ="http://roadrunnr.in/oauth/token?grant_type="
					+"client_credentials&client_id="+apiDetails[0]+"&client_secret="+apiDetails[1];
			/*URL url = new URL("http://roadrunnr.in/oauth/token?grant_type="
					+"client_credentials&client_id="+apiDetails[0]+"&client_secret="+apiDetails[1]);*/
			URL url = new URL(urlstr.trim());
			 // URL url = new URL(url.trim());
			  System.out.println("URL for token->"+url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}
			String output ;
			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
				org.json.simple.JSONObject json = (org.json.simple.JSONObject)new JSONParser().parse(output);
				String token = (String) json.get("access_token");
				authToken = "Token "+token ; 	
			}
		   conn.disconnect();
		  } catch (MalformedURLException e) {
			e.printStackTrace();
		  } catch (IOException e) {
			e.printStackTrace();
		  }
		System.out.println("access_token->"+authToken);
		return authToken;
	}
	
	private String[] getApiDetails(){
		String[] apiDetails = new String[2];
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT client_id , client_secret from fapp_api_details";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							apiDetails[0] = resultSet.getString("client_id") ; 
							apiDetails[1] = resultSet.getString("client_secret");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("API DETILS->"+apiDetails[0]+" "+apiDetails[1]);
		return apiDetails;
	}
	/**
	 * Receive order b kitchen
	 * @param neworderbean
	 * @return parent call from 199
	 */
	private Boolean notifyOrder(String orderNo , String kitchenName){
		Boolean notified = false;
		try {
			SQL:{
				
				PreparedStatement preparedStatement = null;
				String sql = "UPDATE fapp_order_tracking SET notify = 'Y',notified_time=current_timestamp "
						   + " WHERE order_id = (SELECT order_id from fapp_orders where order_no = ?) "
						   + " AND kitchen_id = (SELECT kitchen_id from fapp_kitchen where kitchen_name = ?)";
				
				try {
				preparedStatement =  connection.prepareStatement(sql);
				preparedStatement.setString(1, orderNo);
				preparedStatement.setString(2, kitchenName);
				int updatedRow = preparedStatement.executeUpdate();
				if(updatedRow>0){
					notified =  true;
					System.out.println("1. Notifed !!");
				}
				} catch (Exception e) {
					System.out.println(e);
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
				}
		
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "UPDATE fapp_orders SET order_status_id = 3"
							   + " WHERE order_id = (SELECT order_id from fapp_orders where order_no = ?) ";
					try {
					preparedStatement =  connection.prepareStatement(sql);
					preparedStatement.setString(1, orderNo);
					int updatedRow = preparedStatement.executeUpdate();
					if(updatedRow>0){
						notified =  true;
						System.out.println("2. order status change!!");
					}
					} catch (Exception e) {
						System.out.println(e);
						}finally{
							if(preparedStatement!=null){
								preparedStatement.close();
							}
						}
					}	
		} catch (Exception e) {
			// TODO: handle exception
		}
		return notified;
	}
	
	/**
	 * Check if all kicthen accepts order or not
	 * @param orderid
	 * @return parent call from 203
	 */
	private Boolean isAllKitchenNotified(String orderNo){
		Integer totalOrders = 0,totalNotifiedOrders = 0 ;
		
		try {
			SQL:{  				 
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					
					String sql = "SELECT "
							 	+" count(ORDER_ID)AS total_order "
								+" from fapp_order_tracking "
								+" where ORDER_ID = (SELECT order_id from fapp_orders where order_no = ?)";
				 try {
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, orderNo);
					resultSet = preparedStatement.executeQuery();
					if (resultSet.next()) {
						totalOrders = resultSet.getInt("total_order");
					}
				} catch (Exception e) {
					// TODO: handle exceptio
					e.printStackTrace();
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
				 System.out.println("total orders-->"+totalOrders);
			}
			
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					
					String sql = "SELECT "
						 	+" count(notify)AS total_order_notified "
							+" from fapp_order_tracking "
							+" where ORDER_ID = (SELECT order_id from fapp_orders where order_no = ?)"
							+ " AND notify = 'Y'";
					 try {
							preparedStatement =  connection.prepareStatement(sql);
							preparedStatement.setString(1, orderNo);
							resultSet = preparedStatement.executeQuery();
							if (resultSet.next()) {
								totalNotifiedOrders = resultSet.getInt("total_order_notified");
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
							}finally{
								if(preparedStatement!=null){
									preparedStatement.close();
								}
								if(preparedStatement!=null){
									preparedStatement.close();
								}
							}
					 System.out.println("Total notified orders::"+totalNotifiedOrders);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		if( totalOrders!=0 && totalNotifiedOrders!=0 && totalOrders == totalNotifiedOrders){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Receive the order
	 * @param neworderbean parent call from 205
	 */
	private Boolean isOrderNotified(String orderNo){
		Boolean notifiedOrder = false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "UPDATE fapp_orders SET order_status_id=9 WHERE order_id="
							+ " (SELECT order_id from fapp_orders where order_no = ?)";
				try {
					preparedStatement =  connection.prepareStatement(sql);
					preparedStatement.setString(1, orderNo);
					int updatedRow = preparedStatement.executeUpdate();
					if(updatedRow>0){
					//	Messagebox.show("Order Received Successfully!");
						notifiedOrder = true;
						////System.out.println("Device regid::"+ getDeviceRegId(orderNo) );
						//sendMessage(getDeviceRegId(orderNo),orderNo,2);
						System.out.println("3. Final order status!!");
					}
				} catch (Exception e) {
					System.out.println(e);
				}finally{
					if(preparedStatement!=null){
						preparedStatement.close();
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return notifiedOrder;
	}
	
	
	public void assignLogistic(AssignLogisticsBean logisticsBean){
		try {
			SQL:{
    				PreparedStatement preparedStatement= null;
    				String sql = "UPDATE fapp_order_tracking SET logistics_id=? WHERE rejected='N' AND order_id=?"
    						    +"AND kitchen_id=(SELECT kitchen_id FROM fapp_kitchen WHERE kitchen_name=?)";
    				try {
						preparedStatement =connection.prepareStatement(sql);
						preparedStatement.setInt(1, logisticsBean.logisticsId);
						preparedStatement.setInt(2, logisticsBean.orderId);
						preparedStatement.setString(3, username);
    					int count = preparedStatement.executeUpdate();
    					if(count > 0){
    						Messagebox.show("Logistics Assigned!","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
    						sendMessage(getDeviceRegId(logisticsBean.orderId));
    					}
					} catch (Exception e) {
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
	
	public String getDeviceRegId(Integer orderId ){
		String deviceregId = "";
		try {
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			SQL:{
					String sql = "SELECT device_reg_id FROM fapp_devices"
								+" WHERE email_id = (SELECT user_mail_id FROM fapp_orders"
								+ " WHERE order_id = ? )";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, orderId);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							deviceregId = resultSet.getString("device_reg_id");
						}
					} catch (Exception e) {
						Messagebox.show("ERROR DUE TO ::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
					}finally{
						if(preparedStatement !=null){
							preparedStatement.close();
						}
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return deviceregId;
	}
	
	public static void sendMessage(String deviceregId) throws IOException
	{
		System.out.println("Reg id::"+deviceregId);
		//String API_KEY = "AIzaSyCvaJt0tbW3Nn1pCPynPduVxo3T3l3_Yek"; //sender id got from google api console project(My)
		String API_KEY = "AIzaSyA03muwGMLqGmk2mwY3x1di5mI3jEVViqM";//(sir)
		String collpaseKey = "gcm_message"; //if messages are sent and not delivered yet to android device (as device might not be online), then only deliver latest message when device is online
		//String messageStr = "message content here"; //actual message content
		System.out.println("Reg id::"+deviceregId);
		String messageStr = "Order send to logistics!";
		//String messageId = "APA91bGgGzVQWb88wkRkACGmHJROeJSyQbzLvh3GgP2CASK_NBsuIXH15HcnMta3e9ZXMhdPN6Z3FSD2Pezf6bhgUuM2CF74SgZbG4Zr57LA76VVaNvSi7XM7QEuAVLIiTsXnVq3QAUFDo-ynD316bF10JGT3ZOaSQ"; //gcm registration id of android device
		String messageId = deviceregId;
				
		Sender sender = new Sender(API_KEY);
		Message.Builder builder = new Message.Builder();
		
		builder.collapseKey(collpaseKey);
		builder.timeToLive(30);
		builder.delayWhileIdle(true);
		builder.addData("message", messageStr);
		
		Message message = builder.build();
		
		List<String> androidTargets = new ArrayList<String>();
		//if multiple messages needs to be deliver then add more message ids to this list
		androidTargets.add(messageId);
		
		MulticastResult result = sender.send(message, androidTargets, 1);
		System.out.println("result = "+result);
		
		if (result.getResults() != null) 
		{
			System.out.println("Status:"+messageStr+" is sent to device reg id:"+messageId);
			int canonicalRegId = result.getCanonicalIds();
			System.out.println("canonicalRegId = "+canonicalRegId);
			
			if (canonicalRegId != 0) 
			{
            }
		}
		else 
		{
			int error = result.getFailure();
			System.out.println("Broadcast failure: " + error);
		}
	}
	/**
	 * Receive the order
	 * @param neworderbean parent call from 205
	 */
	private Boolean orderNotified(String orderNo){
		Boolean notifiedOrder = false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "UPDATE fapp_orders SET order_status_id=9 WHERE order_id="
							+ " (SELECT order_id from fapp_orders where order_no = ?)";
				try {
					preparedStatement =  connection.prepareStatement(sql);
					preparedStatement.setString(1, orderNo);
					int updatedRow = preparedStatement.executeUpdate();
					if(updatedRow>0){
					//	Messagebox.show("Order Received Successfully!");
						notifiedOrder = true;
						////System.out.println("Device regid::"+ getDeviceRegId(orderNo) );
						//sendMessage(getDeviceRegId(orderNo),orderNo,2);
						System.out.println("3. Final order status!!");
					}
				} catch (Exception e) {
					System.out.println(e);
				}finally{
					if(preparedStatement!=null){
						preparedStatement.close();
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return notifiedOrder;
	}
	
	public static String toCamelCase(String inputString) {
	       String result = "";
	       if (inputString.length() == 0) {
	           return result;
	       }
	       char firstChar = inputString.charAt(0);
	       char firstCharToUpperCase = Character.toUpperCase(firstChar);
	       result = result + firstCharToUpperCase;
	       for (int i = 1; i < inputString.length(); i++) {
	           char currentChar = inputString.charAt(i);
	           char previousChar = inputString.charAt(i - 1);
	           if (previousChar == ' ') {
	               char currentCharToUpperCase = Character.toUpperCase(currentChar);
	               result = result + currentCharToUpperCase;
	           } else {
	               char currentCharToLowerCase = Character.toLowerCase(currentChar);
	               result = result + currentCharToLowerCase;
	           }
	       }
	       return result;
	   }
	
	public AssignLogisticsBean getAssignLogisticsBean() {
		return assignLogisticsBean;
	}

	public void setAssignLogisticsBean(AssignLogisticsBean assignLogisticsBean) {
		this.assignLogisticsBean = assignLogisticsBean;
	}

	public ArrayList<AssignLogisticsBean> getAssignLogisticsBeanList() {
		return assignLogisticsBeanList;
	}

	public void setAssignLogisticsBeanList(
			ArrayList<AssignLogisticsBean> assignLogisticsBeanList) {
		this.assignLogisticsBeanList = assignLogisticsBeanList;
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

	public PropertyFile getPropertyfile() {
		return propertyfile;
	}

	public void setPropertyfile(PropertyFile propertyfile) {
		this.propertyfile = propertyfile;
	}

	public ArrayList<String> getLogisticsList() {
		return logisticsList;
	}

	public void setLogisticsList(ArrayList<String> logisticsList) {
		this.logisticsList = logisticsList;
	}
	
}
