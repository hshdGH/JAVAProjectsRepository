package cn.hsh.ssh.test;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class TestApp2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/**
		 * scope属相取值：
		 * prototype:两次取的sessionfactory 不一样;
		 * singleton:两次取的sessionfactory 一样;
		 * request：
		 */

		//ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
		ApplicationContext ac=new FileSystemXmlApplicationContext("WebRoot/WEB-INF/config/spring/applicationContext.xml");
		
		SessionFactory sf=(SessionFactory)ac.getBean("sessionFactory"); 
        SessionFactory sf2=(SessionFactory)ac.getBean("sessionFactory");
        System.out.println(sf==sf2);
	}

}
