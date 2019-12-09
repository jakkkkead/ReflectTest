package com.web.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class PageInfo<T> {
    private int pageNumber;
    private int pageSize;
    private List<T> list;
    public PageInfo(){}
    public PageInfo (List<T> data){
        this.list = data;
    }
    public PageInfo (int pageNumber,int pageSize){
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }
}
