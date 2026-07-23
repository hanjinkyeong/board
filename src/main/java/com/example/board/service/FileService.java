package com.example.board.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
@Slf4j
@Service
public class FileService {
	
	private final Path uploadPath;
	
	public FileService(@Value("${file.upload-path}") String uploadPath) {
		this.uploadPath = Paths.get(uploadPath);
	}
	
	// 파일을 보유 이름으로 변경하여 디스크에 저장한다.
	public String storeFile(MultipartFile file) throws IOException { //throws IOException 이 부분이 예외처리
		// 파일 저장 경로 설정
	//	Path uploadPath = Paths.get("./upload");
		// 저장 경로가 없으면 생성
		Files.createDirectories(uploadPath); //IOException에 throws하기때문에 오류가 남. 예외처리 해주어야함.
		// 원본 파일명
		String originalFilename = file.getOriginalFilename();
		log.info("originalFilename: " + originalFilename);
		// 저장 파일명 생성 abc.txt
		String ext = "";
		if(originalFilename!=null&&originalFilename.contains(".")){
			ext = originalFilename.substring(originalFilename.lastIndexOf("."));
		}
		//서버 저장용 파일명 생성
		String storedFileName = UUID.randomUUID() + ext; //ext : 확장자
		log.info("storedFileName: " + storedFileName);
		
		// 실제 디스크에 저장
		file.transferTo(uploadPath.resolve(storedFileName));
		
		log.info("파일 업로드 완료 original={}, stored={}, size={}, type={}",
				originalFilename, storedFileName, file.getSize(), file.getContentType());
		
		return storedFileName;
	}
	
	//저장된 파일 삭제
	public void deleteFile(String storedFileName){
		if(storedFileName ==null){
			return;
		}
		try{
			Files.deleteIfExists(uploadPath.resolve(storedFileName));
		}catch(IOException e){
			log.error("파일 삭제 실패");
		}
	}
}
