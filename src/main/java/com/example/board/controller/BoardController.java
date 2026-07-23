package com.example.board.controller;

import com.example.board.domain.Board;
import com.example.board.domain.Member;
import com.example.board.dto.BoardForm;
import com.example.board.exception.BoardNotFoundException;
import com.example.board.exception.UnauthorizedException;
import com.example.board.service.BoardService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.UriUtil;
import org.aspectj.lang.annotation.RequiredTypes;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@RequiredArgsConstructor  // 생성자를 주입 받겠다는 어노테이션 (아래의 코드가 필요 없음)
@RequestMapping("/boards")
@Controller

public class BoardController {
	
	// Controller는 Service에 의존하기 때문에 불러와야함
	private final BoardService boardService;
	
	private static final int PAGE_SIZE = 1; //모든 게시판 기본 사이즈 지정
	
	// 세션에서 로그인 회원 정보를 꺼낸다. 없으면 null로 return 한다.
//	private Member loginMember(HttpSession session){
//		return (Member) session.getAttribute("loginMember");
//	};
	
	// 생성자 주입
//   @Autowired
//   public BoardController(BoardsService boardsService) {   // BoardsService를 파라미터로 받음
//      this.boardsService = boardsService;
//   }
	
	// 글 목록
	@GetMapping // boards로 가게 하기 위해 사용 ()작성 안해도 됨
	public String list(@RequestParam(defaultValue = "1")int page,
					   @RequestParam(required = false) String searchType,
					   @RequestParam(required = false) String searchKeyword,
					   Model model) {
		/*
		Pageable 객체 생성: 리포지토리에 몇 번째 페이지를, 몇 건씩, 어떤 정렬로
		조회할지 전달하는 값
		PageRequest.of(page, size, sort)
		- page: 조회할 페이지 인덱스 번호(0부터 시작 -> 1페이지는 0을 넘긴다)
		- size: 한 페이지에 담을 데이터의 갯수
		- sort: 정렬 기준
		 */
		int pageIndex = Math.max(page - 1, 0);
		//int pageSize = 5;
		Pageable pageable = PageRequest.of(pageIndex, PAGE_SIZE,
				Sort.by(Sort.Direction.DESC, "id")); //정렬 순서 (역순으로 id기준으로)
		Page<Board> boardPage = boardService.getList(pageable,searchType, searchKeyword);
		/*
		Page<Board>
		- getContent(): 현재 페이지에 해당하는 실제 데이터 리스트(List<Boards>)
		- getTotalElements(): 조건에 맞는 전체 데이터 갯수
		- getTotalPages(): 전체 페이지 갯수
		- getNumber(): 현재 페이지 번호 (0부터 시작)
		- getSize(): 요청한 페이지의 크기
		- getNumberOfElements(): 현재 페이지에 실제로 담긴 데이터의 수
		- isFirst() / isLast(): 첫 페이지 / 마지막 페이지 여부
		- hasNext() / hasPrevious(): 다음 / 이전 페이지 존재 여부
		- isEmpty(): 현재 페이지 내용이 비어있는지 여부
		 */
		model.addAttribute("boards", boardPage.getContent());
		model.addAttribute("boardPage", boardPage);
		model.addAttribute("searchType", searchType);
		model.addAttribute("searchKeyword", searchKeyword);
		return "board/list";
	}
	
	// 글 쓰기 화면 이동  (로그인 필요)
	@GetMapping("/write")
	public String writeForm(Model model) {
//		if (loginMember(session) == null){
//			// 로그인 안함 -> 로그인 페이지로 redirect
//			return "redirect:/login";
//		}
		model.addAttribute("boardForm", new BoardForm());
		return "board/write";
	}
	
	// 글 쓰기 처리
	@PostMapping("/write")
	public String write(
			@Valid @ModelAttribute("boardForm") BoardForm boardForm,
			BindingResult bindingResult, // 유효성 검증
			@RequestParam(required = false) MultipartFile file,
			@SessionAttribute("loginMember") Member member) throws IOException {
		log.info("첨부파일 file={}, size={}", file.getOriginalFilename(), file.getSize());
		if (member == null) {
			return "redirect:login";
		}
		//유효성 검증에 오류가 있으면 글쓰기 화면으로 다시 보낸다.
		if (bindingResult.hasErrors()) {
			return "board/write";
		}
		boardService.write(boardForm, member.getLoginId(), file);
		return "redirect:/boards";
	}
	
	// 글 상세보기
	@GetMapping("/{id}")
	public String detail(@PathVariable Long id, Model model) {
		// id로 Board를 받아온다.
		// model에 담아서 리턴
		model.addAttribute("board", boardService.getDetailAndIncreaseView(id));
		//	return "error/board-not-found";
		return "board/detail";
	}
	
	// 글 수정 화면 이동
	@GetMapping("/{id}/edit")
	public String editForm(
			@PathVariable Long id,
			@SessionAttribute("loginMember") Member member,
			Model model) {
//		Member login = loginMember(session);
//		// 로그인 하지 않았을 때
//		if (login == null){
//			return "redirect:/login?redirectUrl=/boards/"+ id + "/edit";
//		}
		
		// 작성자와 로그인 사용자가 다를 때
		if (!boardService.isOwner(id, member.getLoginId())) {
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
	public String edit(@PathVariable Long id,
					   @ModelAttribute("boardForm") BoardForm boardForm,
					   @RequestParam(required = false) MultipartFile file,
					   @RequestParam(defaultValue = "false") boolean deleteFile,
					   @SessionAttribute("loginMember") Member member) throws IOException {
		//로그인 한 사용자가 작성자와 같은지 확인
		if (!boardService.isOwner(id, member.getLoginId())) {
			return "redirect:/boards/" + id;
		}
		// 서비스 클래스의 수정 메서드를 호출
		boardService.update(id, boardForm, file, deleteFile);
		return "redirect:/boards";
	}
	
	// 글 삭제
	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Long id,
						 @SessionAttribute("loginMember") Member member) {
		//로그인 한 사용자가 작성자와 같은지 확인
		if (!boardService.isOwner(id, member.getLoginId())) {
			return "redirect:/boards/" + id;
		}
		// 서비스 클래스의 삭제 메서드를 호출
		boardService.delete(id);
		return "redirect:/boards";
		
	}
	
	// 첨부파일 다운로드
	//링크를 클릭했을 때 다운로드 되어야 한다
	@GetMapping("/{id}/download")
	public ResponseEntity<Resource> download(@PathVariable Long id){ //Resource : org.spring.core.i
		Board board = boardService.getById(id);
		if (!board.isHasAttachment()) {
			//첨부파일이 없으면
			return ResponseEntity.notFound().build();
		}
		
		//첨부파일이 있으면
		Path uploadPath = Paths.get("./upload");
		Path file = uploadPath.resolve(board.getStoredFileName());
		Resource resource = null;
		try {
			resource = new UrlResource(file.toUri());
		}catch (MalformedURLException e){
			return ResponseEntity.notFound().build();
		}
		
		// 한글 파일명이 깨지지 않도록 인코딩
		String encode = UriUtils.encode(board.getOriginalFileName(), StandardCharsets.UTF_8);
		
		// contentDisposition 생성
		String contentDisposition = "attachment; filename=\"" + encode + "\"";
		//웹서버에서 브라우저로 다운로드 기능을 제공할 떄 화면에 띄우지 말고
		//파일 다운로드로 처리하도록 한다는것을 알려주는 HTTP 응답 헤더 -> contentDisposition
		
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
				.body(resource);
	}
}
