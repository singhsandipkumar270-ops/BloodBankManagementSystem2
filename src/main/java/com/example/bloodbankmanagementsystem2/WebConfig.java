package com.example.bloodbankmanagementsystem2;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns(
                        "/donor",
                        "/donors",
                        "/find-blood",
                        "/donor/edit/**",
                        "/donor/update",
                        "/donor/delete/**"
                )
                .excludePathPatterns(
                        "/login",
                        "/register",
                        "/"
                );
    }
}
