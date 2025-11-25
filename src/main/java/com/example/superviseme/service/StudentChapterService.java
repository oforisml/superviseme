package com.example.superviseme.service;

import com.example.superviseme.entities.Chapter;
import com.example.superviseme.entities.StudentChapter;
import com.example.superviseme.enums.StudentChapterStatus;
import com.example.superviseme.repository.StudentChapterRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class StudentChapterService {
    private final StudentChapterRepository repository;
    private final ChapterService chapterService;

    public StudentChapterService(StudentChapterRepository repository,
                                 ChapterService chapterService) {
        this.repository = repository;
        this.chapterService = chapterService;
    }


    public StudentChapter findById(UUID studentChapterId) {
        return repository.findById(studentChapterId).orElse(new StudentChapter());
    }


    public StudentChapter persist(StudentChapter studentChapter) {
        return repository.save(studentChapter);
    }

    public void advanceStudentChapter(StudentChapter studentChapter) {
        if(studentChapter == null) throw new IllegalArgumentException("Could not open next chapter");

        // About to create new chapter for the student
        UUID nextChapterId = studentChapter.getChapter().getNextChapterId();
        if(nextChapterId == null)
            return;
        Chapter nextChapter = chapterService.findChapterById(nextChapterId);

        StudentChapter newStudentChapter = new StudentChapter();

        // Opening new chapter for student to be able to update
        newStudentChapter.setStudentId(studentChapter.getStudentId());
        newStudentChapter.setChapter(nextChapter);
        newStudentChapter.setStatus(StudentChapterStatus.OPENED);

        newStudentChapter = repository.save(newStudentChapter);

        // Changing status of the old student chapter
        studentChapter.setStatus(StudentChapterStatus.CLOSED);
        studentChapter = repository.save(studentChapter);


    }

    public ResponseEntity<?> findAllChaptersByStudentId(String studentId) {
        List<StudentChapter> studentChapterList = repository.findByStudentIdOrderByCreatedAtAsc(studentId);
        return ResponseEntity.ok(studentChapterList);

    }

    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }
}
