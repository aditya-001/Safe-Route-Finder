package api.rest;

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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.entity.LatLng;
import api.entity.Leg;
import api.entity.LocationInPrayagRaj;
import api.entity.Route;
import api.entity.Step;

@RestController
@RequestMapping("/Getpath")
public class LocationRestController {
	private static HttpURLConnection connection;
	@GetMapping(path = "/{lat1:.*}/{lon1:.+}/{lat2:.*}/{lon2:.*}")
	public ObjectToSend getpaths(@PathVariable("lat1") String lat1,@PathVariable("lon1") String lon1,@PathVariable("lat2") String lat2,@PathVariable("lon2") String lon2){
		BufferedReader reader;
		String line;
		StringBuffer responsecontent=new StringBuffer();
		try {
			//System.out.println(lon1+"   " +lat1 );
			lon1.trim();
			lon2.trim();
			lat1.trim();
			lat2.trim();
			//System.out.println("https://maps.googleapis.com/maps/api/directions/json?origin="+lat1+","+lon1+"&destination="+lat2+","+lon2+"&alternatives=true&key=AIzaSyAwdxRlmAXwjm_mcFQnM-f-vguZr6JkxV8");
			URL url=new URL("https://maps.googleapis.com/maps/api/directions/json?origin="+lat1+","+lon1+"&destination="+lat2+","+lon2+"&alternatives=true&key=AIzaSyAwdxRlmAXwjm_mcFQnM-f-vguZr6JkxV8");
			 //https://maps.googleapis.com/maps/api/directions/json?origin=41.43206,-81.38992&destination=40.43399,-81.38600lon1&alternatives=true&key=AIzaSyAwdxRlmAXwjm_mcFQnM-f-vguZr6JkxV8");
			 //			URL url=new URL("https://maps.googleapis.com/maps/api/directions/json?origin="+lat1+","+lon1+"&destination="+lat2+","+lon2+"&alternatives=true&key=AIzaSyAwdxRlmAXwjm_mcFQnM-f-vguZr6JkxV8");
			
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("172.31.2.3", 8080));
					Authenticator authenticator = new Authenticator() {

					    public PasswordAuthentication getPasswordAuthentication() {
						return (new PasswordAuthentication("itm2017001", "Riyali@123".toCharArray()));
					    }
					};
			Authenticator.setDefault(authenticator);
			proxy=null;
			
			if (proxy != null)
			{
				//System.out.println("here in proxy not null");
				connection = (HttpURLConnection) url.openConnection(proxy);
			}
			else
			{
				//System.out.println("here in null");
				connection = (HttpURLConnection) url.openConnection();
			}
    
			//connection=(HttpURLConnection) url.openConnection();
			
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(10000);
			
			int status=connection.getResponseCode();
			//System.out.println("here is status" +status);
			
			//System.out.println();
			if(status>299)
			{
				reader=new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				while((line=reader.readLine())!=null)
				{
					//System.out.println(line);
					responsecontent.append(line);
				}
				reader.close();
			}
			else
			{
				reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while((line=reader.readLine())!=null)
				{
					//System.out.println("here i am : "+ line);
					responsecontent.append(line);
					//System.out.println("responese is "+responsecontent.toString());
				}
				reader.close();
			}
			//System.out.println("i am here");
			//System.out.println(responsecontent.toString()+"mmmmmm");
			ObjectToSend obj=new ObjectToSend();

			
			List<Route> routelist = parse(responsecontent.toString());
			int count=0;
			ArrayList<Integer> routescrimecount= new ArrayList<>();
;
			Set<LocationInPrayagRaj> pointstosend=new HashSet<LocationInPrayagRaj>();
			HashSet<LatLng> s= new HashSet<LatLng>();

 
		    for(int j=0;j<routelist.size();j++)
		    {
		    	
		    	List<LatLng> oneroutepoints=new ArrayList<>();
		    	Route temproute=routelist.get(j);
		    	List<Leg> templeglist=temproute.getLegs();
		    	for(int k=0;k<templeglist.size();k++)
		    	{
		    		
		    		Leg templeg=templeglist.get(k);
		    		List<Step> tempsteplist=templeg.getSteps();
		    		for(int l=0;l<tempsteplist.size();l++)
		    		{
		    			
		    			Step tempstep=tempsteplist.get(l);
		    			List<LatLng> points=tempstep.getPoints();
		    			
		    			for(int z=0;z<points.size();z++)
		    			{
		    				count++;
		    				oneroutepoints.add(points.get(z));
		    			}
		    		}
		    		
		    	}
				s.addAll(oneroutepoints);
				Set<LocationInPrayagRaj> pointset= points(s);
				int size= pointset.size();
				//System.out.println("Adding points to set"+ oneroutepoints.size());
				pointstosend.addAll(pointset);
				routescrimecount.add(size);
		    }
		    //System.out.println("Adding points to set size"+ pointstosend.size());
			
			//collecting points from database
		   obj.setNumofcrimes(routescrimecount);
		   obj.setPoints(pointstosend);
			
			return obj;
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
        return poly;
    }
	public static List<Route> parse(String responsebody)
	{
		ArrayList<LatLng> listtoreturn = new ArrayList<LatLng>();
		try {
			JSONObject jsonobject = new JSONObject(responsebody);
			JSONArray routejsonarray = jsonobject.getJSONArray("routes");
			System.out.println(routejsonarray.toString()+"routes");
			 List<Route> routelist = new ArrayList<Route>();
		     for(int m = 0; m < routejsonarray.length(); m++)
		     {
		    	 Route route=new Route();
		    	 List<Leg> leglist=new ArrayList();
		    	 JSONObject routejsonobject = routejsonarray.getJSONObject(m);
		         JSONArray legjsonarray= routejsonobject.getJSONArray("legs");  
		         //System.out.println(legjsonarray.toString()+"legs");
		         for(int b = 0; b < legjsonarray.length(); b++)
		         {
		        	 List<Step> steplist=new ArrayList<>();
		        	 Leg leg=new Leg();
		        	 JSONObject legjsonobject=legjsonarray.getJSONObject(b);
		        	 JSONArray stepjsonarray=legjsonobject.getJSONArray("steps");
		        	// System.out.println(stepjsonarray.toString()+"steps");
		        	 for(int stepcount=0;stepcount<stepjsonarray.length();stepcount++)
		        	 {
		        		 Step step=new Step();
		        		 JSONObject stepjsonobject=stepjsonarray.getJSONObject(stepcount);
		        		 //System.out.println(stepjsonobject.toString()+"singlestep");
		        		 JSONObject polylinejsonobject=stepjsonobject.getJSONObject("polyline");
		        		 //System.out.println(polylinejsonobject.toString()+"polyline");
		        		 String encodedpolyline=polylinejsonobject.getString("points");
		        		 //System.out.println(encodedpolyline+"encoded");
		        		 List<LatLng> decodedpolyline=decodePoly(encodedpolyline);
		        		 //System.out.println(decodedpolyline.size());
		        		 listtoreturn.addAll(decodedpolyline);
		        		 /*for(int i=0;i<decodedpolyline.size();i++)
		        		 {
		        			 System.out.println("Lat: "+decodedpolyline.get(i).Latitude+" long: "+decodedpolyline.get(i).Longitude);
		        		 }*/
		        		 step.setPoints(decodedpolyline);
		        		 steplist.add(step);
		        	 }
		        	 leg.setSteps(steplist);
		        	 leglist.add(leg);
		         }
		         route.setLegs(leglist);
		         routelist.add(route);
		     }
		     //System.out.println("now printing points of all routes");
		     int count=0;
		    for(int j=0;j<routelist.size();j++)
		    {
		    	
		    	Route temproute=routelist.get(j);
		    	List<Leg> templeglist=temproute.getLegs();
		    	for(int k=0;k<templeglist.size();k++)
		    	{
		    		
		    		Leg templeg=templeglist.get(k);
		    		List<Step> tempsteplist=templeg.getSteps();
		    		for(int l=0;l<tempsteplist.size();l++)
		    		{
		    			
		    			Step tempstep=tempsteplist.get(l);
		    			List<LatLng> points=tempstep.getPoints();
		    			for(int z=0;z<points.size();z++)
		    			{
		    				count++;
		    			}
		    		}
		    	}
		    }
		   // System.out.println(count);
		    return routelist;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
		
	}
	
	public static Set<LocationInPrayagRaj> points(HashSet<LatLng> l1){
		Set<LocationInPrayagRaj> s= new HashSet<LocationInPrayagRaj>();
		SessionFactory factory= new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(LocationInPrayagRaj.class).buildSessionFactory();
		Session session=factory.getCurrentSession();
		Iterator<LatLng> i = l1.iterator();
		while(i.hasNext()) {
		LatLng l=i.next();
		double lat1u=l.Latitude +.000950;
		double lat1d=l.Latitude -.000950;
		double long1w=l.Longitude+.000950;
		double long1e=l.Longitude-.000950;
		try {
			session.beginTransaction();
		    Query logincheckquery=session.createQuery("from LocationInPrayagRaj P where P.longitude<"+long1w+" and P.longitude>"+long1e+" and P.latitude>"+lat1d+" and P.latitude<"+lat1u);
			List<LocationInPrayagRaj> placelist=logincheckquery.list();
			//System.out.println(placelist.size());
			s.addAll(placelist);			
		}
		catch(Exception e) {
			e.printStackTrace();
		}}
		//System.out.println(s.size());
		return s;
		
	}
	

}
