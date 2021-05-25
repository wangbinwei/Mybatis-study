package com.kuang.dao;

import com.kuang.pojo.User;
import com.kuang.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wbw
 * @date 2021/4/21 15:03
 */
public class UserDaoTest {
    @Test
    public void test(){
        //第一步获取sqlSession对象
        SqlSession sqlSession =  MybatisUtils.getSqlSession();
        try{
            //方式一： getMapper
            UserMapper userDao =  sqlSession.getMapper(UserMapper.class);
            List<User> userList = userDao.getUserList();

            //方式二：
//        List<User> userList = sqlSession.selectList("com.kuang.UserMapper.getUserList");

            //流运算
            userList.stream().forEach(System.out::println);
//        for(User user: userList){
//            System.out.println(user.toString());
//        }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭SqlSession
            sqlSession.close();
        }

    }
    @Test
    public void getUserById(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.getUserById(1);
        System.out.println(user);
        sqlSession.close();
    }
    //增删改查需要提交事务
    @Test
    public void addUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int result = mapper.addUser(new User(9, "小明", "11"));

        //提交事务
        sqlSession.commit();
        sqlSession.close();
    }
    @Test
    public void addUser2(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        Map<String,Object> map = new HashMap<>();
        map.put("userId",10);
        map.put("userName","哈哈");
        map.put("password","1");
        //mapper.addUser2(map);
        //提交事务
        sqlSession.commit();
        sqlSession.close();
    }
    @Test
    public void updateUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.updateUser(new User(9,"胖弟弟","12"));

        //提交事务
        sqlSession.commit();
        sqlSession.close();
    }
    @Test
    public void deleteByUserId(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.deleteUserById(8);

        sqlSession.commit();
        sqlSession.close();
    }
    @Test
    public void selectUserByName(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //List<User> list = mapper.getUserByName("明");
//        for (User user : list) {
//            System.out.println(user);
//        }
        sqlSession.close();
    }
}
