package com.example.board.exception;

//권한이 없을 경우 발생할 예외
public class UnauthorizedException extends RuntimeException{
	public UnauthorizedException(String message){
		super(message);
	}
}
