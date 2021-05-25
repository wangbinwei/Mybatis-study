package com.kuang.dao;

import com.kuang.pojo.Student;


import java.util.List;

/**
 * @author wbw
 * @date 2021/5/16 11:33
 */
public interface StudentMapper {
    //查询所有的学生信息，以及对应的老师信息
    List<Student> getStudent();
    List<Student> getStudent2();
}
