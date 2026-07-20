package com.example.board.web.exception;

import com.example.board.exception.BoardNotFoundException;
import com.example.board.exception.UnauthorizedException;
import org.springframework.core.annotation.Order;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// 여러 컨트롤러에 흩어져있는 공통 관심사를 한곳에서 관리하여 처리할 수 있도록 한다.
// 대부분 예외처리 등에서 사용한다.
//(assignableTypes = BoardController.class) 이런식으로 전체 아닌 지정 가능
@ControllerAdvice //앱 전체의 @Controller에서 발생한 예외를 가로챈다. (지정 가능)
@Order(value=2)
public class GlobalExceptionHandler {
	
	// 게시글을 찾을 수 없을 경우 발생한 예외를 처리하는 메서드
	@ExceptionHandler(BoardNotFoundException.class)
	public String handleBoardNotFound(BoardNotFoundException ex, Model model) {
		model.addAttribute("errorMessage", ex.getMessage());
		return "error/board-not-found";
	}
	
	// 게시글 수정/삭제 권한이 없을 경우 발생한 예외를 처리하는 메서드
	@ExceptionHandler(UnauthorizedException.class)
	public String handleUnauthorized(UnauthorizedException ex, Model model) {
		model.addAttribute("errorMessage", ex.getMessage());
		return "error/access-denied";
	}
	
	//그 외 알 수 없는 모든 예외를 처리
	@ExceptionHandler(Exception.class)
	public String handleException(Exception ex, Model model) {
		model.addAttribute("errorMessage", "서버 내부 오류 발생!");
		return "error/500";
	}
}
