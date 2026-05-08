package com.concesionario.concesionario.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        Path uploadsDir = Paths.get(
                System.getProperty("user.dir"),
                "src", "main", "resources", "static", "uploads");

        String uploadsLocation = uploadsDir.toUri().toString();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadsLocation);
    }
}
