package com.bjpowernode.crm.web.filter;


import com.bjpowernode.crm.settings.entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("进入到验证用户是否由登录页进来的拦截器");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getServletPath();
        if ("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){

            //如果是登录操作，则直接放行
            filterChain.doFilter(servletRequest,servletResponse);

        }else{

            //不是登录操作，需要验证
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if (user != null){
                //程序执行到此处，说明用户是通过登录界面正常访问的,放行
                filterChain.doFilter(servletRequest,servletResponse);
            }else{
                //程序执行到此处，说明用户没有通过登录界面正常访问，则，重定向到登录页
                /**
                 * 1、拿到项目名  /crm
                 *  request.getContextPath()
                 */

                response.sendRedirect(request.getContextPath()+"/login.jsp");
            }
        }




    }
}
