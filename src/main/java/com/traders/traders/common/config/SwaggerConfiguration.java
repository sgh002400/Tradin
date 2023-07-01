package com.traders.traders.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public OpenAPI openApi() {
        //SecurityRequirement securityRequirement = new SecurityRequirement().addList("Access Token");

        return new OpenAPI()
                .addServersItem(new Server().url("/"))
                .info(new Info()
                        .title("Tradin API")
                        .description("[인증]이 붙은 API는 요청 헤더에 Key: Authorization, Value: Bearer {token}을 포함해야 합니다."));
//                .components(new Components()
//                        .addSecuritySchemes("Access Token",
//                                new SecurityScheme().type(SecurityScheme.Type.HTTP)
//                                        .scheme("bearer")
//                                        .bearerFormat("JWT")
//                                        .in(SecurityScheme.In.HEADER)
//                                        .name("Authorization")))
//                .security(List.of(securityRequirement));
    }
}
