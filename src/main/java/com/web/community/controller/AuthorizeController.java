package com.web.community.controller;

import com.web.community.dto.AccessTokenDTO;
import com.web.community.dto.GithubUser;
import com.web.community.mapper.UserMapper;
import com.web.community.model.User;
import com.web.community.provider.GithubProvider;
import com.web.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@Slf4j
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecrect;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserService userService;

    @GetMapping("/callback")
    public String callBack(@RequestParam(name = "code")String code,
                           @RequestParam("state")String state,
                           HttpServletRequest request,
                           HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecrect);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_url(redirectUri);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if (githubUser != null && githubUser.getId() != null){  //获取github的用户信息
            System.out.println("头像->"+githubUser.getAvatarUrl());
            //登录成功
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setAvatarUrl(githubUser.getAvatarUrl());
            //把该用户信息写入或更新数据库中
            userService.createOrUpdate(user);
            //写cookie 和 session
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        }else {
            //登录失败，重新登录
            log.error("callback get github error,{}",githubUser);
            return "redirect:/";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        request.getSession().removeAttribute("user");
        //清除cookie
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
