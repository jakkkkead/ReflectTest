package com.web.service.Impl;

import com.web.bean.Good;
import com.web.bean.PageInfo;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
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
    public List<Map<String, Object>> searchByNameOrMark(String context) throws IOException {
        RestHighLevelClient elasticClient = ElasticClientUtil.getElasticClient();
        SearchRequest searchRequest = new SearchRequest("good_index");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        builder.should(QueryBuilders.matchQuery("good_name",context));
        builder.should(QueryBuilders.matchQuery("good_mark",context));
        sourceBuilder.query(builder);
        searchRequest.source(sourceBuilder);
        SearchResponse res = elasticClient.search(searchRequest);
        SearchHits hits = res.getHits();
        List<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : hits){
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            list.add(sourceAsMap);

        }
        return list;
    }
}
