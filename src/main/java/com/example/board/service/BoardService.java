package com.example.board.service;

import com.example.board.domain.Board;
import com.example.board.repository.BoardMemoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
/*
 *스프링 빈 주입 방법
 * 1. 필드 주입 @Autowired
 * 2. 생성자 주입 생성자에 @Autowired
 * 3. setter 주입 setter에 @Autowired
 */
@Service
@Slf4j
//@Component
public class BoardService {
//	필드 주입
	//	@Autowired
	private static BoardMemoryRepository boardRepository;

//	생성자 주입
	@Autowired
	public BoardService(BoardMemoryRepository boardRepository){
		this.boardRepository=boardRepository; //생성자 주입방식
	log.info("BoardService 생성");
	}
	
//	@Autowired
//	public void setBoardRepository(BoardMemoryRepository boardRepository){
//		this.boardRepository = boardRepository;
//	}
	
	//전체 글 목록
	public List<Board> getList(){
		return boardRepository.findAll();
	}
	
	//글 작성
	public Board write(Board board){
		if(board != null){
			board.setId(null);
			board.setViewCount(0);
			board.setCreatedAt(LocalDateTime.now());
		}
		return boardRepository.save(board);
	}
	
	// 글 조회
	public Board getById(Long id){
		return boardRepository.findById(id);
	}
	// 글 수정
	public void update(Long id, Board board){
		Board findBoard = boardRepository.findById(id);
		
		findBoard.setTitle(board.getTitle());
		findBoard.setWriter(board.getWriter());
		findBoard.setContent(board.getContent());
		
		boardRepository.save(findBoard);
	}
	// 글 삭제
	public void delete(Long id){
		boardRepository.deleteById(id);
	}
	
}

