package com.web.community.controller;

import com.web.community.dto.PaginationDTO;
import com.web.community.dto.QuestionDTO;
import com.web.community.mapper.QuestionMapper;
import com.web.community.mapper.UserMapper;
import com.web.community.model.Question;
import com.web.community.model.User;
import com.web.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model,
                        @RequestParam(name = "page",defaultValue = "1")Integer page,
                        @RequestParam(name = "size",defaultValue = "5")Integer size){


        PaginationDTO  pagination = questionService.list(page,size);
        model.addAttribute("pagination",pagination);

        return "index";
    }
}
