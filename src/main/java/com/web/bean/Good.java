package com.web.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Good {
    private String goodName;
    private Integer goodId;
    private Integer goodType;
    private Double goodSale;
    /**
     * 品牌
     */
    private String goodMark;
    private String createTime;
    private Integer goodTenantId;
}
