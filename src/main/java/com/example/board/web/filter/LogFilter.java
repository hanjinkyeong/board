package com.example.board.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

/*
	실행 위치: HTTP 요청 -> WAS -> [필터] -> 서블릿(DispatcherServlet : 요청과 응답 모두 처리) -> 컨트롤러
	필터 생성: Filter 인터페이스를 구현
	필터 빈으로 등록해서 실행
	필터는 doFilter()메서드에서 동작하고 chain.doFilter()메서드를 호출해야 다음 단계로 진행된다.
 */
@Slf4j
public class LogFilter implements Filter {
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	//	log.info("doFilter 실행");
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String requestURI = httpRequest.getRequestURI();
		String uuid = UUID.randomUUID().toString().substring(0, 8);
		try{
			//클라이언트가 요청한 URL 정보를 확인
			log.info("[{}]REQUEST = {}", uuid, requestURI);
			chain.doFilter(request, response); //다음 필터 또는 서블릿 진행
		}catch (Exception e){
			throw e;
		}finally {
			log.info("[{}]RESPONSE = {}", uuid, requestURI);
		}
	}
	
	@Override
	//필터가 생성될 때 실행되는 메서드
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("LogFilter 생성");
	}
	
	@Override
	//필터가 종료될 때 실행되는 메서드
	public void destroy() {
		log.info("LogFilter 종료");
	}
	
	
}
