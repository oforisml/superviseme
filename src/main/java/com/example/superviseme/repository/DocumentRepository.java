package com.example.superviseme.repository;

import com.example.superviseme.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {
    @Query("select t from tbl_resource t where t.submission.id = ?1")
    List<Document> findBySubmission_IdEquals(UUID id);
}
