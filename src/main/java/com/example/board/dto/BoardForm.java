package com.example.board.dto;

import lombok.Getter;
import lombok.Setter;

// 글쓰기, 수정 화면
@Getter @Setter
public class BoardForm {
	private String title;
	private String content;
}
