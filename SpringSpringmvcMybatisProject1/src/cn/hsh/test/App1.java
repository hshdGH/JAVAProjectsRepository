package cn.hsh.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import cn.hsh.advice.TestInterface;
import cn.hsh.advice.TestInterface2;

public class App1 {

	//前置通知和后置通知
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	 //传统写法
		/*ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
		TestInterfaceImpl tif=(TestInterfaceImpl) ac.getBean("testService");
		tif.sayHello();*/
  
     //aop 写法  pfb（调用class路径下的文件）
		/*ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
		TestInterface tif=(TestInterface) ac.getBean("pfb");
		tif.sayHello();
		
		TestInterface2 tif2=(TestInterface2) ac.getBean("pfb");
		tif2.sayBye();*/
		
	//aop 写法  pfb（调用web-inf下的文件）
		ApplicationContext ac=new FileSystemXmlApplicationContext("WebRoot/WEB-INF/config/spring/applicationContext_advice.xml");
		TestInterface tif=(TestInterface) ac.getBean("pfb");
		tif.sayHello();
		
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	
		TestInterface2 tif2=(TestInterface2) ac.getBean("pfb");
		tif2.sayBye();
			
	}

}
