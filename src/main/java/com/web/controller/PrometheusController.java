package com.web.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.prometheus.client.Summary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *与promethues结合，四大数据类型
 * count：统计值，只能增加
 * gauge：可增可减
 * summary
 *
 */
@RestController
@RequestMapping("pro")
public class PrometheusController {
    /**
     * 注册表
     */
    @Autowired
    MeterRegistry registry;
    private Counter counter;
    private DistributionSummary summary;
    //private  Counter counter2 = Counter.builder("test_total").tags("a","help").register(registry);
    @PostConstruct
    public void init(){
        //为注册表添加通用标签
        //registry.config().commonTags("commomsdf","conmomRegistry");
        //会将counter注册到MeterRegistry中，MeterRegistry能保存name,即使将counter放入局部变量，
        //MeterRegistry 仍然能够保存相同name值（累加）
        counter = registry.counter("test_inline_total","status","success");

        //tags:标签，以键值对方式成对出现
        summary = registry.summary("test_summary","sum","toal");
    }

    static AtomicInteger atomicInteger = new AtomicInteger();
    static List list = new ArrayList();
    @GetMapping("/total/req")
    public String testRequestTotal(){
        //gauge自动追踪list的长度
        registry.gaugeCollectionSize("guage.list",null,list);
        list.add(1);
        registry.gauge("gauge_test",atomicInteger);
        Counter c = Counter.builder("").description("").tags("inline","ss").register(registry);
        c.increment();
        counter.increment();
       // counter2.increment();
        //userCounter.inc();
        int i = atomicInteger.addAndGet(1);
        summary.record(i);
       // list.remove(0);
        Random random = new Random(10);
        summary.record(random.nextDouble());
        summary.record(random.nextDouble());
        return "请求成功，请求数为："+i;
    }
}
