package com.example.board.repository;

import com.example.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
	@Query("select b from Board b join fetch b.member")
	List<Board> findWithMember();
	
	@Query("select b from Board b join fetch b.member where b.id = :id")
	Board findWithMember(Long id);
}
