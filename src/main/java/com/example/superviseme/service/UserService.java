package com.example.superviseme.service;


import com.example.superviseme.entities.StudentProfile;
import com.example.superviseme.entities.User;
import com.example.superviseme.record.StudentRegistrationDto;
import com.example.superviseme.repository.StudentProfileRepository;
import com.example.superviseme.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;


    public UserService(UserRepository userRepository,
                       StudentProfileRepository studentProfileRepository
    ) {
        this.userRepository = userRepository;
        this.studentProfileRepository = studentProfileRepository;
    }

    // Core User Methods
    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmailEqualsIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }


    public void deleteUser(UUID id) {
        User user = findById(id);
        userRepository.delete(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public List<User> findByRole(User.Role role) {
        return userRepository.findByRoleIs(role);
    }

    // Student Profile Methods
    public StudentProfile createStudentProfile(UUID userId, StudentProfile studentProfile) {
        User user = findById(userId);

        if (user.getRole() != User.Role.STUDENT) {
            throw new RuntimeException("User is not a student");
        }

        // Check if student profile already exists
        if (studentProfileRepository.findByUserIs(user).isPresent()) {
            throw new RuntimeException("Student profile already exists for this user");
        }

        // Check if student ID is unique
        if (studentProfile.getStudentId() != null &&
                studentProfileRepository.findByStudentIdIs(studentProfile.getStudentId()).isPresent()) {
            throw new RuntimeException("Student ID already exists: " + studentProfile.getStudentId());
        }

        studentProfile.setUser(user);
        StudentProfile savedProfile = studentProfileRepository.save(studentProfile);

        // Update user's student profile reference
        user.setStudentProfile(savedProfile);
        userRepository.save(user);

        return savedProfile;
    }

    public StudentProfile updateStudentProfile(UUID userId, StudentProfile studentProfileDetails) {
        User user = findById(userId);
        StudentProfile studentProfile = studentProfileRepository.findByUserIs(user)
                .orElseThrow(() -> new RuntimeException("Student profile not found"));

        // Update allowed fields
        if (studentProfileDetails.getFullName() != null) {
            studentProfile.setFullName(studentProfileDetails.getFullName());
        }
        if (studentProfileDetails.getStudentId() != null) {
            // Check if student ID is unique (if changing)
            if (!studentProfileDetails.getStudentId().equals(studentProfile.getStudentId())) {
                if (studentProfileRepository.findByStudentIdIs(studentProfileDetails.getStudentId()).isPresent()) {
                    throw new RuntimeException("Student ID already exists: " + studentProfileDetails.getStudentId());
                }
                studentProfile.setStudentId(studentProfileDetails.getStudentId());
            }
        }
        if (studentProfileDetails.getResearchArea() != null) {
            studentProfile.setResearchArea(studentProfileDetails.getResearchArea());
        }
        if (studentProfileDetails.getResearchTopic() != null) {
            studentProfile.setResearchTopic(studentProfileDetails.getResearchTopic());
        }
        if (studentProfileDetails.getThesisTitle() != null) {
            studentProfile.setThesisTitle(studentProfileDetails.getThesisTitle());
        }
        if (studentProfileDetails.getAbstractText() != null) {
            studentProfile.setAbstractText(studentProfileDetails.getAbstractText());
        }
        if (studentProfileDetails.getResearchObjectives() != null) {
            studentProfile.setResearchObjectives(studentProfileDetails.getResearchObjectives());
        }
        if (studentProfileDetails.getEnrollmentDate() != null) {
            studentProfile.setEnrollmentDate(studentProfileDetails.getEnrollmentDate());
        }
        if (studentProfileDetails.getExpectedGraduationDate() != null) {
            studentProfile.setExpectedGraduationDate(studentProfileDetails.getExpectedGraduationDate());
        }
        if (studentProfileDetails.getCurrentSemester() != null) {
            studentProfile.setCurrentSemester(studentProfileDetails.getCurrentSemester());
        }

        return studentProfileRepository.save(studentProfile);
    }

    public StudentProfile getStudentProfile(UUID userId) {
        User user = findById(userId);
        return studentProfileRepository.findByUserIs(user)
                .orElseThrow(() -> new RuntimeException("Student profile not found"));
    }


    // Utility Methods
    public Object getProfileByRole(UUID userId) {
        User user = findById(userId);
        return switch (user.getRole()) {
            case STUDENT -> getStudentProfile(userId);
//            case SUPERVISOR -> getSupervisorProfile(userId);
//            case ADMIN -> getAdminProfile(userId);
        };
    }

    public boolean hasProfile(UUID userId) {
        User user = findById(userId);
        return switch (user.getRole()) {
            case STUDENT -> studentProfileRepository.findByUserIs(user).isPresent();
//            case SUPERVISOR -> supervisorProfileRepository.findByUserIs(user).isPresent();
//            case ADMIN -> adminProfileRepository.findByUserIs(user).isPresent();
        };
    }

    public boolean existsByStudentIdAndPin(StudentRegistrationDto dto) {

        return userRepository.findByPinIsAndStudentIdIs(dto.pin(), dto.studentId())
                .map(User::getIsActive)
                .orElse(false);
    }


    public ResponseEntity<?> intializeStudentCreation(StudentRegistrationDto request) {
        // Check if the student exists

        Optional<User> optionalUser = userRepository.findByPinIsAndStudentIdIs(request.pin(), request.studentId());
        if(optionalUser.isPresent()){
            return ResponseEntity.ok(optionalUser.get());
        }

        User user = new User();
        user.setPin(request.pin());
        user.setStudentId(request.studentId());
        user = userRepository.save(user);
        return ResponseEntity.ok(user);
    }
}