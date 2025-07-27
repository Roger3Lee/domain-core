package com.artframework.domain.web.generator.config;


import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @Data是lombok，可以自己写getter/setter
 * @ConfigurationProperties("swagger")是读取application.properties中swagger开头的配置
 */
@Component
@Data
@EnableOpenApi
public class SwaggerConfiguration {


    @Bean
    public Docket createRestApi() {
        //swagger设置，基本信息，要解析的接口及路径等
        return new Docket(DocumentationType.OAS_30)
                .pathMapping("/")

                //是否开启swagger，false为关闭，可以通过变量控制，线上关闭
                .enable(true)
                //配置api文档的元信息
                .apiInfo(apiInfo())

                //选择哪些接口作为swagger的doc发布
                .select()

                //RequestHandlerSelectors.any()  所有都暴露
                //RequestHandlerSelectors.basePackage("com.autumn.*")  指定包位置
                //设置通过什么方式定位需要自动生成文档的接口，这里定位方法上的@ApiOperation注解
                .apis(RequestHandlerSelectors.any())

                //接口URI路径设置，any是全路径，也可以通过PathSelectors.regex()正则匹配
                .paths(PathSelectors.any())

                .build();
    }

    //生成接口信息，包括标题、联系人，联系方式等
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("DDD sample")
                .description("DDD sample")
                .version("1.0")
                .build();
    }
}
