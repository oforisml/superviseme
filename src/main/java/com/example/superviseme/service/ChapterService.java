package com.example.superviseme.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.superviseme.entities.Chapter;
import com.example.superviseme.record.ChapterRecord;
import com.example.superviseme.repository.ChapterRepository;

@Service
public class ChapterService {
	
	private final ChapterRepository chapterRepository;
	
	ChapterService(ChapterRepository chapterRepository){
		this.chapterRepository = chapterRepository;
	}
	
	//Create or update chapter 
	public ResponseEntity<?> saveChapter(ChapterRecord chapterDto) {
		
		if(chapterDto != null) {
			Chapter chapter = new Chapter();
			
			if(chapterDto.uuid() != null) {
				chapter.setId(UUID.fromString(chapterDto.uuid()));
			}
			if(chapterDto.nextChapterId() != null) {
				Optional<Chapter> nexChapter = chapterRepository.findById(UUID.fromString(chapterDto.uuid()));
				chapter.setNextChapter(nexChapter.get());
			}
			if(chapterDto.name() != null) {
				chapter.setName(chapterDto.name());
			}
			
			chapter = chapterRepository.save(chapter);
			return ResponseEntity.ok(chapter);
		}
		return ResponseEntity.ofNullable(null);
		
	}
	
	
	//Delete chapter
	public ResponseEntity<?> deleteChapter(UUID id) {
		Optional<Chapter> chapterToDelete = chapterRepository.findById(id);
		if(chapterToDelete.isPresent()) {
			chapterRepository.delete(chapterToDelete.get());
			return ResponseEntity.ok(chapterToDelete.get());
		}
		return ResponseEntity.ofNullable(null);
		
	}
	

}
