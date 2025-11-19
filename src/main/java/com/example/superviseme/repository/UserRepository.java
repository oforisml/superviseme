package com.example.superviseme.repository;

import com.example.superviseme.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("select u from User u where u.pin = ?1 and u.studentId = ?2")
    Optional<User> findByPinIsAndStudentIdIs(String pin, String studentId);
    @Query("select u from User u where u.role = ?1")
    List<User> findByRoleIs(User.Role role);
    @Query("select count(u) from User u where upper(u.role) = upper(?1)")
    long countByRoleIsIgnoreCase(String role);
    @Query("select u from User u where upper(u.email) = upper(?1)")
    Optional<User> findByEmailEqualsIgnoreCase(String email);
    boolean existsByEmail(String email);

}

