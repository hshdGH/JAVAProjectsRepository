package cn.hsh.ssh.test;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import cn.hsh.ssh.domain.StuCourse;
import cn.hsh.ssh.service.ServiceTest1;

public class TestApp1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
        ServiceTest1 st1=(ServiceTest1)ac.getBean("serviceTest");     

        List<StuCourse> list=st1.getQueryList();
        for(StuCourse sc : list){
        	System.out.println("课程名称："+sc.getCourse().getName());
        }
	}

}
