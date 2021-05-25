package com.kuang.dao;

import com.kuang.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author wbw
 * @date 2021/4/21 14:04
 */
public interface UserMapper {
    //获取全部用户
    List<User> getUserList();
    //分页
    List<User> getUserByLimit(Map<String, Integer> map);
    //根据ID查询用户
    User getUserById(@Param("id") int id);
    //增加用户
    int addUser(User user);

    //更新用户
    int updateUser(User user);
    //删除用户
    int deleteUserById(int id);


}
