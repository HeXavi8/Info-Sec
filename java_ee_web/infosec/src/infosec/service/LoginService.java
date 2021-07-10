package infosec.service;

import infosec.dao.UserMapper;
import infosec.model.LoginVO;
import infosec.model.User;
import infosec.utils.*;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;


@Service
public class LoginService {

    @Autowired
    private UserMapper userMapper;

    public Boolean loginAuth(LoginVO loginUser) throws Exception {
        //取得mybatis工具
        SqlSession sqlSession = MybatisUtil.getSqlsession();
        // 使用私钥解密密码
        //String dePassword= RSAEncrypt.decrypt(loginUser.getPassword());

        try {
            //将sql与接口相连
            userMapper = sqlSession.getMapper(UserMapper.class);
            //执行sql并取得登录用户信息
            System.out.println(loginUser.getUsername());
            User user = userMapper.getUser(loginUser.getUsername());

            if (user != null) {
                BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();

                if(bCrypt.matches(loginUser.getPassword(),user.getPassword())){
                    return true;
                }
                System.out.println("密码错误");
                return false;

            } else {
                System.out.println("用户名不存在");
                return false;
            }
        } catch (Exception ex) {
            // 捕获异常
            System.out.println(ex.getStackTrace());
            return false;
        } finally {
            //官方文档建议使用fianlly关闭sqlSession以保证数据链接的安全
            sqlSession.close();
        }
    }
    @Test
    public void test() throws Exception {
        byte[] serpbBytes=RSApem.getPublicKeyFromPem(SystemConfig.PUBLIC_KEY).getEncoded();
        String serpbString=new String(Base64.getEncoder().encode(serpbBytes));
        String abstractWithHuanhang=PasswordHelper.signatureWithSHA1_RSA(serpbString.getBytes());
        String signature=abstractWithHuanhang.replaceAll("\r|\n","");

        System.out.println(signature);
    }

}
