package cn.hsh.controller;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.hsh.service.impl.StudentServiceImpl;


@Controller
public class StudentContro {

	@Resource
	private StudentServiceImpl stutService;
	
	@RequestMapping("/getStudentInfoControUrl")
	@ResponseBody
	public ModelAndView getStudentInfoContro(){
		
		ModelAndView mav=new ModelAndView();
		List list=stutService.getStudentInfoSer();
		
		mav.addObject("list", list);
		mav.setViewName("success");
		
		return mav;
	}

	/*@Test
	public void testMethod1(){		
		PageHelper.startPage(1,5);
		List list=stuService.getStudentInfoSer();
		PageHelper.Page phs=PageHelper.endPage();
		DataGrid dg=new DataGrid();
		dg.setRows(phs.getResult());
		dg.setTotal(phs.getTotal());
		
		List list2=dg.getRows();
		for(int i=0;i<list.size();i++){
			Student stu=(Student) list.get(i);
			System.out.println(stu.getName());
		}
	}*/


}
