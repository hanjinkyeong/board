package com.example.board.controller;

import com.example.board.domain.Board;
import com.example.board.repository.BoardMemoryRepository;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("boards") //공통경로
@Slf4j
@RequiredArgsConstructor //생성자 주입 정의하지않아도 알아서 만들어줌
public class BoardController {
	private final BoardService boardService;

//	@Autowired //생성자 주입
//	public BoardController(BoardService boardService) {
//		this.boardService = boardService;
//	}
	
	// 글 목록
	@GetMapping
	public String list(Model model) {
//		List<Board> list = boardService.getList(); //모델에 담아서 밑에 넣음
		model.addAttribute("boards", boardService.getList());
		
		return "board/list";
	}
	
	// 글쓰기 화면 이동 (핸들러 메서드)
	@GetMapping("/write")
	public String writeForm() {
		return "board/write";
	}
	
	// 글쓰기 처리 (핸들러 처리 메서드)
	@PostMapping("/write")
	public String write(@ModelAttribute Board board) {
		log.info("글쓰기 처리");
		log.info("제목:{}", board.getTitle());
		log.info("작성자:{}", board.getWriter());
		log.info("내용:{}", board.getContent());
		boardService.write(board);
		return "redirect:/boards";
	}
	
	// 글 상세보기
	@GetMapping("/{id}")
	public String detail(@PathVariable Long id, Model model) {
		//id로 Board를 받아온다.
		Board board = boardService.getById(id);
		//model에 담아서 리턴
		model.addAttribute("board",board);
		return "board/detail";
	}
	
	// 글 수정화면 이동 메서드
	@GetMapping("/{id}/edit")
	public String editForm(@PathVariable Long id, Model model) {
		Board board = boardService.getById(id);
		model.addAttribute("board",board);
		return "board/edit";
	}
	
	// 글 수정 처리 메서드
	@PostMapping("/{id}/edit")
	public String edit(@PathVariable Long id, @ModelAttribute Board board){
		//서비스 클래스의 수정 메서드를 호출
		boardService.update(id, board);
		return "redirect:/boards/"+ id;
	}
	
	// 글 삭제
	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Long id) {
		// 서비스 클래스의 삭제 메서드를 호출
		boardService.delete(id);
		return "redirect:/boards";
	}
}
