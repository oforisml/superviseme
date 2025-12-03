package com.example.superviseme.repository;

import com.example.superviseme.entities.User;
import com.example.superviseme.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("select (count(t) > 0) from tbl_user t where upper(t.email) = upper(?1) and t.id <> ?2")
    boolean existsByEmailEqualsIgnoreCaseAndIdNot(String email, UUID id);
    @Query("select u from tbl_user u where u.pin = ?1 and u.studentId = ?2")
    Optional<User> findByPinEqualsAndStudentIdEquals(String pin, String studentId);
    @Query("select u from tbl_user u where u.pin = ?1 and u.studentId = ?2")
    Optional<User> findByPinIsAndStudentIdIs(String pin, String studentId);
    @Query("select u from tbl_user u where u.role = ?1")
    List<User> findByRoleIs(Role role);
    @Query("select count(u) from tbl_user u where upper(u.role) = upper(?1)")
    long countByRoleIsIgnoreCase(String role);
    @Query("select u from tbl_user u where upper(u.email) = upper(?1)")
    Optional<User> findByEmailEqualsIgnoreCase(String email);

    Optional<User> findByPinAndStudentId(String pin, String studentId);
}

