package com.example.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

//댓글
@Entity //ToString 만들지 않습니다
@NoArgsConstructor //Entity는 기본생성자가 반드시 필요해서 넣어줌
@Getter @Setter
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	@ManyToOne(fetch = FetchType.LAZY) //지연로딩
	@JoinColumn(name = "board_id")
	private Board board;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	
	private LocalDateTime createdAt;
	
	public Comment(String content, Board board, Member member) {
		this.content = content;
		this.board = board;
		this.member = member;
		this.createdAt = LocalDateTime.now();
	}
}
