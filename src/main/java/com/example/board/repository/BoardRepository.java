package com.example.board.repository;

import com.example.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
	/*
		value : 실제 화면에 보여줄 데이터를 조회하는 쿼리
		countQuery : 전체 건수를 세는 쿼리
	 */
	@Query(value = "select b from Board b join fetch b.member",
			countQuery = "select count(b) from Board b")
	Page<Board> findAllWithMember(Pageable pageable);
	
	//제목 검색
	@Query(value = "select b from Board b join fetch b.member " +
					"where b.title like %:keyword%",
					countQuery = "select count(b) from Board b where b.title like %:keyword%")
	Page<Board> findByTitleContaining(String keyword, Pageable pageable);
	
	//제목+내용 검색
	@Query(value = "select b from Board b join fetch b.member " +
			"where b.title like %:keyword% or b.content like %:keyword%",
			countQuery = "select count(b) from Board b where b.title like %:keyword% or b.content like %:keyword%")
	Page<Board> findByTitleContainingOrContentContaining(String keyword, Pageable pageable);
	
	
	@Query("select b from Board b join fetch b.member where b.id = :id")
	Board findWithMember(Long id);
}
