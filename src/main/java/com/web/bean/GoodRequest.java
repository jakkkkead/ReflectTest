package com.web.bean;

import lombok.Data;

@Data
public class GoodRequest {
    private String url;
    private String goodMark;
    private int id;
    private int goodType;
}
