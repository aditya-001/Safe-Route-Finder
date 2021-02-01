package api.rest;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import api.entity.UserDetail;
//@PathVariable String id
@RestController
@RequestMapping("/login")
public class LoginController {
	@GetMapping(path = "/email/{id:.+}/{password:.+}")
	public String loginwithemail(@PathVariable("id") String id,@PathVariable("password") String password)
	{
		System.out.println(id+"here is id");
		System.out.println("I am here");
	String invalid_str="isinvalid";
	//System.out.println("here is "+id+" "+password);
	SessionFactory factory= new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(UserDetail.class).buildSessionFactory();
	Session session=factory.getCurrentSession();
	try {
	session.beginTransaction();
	Query logincheckquery=session.createQuery("from UserDetail where email='"+ id+"' and password='"+password+"'");
	List<UserDetail> userlist=logincheckquery.list();
	if(userlist.size()==0)
	{
	session.getTransaction().commit();
	System.out.println(" returning invalid");

	return invalid_str;
	}
	else
	{
	UserDetail user;
	String userpassword;
	user=userlist.get(0);
	userpassword=user.getPassword();
	session.getTransaction().commit();
	System.out.println(" returning valid");
	return "valid";
	
	}

	}
	catch(Exception e) {
	System.out.println(" returning invalid");
	return invalid_str;
	}
	finally {
	factory.close();
	}
	}
	@GetMapping(path = "/phone/{pid:.*}/{password:.+}")
	public String loginwithphone(@PathVariable("pid") String id,@PathVariable("password") String password)
	{
	String invalid_str="isinvalid";
	//System.out.println("here is "+id+" "+password);
	SessionFactory factory= new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(UserDetail.class).buildSessionFactory();
	Session session=factory.getCurrentSession();
	try {
	session.beginTransaction();
	Query logincheckquery=session.createQuery("from UserDetail where phoneno='"+id+"' and password='"+password+"'");
	List<UserDetail> userlist=logincheckquery.list();
	if(userlist.size()==0)
	{
	session.getTransaction().commit();
	return invalid_str;
	}
	else
	{
	UserDetail user;
	String userpassword;
	user=userlist.get(0);
	userpassword=user.getPassword();
	session.getTransaction().commit();
	return userpassword;
	}

	}
	catch(Exception e) {
	return e.toString();
	}
	finally {
	factory.close();
	}
	}

}
