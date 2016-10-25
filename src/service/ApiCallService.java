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
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import Bean.RoadRunnrBean;

public class ApiCallService {

	public ArrayList<RoadRunnrBean> fetchLocationList(){
		ArrayList<RoadRunnrBean> locationList = new ArrayList<RoadRunnrBean>();
		
		return locationList;
	}
	
	public JSONObject callApi() throws JSONException{
		JSONObject jsonObject = new JSONObject();
		DefaultHttpClient client = new DefaultHttpClient();
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
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
}
