package ch.sebastianhaeni.prophector.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private static final String ASSETS_RESOURCES = "/assets/";

    @Override
    public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
        var staticAngularResources = new HashSet<String>();
        staticAngularResources.add("classpath:/static/");

        var assetsResourceLocations = staticAngularResources.stream()
                .map(res -> res + ASSETS_RESOURCES)
                .collect(Collectors.toSet())
                .toArray(new String[]{});
        var generalResourceLocations = staticAngularResources.toArray(new String[]{});

        registry.addResourceHandler(ASSETS_RESOURCES + "**")
                .addResourceLocations(assetsResourceLocations)
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS));
        registry.addResourceHandler("/**")
                .addResourceLocations(generalResourceLocations)
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
    }
}
