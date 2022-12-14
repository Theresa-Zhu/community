package com.web.community.service;
import com.web.community.dto.PaginationDTO;
import com.web.community.dto.QuestionDTO;
import com.web.community.dto.QuestionQueryDTO;
import com.web.community.exception.CustomizeErrorCode;
import com.web.community.exception.CustomizeException;
import com.web.community.mapper.QuestionExtMapper;
import com.web.community.mapper.QuestionMapper;
import com.web.community.mapper.UserMapper;
import com.web.community.model.Question;
import com.web.community.model.QuestionExample;
import com.web.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    //分页
    public PaginationDTO list(String search,Integer page, Integer size) {
        if (StringUtils.isNotBlank(search)){
            String[] tags = StringUtils.split(search," ");
            search = Arrays.stream(tags).collect(Collectors.joining("|"));
        }

        PaginationDTO paginationDTO =  new PaginationDTO();
        Integer totalPage;

        //总记录
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);
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
        QuestionExample questionExample = new QuestionExample();
        questionExample.setOrderByClause("gmt_create desc");
        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(offset);
        List<Question> questionList = questionExtMapper.selectBySearch(questionQueryDTO);
        //最终展示的数据
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questionList) {
            User user =   userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO =  new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO); //可以免去各种getset
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }

    //我的问题
    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO paginationDTO =  new PaginationDTO();
        Integer totalPage;

        //总记录
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(questionExample);
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

        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));
        //最终展示的数据
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questionList) {
            User user =   userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO =  new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO); //可以免去各种getset
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null){
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            questionMapper.insert(question);
        }else {
            //更新
            question.setGmtModified(question.getGmtCreate());
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setTag(question.getTag());

            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int update = questionMapper.updateByExampleSelective(updateQuestion, example);
            if (update != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    //阅读数
    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }

    //相关问题
    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if (StringUtils.isBlank(queryDTO.getTag())){
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(queryDTO.getTag(),",");
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);

        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS = questions.stream().map(q ->{
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q,questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());

        return questionDTOS;
    }
}
