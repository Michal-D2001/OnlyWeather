package com.onlyweather.OnlyWeather.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI myOpenAPI(){
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Local server URL");

        Contact contact = new Contact();
        contact.setName("Michal-D2001");
        contact.setUrl("https://github.com/Michal-D2001");

        License license = new License().name("Personal project").url("https://github.com/Michal-D2001/OnlyWeather");

        Info info = new Info()
                .title("OnlyWeather API")
                .version("1.0")
                .contact(contact)
                .description("API for retrieving weather data from OpenWeatherMap")
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));
    }
}
