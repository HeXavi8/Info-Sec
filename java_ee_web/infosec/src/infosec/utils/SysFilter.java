package infosec.utils;

import infosec.model.LoginVO;

import javax.servlet.*;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class SysFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //转换请求和响应数据类型为HttpServlet形式
        HttpServletRequest req= (HttpServletRequest) servletRequest;
        HttpServletResponse resp= (HttpServletResponse) servletResponse;

        //从session中获得user的信息
        Object user = (LoginVO) req.getSession().getAttribute("user");
        //若未登录或者注销了用户则只能进入登陆页面
        if(user==null){resp.sendRedirect("/infosec/unLogin.jsp");}
        //登录了则通过权限过滤器
        else{filterChain.doFilter(servletRequest,servletResponse);}

    }

    public void destroy() {

    }
}
