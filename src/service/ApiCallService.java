package service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import Bean.Item;
import Bean.ManageCategoryBean;
import Bean.ManageCuisinBean;
import Bean.RoadRunnrBean;

public class ApiCallService {

	public static ArrayList<ManageCuisinBean> getCuisineList(String pincode, String deliveryDay) throws JSONException{
		ArrayList<ManageCuisinBean> cuisineList = new ArrayList<ManageCuisinBean>();
		JSONObject fetchCuisineJSon = callFetchCuisineApi(pincode, deliveryDay);
		if(fetchCuisineJSon.getString("status").equalsIgnoreCase("200")){
			JSONArray cuisinejsonArray = fetchCuisineJSon.getJSONArray("cuisinelist");
			for(int i=0;i<cuisinejsonArray.length();i++){
				JSONObject cuisineJson = cuisinejsonArray.getJSONObject(i);
				ManageCuisinBean cuisinBean = new ManageCuisinBean();
				cuisinBean.cuisinId = cuisineJson.getInt("cuisineid");
				cuisinBean.cuisinName = cuisineJson.getString("cuisinename");
				
				ArrayList<ManageCategoryBean> categoryBeanList = new ArrayList<ManageCategoryBean>();
				JSONArray categoryJsonArray = cuisineJson.getJSONArray("categorylist");
				for(int j = 0 ; i < categoryJsonArray.length() ; i++){
					JSONObject categoryJson = categoryJsonArray.getJSONObject(j);
					ManageCategoryBean categoryBean = new ManageCategoryBean();
					categoryBean.categoryId = Integer.valueOf(categoryJson.getString("categoryid"));
					categoryBean.categoryName = categoryJson.getString("categoryname");
					
					
					ArrayList<Item> itemList = new ArrayList<Item>();
					JSONArray itemJsonArray = categoryJson.getJSONArray("itemlist");
					for(int k = 0; k < itemJsonArray.length() ; i++){
						JSONObject itemJson = itemJsonArray.getJSONObject(k);
						Item item = new Item();
						item.setItemCode(itemJson.getString("itemcode"));
						item.setCuisineId(Integer.valueOf(itemJson.getString("cuisineid")));
						item.setCategoryId(Integer.valueOf(itemJson.getString("categoryid")));
						item.setItemDescription(itemJson.getString("categorydescription"));
						item.setCategoryName(itemJson.getString("categoryname"));
						item.setLunchStock(Integer.valueOf(itemJson.getString("stock")));
						item.setDinnerStock(Integer.valueOf(itemJson.getString("dinnerstock")));
						item.setPrice(itemJson.getDouble("categoryprice"));
						item.setBikerAvailabilityForLunch(itemJson.getBoolean("availableBikerForLunch"));
						item.setBikerAvailabilityForDinner(itemJson.getBoolean("availableBikerForDinner"));
						itemList.add(item);
					}
					System.out.println("Item list size:: "+itemList.size());
					categoryBean.setItemBeanList(itemList);
					categoryBeanList.add(categoryBean);
					System.out.println("Category list size:: "+categoryBeanList.size());
					cuisinBean.setCategoryBeanList(categoryBeanList);
				}
				cuisineList.add(cuisinBean);
			}
		}
		
		return cuisineList;
	}
	
	public static JSONObject callFetchCuisineApi(String pincode, String deliveryDay) throws JSONException{
		JSONObject jsonObject = new JSONObject();
		DefaultHttpClient client = new DefaultHttpClient();
		List<NameValuePair> urlParameters = setInputParamForFetchCuisine(pincode, deliveryDay);
		String line = "";
		try {
   	    	String url ="http://appsquad.cloudapp.net:8080/RESTfulExample/rest/category/fetchcuisine";
   			HttpPost post = new HttpPost(url.trim());
   			System.out.println("Api calling...");
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
		return jsonObject;
	}
	
	public static List<NameValuePair> setInputParamForFetchCuisine(String pincode, String deliveryDay) throws JSONException{
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("pincode", pincode));
			urlParameters.add(new BasicNameValuePair("deliveryday", deliveryDay));
		return urlParameters;
	}
}
