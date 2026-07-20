package com.example.board.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

//사용자가 로그인을 했는지 체크하는 필터
@Slf4j //log.info를 찍을 수 있게 해주는 롬복
public class LoginCheckFilter implements Filter {
	
	private static final String[] loginCheckPaths = {
			"/boards/write",
			"/boards/*/edit",
			"/boards/*/delete"
	};
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		//사용자가 요청한 경로
		String requestURI = httpRequest.getRequestURI();
		try{
			//현재 요청 경로가 로그인 필수 경로 패턴과 일치하는지 확인
			if(PatternMatchUtils.simpleMatch(loginCheckPaths, requestURI)){
				HttpSession session = httpRequest.getSession(false);
				if (session == null || session.getAttribute("loginMember") == null) {
					log.info("로그인 체크 실행");
					// 로그인 안됨 -> 로그인 페이지로 리다이렉트
					httpResponse.sendRedirect("/login?redirectUrl=" + requestURI);
					// 다음 필터나 서블릿으로 진행되는 것을 막는다.
					return;
				}
			}
			
			chain.doFilter(request, response);
		}catch(Exception e){
			throw e;
		}
		
	}
}
