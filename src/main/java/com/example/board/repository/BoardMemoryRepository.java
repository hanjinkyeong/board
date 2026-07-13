//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.board.repository;

import com.example.board.domain.Board;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class BoardMemoryRepository {
	@Generated
	private static final Logger log = LoggerFactory.getLogger(BoardMemoryRepository.class);
	private final Map<Long, Board> store = new ConcurrentHashMap();
	private Long sequence = 1L;
	
	public BoardMemoryRepository() {
		log.info("BoardMemoryRepository 생성");
	}
	
	public Board save(Board board) {
		if (board.getId() == null) {
			Long var2 = this.sequence;
			this.sequence = this.sequence + 1L;
			board.setId(var2);
		}
		
		this.store.put(board.getId(), board);
		return board;
	}
	
	public List<Board> findAll() {
		return this.store.values().stream().toList();
	}
	
	public Board findById(Long id) {
		return (Board)this.store.get(id);
	}
	
	public void deleteById(Long id) {
		this.store.remove(id);
	}
}
