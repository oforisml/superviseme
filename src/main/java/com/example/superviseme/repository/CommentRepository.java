package com.example.superviseme.repository;

import com.example.superviseme.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    @Query("select t from tbl_comment t where t.submission.id = ?1")
    List<Comment> findBySubmission_IdEquals(UUID id);
}
