package com.st.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;

/**
 * Created by zhaoyx on 2016/8/15.
 */
@Configuration
@EnableSwagger2
public class Swagger2Configuration {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.st.controller"))
                .paths(PathSelectors.any())
                .build()
                .directModelSubstitute(LocalDate.class,String.class);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("玛氏巧克力用户画像项目数据接口API")
                .description("此文档描述了玛氏巧克力用户画像项目的数据接口API，包括请求url、请求方式、参数、返回数据格式等！")
                .build();
    }
}
