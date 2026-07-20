package com.example.board.web.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/*
	http 요청 -> WAS -> 필터 -> 서블릿(DispatcherServlet) -> [인터셉터] -> 컨트롤러
 */
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
	//컨트롤러 메서드 호출 전에 실행
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		log.info("preHandle 호출");
		HttpSession session = request.getSession(false);
		if(session == null || session.getAttribute("loginMember") == null){
			//로그인 안됨
			String requestURI = request.getRequestURI();
			//로그인 후 원래 가려고 했던 곳으로 redirectUrl을 함께 넘긴다.
			response.sendRedirect("/login?redirectUrl="+requestURI);
			//컨트롤러를 실행하지 않는다.
			return false;
		}
		return true;
	}
	// 컨트롤러의 메서드가 정상처리되면 호출, 예외가 발생하면 호출되지 않는다.
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		log.info("postHandle 호출");
	}
	
	// 컨트롤러의 예외 발생과 관계없이 호출
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		log.info("afterCompletion 호출");
	}
}
