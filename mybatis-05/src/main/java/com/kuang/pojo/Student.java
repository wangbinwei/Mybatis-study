package com.kuang.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author wbw
 * @date 2021/5/14 16:36
 */
@Data
@AllArgsConstructor
public class Student {
    private int id;
    private String name;

    //学生需要关联一个老师
    private int tid;
}
