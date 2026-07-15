package com.example.board.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

// 글쓰기, 수정 화면
@Getter @Setter
public class BoardForm {
	@NotEmpty(message = "제목을 입력해 주세요")
	@Size(max = 100)
	private String title;
	@NotEmpty
	private String content;
}
