package com.web.service.Impl;

import com.web.dao.Test;
import com.web.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusinessServiceImpl implements BusinessService {
    @Autowired
    public Test test;
    @Override
    public Integer getVersionById(int id) {
        Integer version = test.getVersion(id);
        return version;
    }
}
