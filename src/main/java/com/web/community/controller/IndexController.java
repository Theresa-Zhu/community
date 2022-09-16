package com.web.community.controller;

import com.web.community.mapper.UserMapper;
import com.web.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/")
    public String index(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            if (cookie.getName().equals("token")){
                String token = cookie.getValue();//请求拿到 key为token的值
                User user = userMapper.findByToken(token);//查找相对应的用户
                if (user != null){
                    //如果不为空，就返回该用户的信息，否则展示为登录
                    request.getSession().setAttribute("user",user);
                }
                break;
            }
        }
        return "index";
    }
}
