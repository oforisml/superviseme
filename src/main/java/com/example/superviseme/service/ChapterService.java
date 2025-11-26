package com.example.superviseme.service;

import java.util.Optional;
import java.util.UUID;

import com.example.superviseme.exceptionhandler.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.superviseme.entities.Chapter;
import com.example.superviseme.record.ChapterRecord;
import com.example.superviseme.repository.ChapterRepository;

@Service
@Transactional
public class ChapterService {

    private final ChapterRepository chapterRepository;

    ChapterService(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    //Create or update chapter
    public ResponseEntity<?> saveChapter(ChapterRecord chapterDto) {

        if (chapterDto == null) throw new IllegalArgumentException("Chapter object cannot be null");

        Chapter chapter = new Chapter();

        if (chapterDto.id() != null) {
            chapter = chapterRepository.findById(chapterDto.id()).orElseThrow(() -> new ResourceNotFoundException("Chapter for the id provided NOT found"));
        }

        if (chapterDto.nextChapterId() != null) {
            Optional<Chapter> nexChapter = chapterRepository.findById(chapterDto.id());
            if(!nexChapter.isPresent()) throw new ResourceNotFoundException("Specified Next Chapter not found");
            // Setting next chapter if it exists
            chapter.setNextChapterId(chapterDto.nextChapterId());
        }

        if (chapterDto.name() != null) {
            chapter.setName(chapterDto.name());
        }
        
        chapter.setStage(chapterDto.stage());

        chapter = chapterRepository.save(chapter);
        return ResponseEntity.ok(chapter);


    }


    //Delete chapter
    public ResponseEntity<?> deleteChapter(UUID id) {
        Optional<Chapter> chapterToDelete = chapterRepository.findById(id);
        if (chapterToDelete.isPresent()) {
            chapterRepository.delete(chapterToDelete.get());
            return ResponseEntity.ok(chapterToDelete.get());
        }
        return ResponseEntity.ofNullable(null);

    }


    public ResponseEntity<?> getChapters() {
        return ResponseEntity.ok(chapterRepository.findAll());
    }

    public Chapter findChapterById(UUID nextChapter) {
        return chapterRepository.findById(nextChapter).orElseThrow(()-> new ResourceNotFoundException("Unable to open next chapter"));
    }
}
