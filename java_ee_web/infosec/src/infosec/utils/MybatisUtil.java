package infosec.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

//SqlSessionFactory构建实例
public class MybatisUtil {
    private static SqlSessionFactory sqlSessionFactory;
    //静态代码块读取数据库配置文件
    static{String resource = "mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //返回一个SqlSession实例对象
    public static SqlSession getSqlsession(){
        return sqlSessionFactory.openSession();
    }

}
