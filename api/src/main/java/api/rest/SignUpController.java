package api.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.entity.UserDetail;

@RestController
@RequestMapping("/SignUp")
public class SignUpController {

	@GetMapping(path = "/AddUser/{name:.+}/{email:.+}/{phoneno:.+}/{password:.+}/{gender:.+}/{dob:.+}")
	public String saveuser(@PathVariable String name,@PathVariable String email,@PathVariable String phoneno,@PathVariable String password,@PathVariable String gender,@PathVariable String dob) throws ParseException
	{
		UserDetail newuser=new UserDetail();
		newuser.setEmail(email);
		newuser.setGender(gender);
		newuser.setPassword(password);
		newuser.setPhoneno(phoneno);
		newuser.setUsername(name);
		
		System.out.println("here is date"+dob);
		
		Date dob1=new SimpleDateFormat("dd-MM-YYYY").parse(dob);
		System.out.println(dob1);
		newuser.setDob(dob1);
		//System.out.println(user.getEmail()+"in email  "+user.getPhoneno()+""); 
		//String useremail=user.getEmail();
		//String userphoneno=user.getPhoneno();
		//Date userdob=user.getDob();
		
		//System.out.println(userdob+" in dob ");
		//Calendar c = Calendar.getInstance(); 
		//c.setTime(userdob); 
		//c.add(Calendar.DATE, 1);
		//userdob = c.getTime();
		//user.setDob(userdob);
		//System.out.println(userdob+" ");
		SessionFactory factory= new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(UserDetail.class).buildSessionFactory();
		Session session=factory.getCurrentSession();
		try {
			session.beginTransaction();
			Query check_email=session.createQuery("from UserDetail where email='"+email+"'");
			List<UserDetail> emailusers=check_email.list();
			Query check_phoneno=session.createQuery("from UserDetail where phoneno='"+phoneno+"'");
			List<UserDetail> phonenousers=check_phoneno.list();
			if(emailusers.size()!=0)
			{
				session.getTransaction().commit();
				return "emailerror";
			}
			else if(phonenousers.size()!=0)
			{
				session.getTransaction().commit();
				return "phonenoerror";
			}
			else
			{
				System.out.println("I am saving");
				session.save(newuser);
				session.getTransaction().commit();
				return "success";
			}
			
			
		}
		finally {
			factory.close();
			System.out.println("All done");
		}
		
	}
	
}
