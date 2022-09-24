package com.web.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class PaginationDTO<T> {
    private List<T> data;  //总条目
    private boolean showPrevious;   //展示上一页
    private boolean showFirstPage;  //展示首页
    private boolean showNext;       //展示下一页
    private boolean showEndPage;    //展示尾页
    private Integer page;           //当前页
    private List<Integer> pages = new ArrayList<>();  //当前展示的情况
    private Integer totalPage;  //总页数

    public void setPagination(Integer totalPage,Integer page){
        this.totalPage = totalPage;
        this.page = page;  //更新当前页数情况

        //因为普通情况，在当前页前面展示三页，其后面展示三页，总共七页
        pages.add(page);
        for (int i = 1; i <= 3;i++){
            if (page - i > 0){  //当前页前面有多少页
                pages.add(0,page - i);
            }
            if (page + i <= totalPage){
                pages.add(page + i);  //当前页后面有多少页
            }
        }

        //是否展示上一页
        if (page == 1){
            showPrevious = false;
        }else {
            showPrevious = true;
        }

        //是否展示下一页
        if (page == totalPage){
            showNext = false;
        }else {
            showNext = true;
        }

        //是否展示第一页
        if (pages.contains(1)){
            showFirstPage = false;
        }else {
            showFirstPage = true;
        }

        //是否展示最后一页
        if (pages.contains(totalPage)){
            showEndPage = false;
        }else {
            showEndPage = true;
        }
    }
}
