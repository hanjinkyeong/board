package com.example.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//회원가입 입력 폼
@Getter
@Setter
@NoArgsConstructor
public class MemberJoinForm {
	@NotBlank(message = "아이디를 입력하세요")
	@Size(min = 4, max = 20, message = "아이디는 4~20자여야 합니다.")
	private String loginId;
	
	@NotBlank(message = "비밀번호를 입력하세요")
	@Size(min = 4, max = 30, message = "비밀번호는 4~30자여야 합니다.")
	private String password;
	
	@NotBlank(message = "이름을 입력하세요")
	private String name;
}
