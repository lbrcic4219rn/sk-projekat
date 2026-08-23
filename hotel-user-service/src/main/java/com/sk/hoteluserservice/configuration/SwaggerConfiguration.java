package com.sk.hoteluserservice.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info()
                        .title("API")
                        .description("API swagger definition")
                        .version("1.0.0")
                        .contact(new Contact().name("Nikola Tadic").email("ntadic4419rn@raf.rs")));
    }

}
