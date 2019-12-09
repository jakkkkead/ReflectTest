package com.web.util;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

public class ElasticClientUtil {
    public static RestHighLevelClient getElasticClient(){
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("192.168.88.128", 9200, "http")));
        return client;
    }
}
