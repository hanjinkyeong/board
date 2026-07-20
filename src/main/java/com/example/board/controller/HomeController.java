package com.example.board.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@Slf4j
public class HomeController {
	@GetMapping("/")
	public String home(){
		log.info("메인 경로 호출");
		return "redirect:/boards";
	}
	
}
