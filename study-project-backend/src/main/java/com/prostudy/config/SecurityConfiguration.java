package com.prostudy.config;

import com.alibaba.fastjson.JSONObject;
import com.prostudy.entity.RestBean;
import com.prostudy.service.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Resource
    AuthorizeService authorizeService;

    @Resource
    DataSource dataSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           PersistentTokenRepository repository) throws Exception {
        return http
                .authorizeHttpRequests()
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/api/auth/login")
                .successHandler(this::onAuthenticationSuccess)
                .failureHandler(this::onAuthenticationFailure)
                .and()
                .logout()
                .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler(this::onAuthenticationSuccess)
                .and()
                .rememberMe()
                .rememberMeParameter("remember")
                .tokenRepository(repository)
                .tokenValiditySeconds(3600 * 24 * 7)
                .and()
                .csrf()
                .disable()
                .cors()
                .configurationSource(this.configurationSource())
                .and()
                .exceptionHandling()
                //没有权限的时候
                .authenticationEntryPoint(this::onAuthenticationFailure)
                .and()
                .build();
    }

    //注册成bean，一开始创建表
    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        //第一次写true，创建表，完了改成false，下次不会再创建表
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }

    //解决跨域请求
    private CorsConfigurationSource configurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        //一般写自己只允许自己服务器的前端
        //这里写* ，允许所有的跨域请求，方便测试
        cors.addAllowedOriginPattern("*");
        cors.setAllowCredentials(true);
        cors.addAllowedHeader("*");
        cors.addAllowedMethod("*");
        cors.addExposedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity security) throws Exception {
        return security
                .getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(authorizeService)
                .and()
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 登录成功处理。
     *
     * @param request
     * @param response
     * @param authentication 登录成功返回用户，这里还没开始对接数据库，返回默认
     * @throws IOException
     * @throws ServletException
     */
    //默认登录成功处理为：重定向到302 continue，前后端分离，返回给前段一个json格式数据。
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //设置编码格式
        response.setCharacterEncoding("utf-8");
        //write只能返回字符串，引入fastjson。
        //直接将对象转化成jsonString
        if (request.getRequestURI().endsWith("/login")) {
            response.getWriter().write(JSONObject.toJSONString(RestBean.success("登录成功")));
        } else if (request.getRequestURI().endsWith("/logout")) {
            response.getWriter().write(JSONObject.toJSONString(RestBean.success("退出 登录成功")));
        }

    }

    /**
     * 登录失败
     *
     * @param request
     * @param response
     * @param exception
     * @throws IOException
     * @throws ServletException
     */
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        //设置编码格式
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(JSONObject.toJSONString(RestBean.failure(401, exception.getMessage())));
    }

}
