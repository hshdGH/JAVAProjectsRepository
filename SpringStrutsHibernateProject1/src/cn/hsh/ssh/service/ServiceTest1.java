package cn.hsh.ssh.service;

import java.util.List;
import javax.annotation.Resource;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sun.xml.internal.bind.v2.runtime.Name;


@Service @Transactional
public class ServiceTest1 {

	@Resource
	private SessionFactory sessionFactory;
	
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public List getQueryList(){
		return sessionFactory.getCurrentSession().createQuery("from StuCourse").list();
		//return sessionFactory.getCurrentSession().get(StuCourse.class, 21);
	}

}
