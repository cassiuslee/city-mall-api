package com.citymall.api.config;


import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI mallOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("商城后端接口文档")
                        .description("city mall server api")
                        .version("1.0.0"))
                .externalDocs(new ExternalDocumentation()
                        .description("项目文档")
                        .url("https://example.com"));
    }
}