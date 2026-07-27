package com.example.board.controller;

import com.example.board.domain.Comment;
import com.example.board.domain.Member;
import com.example.board.dto.CommentDto;
import com.example.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/boards/{boardId}/comments")
@RequiredArgsConstructor
@RestController
public class CommentRestController {
	private final CommentService  commentService;
	
	// 댓글 목록 조회
	@GetMapping
	public ResponseEntity<List<CommentDto>> list(
			@PathVariable Long boardId,
			@SessionAttribute(value="loginMember") Member member
	) {
		String loginId = member != null ? member.getLoginId() : null;
		List<Comment> list = commentService.getList(boardId);
		List<CommentDto> commentDtos = list.stream()
				.map(comment -> new CommentDto(comment,loginId)).toList();
		return ResponseEntity.ok(commentDtos);
	}
	
	// 댓글 저장
	@PostMapping
	public ResponseEntity<CommentDto> write(
			@PathVariable Long boardId,
			@RequestBody Comment comment,
			@SessionAttribute(value = "loginMember") Member member
	) {
		log.info("comment={}", comment);
		Comment savedComment = commentService.write(boardId, comment.getContent(), member.getLoginId());
		return ResponseEntity.ok(new CommentDto(savedComment, member.getLoginId()));
	}
	
	// 댓글 삭제
	@DeleteMapping("/{commentId}")
	public ResponseEntity<Void> delete(@PathVariable Long boardId,
									   @PathVariable Long commentId,
									   @SessionAttribute(value="loginMember") Member member) {
		commentService.delete(commentId, member.getLoginId());
		return ResponseEntity.ok().build();
	}
}
