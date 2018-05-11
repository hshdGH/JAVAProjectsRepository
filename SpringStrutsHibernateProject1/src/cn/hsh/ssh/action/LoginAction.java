package cn.hsh.ssh.action;

import java.util.*;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import com.opensymphony.xwork2.ActionContext;
import cn.hsh.ssh.domain.StuCourse;
import cn.hsh.ssh.service.ServiceTest1;

@Controller
public class LoginAction{
	
	private String username;
	private String userpass;
	
	@Resource
	private ServiceTest1 st1;
	
	public String loginExecute1(){
		
		System.out.println("用户名："+username);
		System.out.println("密码："+userpass);
		
		if(!username.equals("")&&!userpass.equals("")){
			
			ActionContext ac2=ActionContext.getContext();
			Map<String ,Object> request=(Map<String, Object>)ac2.get("request");
			
			List<StuCourse> list=st1.getQueryList();
			request.put("list", list);
			return "success";
		}else{
			return "relogin";
		}
	}
	
	
	public ServiceTest1 getSt1() {
		return st1;
	}
	public void setSt1(ServiceTest1 st1) {
		this.st1 = st1;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserpass() {
		return userpass;
	}
	public void setUserpass(String userpass) {
		this.userpass = userpass;
	}
}
