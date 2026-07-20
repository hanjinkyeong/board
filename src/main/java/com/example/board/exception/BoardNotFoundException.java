package com.example.board.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 이 예외가 발생하면 스프링이 HTTP 404 Not Found 상태로 처리한다.
@ResponseStatus(HttpStatus.NOT_FOUND)
public class BoardNotFoundException extends RuntimeException{
	public BoardNotFoundException(String message) {
		super(message); //super = runtime
	}
}
