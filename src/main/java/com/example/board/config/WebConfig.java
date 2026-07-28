package com.example.board.config;

import com.example.board.web.Interceptor.LoginCheckInterceptor;
import com.example.board.web.filter.LogFilter;
import com.example.board.web.filter.LoginCheckFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//클래스위에 @를 얹는다 or class 위 @Configuration - 메서드 위 @Bean 얹는 방법
//필터 등록 설정
@Configuration
public class WebConfig implements WebMvcConfigurer { //WebMvcConfigurer:스프링 MVC 설정을 커스터마이징할 때 사용하는 인터페이스
	
	// 메서드 구현-> 인터셉터 등록
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(new LoginCheckInterceptor())
//				//인터셉터 실행 순서
//				.order(1)
//				//인터셉터 실행 경로
//				.addPathPatterns(
//						"/boards/write",
//						"/boards/*/edit",
//						"/boards/*/delete");
//
//	}
	
	@Bean
	public FilterRegistrationBean<LogFilter> logFilter(){
		FilterRegistrationBean<LogFilter> registrationBean = new FilterRegistrationBean<>();
		//필터 등록
		registrationBean.setFilter(new LogFilter());
		//필터 실행 순서
		registrationBean.setOrder(1);
		//필터 실행 경로
		registrationBean.addUrlPatterns("/boards/*");
		
		return registrationBean;
	}
	
//	@Bean
	public FilterRegistrationBean<LoginCheckFilter> loginCheckFilter() {
		FilterRegistrationBean<LoginCheckFilter> registrationBean =
				new FilterRegistrationBean<>();
		
		registrationBean.setFilter(new LoginCheckFilter());
		registrationBean.setOrder(2);
		registrationBean.addUrlPatterns("/*");
		
		return registrationBean;
	}
}
