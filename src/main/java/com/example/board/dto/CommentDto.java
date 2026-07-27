package com.example.board.dto;

import com.example.board.domain.Comment;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

//댓글 응답용 DTO
@Getter
public class CommentDto {
	private Long id;
	private String content;
	private String writerName; //작성자 이름
	private String writerLoginId; //작성자 아이디
	private LocalDateTime createdAt;
	private boolean mine; //작성자와 로그인 사용자가 같은지 확인
	
	public CommentDto(Comment comment, String loginId) {
		this.id = comment.getId();
		this.content = comment.getContent();
		this.writerName = comment.getMember().getName();
		this.writerLoginId = comment.getMember().getLoginId();
		this.createdAt = comment.getCreatedAt();
		this.mine = loginId != null&& loginId.equals(this.writerLoginId);
	}
}

