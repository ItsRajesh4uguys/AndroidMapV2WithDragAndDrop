package com.rajesh.android.mapsv2.draganddrop;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class FormattedAddress {

	public  String getAddress(String Source, String Destination) {

		
		
		StringBuilder stringBuilder = new StringBuilder();
		String duration_final_value = "";
		
		try {

			Source = Source.replaceAll(" ", "%20");
			Destination = Destination.replaceAll(" ", "%20");

			HttpPost httppost = new HttpPost(
					"http://maps.googleapis.com/maps/api/geocode/json?latlng="+Source+","+Destination+"&sensor=false");//&mode=walking,driving
			HttpClient client = new DefaultHttpClient();
			HttpResponse response;
			stringBuilder = new StringBuilder();

			response = client.execute(httppost);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				stringBuilder.append((char) b);
			}
		} catch (ClientProtocolException e) {          
		} catch (IOException e) {
		}

		
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(stringBuilder.toString()); // first we need to get the full json object which google returns	
			
			JSONArray routes_Array = jsonObject.getJSONArray("results"); // in that first we need to go into the routes array
			
			JSONObject routes_object = (JSONObject) routes_Array.get(0); // in that we need first item 
			duration_final_value= routes_object.getString("formatted_address").toString();  // then go into the legs array
			
			System.out.println(" Address " + duration_final_value);
				

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return duration_final_value;
	}
}
