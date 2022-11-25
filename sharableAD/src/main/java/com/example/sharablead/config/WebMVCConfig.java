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


    @Override   //拦截器配置
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalInterceptorHandler) //拦截器注册对象
                .addPathPatterns("/**") //指定要拦截的请求
                .excludePathPatterns(
                        "/error",
                        "/user/login",
                        "/user/getProfile",
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
                        "/synchronize-record/getDailyTaskBalance",
                        "/static/**",
                        "/swagger-resources/**",
                        "/swagger-ui.html/**",
                        "/doc.html",
                        "/webjars/**"); //排除请求

    }

    public CorsConfiguration addcorsConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // 请求常用的三种配置，*代表允许所有，当时你也可以自定义属性（比如header只能带什么，只能是post方式等等）
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
        /**
         * 序列换成json时,将所有的long变成string
         * 因为js中得数字类型不能包含所有的java long值
         */
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(simpleModule);

        /**
         * 2021/11/27
         * 遇到一个问题，手动重写json转换后，前端请求参数中的多余字段，导致请求报错，
         * 重写前框架自带的方法是不报错的，那只有手动解决了
         */
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        /**
         * 现在好多项目都用到了long型ID，如果不做处理，返回到前端的精度会丢失，为了解决这个方法，
         * 只能重写configureMessageConverters，很多人都遇到重写这个不生效的情况，都有分析原因，
         * 是因为有一个默认的消息转换器排在我们自定义的前面导致不生效，有的说加注解@EnableWebMvc，
         * 有的说定义一个Bean等等，知道原因后解决其实没那么复杂，我们只需要将自定义的消息转换器放到前边即可，
         * 下面是代码：
         */
        // index 0 关键  核心就在这里，把它添加到首位还担心他不生效吗
        converters.add(0, jackson2HttpMessageConverter);
    }
}
