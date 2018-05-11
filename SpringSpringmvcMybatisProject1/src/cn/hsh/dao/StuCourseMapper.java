package cn.hsh.dao;

import cn.hsh.entity.StuCourse;

public interface StuCourseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StuCourse record);

    int insertSelective(StuCourse record);

    StuCourse selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StuCourse record);

    int updateByPrimaryKey(StuCourse record);
}