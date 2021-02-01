package api.rest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.text.DecimalFormat;
import java.util.Random; 

import com.mysql.cj.xdevapi.Statement;

import api.entity.LocationInPrayagRaj;

public class DataEntry {

	public static void main(String[] args) {
		try {
			 Random rand = new Random();
			Connection con;
			java.sql.Statement stmt;
			 Class.forName("com.mysql.cj.jdbc.Driver");  
	         con=DriverManager.getConnection("jdbc:mysql://localhost:3306/seguro?useSSL=false","root","Aditya@#123");
	         stmt =con.createStatement();
	         LocationInPrayagRaj l1=new LocationInPrayagRaj() ;
	         l1.setLocationName("Sample");
	         	l1.setLatitude(25.429001);
	 			l1.setLongitude(81.770064);
	 			l1.setNumberOfCrimes(20);
	         while(l1.getLatitude()<=25.458620){
	     		l1.setLongitude(81.770063);
	     		while(l1.getLongitude()<= 81.858390){
	     			int rand_int1 = rand.nextInt(100);
	     			 String sqlstmt="insert into PrayagRaj(LocationName,NumberOfCrimes,latitude,longitude) values('"+l1.getLocationName()+ "',"+rand_int1+","+l1.getLatitude()+","+l1.getLongitude()+")";
	    	         stmt.executeUpdate(sqlstmt);
	    	         DecimalFormat numberFormat = new DecimalFormat("#.000000");
		     			String x= numberFormat.format(l1.getLongitude());
		     			double d = Double.parseDouble(x);
		     			double d1=d+0.000800;
		     			String x1=numberFormat.format(d1);
		     			d1=Double.parseDouble(x1);
	     			l1.setLongitude(d1);
	     		}
	     		System.out.println(l1.getLongitude() +"  "+ l1.getLatitude());
	     		DecimalFormat numberFormat1 = new DecimalFormat("#.000000");
     			String y= numberFormat1.format(l1.getLatitude());
     			double e = Double.parseDouble(y);
     			double e1=e+0.000600;
     			String y1= numberFormat1.format(e1);
     			e1=Double.parseDouble(y1);
	     		l1.setLatitude(e1);


	     	}
	         System.out.println("I am here");
	         
		}
		catch(Exception e) {
			System.out.println(e);
		}

	}

}
