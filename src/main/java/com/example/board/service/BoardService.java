package com.example.board.service;

import com.example.board.domain.Board;
import com.example.board.domain.Member;
import com.example.board.dto.BoardForm;
import com.example.board.exception.BoardNotFoundException;
import com.example.board.exception.UnauthorizedException;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/*
 *스프링 빈 주입 방법
 * 1. 필드 주입 @Autowired
 * 2. 생성자 주입 생성자에 @Autowired
 * 3. setter 주입 setter에 @Autowired
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor //생성자 주입
//@Component
public class BoardService {
//	필드 주입
	//	@Autowired
	private final BoardRepository boardRepository;
	private final MemberRepository memberRepository;
	private final FileService fileService;

//	생성자 주입
//	@Autowired
//	public BoardService(BoardRepository boardRepository){
//		this.boardRepository=boardRepository; //생성자 주입방식
//	log.info("BoardService 생성");
//	} 위에서 해서 필요없어짐
	
//	@Autowired
//	public void setBoardRepository(BoardMemoryRepository boardRepository){
//		this.boardRepository = boardRepository;
//	}
	
	//전체 글 목록
	public Page<Board> getList(Pageable pageable, String searchType, String searchKeyword) {
		//전체 조회일 경우
		if(searchKeyword == null || searchKeyword.isBlank()) {
			return boardRepository.findAllWithMember(pageable);
		}
		//제목 검색일 경우
		if(searchType.equals("title")) {
			return boardRepository.findByTitleContaining(searchKeyword, pageable);
		}
		//제목 + 내용
		return boardRepository.findByTitleContainingOrContentContaining(searchKeyword, pageable);
	}
	
	//글 작성
	@Transactional
	public Board write(BoardForm boardForm, String writer, MultipartFile file)throws IOException{
		//writer로 회원 정보 조회
		Optional<Member> member = memberRepository.findByLoginId(writer);
		Member loginMember = member.get();
		if(member.isPresent()){
			loginMember = member.get();
			//Board 클래스의 생성자를 호출하여 Board 객체 생성
			Board board = new Board(boardForm.getTitle(), boardForm.getContent(),loginMember);
			
			//첨부파일이 있을 경우에 저장
			if(file != null && !file.isEmpty()){
				String storedFileName = fileService.storeFile(file);
				board.setOriginalFileName(file.getOriginalFilename());
				board.setStoredFileName(storedFileName);
			}
			
			//데이터베이스에 저장
			return boardRepository.save(board);
		}
//		Board board = new Board();
//		board.setTitle(boardForm.getTitle());
//		board.setContent(boardForm.getContent());
//	//	board.setWriter(writer);
//		board.setViewCount(0);
//		board.setCreatedAt(LocalDateTime.now());
//		return boardRepository.save(board);
		return null;
	}
	
	//글 조회(조회 수 증가) --트랜잭션 필요
	@Transactional
	public Board getDetailAndIncreaseView(Long id){
		Board board = getById(id);
		board.setViewCount(board.getViewCount()+1);
		return board;
	}
	
	// 글 조회 --단순 조회라 필요하지않아
	public Board getById(Long id){
		Board board = boardRepository.findWithMember(id);
		if(board == null){
			throw new BoardNotFoundException("ID : " + id + "게시글이 존재하지 않습니다.");
		}
		return board;
	}
	
	// 글 수정
	@Transactional
	public void update(Long id, BoardForm updateBoard,
					   MultipartFile file,
					   boolean deleteFile) throws IOException {
		Board findBoard = boardRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("게시글이 없습니다."));

		findBoard.setTitle(updateBoard.getTitle());
		findBoard.setContent(updateBoard.getContent());
		
		// 기존 파일 삭제
		if(deleteFile && findBoard.getStoredFileName() != null){
			fileService.deleteFile(findBoard.getStoredFileName());
			findBoard.setOriginalFileName(null);
			findBoard.setStoredFileName(null);
		}
		
		// 새 파일 업로드
		if(file != null && !file.isEmpty()) {
			
			// 기존 파일이 남아있으면 삭제
			if (findBoard.getStoredFileName() != null) {
				fileService.deleteFile(findBoard.getStoredFileName());
			}
			
			String storedFileName = fileService.storeFile(file);
			
			findBoard.setOriginalFileName(file.getOriginalFilename());
			findBoard.setStoredFileName(storedFileName);
		}
	}
	// 글 삭제
	@Transactional
	public void delete(Long id){
		// 첨부파일이 있으면 먼저 삭제
		boardRepository.findById(id).ifPresent(
				board -> fileService.deleteFile(board.getStoredFileName())
		);
		
		boardRepository.deleteById(id);
	}
	
	// 글의 작성자와 로그인 회원의 아이디가 같은지 확인하는 메서드 (수정, 삭제할 떄 권한이 있는지 체크하는 용도)
	public boolean isOwner(Long id, String loginId){
		Board board = getById(id);
		if(board != null && board.getMember().getLoginId().equals(loginId)){
			return true;
		}else {
			throw new UnauthorizedException("수정 및 삭제 권한이 없다");
		}
		
	}
	
	
	
}

