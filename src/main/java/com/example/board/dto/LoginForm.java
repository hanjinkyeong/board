package com.example.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 로그인 입력 폼
@ToString
@Getter
@Setter
public class LoginForm {
	@Size(min = 4, max = 20, message = "아이디는 4자리 이상 20자리 이하여야 합니다.")
	@NotBlank(message = "아이디를 입력하세요") // null 또는 ""(공백) 불가
	private String loginId;
	@Size(min = 4, max = 30)
//	@NotBlank(message = "패스워드를 입력하세요")
	private String password;
}
