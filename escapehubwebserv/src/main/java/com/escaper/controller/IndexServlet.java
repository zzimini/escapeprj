package com.escaper.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/index")
public class IndexServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().println("""
            <!DOCTYPE html>
            <html>
            <head><meta charset='UTF-8'><title>EscapeHub</title></head>
            <body>
                <h1>🔐 EscapeHub - 방탈출 예약 시스템</h1>
                <a href='login.jsp'>로그인</a> | 
                <a href='register.jsp'>회원가입</a>
            </body>
            </html>
        """);
    }
}
