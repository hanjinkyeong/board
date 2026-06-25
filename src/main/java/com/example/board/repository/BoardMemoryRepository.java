package com.example.board.repository;

import com.example.board.domain.Board;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//스프링 빈으로 등록
//1. @Component
//2. @Configuration @Bean
@Slf4j
//@Component
@Repository
public class BoardMemoryRepository {
	private final Map<Long, Board> store = new ConcurrentHashMap<>();
	private Long sequence = 1L;
	
	public BoardMemoryRepository(){
		log.info("BoardMemoryRepository 생성");
		//초기 데이터 생성
		Board board1 = new Board();
		board1.setId(sequence);
		board1.setTitle("첫번 째 글입니다.");
		board1.setContent("첫번 째 글 내용입니다.");
		board1.setWriter("작성자1");
		board1.setCreatedAt(LocalDateTime.now());
		store.put(sequence++,board1);
		
		Board board2 = new Board();
		board2.setId(sequence);
		board2.setTitle("두번 째 글입니다.");
		board2.setContent("두번 째 글 내용입니다.");
		board2.setWriter("작성자2");
		board2.setCreatedAt(LocalDateTime.now());
		store.put(sequence++,board2);
		
		Board board3 = new Board();
		board3.setId(sequence);
		board3.setTitle("세번 째 글입니다.");
		board3.setContent("세번 째 글 내용입니다.");
		board3.setWriter("작성자3");
		board3.setCreatedAt(LocalDateTime.now());
		store.put(sequence++,board3);
	}
	
	//게시글 지정
	public Board save(Board board){
		if(board.getId() == null){
			board.setId(sequence++);
		}
		
		// 중복이 오면 덮어쓰기 때문에 수정의 기능도 가능
		store.put(board.getId(), board);
		return board;
	}
	
	//모든 게시글 조회
	public List<Board> findAll(){
		//return store.values().stream().toList();
		return store.values().stream()
					.sorted((b1, b2) -> Long.compare(b2.getId(), b1.getId()))
					.toList();
	}
	
	//단건 조회 (하나씩 조회)
	public Board findById(Long id) {
		return store.get(id);
	}
	
	//게시글 삭제
	public void deleteById(Long id){
		store.remove(id);
	}
	
	
	
}
