package infosec.controller;

import infosec.model.LoginVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class LogoutController {
    @RequestMapping(value="/logout",method = RequestMethod.GET)
    public void login(HttpServletRequest request, HttpServletResponse resp) throws Exception {
       if (request.getSession().getAttribute("user")!=null){
        request.getSession().removeAttribute("user");}

       resp.sendRedirect("/infosec/unLogin.jsp");
    }
}
