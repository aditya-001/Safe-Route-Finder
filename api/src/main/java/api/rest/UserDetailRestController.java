package api.rest;

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
@RequestMapping("/GetDetail")
public class UserDetailRestController {
	@GetMapping(path = "/email/{id:.+}/{password}")
	public String loginwithemail(@PathVariable("id") String id,@PathVariable("password") String password)
	{
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
	return invalid_str;
	}
	else
	{
	UserDetail user;
	String userpassword;
	user=userlist.get(0);
	userpassword=user.getPassword();
	session.getTransaction().commit();
	return "valid";
	}

	}
	catch(Exception e) {
	return invalid_str;
	}
	finally {
	factory.close();
	}
	}
	@GetMapping(path = "/phone/{pid:.*}")
	public String loginwithphone(@PathVariable("pid") String id)
	{
	String invalid_str="isinvalid";
	//System.out.println("here is "+id+" "+password);
	SessionFactory factory= new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(UserDetail.class).buildSessionFactory();
	Session session=factory.getCurrentSession();
	try {
	session.beginTransaction();
	Query logincheckquery=session.createQuery("from UserDetail where phoneno='"+id+"'");
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
