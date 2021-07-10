package infosec.dao;

import infosec.model.User;
import infosec.utils.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import java.util.List;

public class UserDaoTest {

    @org.junit.Test
    //测试select *语句
    public void testSqlQuery() {
        SqlSession sqlSession = MybatisUtil.getSqlsession();
        try {
            //将sql与接口相连
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            //执行sql并取得结果集
            User user = userMapper.getUser("root");
            //遍历输出结果集
            System.out.println(user.getShowname());
        } finally {
            //官方文档建议使用fianlly关闭sqlSession以保证数据链接的安全
            sqlSession.close();
        }
    }
}
