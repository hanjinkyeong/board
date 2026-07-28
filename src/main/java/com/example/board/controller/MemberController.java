package com.example.board.controller;

import com.example.board.domain.Member;
import com.example.board.dto.LoginForm;
import com.example.board.dto.MemberJoinForm;
import com.example.board.service.BoardService;
import com.example.board.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
	
	// 회원 가입 페이지 이동
	@GetMapping("join")
	public String join(Model model){
		model.addAttribute("joinForm", new MemberJoinForm()); //기본생성자 모델에 저장
		return "member/join";
	}
	
	// 회원 가입 처리
	@PostMapping("/join")
	public String join(@Valid @ModelAttribute("joinForm") MemberJoinForm joinForm,
					   BindingResult bindingResult,
					   Model model){
//      log.info("member: {}", member);
		// 회원 아이디 길이 체크
		if(bindingResult.hasErrors()){
			log.info("4자리 미만 가입 불가");
			return "/member/join";
		}
		// 회원 아이디 중복 체크
		if(memberService.isDuplicateLoginId(joinForm.getLoginId())){
			// 중복된 아이디가 있으면 가입 안됨
			log.info("이미 사용중인 아이디 입니다.");
			model.addAttribute("error", "이미 사용중인 아이디입니다.");
			return "/member/join";
		}
		memberService.join(joinForm);
		log.info("회원가입 완료");
		return "redirect:/login";
	}
	
	// 로그인 페이지
	@GetMapping("login")
	public String login(@RequestParam(required = false) String redirectUrl, Model model) {
		model.addAttribute("loginForm", new LoginForm());
		if(redirectUrl != null){
			model.addAttribute("redirectUrl", redirectUrl);
		}
		model.addAttribute("loginForm", new LoginForm());
		return "member/login";
	}
	
	// 로그인 처리
	@PostMapping("/login")
	public String login(
			@Valid @ModelAttribute("loginForm") LoginForm loginForm,
						BindingResult bindingResult,
						@RequestParam(required = false) String redirectUrl,
						Model model, HttpServletRequest request){ // 세션은 request에 저장되어 있다.
		//파라미터 유효성 검증 성공 여부 확인
		log.info("bindingResult: {}", bindingResult);
		if (bindingResult.hasErrors()) {
			log.info("파라미터 검증 실패");
			return "member/login";
		}
		Optional<Member> loginMember
				= memberService.login(loginForm.getLoginId(), loginForm.getPassword());
		if(loginMember.isEmpty()){
			log.info("아이디 또는 비밀번호가 올바르지 않습니다.");
			bindingResult.reject("loginFail","아이디 또는 비밀번호가 올바르지 않습니다.");
			return "member/login";
		}
		// 로그인이 성공하면 -> 세션에 회원 정보를 저장 -> 세션은 request에 저장
		HttpSession session = request.getSession();
		session.setAttribute("loginMember", loginMember.get());
		log.info("로그인 성공");
		if(redirectUrl !=null){
			return "redirect:/" + redirectUrl;
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
	
	//에러 페이지 테스트
	@GetMapping("/error-500")
	public String error_500(){
		log.info("500에러 발생");
		throw new RuntimeException("500에러 발생");// 예외처리 따로 안해도
												// 템플릿-에러-500.html을 찾아서 내보낸다
	}
	
	@GetMapping("/error-404")
	public String error_404(){
		log.info("404에러 발생");
		throw new RuntimeException("404에러 발생"); //똑같다
	}
}
