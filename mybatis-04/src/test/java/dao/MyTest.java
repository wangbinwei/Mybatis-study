package dao;

import com.kuang.dao.StudentMapper;
import com.kuang.pojo.Student;
import com.kuang.utils.MybatisUtils;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

/**
 * @author wbw
 * @date 2021/5/16 13:55
 */
public class MyTest {
    @Test
    public void test(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        List<Student> students = mapper.getStudent2();
        for(Student student:students){
            System.out.println(student);
        }

        sqlSession.close();
    }
}
