package infosec.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import infosec.model.DigitalCertification;
import infosec.model.LoginVO;
import infosec.service.LoginService;
import infosec.utils.RSApem;
import infosec.utils.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;


    @RequestMapping(value="/login",method = RequestMethod.POST)
    public String login(@RequestBody LoginVO loginUser, HttpServletRequest request) throws Exception {
        //System.out.println(loginUser.getUsername()+"\n"+loginUser.getPassword());
        try {
            //登录成功
            if (loginService.loginAuth(loginUser)) {
                HttpSession session=request.getSession();
                session.setAttribute("user",loginUser);

                return "home";
            }
            //登录失败
            else {
                return "index";
            }
        } catch (Exception ex) {
            // 如果有任何错误，返回系统错误页面 error.jsp
            return "error";
        }
    }

    @RequestMapping(value = "/handshaking",method = RequestMethod.GET)
    public String handshaking(Model model) throws Exception {
        DigitalCertification dc=DigitalCertification.getInstance();
        System.out.println("发送数字证书认证");

        model.addAttribute("signature",dc.getSignature());
        model.addAttribute("content",dc.getContent());
        model.addAttribute("CApublicKey",dc.getCApublicKey());
        return "login";
    }

    @RequestMapping(value ="/test",method =RequestMethod.POST)
    public String test(Model model)throws Exception{
        DigitalCertification dc=DigitalCertification.getInstance();
        System.out.println("发送数字证书认证");

        String CApr=new String(Base64.getEncoder().encode(RSApem.getPrivateKeyFromPem(SystemConfig.CA_PRIVATE_KEY).getEncoded()));
        model.addAttribute("CAprivateKey",CApr);
        model.addAttribute("signature",dc.getSignature());
        model.addAttribute("content",dc.getContent());
        model.addAttribute("CApublicKey",dc.getCApublicKey());
        return "keytest";
    }


}
