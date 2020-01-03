package com.web.service;

import java.io.IOException;
import java.util.Map;

public interface GoodDataSynService {
    public boolean synGoodData(String start,String end) throws IOException;
    public void getJDGoodData(String url, Map<String,String> param);
}
