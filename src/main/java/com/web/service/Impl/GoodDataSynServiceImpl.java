package com.web.service.Impl;

import com.web.bean.Good;
import com.web.dao.GoodDao;
import com.web.service.GoodDataSynService;
import com.web.util.DateUtils;
import com.web.util.ElasticClientUtil;
import com.web.util.HttpClientUtils;
import io.micrometer.core.instrument.util.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
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

    @Override
    public void getJDGoodData(String url, Map<String, String> param) {
        String html = HttpClientUtils.sendGet(url, null, null);
        List<Good> list = new ArrayList<>();
        if(!StringUtils.isBlank(html)) {
            Document document = Jsoup.parse(html);
            Elements elements = document.select("ul[id=J_PicMode]").select("li[data-follow-id]");
            for(Element element : elements) {
                Elements img = element.select("a[class=pic]").select("img");
                String imgUrl = img.get(0).attr(".src");
                String goodName = img.get(0).attr("alt");
                String goodId = element.attr("data-follow-id").substring(1);
                System.out.println(goodId);
                String remark = element.select("h3").select("a").select("span").get(0).text();
                Elements prices = element.select("div[class=price-row]").select("span[class=price price-normal]");
                if (prices.size()==0){
                    prices = element.select("div[class=price-row]").select("span[class=price price-na]");
                }
                if (prices.size() == 0){
                    continue;
                }
                String price = prices.get(0).select("b[class=price-type]").get(0).text();
                if (price.contains("万")){
                     price = price.substring(0,price.indexOf("万"));
                     price = Double.parseDouble(price)*10000+"";
                }
                Good good = new Good(imgUrl,goodName,Integer.parseInt(goodId),Integer.parseInt(param.get("goodType")),
                        Double.valueOf(price),param.get("goodMark"), DateUtils.getNowDate(),
                        Integer.parseInt(param.get("id")),remark,Integer.parseInt(param.get("detail_type")));
                list.add(good);
            }
            int i = goodDao.batchInsert(list);
            if (i == list.size()){
                System.out.println("全部插入成功,总计："+i+" 条记录");
            }else {
                System.out.println("插入失败");
            }
        }

    }
}
