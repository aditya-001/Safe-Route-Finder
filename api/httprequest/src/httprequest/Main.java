package httprequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
public class Main {

	private static HttpURLConnection connection;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		BufferedReader reader;
		String line;
		StringBuffer responsecontent=new StringBuffer();
		try {
			URL url=new URL("https://maps.googleapis.com/maps/api/directions/json?origin=JHALWA&destination=Civil-Lines-Allahbad&alternatives=true&key=AIzaSyAwdxRlmAXwjm_mcFQnM-f-vguZr6JkxV8");
			 
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("172.31.2.4", 8080));
					Authenticator authenticator = new Authenticator() {

					    public PasswordAuthentication getPasswordAuthentication() {
						return (new PasswordAuthentication("iim2017001", "sd.g8904".toCharArray()));
					    }
					};
			Authenticator.setDefault(authenticator);
			
			
			if (proxy != null)
			{
				System.out.println("here in proxy not null");
				connection = (HttpURLConnection) url.openConnection(proxy);
			}
			else
			{
				System.out.println("here in null");
				connection = (HttpURLConnection) url.openConnection();
			}
    
			//connection=(HttpURLConnection) url.openConnection();
			
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(10000);
			
			int status=connection.getResponseCode();
			System.out.println(status);
			
			System.out.println();
			if(status>299)
			{
				reader=new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				while((line=reader.readLine())!=null)
				{
					responsecontent.append(line);
				}
				reader.close();
			}
			else
			{
				reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while((line=reader.readLine())!=null)
				{
					System.out.println("here i am : "+ line);
					responsecontent.append(line);
					System.out.println("responese is "+responsecontent.toString());
				}
				reader.close();
			}
			System.out.println("i am here");
			System.out.println(responsecontent.toString()+"mmmmmm");
			
			parse(responsecontent.toString());
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static List<Route> parse(String responsebody)
	{
		System.out.println("heoo i M enter");
		try {
			JSONObject jsonobject = new JSONObject(responsebody);
			JSONArray routejsonarray = jsonobject.getJSONArray("routes");
			System.out.println("heoo i M here");
			System.out.println(routejsonarray.toString()+"routes");
			 List<Route> routelist = new ArrayList<Route>();
		     for(int m = 0; m < routejsonarray.length(); m++)
		     {
		    	 Route route=new Route();
		    	 List<Leg> leglist=new ArrayList();
		    	 JSONObject routejsonobject = routejsonarray.getJSONObject(m);
		         JSONArray legjsonarray= routejsonobject.getJSONArray("legs");  
		         System.out.println(legjsonarray.toString()+"legs");
		         for(int b = 0; b < legjsonarray.length(); b++)
		         {
		        	 List<Step> steplist=new ArrayList<>();
		        	 Leg leg=new Leg();
		        	 JSONObject legjsonobject=legjsonarray.getJSONObject(b);
		        	 JSONArray stepjsonarray=legjsonobject.getJSONArray("steps");
		        	 System.out.println(stepjsonarray.toString()+"steps");
		        	 for(int stepcount=0;stepcount<stepjsonarray.length();stepcount++)
		        	 {
		        		 Step step=new Step();
		        		 JSONObject stepjsonobject=stepjsonarray.getJSONObject(stepcount);
		        		 System.out.println(stepjsonobject.toString()+"singlestep");
		        		 JSONObject polylinejsonobject=stepjsonobject.getJSONObject("polyline");
		        		 System.out.println(polylinejsonobject.toString()+"polyline");
		        		 String encodedpolyline=polylinejsonobject.getString("points");
		        		 System.out.println(encodedpolyline+"encoded");
		        		 List<LatLng> decodedpolyline=decodePoly(encodedpolyline);
		        		 System.out.println(decodedpolyline.size());
		        		 for(int i=0;i<decodedpolyline.size();i++)
		        		 {
		        			 System.out.println("Lat: "+decodedpolyline.get(i).Latitide+" long: "+decodedpolyline.get(i).Longitude);
		        		 }
		        		 step.setPoints(decodedpolyline);
		        		 steplist.add(step);
		        	 }
		        	 leg.setSteps(steplist);
		        	 leglist.add(leg);
		         }
		         route.setLegs(leglist);
		         routelist.add(route);
		     }
		     System.out.println("now printing points of all routes");
		     int count=0;
		    for(int j=0;j<routelist.size();j++)
		    {
		    	System.out.println("here is route: "+j);
		    	Route temproute=routelist.get(j);
		    	List<Leg> templeglist=temproute.getLegs();
		    	for(int k=0;k<templeglist.size();k++)
		    	{
		    		System.out.println("here is leg: "+k);
		    		Leg templeg=templeglist.get(k);
		    		List<Step> tempsteplist=templeg.getSteps();
		    		for(int l=0;l<tempsteplist.size();l++)
		    		{
		    			System.out.println("here is step: "+l);
		    			Step tempstep=tempsteplist.get(l);
		    			List<LatLng> points=tempstep.getPoints();
		    			for(int z=0;z<points.size();z++)
		    			{
		    				System.out.println("Lat: "+points.get(z).Latitide+" long: "+points.get(z).Longitude);
		    				count++;
		    			}
		    		}
		    	}
		    }
		    System.out.println(count);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
		
	}

	 private static ArrayList<LatLng> decodePoly(String encoded) {
	        ArrayList<LatLng> poly = new ArrayList<LatLng>();
	        int index = 0, len = encoded.length();
	        int lat = 0, lng = 0;
	        while (index < len) {
	            int b, shift = 0, result = 0;
	            do {
	                b = encoded.charAt(index++) - 63;
	                result |= (b & 0x1f) << shift;
	                shift += 5;
	            } while (b >= 0x20);
	            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	            lat += dlat;
	            shift = 0;
	            result = 0;
	            do {
	                b = encoded.charAt(index++) - 63;
	                result |= (b & 0x1f) << shift;
	                shift += 5;
	            } while (b >= 0x20);
	            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	            lng += dlng;

	            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
	            poly.add(position);
	        }
	        System.out.println("i am returning");
	        return poly;
	    }
}
