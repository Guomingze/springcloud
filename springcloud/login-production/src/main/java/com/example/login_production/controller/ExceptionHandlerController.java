package com.example.login_production.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(value = Exception.class)
    public void defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        System.out.println("=====================全局异常信息捕获=======================");
        System.err.println(ex.getMessage());
        PrintWriter writer = null;
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setCharacterEncoding("UTF-8");
            writer = httpServletResponse.getWriter();
            writer.write("令牌无效,请重新登录");
        } catch (IOException e) {

        }finally {
        	if(writer != null) {
        		writer.close();
        	}
        }
    }
}
