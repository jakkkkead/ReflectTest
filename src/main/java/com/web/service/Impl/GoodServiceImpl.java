package com.web.service.Impl;

import com.web.bean.Good;
import com.web.bean.PageInfo;
import com.web.bean.SearchRequestBody;
import com.web.dao.GoodDao;
import com.web.service.GoodService;
import com.web.util.ElasticClientUtil;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodServiceImpl implements GoodService {
    @Autowired
    private GoodDao goodDao;
    @Override
    public int add(Good good) {
        int i = goodDao.add(good);
        return i;
    }

    @Override
    public List<Good> listGoods(PageInfo pageInfo) {
        List<Good> goods = goodDao.listGood(pageInfo);
        return goods;
    }

    @Override
    public int getTotal() {
        return goodDao.getTotal();
    }

    @Override
    public PageInfo searchByNameOrMark(SearchRequestBody param) throws IOException {
        RestHighLevelClient elasticClient = ElasticClientUtil.getElasticClient();
        SearchRequest searchRequest = new SearchRequest("good_index");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        //在商品名称，品牌，备注中搜索关键词
        if (!StringUtils.isEmpty(param.getContent())){
            builder.should(QueryBuilders.matchQuery("good_name",param.getContent()));
            builder.should(QueryBuilders.matchQuery("good_mark",param.getContent()));
            builder.should(QueryBuilders.matchQuery("remark",param.getContent()));
            builder.should(QueryBuilders.matchQuery("detail_name",param.getContent()));
        }
        if (param.getDetailType() != 0){
            builder.must(QueryBuilders.termQuery("detail_type",param.getDetailType()));
            builder.must(QueryBuilders.termQuery("good_type",param.getGoodType()));
        }
        //排序
        if (!StringUtils.isEmpty(param.getOrderByPrice())){
            sourceBuilder.sort("good_sale", SortOrder.valueOf(param.getOrderByPrice()));
        }
        //分页
        sourceBuilder.query(builder).from(param.getPageIndex()).size(param.getPageSize());
        searchRequest.source(sourceBuilder);
        SearchResponse res = elasticClient.search(searchRequest);
        SearchHits hits = res.getHits();
        Long total = hits.totalHits;
        if (total > 0){
            List<Map<String, Object>> list = new ArrayList<>();
            for (SearchHit hit : hits){
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                list.add(sourceAsMap);
            }
            PageInfo pageInfo = new PageInfo(list);
            pageInfo.setTotal(total);
            return pageInfo;
        }else {
            return null;
        }
    }

    @Override
    public Map<String, Map<String, String>> getTypeList() {
        List<Map<String, String>> typeList = goodDao.getTypeList();
        Map<String, Map<String, String>> res = new HashMap<>();
        for (Map<String, String> detail : typeList){
            String goodType = detail.get("good_type");
            detail.remove("good_type");
            res.put(goodType,detail);
        }
        return res;
    }
}
