package com.example.board.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class Board {
	private Long id;				 // 식별자
	private String title; 			 // 제목
	private String content; 		 // 내용
	private String writer;			 // 작성자
	private int viewCount; 			 // 조회수
	private LocalDateTime createdAt; // 작성시간

}
