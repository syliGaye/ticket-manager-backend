package org.jpa.ticketmanagerbackend.application.configuration;

import org.jpa.ticketmanagerbackend.application.resolver.SessionTokenHandlerArgumentResolver;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.List;
import java.util.Locale;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
    @Bean
    public SessionTokenHandlerArgumentResolver sessionHandlerMethodArgumentResolver() {
        return new SessionTokenHandlerArgumentResolver();
    }

    @Bean
    public LocaleResolver localeResolver() {
        final SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        sessionLocaleResolver.setDefaultLocale(Locale.FRENCH);
        return sessionLocaleResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:locale/messages");
        //messageSource.setDefaultEncoding("UTF-8");
        messageSource.setDefaultEncoding("ISO-8859-1");
        return messageSource;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
        //registry.addInterceptor(deviceResolverHandlerInterceptor());

        //registry.addInterceptor(new MappedInterceptor(new String[]{"/" + RestControllerEndpoint.API_BASE_SECURED_URL + "/**"}, apiSecuredHandlerInterceptor));
        //registry.addInterceptor(new MappedInterceptor(new String[]{"/" + RestControllerEndpoint.API_BASE_URL + "/**"}, apiHandlerInterceptor));
        //registry.addInterceptor(new MappedInterceptor(new String[]{"/app/**", "/**"}, authenticatedUserInterceptor));
        //registry.addInterceptor(new MappedInterceptor(new String[]{"/**"}, helperInterceptor));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOrigins("*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**", "/static/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/", "classpath:/static/");
        //registry.addResourceHandler("/" + storageService.getUploadDirectory() + "/**")
        //      .addResourceLocations(storageService.getUploadLocation());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        //resolvers.add(deviceHandlerMethodArgumentResolver());
        resolvers.add(sessionHandlerMethodArgumentResolver());
    }
}
