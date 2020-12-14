package com.bjpowernode.crm.web.filter;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        //进入到过滤字符编码的过滤器
        System.out.println("进入到过滤字符编码的过滤器");

        //过滤post请求中文乱码
        servletRequest.setCharacterEncoding("utf-8");
        //过滤响应流中文乱码
        servletResponse.setContentType("text/html;charset=utf-8");

        //将请求放行
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
