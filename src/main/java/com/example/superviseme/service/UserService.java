package com.example.superviseme.service;


import com.example.superviseme.entities.StudentProfile;
import com.example.superviseme.entities.User;
import com.example.superviseme.enums.Role;
import com.example.superviseme.record.StudentLoginCheck;
import com.example.superviseme.record.StudentRecord;
import com.example.superviseme.record.StudentRegistrationDto;
import com.example.superviseme.record.StudentProfileRecord;
import com.example.superviseme.repository.StudentProfileRepository;
import com.example.superviseme.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@Getter
@Setter
public class UserService {

    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final StudentChapterService studentChapterService;


    public UserService(UserRepository userRepository, StudentProfileRepository studentProfileRepository, StudentChapterService studentChapterService) {
        this.userRepository = userRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.studentChapterService = studentChapterService;
    }

    // Core User Methods
    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmailEqualsIgnoreCase(email).orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }


    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public List<User> findByRole(Role role) {
        return userRepository.findByRoleIs(role);
    }

    // Student Profile Methods
    public StudentProfile createStudentProfile(UUID userId, StudentProfile studentProfile) {
        User user = findById(userId);

        if (user.getRole() != Role.STUDENT) {
            throw new RuntimeException("User is not a student");
        }

        // Check if student profile already exists
        if (studentProfileRepository.findByUserIs(user).isPresent()) {
            throw new RuntimeException("Student profile already exists for this user");
        }

        // Check if student ID is unique
        if (studentProfile.getStudentId() != null && studentProfileRepository.findByStudentIdIs(studentProfile.getStudentId()).isPresent()) {
            throw new RuntimeException("Student ID already exists: " + studentProfile.getStudentId());
        }

        studentProfile.setUser(user);
        StudentProfile savedProfile = studentProfileRepository.save(studentProfile);

        // Update user's student profile reference
        user.setStudentProfile(savedProfile);
        userRepository.save(user);

        return savedProfile;
    }

    public ResponseEntity<User> updateStudentProfile(StudentProfileRecord record) {

        // Student record Id
        User user = findById(record.id());


        // Updating User record
        if (record.email() != null && !existsByEmail(record.email().trim())) {
            user.setEmail(record.email().trim());
        } else {
            throw new RuntimeException("Email already taken");
        }

        if (record.phoneNumber() != null) {
            user.setPhoneNumber(record.phoneNumber());
        }


        if (record.role() != null) {
            user.setRole(record.role());
        }


        StudentProfile studentProfile = user.getStudentProfile();
        if (studentProfile == null || studentProfile.getId() == null) studentProfile = new StudentProfile();
        // Update allowed fields
        if (record.fullName() != null) {
            studentProfile.setFullName(record.fullName());
        }

        if (record.isActive() != null) {
            // Activating student profile
            studentProfile.setIsActive(record.isActive());

            // todo: Student Create chapter 1 for the new user

        }

        if (record.programType() != null) studentProfile.setProgramType(record.programType());

        if (record.researchArea() != null) {
            studentProfile.setResearchArea(record.researchArea());
        }
        if (record.lectureAlignment() != null) {
            studentProfile.setLectureAlignment(record.lectureAlignment());
        }
        if (record.thesisTitle() != null) {
            studentProfile.setResearchTopic(record.thesisTitle());
        }

        studentProfile.setStudentId(user.getStudentId());


        if (record.abstractText() != null) {
            studentProfile.setAbstractText(record.abstractText());
        }
        if (record.researchObjective() != null) {
            studentProfile.setResearchObjectives(record.researchObjective());
        }


        user = userRepository.save(user);

        studentProfile.setUser(user);
        studentProfileRepository.save(studentProfile);
        return ResponseEntity.ok(findById(user.getId()));
    }

    public StudentProfile getStudentProfile(UUID userId) {
        User user = findById(userId);
        return studentProfileRepository.findByUserIs(user).orElseThrow(() -> new RuntimeException("Student profile not found"));
    }


    // Utility Methods
    public ResponseEntity<Object> getProfileByRole(UUID userId) {
        User user = findById(userId);
        Object response = switch (user.getRole()) {
            case STUDENT -> getStudentProfile(userId);
//            case SUPERVISOR -> getSupervisorProfile(userId);
//            case ADMIN -> getAdminProfile(userId);
        };
        return ResponseEntity.ok(response);
    }

    public boolean hasProfile(UUID userId) {
        User user = findById(userId);
        return switch (user.getRole()) {
            case STUDENT -> studentProfileRepository.findByUserIs(user).isPresent();
//            case SUPERVISOR -> supervisorProfileRepository.findByUserIs(user).isPresent();
//            case ADMIN -> adminProfileRepository.findByUserIs(user).isPresent();
        };
    }


    public StudentLoginCheck existsByStudentIdAndPin(StudentRegistrationDto dto) {
        // Check if user exist
        Optional<User> optional = userRepository.findByPinEqualsAndStudentIdEquals(dto.pin(), dto.studentId());

        // Set default return parameters
        boolean isUserCreated = false;
        boolean isUserProfileCreated = false;
        boolean isUserProfileActive = false;
        User user = new User();

        // Actual return parameters
        if (optional.isPresent()) {

            // if user exists
            isUserCreated = true;
            user = optional.get();

            // if Profile exists
            if (optional.get().getStudentProfile() != null) {
                isUserProfileCreated = true;

                // Get profile status
                isUserProfileActive = optional.get().getStudentProfile().getIsActive();
            }
        }

        return new StudentLoginCheck(isUserCreated, isUserProfileCreated, isUserProfileActive, user);
    }


    // todo: handle errors
    public ResponseEntity<?> initializeStudentCreation(StudentRegistrationDto dto) {

        StudentLoginCheck student = existsByStudentIdAndPin(dto);
        StudentRecord studentRecord;

        // if user exists and profile is active, return it
        if (student.isStudentCreated()) {
            studentRecord = new StudentRecord(student.user().getId(), student.user().getStudentId(), student.user().getRole(), student.user().getStudentProfile(), student.isStudentCreated(), student.isStudentProfileCreated(), student.isStudentProfileActive());
        } else {
            // Create the user object
            User user = new User();
            user.setPin(dto.pin());
            user.setStudentId(dto.studentId());
            user.setRole(Role.STUDENT);

            // Save the user object
            user = createUser(user);


            // Build the return object
            studentRecord = new StudentRecord(user.getId(), user.getStudentId(), user.getRole(), user.getStudentProfile(), true, false, false);
        }


        return ResponseEntity.ok(studentRecord);


    }
}