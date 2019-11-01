package com.web.controller;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.*;
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
 * gauge：可增可减，注意，追踪的对象如果被垃圾收集掉，可能报告无数据或者NAN
 * summary
 *
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
    private DistributionSummary perSummary;
    private DistributionSummary scalSummary;

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
        /**
         * 分布概要（Distribution summary）用来记录事件的分布情况。
         * 计时器本质上也是一种分布概要。表示分布概要的类 DistributionSummary 可以从注册表中创建，
         * 也可以使用 DistributionSummary.builder() 提供的构建器来创建。分布概要根据每个事件所对应的值
         * ，把事件分配到对应的桶（bucket）中。Micrometer 默认的桶的值从 1 到最大的 long 值。
         * 可以通过 minimumExpectedValue 和 maximumExpectedValue 来控制值的范围。
         * 如果事件所对应的值较小，可以通过 scale 来设置一个值来对数值进行放大。
         * 与分布概要密切相关的是直方图和百分比（percentile）。大多数时候，我们并不关注具体的数值，而是数值的分布区间。
         * 比如在查看 HTTP 服务响应时间的性能指标时，通常关注是的几个重要的百分比，如 50%，75%和 90%等。
         * 所关注的是对于这些百分比数量的请求都在多少时间内完成。
         * Micrometer 提供了两种不同的方式来处理百分比。
         * 对于 Prometheus 这样本身提供了对百分比支持的监控系统，Micrometer 直接发送收集的直方图数据，由监控系统完成计算。
         * 对于其他不支持百分比的系统，Micrometer 会进行计算，并把百分比结果发送到监控系统。
         */
        perSummary = DistributionSummary.builder("per_summary")
                     .description("百分比占比")
                //最小值期望范围
                     .minimumExpectedValue(1L)
                //最大值期望范围
                     .maximumExpectedValue(10L)
                //桶个数，promethues会自动计算（百分比,正太？）
                     .publishPercentiles(0,0.5,0.9)
                     .register(registry);
//        scalSummary = DistributionSummary.builder("scan_summary")
//                      .description("控制百分比")
//                      .scale(100)
//                      .maximumExpectedValue(100L)
//                      .minimumExpectedValue(1L)
//                      .sla(20,40,60,90)
//                      .register(registry);
    }

    static AtomicInteger atomicInteger = new AtomicInteger();
    static List list = new ArrayList();
    @Timed(value = "time.total")
    @GetMapping("/total/req")
    public String testRequestTotal(){
        long t1 = System.currentTimeMillis();
        //开始时间
        Timer.Sample sample = Timer.start(registry);
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
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        perSummary.record(2);
        perSummary.record(5);
        perSummary.record(9);
        perSummary.record(6);
        perSummary.record(7);
        //结算计算时间
        sample.stop(registry.timer("time_test","status","success"));
        long t2 = System.currentTimeMillis();
        System.out.println("t2-t1:"+(t2-t1)/1000);
        return "请求成功，请求数为："+i;
    }
}
