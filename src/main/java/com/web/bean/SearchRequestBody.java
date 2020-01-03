package com.web.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequestBody {
    /**
     * 搜索内容
     */
    private String content;
    /**
     * 价格排序
     */
    private String orderByPrice;
    private String orderByNum;
    /**
     * 综合排序
     */
    private String orderBycomposite;
    /**
     * 商品明细类型
     */
    private int detailType;
    /**
     * 商品大类类型
     */
    private int goodType;
    private int pageIndex;
    private int pageSize;
}
