package dao;

import com.kuang.dao.UserMapper;
import com.kuang.pojo.User;
import com.kuang.utils.MybatisUtils;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wbw
 * @date 2021/4/21 15:03
 */
public class UserDaoTest {
    static Logger logger = Logger.getLogger(UserDaoTest.class); //LogDemo为相关的类
    @Test
    public void test(){
        String str ="123";
        String pattern = "";
        System.out.println(str.contains(pattern));
        //第一步获取sqlSession对象
        SqlSession sqlSession =  MybatisUtils.getSqlSession();

        //方式一： getMapper
        UserMapper userDao =  sqlSession.getMapper(UserMapper.class);
        User user = userDao.getUserById(1);
        System.out.println(user);
        //流运算
        //userList.stream().forEach(System.out::println);
//        for(User user: userList){
//            System.out.println(user.toString());
//        }
        sqlSession.close();

    }

    @Test
    public void testLog4j(){
        logger.info("info:进入");
        logger.debug("debug:");
        logger.error("error:");
    }
    @Test
    public void getUserByLimit(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        //底层主要应用反射
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        Map<String, Integer> map = new HashMap<>();
        map.put("startIndex",0);
        map.put("pageSize",2);

        List<User> userByLimit = mapper.getUserByLimit(map);
        userByLimit.stream().forEach(System.out::println);
        sqlSession.close();
    }
}
