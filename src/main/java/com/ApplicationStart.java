package com;

import io.micrometer.core.instrument.MeterRegistry;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@MapperScan(value = "com.web.dao")
@SpringBootApplication(scanBasePackages={"com.web.*"})
public class ApplicationStart {
    public static void main(String[] args) {
        //DefaultExports.initialize();
        SpringApplication.run(ApplicationStart.class,args);
    }

    /**
     * springboot2.x专用，使用Micrometer 作为收集指标库
     * 如果需要对该注册表进行配置，添加类型为 MeterRegistryCustomizer 的 bean 即可。
     * 在需要使用注册表的地方，可以通过依赖注入的方式来使用 MeterRegistry 对象。
     * @param applicationName
     * @return
     */
    @Bean
    MeterRegistryCustomizer<MeterRegistry> configurer(
            @Value("${spring.application.name}") String applicationName) {
        return (registry) -> registry.config().commonTags("application", applicationName);
    }


}
