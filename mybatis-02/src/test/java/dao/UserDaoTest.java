package dao;

import com.kuang.dao.UserMapper;
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

    //增删改查需要提交事务

}
