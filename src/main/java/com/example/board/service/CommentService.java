package com.example.board.service;

import com.example.board.domain.Board;
import com.example.board.domain.Comment;
import com.example.board.domain.Member;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.CommentRepository;
import com.example.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor //생성자 주입
@Transactional(readOnly = true)
@Slf4j
public class CommentService {
	private final CommentRepository commentRepository;
	private final BoardRepository boardRepository;
	private final MemberRepository memberRepository;
	
	// 댓글 목록 조회
	public List<Comment> getList(Long boardId) {
		return commentRepository.findByBoardId(boardId);
	}
	
	// 댓글 저장
	@Transactional
	public Comment write(Long boardId, String content, String loginId) {
		Board board = boardRepository.findById(boardId)
				.orElseThrow(() -> new RuntimeException("Board not found"));
		Member member = memberRepository.findByLoginId(loginId)
				.orElseThrow(() -> new RuntimeException("Member not found"));
		Comment comment = new Comment(content, board, member);
		return commentRepository.save(comment);
		
	}
	
	// 댓글 삭제
	@Transactional
	public void delete(Long commentId, String loginId) {
		isOwner(commentId, loginId);
		commentRepository.deleteById(commentId);
	}
	
	// 댓글 작성자와 로그인 사용자가 일치하는지 확인(삭제 권한 확인)
	public boolean isOwner(Long commentId, String loginId) {
	Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new RuntimeException(commentId + "댓글이 존재하지 않습니다."));
	if (comment.getMember().getLoginId().equals(loginId)) {
		return true;
	}
//	return false;
		throw new RuntimeException("삭제 권한이 없습니다.");
	}
	
}
