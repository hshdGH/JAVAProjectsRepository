package cn.hsh.advice;

import java.lang.reflect.Method;

import org.springframework.aop.ThrowsAdvice;

public class MyThrowAdvice implements ThrowsAdvice {

	public void afterThrowing(Method method, Object[] args, Object target, Exception ex){
		System.out.println("出大事了： "+ex.getMessage());
	}
}
