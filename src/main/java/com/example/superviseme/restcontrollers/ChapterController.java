package com.example.superviseme.restcontrollers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

	@GetMapping(value = "/")
	public ResponseEntity<?> getChapters(){
		return chapterService.getChapters();
	}
	
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<?> deleteChapter(@PathVariable UUID id){
        return chapterService.deleteChapter(id);
    }

}
