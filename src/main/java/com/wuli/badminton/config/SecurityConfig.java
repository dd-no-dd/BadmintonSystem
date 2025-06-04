package com.wuli.badminton.config;

import com.wuli.badminton.security.JwtAuthenticationFilter;
import com.wuli.badminton.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

// 🟢 公开接口（游客可访问）    ←→ 🔴 管理接口（需要admin权限）
// ┌─────────────────────┐      ┌─────────────────────┐
// │ GET /venue/list     │      │ POST /venue/add     │
// │ GET /venue/{id}     │      │ PUT  /venue/update  │
// │ GET /venue/schedule │      │ PUT  /venue/status  │
// └─────────────────────┘      │ DELETE /venue/delete│
//          ↓                   └─────────────────────┘
//    加入permitAll()                保持认证要求
//     (游客访问)                    (管理员权限)
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors().configurationSource(corsConfigurationSource())
            .and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(
                "/",
                "/index.html",
                "/admin.html",
                "/*.js",
                "/*.css",
                "/static/**",
                "/api/auth/login",
                "/api/auth/register",
                "/api/auth/send-code", // 发送验证码接口（游客可访问）
                "/uploads/avatars/**",
                "/pay/notify/**", // 支付平台异步通知接口（微信/支付宝回调）
                "/api/venue/list",
                "/api/venue/list/status/*",
                "/api/venue/*/",
                "/api/reservations/availability",
                "/api/reservations/venue/*",
                // 场地相关浏览接口（游客可访问）
                "/api/venue/availability",
                "/api/venue/status-matrix",
                // 预约通知相关浏览接口（游客可访问）
                "/api/reservation/notice",
                "/api/reservation/notice/**",
                // 商城相关浏览接口（游客可访问）
                "/api/mall/products",
                "/api/mall/products/**",
                "/api/mall/categories",
                // 论坛相关浏览接口（游客可访问）
                "/api/forum/posts",
                "/api/forum/posts/detail",
                "/api/forum/posts/*/replies"
            ).permitAll()
            .anyRequest().authenticated()
            .and()
            .exceptionHandling()
            .authenticationEntryPoint((request, response, e) -> {
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"message\":\"未授权\"}");
            })
            .accessDeniedHandler((request, response, e) -> {
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("{\"message\":\"访问被拒绝\"}");
            })
            .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> {
            com.wuli.badminton.pojo.User user = userService.findByUsername(username);
            if (user == null) {
                throw new org.springframework.security.core.userdetails.UsernameNotFoundException("User not found: " + username);
            }
            return org.springframework.security.core.userdetails.User
                    .withUsername(username)
                    .password(user.getPassword())
                    .roles(user.getRole().replace("ROLE_", ""))
                    .build();
        }).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
} 