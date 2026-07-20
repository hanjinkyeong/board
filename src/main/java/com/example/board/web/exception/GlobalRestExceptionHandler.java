package com.example.board.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(value=1)
@RestControllerAdvice //이 프로젝트에서 존재하는 모든 restcontroller 예외처리를 여기서 처리함
public class GlobalRestExceptionHandler {
	
	//없는 id 조회 시
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handleException(RuntimeException ex){
		log.error(ex.getMessage(),ex); //예외 메세지값
		return ResponseEntity.badRequest().body(ex.getMessage());
	}
}
