package com.example.superviseme.service;

import com.example.superviseme.entities.Chapter;
import com.example.superviseme.entities.StudentChapter;
import com.example.superviseme.entities.Submission;
import com.example.superviseme.enums.StudentChapterStatus;
import com.example.superviseme.record.StatisticsRecord;
import com.example.superviseme.record.WeeklySubmission;
import com.example.superviseme.repository.StudentChapterRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * This class basically is services as the service for the student chapter and it handles
 * the business logic for student-chapter management.
 * Within a student chapter multiple submissions can be submitted for that chapter depending on whether the supervisor
 * requests them.
 * One each submission only the supervisor can comment on
 * @author Samuel O.
 *
 */

@Service
@Transactional
public class StudentChapterService {
    private final StudentChapterRepository repository;
    private final ChapterService chapterService;


    private final JdbcTemplate template;


    public StudentChapterService(StudentChapterRepository repository,
                                 ChapterService chapterService, JdbcTemplate template) {
        this.repository = repository;
        this.chapterService = chapterService;
        this.template = template;
    }

    /**
     * Handle takes care of returning student chapter objects when requested
     * @param studentChapterId
     * @return
     */

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

    public ResponseEntity<?> findAllChaptersByStudentId(UUID studentId) {
        List<StudentChapter> studentChapterList = repository.findByStudentIdOrderByCreatedAtAsc(studentId);
        return ResponseEntity.ok(studentChapterList);

    }

    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    public Map<String, Map<String, Integer>> findPermissionPerMonth(String studentId) {

        // Get lists
        List<StatisticsRecord> submissions = getSubmissionsForThePastMonths(studentId);
        List<StatisticsRecord> meetings = getMeetingsForThePastMonths(studentId);

        // Map to store combined data
        Map<String, Map<String, Integer>> monthStats = new LinkedHashMap<>();

        // Populate submissions
        for (StatisticsRecord record : submissions) {
            String key = record.monthName() + " " + record.monthYear(); // e.g., "January 2025"
            monthStats.putIfAbsent(key, new HashMap<>());
            monthStats.get(key).put("submissions", record.count());
            monthStats.get(key).put("meetings", 0); // default
        }

        // Populate meetings
        for (StatisticsRecord record : meetings) {
            String key = record.monthName() + " " + record.monthYear();
            monthStats.putIfAbsent(key, new HashMap<>());
            monthStats.get(key).put("meetings", record.count());
            monthStats.get(key).putIfAbsent("submissions", 0);
        }

        // Calculate total per month
        for (Map<String, Integer> counts : monthStats.values()) {
            int submissionsCount = counts.getOrDefault("submissions", 0);
            int meetingsCount = counts.getOrDefault("meetings", 0);
            counts.put("total", submissionsCount + meetingsCount);
        }

        return monthStats;
    }

    public List<WeeklySubmission> findSubmissionsPerWeek(String studentId){
        StudentChapter studentChapter = repository.findByStudentIdEqualsAllIgnoreCaseOrderByCreatedAtAsc(studentId)
                .orElseGet(()->{
                    StudentChapter chapter = new StudentChapter();
                    chapter.setCreatedAt(LocalDateTime.now());
                    return chapter;
                });

        LocalDate date = studentChapter.getCreatedAt().toLocalDate();


        String fetchSubmissionsPerWeek = "WITH RECURSIVE weeks(week_start, week_end, week_number) AS ( " +
                "    SELECT CAST('" + date+"' AS DATE), " +
                "           DATEADD('DAY', 6, CAST('" + date+"' AS DATE)), " +
                "           1 " +
                "    UNION ALL " +
                "    SELECT DATEADD('DAY', 7, week_start), " +
                "           DATEADD('DAY', 7, week_end), " +
                "           week_number + 1 " +
                "    FROM weeks " +
                "    WHERE week_start < CURRENT_DATE " +
                ") " +
                "SELECT " +
                "        'Week ' || week_number AS week_label, " +
                "        COUNT(s.id) AS submission_count " +
                "FROM weeks w " +
                "         LEFT JOIN TBL_SUBMISSION s " +
                "                   ON s.created_at >= w.week_start " +
                "                       AND s.created_at < DATEADD('DAY', 1, w.week_end)  " +
                "                       AND s.STUDENT_CHAPTER_ID IN ( " +
                "                           SELECT ID FROM TBL_STUDENT_CHAPTER WHERE STUDENT_ID =  '" + studentId +"'"+
                "                           ) " +
                "GROUP BY week_number " +
                "ORDER BY week_number;";

            return template.query(fetchSubmissionsPerWeek, (rs, rc) -> new WeeklySubmission(rs.getString("week_label"),  rs.getInt("submission_count")));



    }

    private List<StatisticsRecord> getSubmissionsForThePastMonths(String studentId) {
        String submissionsForThePastMonths = "SELECT    " +
                "    m.month_year,    " +
                "    m.month_number,    " +
                "    m.month_name,    " +
                "    COUNT(TSC.id) AS submission_count    " +
                "FROM (    " +
                "         SELECT    " +
                "             FORMATDATETIME(CURRENT_DATE, 'yyyy') AS month_year,    " +
                "             FORMATDATETIME(CURRENT_DATE, 'MM') AS month_number,    " +
                "             FORMATDATETIME(CURRENT_DATE, 'MMMM') AS month_name,    " +
                "             0 AS month_index    " +
                "         UNION ALL    " +
                "         SELECT FORMATDATETIME(DATEADD('MONTH', -1, CURRENT_DATE), 'yyyy'),    " +
                "                FORMATDATETIME(DATEADD('MONTH', -1, CURRENT_DATE), 'MM'),    " +
                "                FORMATDATETIME(DATEADD('MONTH', -1, CURRENT_DATE), 'MMMM'),    " +
                "                1    " +
                "         UNION ALL    " +
                "         SELECT FORMATDATETIME(DATEADD('MONTH', -2, CURRENT_DATE), 'yyyy'),    " +
                "                FORMATDATETIME(DATEADD('MONTH', -2, CURRENT_DATE), 'MM'),    " +
                "                FORMATDATETIME(DATEADD('MONTH', -2, CURRENT_DATE), 'MMMM'),    " +
                "                2    " +
                "         UNION ALL    " +
                "         SELECT FORMATDATETIME(DATEADD('MONTH', -3, CURRENT_DATE), 'yyyy'),    " +
                "                FORMATDATETIME(DATEADD('MONTH', -3, CURRENT_DATE), 'MM'),    " +
                "                FORMATDATETIME(DATEADD('MONTH', -3, CURRENT_DATE), 'MMMM'),    " +
                "                3    " +
                "         UNION ALL    " +
                "         SELECT FORMATDATETIME(DATEADD('MONTH', -4, CURRENT_DATE), 'yyyy'),    " +
                "                FORMATDATETIME(DATEADD('MONTH', -4, CURRENT_DATE), 'MM'),    " +
                "                FORMATDATETIME(DATEADD('MONTH', -4, CURRENT_DATE), 'MMMM'),    " +
                "                4    " +
                "     ) AS m    " +
                "         LEFT JOIN TBL_SUBMISSION s    " +
                "                   ON FORMATDATETIME(s.created_at, 'yyyy') = m.month_year    " +
                "                       AND FORMATDATETIME(s.created_at, 'MM') = m.month_number    " +
                "         LEFT JOIN TBL_STUDENT_CHAPTER TSC    " +
                "                   ON s.STUDENT_CHAPTER_ID = TSC.ID    " +
                "                       AND TSC.STUDENT_ID = '" +studentId + "'"+
                "GROUP BY m.month_number, m.month_name, m.month_index, m.month_year    " +
                "ORDER BY m.month_index DESC;    ";

        return template.query(submissionsForThePastMonths, (rs,rc) -> new StatisticsRecord(
                rs.getInt("month_year"),
                rs.getInt("month_number"),
                rs.getString("month_name"),
                rs.getInt("submission_count"))
                );

    }

    private List<StatisticsRecord> getMeetingsForThePastMonths(String studentId) {
        String meetingForThePastMonths = "SELECT  " +
                "    m.month_year,  " +
                "    m.month_number,  " +
                "    m.month_name,  " +
                "    COUNT(s.id) AS meeting_count  " +
                "FROM (  " +
                "         SELECT FORMATDATETIME(CURRENT_DATE, 'yyyy') AS month_year,  " +
                "                FORMATDATETIME(CURRENT_DATE, 'MM') AS month_number,  " +
                "                FORMATDATETIME(CURRENT_DATE, 'MMMM') AS month_name,  " +
                "                0 AS month_index  " +
                "         UNION ALL  " +
                "         SELECT FORMATDATETIME(DATEADD('MONTH', -1, CURRENT_DATE), 'yyyy'),  " +
                "                FORMATDATETIME(DATEADD('MONTH', -1, CURRENT_DATE), 'MM'),  " +
                "                FORMATDATETIME(DATEADD('MONTH', -1, CURRENT_DATE), 'MMMM'),  " +
                "                1  " +
                "         UNION ALL  " +
                "         SELECT FORMATDATETIME(DATEADD('MONTH', -2, CURRENT_DATE), 'yyyy'),  " +
                "                FORMATDATETIME(DATEADD('MONTH', -2, CURRENT_DATE), 'MM'),  " +
                "                FORMATDATETIME(DATEADD('MONTH', -2, CURRENT_DATE), 'MMMM'),  " +
                "                2  " +
                "         UNION ALL  " +
                "         SELECT FORMATDATETIME(DATEADD('MONTH', -3, CURRENT_DATE), 'yyyy'),  " +
                "                FORMATDATETIME(DATEADD('MONTH', -3, CURRENT_DATE), 'MM'),  " +
                "                FORMATDATETIME(DATEADD('MONTH', -3, CURRENT_DATE), 'MMMM'),  " +
                "                3  " +
                "         UNION ALL  " +
                "         SELECT FORMATDATETIME(DATEADD('MONTH', -4, CURRENT_DATE), 'yyyy'),  " +
                "                FORMATDATETIME(DATEADD('MONTH', -4, CURRENT_DATE), 'MM'),  " +
                "                FORMATDATETIME(DATEADD('MONTH', -4, CURRENT_DATE), 'MMMM'),  " +
                "                4  " +
                "     ) AS m  " +
                "         LEFT JOIN TBL_MEETING s  " +
                "                   ON FORMATDATETIME(s.created_at, 'yyyy') = m.month_year  " +
                "                       AND FORMATDATETIME(s.created_at, 'MM') = m.month_number  " +
                "                       AND s.STUDENT_ID =   '" + studentId + "'" +
                "GROUP BY m.month_year, m.month_number, m.month_name, m.month_index  " +
                "ORDER BY m.month_index DESC;";

        return template.query(meetingForThePastMonths, (rs, rc) -> new StatisticsRecord(
                rs.getInt("month_year"),
                rs.getInt("month_number"),
                rs.getString("month_name"),
                rs.getInt("meeting_count"))
        );
    }
    public ResponseEntity<?> getCurrentStage(String studentId){
    	List<StudentChapter> studentChapters = repository.findByStudentIdAndStatus(studentId,StudentChapterStatus.OPENED);
    	return ResponseEntity.ok(studentChapters);    
    }
    
    public ResponseEntity<?> getCompletedChapters(String studentId){
    	List<StudentChapter> studentChapters = repository.findByStudentIdAndStatus(studentId,StudentChapterStatus.CLOSED);
    	return ResponseEntity.ok(studentChapters);    
    }

    public List<Map<String, String>> getRecentSubmissions(String studentId) {
        List<Map<String, String>> recents = new ArrayList();

        List<StudentChapter> studentChapters = repository.findByStudentIdEqualsIgnoreCase(studentId);
        for (StudentChapter chapter : studentChapters) {

            Map<String, String> sub = new HashMap<>();
            sub.put("chapter", chapter.getChapter().getName());

            // Find the latest submission once
            Optional<Submission> latest = chapter.getSubmissions().stream()
                    .max(Comparator.comparing(Submission::getCreatedAt));

            // Add date only if submission exists
            latest.map(Submission::getCreatedAt)
                    .map(LocalDateTime::toString)
                    .ifPresent(date -> sub.put("date", date));

            // Add status only if submission exists
            latest.map(Submission::getStatus)
                    .map(Object::toString)
                    .ifPresent(status -> sub.put("status", status.toLowerCase()));

            recents.add(sub);
        }


        return recents;
    }
}
