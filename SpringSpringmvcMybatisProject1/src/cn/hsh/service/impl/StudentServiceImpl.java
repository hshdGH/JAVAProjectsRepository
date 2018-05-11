package cn.hsh.service.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hsh.dao.StudentMapper;
import cn.hsh.entity.Student;
import cn.hsh.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentMapper stuMapper;
	
	@Override
	public List<Student> getStudentInfoSer() {
		// TODO Auto-generated method stub
		
		List<Student> list=new ArrayList<Student>();
		list=stuMapper.getStudentInfoMP();
		return list;
	}
	
}
