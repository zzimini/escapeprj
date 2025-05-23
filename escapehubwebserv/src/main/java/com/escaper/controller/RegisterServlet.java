package com.escaper.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.escaper.model.UserDAO;
import com.escaper.model.UserDTO;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        UserDAO dao = new UserDAO();
        dao.registerUser(new UserDTO(userId, password));

        response.sendRedirect("login.jsp");
    }
}
