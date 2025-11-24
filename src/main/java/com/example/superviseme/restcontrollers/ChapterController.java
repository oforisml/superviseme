package com.example.superviseme.restcontrollers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.superviseme.record.ChapterRecord;
import com.example.superviseme.record.MeetingRecord;
import com.example.superviseme.service.ChapterService;

@RestController
@RequestMapping(name="Chapter Controller", value = "/chapter")
public class ChapterController {
	
	private final ChapterService chapterService;
	
	public ChapterController(ChapterService chapterService) {
		this.chapterService = chapterService;
	}
	
	@PostMapping(value = "/")
    public ResponseEntity<?> saveChapter(@RequestBody ChapterRecord chapterDto){
        return chapterService.saveChapter(chapterDto);
    }
	
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<?> deleteChapter(@PathVariable UUID id){
        return chapterService.deleteChapter(id);
    }

}
