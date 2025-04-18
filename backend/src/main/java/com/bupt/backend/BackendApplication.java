package com.bupt.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@SpringBootApplication
@MapperScan("com.bupt.backend.mapper")
@ComponentScan(
    basePackages = "com.bupt.backend",
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = "com\\.bupt\\.backend\\.common\\..*"
    )
)
public class BackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean // 添加默认路由用于测试
    public RouterFunction<ServerResponse> testRoutes() {
        return RouterFunctions.route()
                .GET("/api/test", request ->
                        ServerResponse.ok().body("API working"))
                .build();
    }
}