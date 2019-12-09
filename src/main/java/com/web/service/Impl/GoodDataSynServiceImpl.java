package com.web.service.Impl;

import com.web.dao.GoodDao;
import com.web.service.GoodDataSynService;
import com.web.util.ElasticClientUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class GoodDataSynServiceImpl implements GoodDataSynService {
    @Autowired
    private GoodDao goodDao;
    @Override
    public boolean synGoodData(String start, String end) throws IOException {
        List<Map<String,Object>> list = goodDao.getDataByDate(start,end);
        if (CollectionUtils.isEmpty(list)){
            return false;
        }
        RestHighLevelClient client = ElasticClientUtil.getElasticClient();
        BulkRequest bulkRequest = new BulkRequest();
        for (Map<String,Object> map : list){
            IndexRequest indexRequest = new IndexRequest("good_index","t_good",""+map.get("good_id"))
                                                        .source(map);
            bulkRequest.add(indexRequest);
        }

        BulkResponse res = client.bulk(bulkRequest);
        System.out.println(res.status());
        return true;
    }
}
