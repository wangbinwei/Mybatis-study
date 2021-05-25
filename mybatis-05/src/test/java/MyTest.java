import com.kuang.dao.StudentMapper;
import com.kuang.dao.TeacherMapper;
import com.kuang.pojo.ListNode;
import com.kuang.pojo.Student;
import com.kuang.pojo.Teacher;
import com.kuang.utils.ListUtil;
import com.kuang.utils.MybatisUtils;
import lombok.val;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.*;

/**
 * @author wbw
 * @date 2021/5/16 13:55
 */
public class MyTest {
    @Test
    public void test(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
        Teacher teacher = mapper.getTeacherById(1);
        System.out.println(teacher);

        sqlSession.close();
    }
    @Test
    public void queryById(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
//        Map<String, String> map = new HashMap<>();
//        map.put("name","小红");
//        mapper.queryByName(map);
        Student student = new Student(1,"小土",1);
        mapper.updateById(student);
        sqlSession.commit(); //没有上传
        sqlSession.close();
    }
    @Test
    public void testPriorityQueue(){
        //默认采用的是最小堆实现的
        PriorityQueue<Integer> queue = new PriorityQueue<Integer>(10,new Comparator<Integer>(){
            public int compare(Integer a, Integer b){
                return a-b; //if a>b 则交换，so这是递增序列
            }
        });
        queue.offer(13);
        queue.offer(9);
        int len = queue.size();
        for(int i=0;i<len;i++){
            System.out.println(queue.poll());
        }
        //输出 9  13
        //默认采用的是最小堆实现的
        PriorityQueue<Integer> queue2 = new PriorityQueue<>(10);
        queue2.offer(11);
        queue2.offer(9);
        len = queue2.size();
        for(int i=0;i<len;i++){
            System.out.println(queue2.poll());
        }
        //输出 9， 11
    }

    @Test
    public void queryByIds(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        System.out.println(ids.toString());
        List<Student> students = mapper.queryByIds(ids);
        sqlSession.clearCache();
        List<Student> students1 = mapper.queryByIds(ids);
        System.out.println(students1==students);
        sqlSession.close();
    }

    @Test
    public void getIntersectionNode(){
        int[] arrayA = new int[]{4,1,8,4,5};
        int[] arrayB = new int[]{5,6,1}; //5,6,1,8,4,5
        ListUtil listUtilA = new ListUtil();
        ListUtil listUtilB = new ListUtil();
        ListNode headA = listUtilA.init(arrayA);
        ListNode headB = listUtilB.init(arrayB);
        listUtilB.findNodeByIndex(2).next = listUtilA.findNodeByIndex(2);

        ListNode temp = listUtilA.getIntersectionNode(headA,headB);

        //System.out.println(ans.val);
//        while(headB!=null){
//            System.out.println(headB.val);
//            headB = headB.next;
//        }

    }
}
