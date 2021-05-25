## Mybatis

## 1、简介

### 1.1、什么是 MyBatis？

- MyBatis 是一款优秀的持久层框架
- 它支持自定义 SQL、存储过程以及高级映射。
- MyBatis 免除了几乎所有的 JDBC 代码以及设置参数和获取结果集的工作。
- MyBatis 可以通过简单的 XML 或注解来配置和映射原始类型、接口和 Java POJO（Plain Old Java Objects，普通老式 Java 对象）为数据库中的记录。

如何获取Mybatis?

- maven仓库：

  ```java
  <!-- https://mvnrepository.com/artifact/org.mybatis/mybatis -->
  <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis</artifactId>
      <version>3.5.2</version>
  </dependency>
  
  ```

- Github:https://github.com/mybatis/mybatis-3

- 中文文档：https://mybatis.org/mybatis-3/zh/index.html

### 1.2、持久化

数据持久化（动作）

- 持久化就是将程序的数据在持久状态和瞬时状态转化的过程

- 闪存： 断电既失

- 数据库（jdbc）,io文件持久化

  

#### 1.3、持久层

Dao层、Service层、Controller层、、、

- 完成持久化工作的代码块
- 层界限十分明显

#### 1.4 为什么需要Mybatis？

- 传统的JDBC代码太复杂。简化。框架。自动化。

- 优点： 

  - 简单易学
  - sql与代码的分离。提高可维护性
  - 提供映射标签，支持对象与数据库的ORM(Object Relationship Map)

  - 提供对象关系隐射标签，支持对象关系组建维护
  - 提供xml标签，支持编写动态sql

- java  Spring SpringMVC  SpringBoot

### 2、第一个Mybatis程序

思路：搭建环境-->导入Mybatis--> 编写代码-->测试

#### 2.1 搭建环境

搭建数据库

```mysql
CREATE DATABASE IF NOT EXISTS `mybatis`;

USE `mybatis`;

CREATE TABLE IF NOT EXISTS `user` (
	`id` INT(20) NOT NULL,
	`name` VARCHAR(30) DEFAULT NULL,
	`pwd` VARCHAR(30) DEFAULT NULL,
	PRIMARY KEY(`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8;


INSERT INTO `user` (`id`, `name`, `pwd`) VALUES
(1, "wbw","123"),
(2,"wk","456"),
(3,"ph","12346")
```

新建项目

1、新建一个maven项目

2、删除src目录，这就是父工程

3、导入maven依赖

```xml
<dependencies>
        <!--mysql驱动-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.47</version>
        </dependency>
        <!--mybatis-->
        <!-- https://mvnrepository.com/artifact/org.mybatis/mybatis -->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.2</version>
        </dependency>
        <!--junit-->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>
    </dependencies>
```

#### 2.2、创建一个模块

- 编写mybatis的核心配置文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<!--configuration核心配置文件-->
<configuration>

    <!--导入属性配置-->
    <properties resource="datasource.properties"></properties>

    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mybatis?useSSL=true&amp;useUnicode=true&amp;characterEncoding=UTF-8"/>
                <property name="username" value="root"/>
                <property name="password" value="admin"/>
            </dataSource>
        </environment>
    </environments>

</configuration>
```

- 编写mybatis工具类

```java
//sqlSessionFactory --> sqlSession
public class MybatisUtils {
    private static SqlSessionFactory sqlSessionFactory;
    static{
        try {
            //获取Mybatis 第一步：获取sqlSessionFactory对象
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //既然有了 SqlSessionFactory，顾名思义，我们可以从中获得 SqlSession 的实例。
    // SqlSession 提供了在数据库执行 SQL 命令所需的所有方法。你可以通过 SqlSession 实例来直接执行已映射的 SQL 语句
    public static SqlSession getSqlSession(){
        return sqlSessionFactory.openSession();
    }

}
```

#### 2.3、编写代码

- 实体类   :Pojo Plain Ordinary Java Object简单的Java对象， 展示表的属性，其中每个属性的Getter、Set。没有业务逻辑

```java
public class User {
    private int id;
    private String name;
    private String pwd;

    public User() {
    }

    public User(int id, String name, String pwd) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }
}
```



- Dao接口，写Mapper的各种接口，增删改查的接口 insert、delete

```java
public interface UserDao {
    List<User> getUserList();

}
```

- 接口实现类，Mapper.xml 代替接口实现类

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace=绑定一个对应的Dao/Mapper接口-->
<mapper namespace="com.kuang.dao.UserDao">
    <!--select查询语句-->
    <select id="getUserList" resultType="com.kuang.pojo.User">
        select * from mybatis.user;
    </select>
</mapper>
```

#### 2.4、测试

注意点：org.apache.ibatis.binding.BindingException: Type interface com.kuang.dao.UserDao is not known to the MapperRegistry.

未注册到MapperRegistry

- junit测试

  ```java
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
  }
  ```

  

遇到的问题：

1、配置文件没有注册

2、绑定接口错误



3、方法名不对

4、返回类型不对

5、Maven导出资源问题，在生成的target的classes中无需要的UserMapper.xml。所以在对应的pom文件中加载下面的配置文件

```xml
    <!--在build中配置resources,来防止我们资源导出失败的问题-->
    <build>
        <resources>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>false</filtering>
            </resource>
            <resource>
                <directory>src/main/resources</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>false</filtering>
            </resource>
        </resources>
    </build>
```



### 3、CRUD

- id：就是对应namespace中的方法名
- resultType: sql语句中执行的返回值
- parameterType: 参数类型

1、编写接口

```java
int deleteUserById(int id);
```

2、编写Mapper中的sql语句

```xml
<delete id="deleteUserById" parameterType="com.kuang.pojo.User">
    DELETE from user
    where id = #{id}
</delete>
```

3、测试

```java
@Test
public void getUserById(){
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    User user = mapper.getUserById(1);
    System.out.println(user);
    sqlSession.close();
}
```

#### 1、namespace

namespace中的包名要和Dao/mapper接口的包名一致

#### 2、select

```xml
<select id="getUserById" parameterType="int" resultType="com.kuang.pojo.User">
    select  * from user where id = #{id};
</select>
```

#### **3、insert**

```xml
<!--对象中的属性，可以直接取出来-->
<insert id="addUser" parameterType="com.kuang.pojo.User" >
    insert into user (id,name,pwd) values (#{id}, #{name},#{pwd});
</insert>

  <insert id="insert" parameterType="com.example.pojo.AppVersion">
    <selectKey keyProperty="appVersionId" order="AFTER" resultType="java.lang.Integer">
      SELECT LAST_INSERT_ID()
    </selectKey>
    insert into app_version (app_version_code, app_version_name, app_update_url, 
      app_update_desc, create_time, update_time
      )
    values (#{appVersionCode,jdbcType=INTEGER}, #{appVersionName,jdbcType=VARCHAR}, #{appUpdateUrl,jdbcType=VARCHAR}, 
      #{appUpdateDesc,jdbcType=VARCHAR}, now(), now()
      )
  </insert>
```

插入可以使用now()函数来使用当前时间

#### **4、update**

```xml
<update id="updateUser" parameterType="com.kuang.pojo.User">
    update user
    set  name=#{name},pwd=#{pwd}
    where id = #{id};
</update>
```

#### 5、delete

```mysql
<delete id="deleteUserById" parameterType="com.kuang.pojo.User">
    DELETE from user
    where id = #{id}
</delete>
```

注意点：

- 增删改需要提交事务！	

#### 6、分析错误

- 标签不能配错
- resource 绑定mapper,需要使用路径
- 配置文件必须符合规范！
- NPE(NullPoionterException)，没有注册到资源！



#### 7、万能的map

假设，我们的实体类，或者数据库中的表，字段or参数太多，我们应当考虑使用Map!

```java
//接口类
int addUser2(Map<String,Object> map);
```

```xml
<！--mapper实现类-->
<insert id="addUser2" parameterType="map">
        insert into user (id,name,pwd) values (#{userId}, #{userName},#{password})
</insert>
```

```java
//测试类    
@Test
public void addUser2(){
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    Map<String,Object> map = new HashMap<>();
    map.put("userId",10);
    map.put("userName","哈哈");
    map.put("password","1");
    mapper.addUser2(map);
    //提交事务
    sqlSession.commit();
    sqlSession.close();
}
```

Map专递参数，直接在sql中取出key即可！  【parameterType="map"】

对象传递参数，直接在sql中取对象的属性即可！

只有一个基本类型参数的情况下。直接在sql中取到

多个参数用Map，**或者注解**

#### 8、模糊查询

​	1、java代码执行的时候，专递通配符  %李%

```java
List<User> list = mapper.getUserByName("%明%");
```

​	2、在sql拼接中使用通配符！  

```sql
select * from user where name like concat("%",#{value},"%")
```

​	固定sql语句形式可以防止sql注入错误  “” or 1==1	

### 4、配置解析

#### 1、核心配置文件

- mybatis-config.xml

```
configuration（配置）
properties（属性）
settings（设置）
typeAliases（类型别名）
typeHandlers（类型处理器）
objectFactory（对象工厂）
plugins（插件）
environments（环境配置）
environment（环境变量）
transactionManager（事务管理器）
dataSource（数据源）
databaseIdProvider（数据库厂商标识）
mappers（映射器）
```

#### 2、环境配置（environments）

Mybatis可以配置成适应多种环境

**不过要记住：尽管可以配置多个环境，但每个 SqlSessionFactory 实例只能选择一种环境。**

学会使用配置多套运行环境！

Mybatis默认的事务管理器就是JDBC，连接池：pooled

#### 3、属性（properties）

这些属性可以在外部进行配置，并可以进行动态替换。你既可以在典型的 Java 属性文件中配置这些属性，也可以在 properties 元素的子元素中设置。

编写一个配置文件 datasource.properties

```properties
driver= com.mysql.jdbc.Driver
url = jdbc:mysql://localhost:3306/mybatis?useSSL=false&amp;useUnicode=true&amp;characterEncoding=UTF-8
username = root
password = admin
```

在核心配置文件中映入

```xml
 <!--导入属性配置-->
    <properties resource="datasource.properties">
```

- 可以直接引入外部文件
- 可以在其中增加一些属性配置
- 如果两个文件有同一字段，优先使用外部配置文件

#### 4、类型别名（typeAliases）

- 型别名可为 Java 类型设置一个缩写名字。 
- 它仅用于 XML 配置，意在降低冗余的全限定类名书写。

```xml
<!-- 可以给实体类起别名-->
    <typeAliases>
        <typeAlias type="com.kuang.pojo.User" alias="User"/>
    </typeAliases>
```

也可以指定一个包名，Mybatis会在包名下面搜索需要的JavaBean,比如：

扫描实体类的包package，它的默认别名就是这个类的类名，首字母小写

```xml
    <typeAliases>
<!--        <typeAlias type="com.kuang.pojo.User" alias="User"/>-->
        <package name="com.kuang.pojo"/>
    </typeAliases>
```

如果实体类比较少的时候，使用第一种方式。

如果实体类十分多，建议使用第二种。

第一种可以DIY，第二种可以通过在实体类上加注解进行DIY

```java
@Alias("Hello")
public class User {
}
```

#### 5、设置 Setting

#### 6、其他配置

#### 7、映射器（mappers）

MapperRegistry: 注册绑定我们的Mapper文件；

方式一：

```xml
<!--每一个Mappper.XML都需要在Mybatis核心配置,注册-->
    <mappers>
        <mapper resource="com/kuang/dao/UserMapper.xml"/>

    </mappers>
```

方式二：使用class文件绑定注册

```xml
    <!--每一个Mappper.XML都需要在Mybatis核心配置,注册-->
    <mappers>
<!--        <mapper resource="com/kuang/dao/UserMapper.xml"/>-->
        <mapper class="com.kuang.dao.UserMapper" />
    </mappers>
```

注意点：

接口和他的Mapper配置文件必须同名

接口和它的Mapper配置文件必须在同一包下！

#### 8、生命周期和作用域

作用域和生命周期类别是至关重要的，因为错误的使用会导致非常严重的**并发问题**。

![image-20210505203924910](C:\Users\Think\AppData\Roaming\Typora\typora-user-images\image-20210505203924910.png)

##### SqlSessionFactoryBuilder:

- 一旦创建SqlSessionFactory，就不需要它了
- 局部变量

##### SqlSessionFactory

- 可以想象为：数据库连接池
- SqlSessionFactory 一旦被创建就应该在应用的运行期间一直存在，**没有任何理由丢弃它或重新创建另一个实例。**
- SqlSessionFactory 的最佳作用域是应用作用域，全局
- 最简单的就是使用单例模式或者静态单例模式

#### SqlSession

- 连接到请求池的一个请求！
- SqlSession 的实例不是线程安全的，因此是不能被共享的，最佳的作用域是请求或方法作用域

- 用完之后需要干净关闭，否则资源被占用！

![image-20210505204830129](C:\Users\Think\AppData\Roaming\Typora\typora-user-images\image-20210505204830129.png)

这里面每一个Mapper，就代表一个具体的业务!

### 5、解决属性名和字段名不一致的问题

#### 1、问题

数据库中的字段与属性名不一致

![image-20210505211020925](C:\Users\Think\AppData\Roaming\Typora\typora-user-images\image-20210505211020925.png)

```java
	public class User {
    private int id;
    private String name;
    private String password;
```

测试出现问题

![image-20210505211807156](C:\Users\Think\AppData\Roaming\Typora\typora-user-images\image-20210505211807156.png)

解决方法：

- 起别名

```JAVA
// select  * from user where id = #{id};
//类型处理器
//select  id, name,pwd as password from user where id = #{id};
```

#### 2、resultMap

结构集映射

```java
<!--    结果集合映射-->
    <resultMap id="UserMap" type="User">
<!--        column数据中的字段，property实体类中的属性-->
        <result column="id" property="id"/>
        <result column="name" property="name"/>
        <result column="pwd" property="password"/>
    </resultMap>
```

### 6、日志

##### 6.1、日志工厂

如果一个数据操作，出现了异常，需要排错。日志是最好的助手！

- SLF4J
- LOG4J 【掌握】
- Apache Commons Logging
- Log4j 2
- Log4j
- STDOUT_LOGGING 【掌握】
- NO_LOGGING
- JDK logging

在Mybatis中具体使用个以日志实现，在设置中设定

**STDOUT_LOGGING标准日志输出**

在mybatis核心配置文件中，配置我们的日志！

```xml
<settings>
        <setting name="logImpl" value="STDOUT_LOGGING"/>
    </settings>
```



![image-20210510092318678](C:\Users\Think\AppData\Roaming\Typora\typora-user-images\image-20210510092318678.png)

##### 6.2、Log4j

- Log4j是[Apache](https://baike.baidu.com/item/Apache/8512995)的一个开源项目，通过使用Log4j，我们可以控制日志信息输送的目的地是[控制台](https://baike.baidu.com/item/控制台/2438626)、文件、[GUI](https://baike.baidu.com/item/GUI)组件

- 控制每一条日志的输出格式
- 定义每一条日志信息的级别
- 通过一个[配置文件](https://baike.baidu.com/item/配置文件/286550)来灵活地进行配置，而不需要修改应用的代码

1、导入log4j的包

```xml
   <!-- https://mvnrepository.com/artifact/log4j/log4j -->
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.17</version>
        </dependency>
```

2、log4j.properties

```properties
######################## 将等级为DEBUG的日志信息输出到consoleh和file两个目的地, console和file的定义在下面的代码
log4j.rootLogger=DEBUG,console,file

########################控制台输出的相关设置
log4j.appender.console = org.apache.log4j.ConsoleAppender
#在控制台输出
log4j.appender.console.Target = System.out
#在DEBUG级别输出
log4j.appender.console.Threshold = DEBUG
log4j.appender.console.layout = org.apache.log4j.PatternLayout
#日志格式
log4j.appender.console.layout.ConversionPattern=[%c]-%m%n

######################文件输出的相关配置
log4j.appender.appender1=org.apache.log4j.RollingFileAppender
log4j.appender.appender1.MaxFileSize=1KB
log4j.appender.file.Threshold = DEBUG
log4j.appender.appender1.File=./logs/log.txt
log4j.appender.appender1.layout=org.apache.log4j.PatternLayout
log4j.appender.appender1.layout.ConversionPattern=[%p][%d{yy-MM-dd}][%c]%m%n


######################日志输出级别
log4j.logger.org.mybatis=DEBUG
log4j.logger.java.sql=DEBUG
log4j.logger.java.sql.Statement=DEBUG
log4j.logger.java.sql.ResultSet=DEBUG
log4j.logger.java.sql.PreparedStatement=DEBUG

```

3、配置log4j为日志的实现

```xml
    <settings>
<!--        标准的日志工厂输出模式-->
<!--        <setting name="logImpl" value="STDOUT_LOGGING"/>-->
        <setting name="logImpl" value="LOG4J"/>
    </settings>
```

**简单实用**

1、log4j的使用，导入包 import org.apache.log4j.Logger;

2、日志对象，参数为当前类的class

```java
static Logger logger = Logger.getLogger(UserDaoTest.class); //LogDemo为相关的类
```

3、日志级别

```java
  logger.info("info:进入");
  logger.debug("debug:");
  logger.error("error:");
```

### **7、分页**

思考：为什么分页？

- 减少数据的处理量

**1、使用limit分页**

```sql
语法： SELECT * from user limit startIndex, pageSize; //初始偏移量为1
SELECT * from user limit 3;[1,n]
每一页显示pageSzie的数量，从startIndex开始查

如果第二个参数为 -1表示从当前位置到最好一项的查询，不过现在这个-1参数变为bug
```

**2、Mapper.xml**

```xml
<select id="getUserByLimit" parameterType="map" resultMap="UserMap">
    select * from user limit #{startIndex},#{pageSize}
</select>
```

3、测试

```java
@Test
public void getUserByLimit(){
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    Map<String, Integer> map = new HashMap<>();
    map.put("startIndex",0);
    map.put("pageSize",2);

    List<User> userByLimit = mapper.getUserByLimit(map);
    userByLimit.stream().forEach(System.out::println);
    sqlSession.close();
}
```

### 8、注解开发

1、注解在接口上实现

```java
@Select("select * from user where id = #{id}")
List<User> getUsers();
```

2、需要在核心配置文件中绑定接口

```xml
    <mappers>
<!--        <mapper resource="com/kuang/dao/UserMapper.xml"/>-->
        <mapper class="com.kuang.dao.UserMapper" />
    </mappers>
```

3、测试

本质：反射机制实现

底层：动态代理！手写代理模式

![image-20210514100033538](C:\Users\Think\AppData\Roaming\Typora\typora-user-images\image-20210514100033538.png)

关于@Param()注解

- 基本类型的参数或者String类型，需要加上
- 引用类型不需要加
- 若只有一个基本类型可以忽略，但是建议大家都加上
- 我们在SQL中引用的就是我们这里的@Param()中设定的属性值！



#{}

### 9、Lombok

```java
//Lombok注解
@Getter and @Setter
@FieldNameConstants
@ToString //生成一个toString方法
@EqualsAndHashCode
@AllArgsConstructor, @RequiredArgsConstructor and @NoArgsConstructor  //有参无参构造
@Log, @Log4j, @Log4j2, @Slf4j, @XSlf4j, @CommonsLog, @JBossLog, @Flogger, @CustomLog
@Data //生成各个属性 getter 和 setter的方法
@Builder
@SuperBuilder
@Singular
@Delegate
@Value
@Accessors
@Wither
@With
@SneakyThrows
@val
@var
experimental @var
@UtilityClass
Lombok config system
Code inspections
Refactoring actions (lombok and delombok)
```

### 10、 多对一处理

#### 测试环境搭建

1、导入lombok

2、新建实体类Teacher,Student

3、建立Mapper接口

4、建立Mapper.xml文件

5、在核心配置文件中绑定注册Mapper接口或者文件！

6、测试接口是否能够成功

需要创建 com.kuang.dao (三层路径)  不然不知道在目标文件下生成mapper.xml 和 mapper在同一路径下

#### 按照查询嵌套处理

通过子查询的方式再查了一次

```xml
<!--
    思路：
    1、查询所有的学生信息
    2、根据查询出来的学生tid，寻找对应的老师！
-->
    <!--select查询语句-->
    <resultMap id="StudentTeacher" type="Student">
        <result property="id" column="id" />
        <result property="name" column="name"/>
<!--        复杂对的属性，我们需要单独处理 对象：association  集合：collection-->
        <association property="teacher" column="tid" javaType="Teacher" select="getTeacher"/>
    </resultMap>

    <select id="getTeacher" resultType="Teacher">
        select * from teacher where id=#{id}
    </select>

    <select id="getStudent" resultMap="StudentTeacher">
        select * from student;
    </select>
```

#### 按照结果进行嵌套查询

查询写完后，只要对应里面的每一项的关系就行啦

```xml
<select id="getStudent2" resultMap="StudentTeacher2">
    select s.id sid, s.name sname, t.name tname
    from student s, teacher t
    where s.tid = t.id
</select>

<resultMap id="StudentTeacher2" type="Student">
    <result property="id" column="sid"/>
    <result property="name" column="sname"/>
    <association property="teacher" javaType="Teacher">
        <result property="name" column="tname"/>
    </association>
</resultMap>
```

回顾Mysql的多对一查询

- 联表查询
- 子查询

### 11、一对多处理

比如：一个老师拥有多个学生！

```java
@Data
public class Student {
    private int id;
    private String name;

    //学生需要关联一个老师
    private int tid;
}
```

```java
@Data
public class Teacher {
    private int id;
    private String name;
    private List<Student> students;

}
```



按照结构嵌套处理, **需要注意的是list使用** **ofType**

```xml
<resultMap id="TeacherStudent" type="Teacher">
    <result property="id" column="tid"/>
    <result property="name" column="tname"/>
    <!--        复杂对的属性，我们需要单独处理 对象：association  集合：collection
        javaType=""指定属性的类型！
        集合中的泛型信息，我们使用ofType获取-->
    <collection property="students" ofType="Student">
        <result property="id" column="sid"/>
        <result property="name" column="sname"/>
        <result property="tid" column="tid"/>
    </collection>
</resultMap>

<select id="getTeacherById" resultMap="TeacherStudent">
    select  t.name tname, t.id tid, s.id sid, s.name sname
    from student s, teacher t
    where s.tid = t.id and t.id = #{tid}
</select>
```

#### 小结：

1、关联- association [多对一]

2、集合- collection [一对多]

3、javaType & ofType

​	1、javaType用来指定实体类中属性的类型

​	2、ofType 用来指定隐射到List或者集合中pojo的类型，泛型中的约束类型！



注意点：

- 保证SQL的可读性，尽量保证通俗易懂
- 注意一对多和多对一中，属性和字段的问题！
- 如果问题不好排查，可以使用日志，简易使用log4j



#### 面试高频：

- Mysql引擎
- InnoDB底层原理
- 索引
- 索引优化

### 12、动态SQL

什么是动态sql,可以根据通过条件进行动态的拼接动态SQL

#### **IF**

```xml
<select id="queryByName" parameterType="map" resultType="Student">
    select * from student
    where 1=1
    <if test="name != null">
        and `name` like CONCAT('%', #{name},'%')
    </if>
    <if test="tid != null">
        and tid = #{tid}
    </if>
</select>
```



#### choose(where,set)

```xml
<select id="queryByName" parameterType="map" resultType="Student">
    select * from student
    <where>
        <if test="name != null">
            and `name` like CONCAT('%', #{name},'%')
        </if>
        <if test="tid != null">
            and tid = #{tid}
        </if>
    </where>
</select>
```

![image-20210518204819291](C:\Users\Think\AppData\Roaming\Typora\typora-user-images\image-20210518204819291.png)

***set* 元素会动态地在行首插入 SET 关键字，并会删掉额外的逗号**

#### trim

前缀 prefix

```xml
<trim prefix="WHERE" prefixOverrides="AND |OR ">
  ...
</trim>
```

后缀 suffix

```xml
<trim prefix="SET" suffixOverrides=",">
  ...
</trim>
```

所谓的动态SQL，本质还是SQL语句，只是我们可以在SQL层面，去执行一个逻辑代码

#### sql片段

通过提取一部分sql语句，将其复用。 <sql id =""> </sql>

用的时候 <include refid=""/>

1、使用SQL标签抽取公共部分		2、在需要使用的时候使用include标签引用即可

```xml
  <sql id="Base_Column_List">
    base_station_id, device_mac, police_id, name, band_content, band_point, work_mode, 
    cell_status, tac_period, pci, sniffer1, sniffer2, sniffer3, sniffer4, device_id, 
    bandwidth, Longitude, Latitude, create_time, update_time,location
  </sql>
  <select id="selectByExample" parameterType="com.example.pojo.BaseStationExample" resultMap="BaseResultMap">
    select
    <if test="distinct">
      distinct
    </if>
    'true' as QUERYID,
    <include refid="Base_Column_List" />
    from base_station
    <if test="_parameter != null">
      <include refid="Example_Where_Clause" />
    </if>
    <if test="orderByClause != null">
      order by ${orderByClause}
    </if>
  </select>
```

#### foreach

传入的是集合，可以使用集合

```xml
<delete id="deleteByIds" parameterType="java.util.List">
    delete from base_station
    where base_station_id in
    <foreach collection="list" item="item" open="(" separator="," close=")">
        #{item,jdbcType=INTEGER}
    </foreach>
</delete>
```

== 动态sql==

### mybatis三剑客

- Mybatis-generator 自动化生成数据库交互代码

  在resources文件夹下面配置mybaits-generator 

- Mybatis-plugin 	

  Mybatis-pagehelper Mybatis非常好用的分页组件

通过mybatis-generator 生成 pojo、dao、对应的xml文件

在插件中点击  mybatis-generator:generate

<**sql** **id=** **"Base_Column_List"** >

​    id, username, password, email, phone, question, answer, role, create_time, update_time

</**sql**>

使用

select

<**include** **refid=** **"Base_Column_List"**/>

from mmall_user

where id = #{id,jdbcType=INTEGER}