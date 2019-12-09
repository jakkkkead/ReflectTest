package com.web.aspect;

import com.web.controller.PrometheusController;
import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Aspect
public class ApiCounterAspect {
    private static Logger logger = LoggerFactory.getLogger(PrometheusController.class);
    /**
     * 注册表
     */
    @Autowired
    MeterRegistry registry;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    @Pointcut("@annotation(com.web.annotation.ApiCounter)")
    public void cut(){}
    @Around("cut()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        int total = atomicInteger.incrementAndGet();
        long s1 = System.currentTimeMillis();
        Timer.Sample sample = Timer.start(registry);
        MethodSignature signature = (MethodSignature) point.getSignature();
        String methodName = signature.getName();
        Object[] args = point.getArgs();
        if (args != null && args.length >0){
            int sum = (int) args[0];
            ImmutableTag tag = new ImmutableTag("methodName",methodName);
            List list = new ArrayList();
            list.add(tag);
            registry.gauge("param_sum",list,sum);
        }
        Object res = point.proceed();
        Counter.builder("api_use_total").tags("methodName",methodName).register(registry).increment();
        sample.stop(registry.timer("exec_time","methodName",methodName));
        long s2 = System.currentTimeMillis();
        long s3 = s2-s1;
        logger.info("api_user_total_interface is [{}], exec_time is[{}]",total,s3);
        return res;
    }
}
