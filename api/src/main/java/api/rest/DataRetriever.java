package api.rest;
import java.util.*;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.lang.Math;

import api.entity.LatLng;
import api.entity.LocationInPrayagRaj;
import api.entity.UserDetail;

public class DataRetriever {

	public static void main(String[] args) {
		ArrayList <LocationInPrayagRaj> locations= new ArrayList<LocationInPrayagRaj>();
		LocationInPrayagRaj l1=new LocationInPrayagRaj();
		LocationInPrayagRaj l2=new LocationInPrayagRaj();
		l1.setLatitude(25.429111);
		l1.setLongitude(81.770063);
		locations=points(l1,l2);
		for(LocationInPrayagRaj l: locations) {
			System.out.println(l);
		}
		System.out.println(locations.size());

	}
	public static ArrayList <LocationInPrayagRaj> points(LatLng l1){
		double lat1u=l1.Latitude +.000950;
		double lat1d=l1.Latitude -.000950;
		double long1w=l1.Longitude+.000950;
		double long1e=l1.Longitude-.000950;
		SessionFactory factory= new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(LocationInPrayagRaj.class).buildSessionFactory();
		Session session=factory.getCurrentSession();
		try {
			session.beginTransaction();
		    Query logincheckquery=session.createQuery("from LocationInPrayagRaj P where P.longitude<"+long1w+" and P.longitude>"+long1e+" and P.latitude>"+lat1d+" and P.latitude<"+lat1u);
			List<LocationInPrayagRaj> placelist=logincheckquery.list();
			Set<LatLng> s= new HashSet<LatLng>();
			System.out.println(placelist.size());
			return (ArrayList<LocationInPrayagRaj>) placelist;
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}

}
