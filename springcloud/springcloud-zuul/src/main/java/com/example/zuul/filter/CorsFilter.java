package com.example.zuul.filter;

import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CorsFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {

            HttpServletRequest hreq = (HttpServletRequest) servletRequest;



            HttpServletResponse hresp = (HttpServletResponse) servletResponse;

            //跨域
            hresp.setHeader("Access-Control-Allow-Origin", "*" +
                    "");


            //跨域 Header

            hresp.setHeader("Access-Control-Allow-Methods", "*");

            hresp.setHeader("Access-Control-Allow-Headers", "Content-Type,XFILENAME,XFILECATEGORY,XFILESIZE");



            // 浏览器是会先发一次options请求，如果请求通过，则继续发送正式的post请求

            // 配置options的请求返回

            if (hreq.getMethod().equals("OPTIONS")) {

                hresp.setStatus(HttpStatus.SC_OK);

                // hresp.setContentLength(0);

                hresp.getWriter().write("OPTIONS returns OK");

                return;

            }

            // Filter 只是链式处理，请求依然转发到目的地址。

            filterChain.doFilter(servletRequest, servletResponse);

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    @Override
    public void destroy() {

    }
}
