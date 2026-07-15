package com.example.board.controller;

import com.example.board.domain.Board;
import com.example.board.domain.Member;
import com.example.board.dto.BoardForm;
import com.example.board.service.BoardService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor  // 생성자를 주입 받겠다는 어노테이션 (아래의 코드가 필요 없음)
@RequestMapping("/boards")
@Controller

public class BoardController {
	
	// Controller는 Service에 의존하기 때문에 불러와야함
	private final BoardService boardService;
	
	// 세션에서 로그인 회원 정보를 꺼낸다. 없으면 null로 return 한다.
	private Member loginMember(HttpSession session){
		return (Member) session.getAttribute("loginMember");
	};
	
	// 생성자 주입
//   @Autowired
//   public BoardController(BoardsService boardsService) {   // BoardsService를 파라미터로 받음
//      this.boardsService = boardsService;
//   }
	
	// 글 목록
	@GetMapping // boards로 가게 하기 위해 사용 ()작성 안해도 됨
	public String list(Model model) {
		model.addAttribute("boards",boardService.getList());
		return "board/list";
	}
	
	// 글 쓰기 화면 이동  (로그인 필요)
	@GetMapping("/write")
	public String writeForm(HttpSession session, Model model) {
		if (loginMember(session) == null){
			// 로그인 안함 -> 로그인 페이지로 redirect
			return "redirect:/login";
		}
		model.addAttribute("boardForm", new BoardForm());
		return "board/write";
	}
	
	// 글 쓰기 처리
	@PostMapping("/write")
	public String write(
			@Valid @ModelAttribute("boardForm") BoardForm boardForm,
			BindingResult bindingResult, // 유효성 검증
			HttpSession session) {
		log.info("bindingResult={}", bindingResult);
		Member member = loginMember(session);
		if (member == null) {
			return "redirect:login";
		}
		//유효성 검증에 오류가 있으면 글쓰기 화면으로 다시 보낸다.
		if(bindingResult.hasErrors()){
			return "board/write";
		}
		boardService.write(boardForm, member.getLoginId());
		return "redirect:/boards";
	}
	
	// 메서드 정의 및 return
	
	// 글 상세보기
	@GetMapping("/{id}")
	public String detail(@PathVariable Long id , Model model) {
		// id로 Board를 받아온다.
		// model에 담아서 리턴
		model.addAttribute("board", boardService.getDetailAndIncreaseView(id));
		return "board/detail";
	}
	
	// 글 수정 화면 이동
	@GetMapping("/{id}/edit")
	public String editForm(
			@PathVariable Long id ,
			HttpSession session,
			Model model) {
		Member login = loginMember(session);
		// 로그인 하지 않았을 때
		if (login == null){
			return "redirect:/login?redirectUrl=/boards/"+ id + "/edit";
		}
		
		// 작성자와 로그인 사용자가 다를 때
		if (!boardService.isOwner(id, login.getLoginId())){
			return "redirect:/boards/" + id;
		}
		
		// id로 Board를 받아온다.
		Board board = boardService.getById(id);
		// model에 담아서 리턴
		model.addAttribute("board", board);
		return "board/edit";
	}
	
	// 글 수정 처리
	@PostMapping("/{id}/edit")
	public String edit(@PathVariable Long id, @ModelAttribute Board board) {
		// 서비스 클래스의 수정 메서드를 호출
		boardService.update(id, board);
		return "redirect:/boards";
	}
	
	// 글 삭제
	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Long id) {
		// 서비스 클래스의 삭제 메서드를 호출
		boardService.delete(id);
		return "redirect:/boards";
		
	}
	
}
