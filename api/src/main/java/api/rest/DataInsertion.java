package api.rest;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import api.entity.LocationInPrayagRaj;
import api.entity.UserDetail;

public class DataInsertion {

	public static void main(String[] args) {
		SessionFactory factory= new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(LocationInPrayagRaj.class).buildSessionFactory();
		Session session=factory.getCurrentSession();
		LocationInPrayagRaj l1=new LocationInPrayagRaj() ;
		l1.setLatitude(25.429000);
		l1.setLocationName("Sample");
		l1.setLongitude(81.770063);
		l1.setNumberOfCrimes(20);
		
		try {
			session.beginTransaction();
			
			session.save(l1);
			session.getTransaction().commit();
			
			
		}
		finally {
			factory.close();
			System.out.println("All done");
		}

	}

}
