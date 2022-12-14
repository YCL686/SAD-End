package com.example.sharablead.config;

import com.example.sharablead.handler.GlobalInterceptorHandler;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.SimpleDateFormat;
import java.util.List;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Autowired
    private GlobalInterceptorHandler globalInterceptorHandler;


    @Override   //interceptor config
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalInterceptorHandler)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/error",
                        "/user/login",
                        "/user/getProfile",
                        "/user/getDotCount",
                        "/comment/pageCommentList",
                        "/comment/showAllCommentList",
                        "/opus/pageOpusList",
                        "/opus/getOpusById",
                        "/opus/pageProfileOpusList",
                        "/daily-task-config/getDailyTask",
                        "/daily-staking-pool/getDailyStakingPool",
                        "/ad/getAdList",
                        "/ad-auction/getAdAuctionInfo",
                        "/ad-auction/getAdAuctionList",
                        "/ad-auction-record/pageBidRecord",
                        "/synchronize-record/getSynchronizeInfo",
                        "/launch/getLaunchList",
                        "/static/**",
                        "/swagger-resources/**",
                        "/swagger-ui.html/**",
                        "/doc.html",
                        "/test/**",
                        "/webjars/**"); //exclude some request paths, consider implement by annotation

    }

    public CorsConfiguration addcorsConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", addcorsConfig());
        return new CorsFilter(source);
    }
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();

        ObjectMapper objectMapper = new ObjectMapper();
        //change Long to String when serialize, cause js could not process Long
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(simpleModule);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        converters.add(0, jackson2HttpMessageConverter);
    }
}
