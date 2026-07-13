package com.example.board.controller;

import com.example.board.domain.Member;
import com.example.board.dto.BoardForm;
import com.example.board.dto.LoginForm;
import com.example.board.service.BoardService;
import com.example.board.service.MemberService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;
	private final BoardService boardService;
	
	
	// 기본 회원정보 생성
	@PostConstruct
	public void init(){
		//기본 회원 정보 등록
		Member member = new Member();
		member.setLoginId("user1");
		member.setPassword("1234");
		member.setName("한진경");
		memberService.join(member);
		log.info("기본 회원정보 생성 완료!");
		
		//기본 게시글 등록
		BoardForm form = new BoardForm();
		form.setTitle("기본 글 제목");
		form.setContent("기본 글 내용");
		boardService.write(form, member.getLoginId());
		
	}
	
	// 회원 가입 페이지 이동
	@GetMapping("join")
	public String join(Model model){
		model.addAttribute("boardForm", new Member());
		return "member/join";
	}
	
	// 회원 가입 처리
	@PostMapping("/join")
	public String join(@ModelAttribute Member member, Model model){
//      log.info("member: {}", member);
		// 회원 아이디 중복 체크
		if(memberService.isDuplicateLoginId(member.getLoginId())){
			// 중복된 아이디가 있으면 가입 안됨
			log.info("이미 사용중인 아이디 입니다.");
			model.addAttribute("error", "이미 사용중인 아이디입니다.");
			return "/member/join";
		}
		memberService.join(member);
		log.info("회원가입 완료");
		return "redirect:/login";
	}
	
	// 로그인 페이지
	@GetMapping("login")
	public String login(@RequestParam(required = false) String redirectUrl, Model model) {
		if(redirectUrl != null){
			model.addAttribute("redirectUrl", redirectUrl);
		}
		return "member/login";
	}
	
	// 로그인 처리
	@PostMapping("/login")
	public String login(@ModelAttribute LoginForm loginForm,
						@RequestParam(required = false) String redirectUrl,
						Model model, HttpServletRequest request){ // 세션은 request에 저장되어 있다.
		Optional<Member> loginMember
				= memberService.login(loginForm.getLoginId(), loginForm.getPassword());
		if(loginMember.isEmpty()){
			log.info("아이디 또는 비밀번호가 올바르지 않습니다.");
			return "member/login";
		}
		// 로그인이 성공하면 -> 세션에 회원 정보를 저장 -> 세션은 request에 저장
		HttpSession session = request.getSession();
		session.setAttribute("loginMember", loginMember.get());
		log.info("로그인 성공");
		if(redirectUrl !=null){
			return "redirect" + redirectUrl;
		}
		return "redirect:/";
	}
	
	// 로그아웃 처리
	@PostMapping("/logout")
	public String logout(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if (session != null){
			session.invalidate(); // 세션 폐기 -> 로그아웃 처리
		}
		return "redirect:/boards";
	}
	
	
	
}
