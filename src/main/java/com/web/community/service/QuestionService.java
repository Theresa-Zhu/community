package com.web.community.service;

import com.web.community.dto.PaginationDTO;
import com.web.community.dto.QuestionDTO;
import com.web.community.mapper.QuestionMapper;
import com.web.community.mapper.UserMapper;
import com.web.community.model.Question;
import com.web.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;

    //分页
    public PaginationDTO list(Integer page, Integer size) {
        PaginationDTO paginationDTO =  new PaginationDTO();
        Integer totalPage;

        //总记录
        Integer totalCount = questionMapper.count();
        if (totalCount % size == 0){  //总页数能装满所有条目
            totalPage = totalCount / size;
        }else {  //装不完，加多一页
            totalPage = totalCount / size + 1;
        }

        if (page < 1){ //当前页数小于1，返回首页
            page = 1;
        }
        if (page > totalPage){ //当前页数大于总页数，返回尾页
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage,page);
        Integer offset = size * (page - 1); //偏移量
        List<Question> questionList = questionMapper.list(offset,size);
        //最终展示的数据
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questionList) {
            User user =   userMapper.findById(question.getCreator());
            QuestionDTO questionDTO =  new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO); //可以免去各种getset
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }

    //我的问题
    public PaginationDTO list(Integer userId, Integer page, Integer size) {
        PaginationDTO paginationDTO =  new PaginationDTO();
        Integer totalPage;

        //总记录
        Integer totalCount = questionMapper.countByUserId(userId);
        if (totalCount % size == 0){  //总页数能装满所有条目
            totalPage = totalCount / size;
        }else {  //装不完，加多一页
            totalPage = totalCount / size + 1;
        }

        if (page < 1){ //当前页数小于1，返回首页
            page = 1;
        }
        if (page > totalPage){ //当前页数大于总页数，返回尾页
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage,page);
        Integer offset = size * (page - 1); //偏移量
        List<Question> questionList = questionMapper.listByUserId(userId,offset,size);
        //最终展示的数据
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questionList) {
            User user =   userMapper.findById(question.getCreator());
            QuestionDTO questionDTO =  new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO); //可以免去各种getset
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }
}
