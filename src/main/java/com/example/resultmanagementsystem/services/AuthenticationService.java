//package com.example.resultmanagementsystem.services;
//
//import com.example.resultmanagementsystem.Dto.Repository.TeacherRepository;
//import com.example.resultmanagementsystem.Dto.Repository.UserRepository;
//import com.example.resultmanagementsystem.Dto.userdto.AuthenticationRequest;
//import com.example.resultmanagementsystem.Dto.userdto.AuthenticationResponse;
//import com.example.resultmanagementsystem.Dto.userdto.RegisterRequest;
//import com.example.resultmanagementsystem.config.JWTService;
//import com.example.resultmanagementsystem.model.Role.Role;
//import com.example.resultmanagementsystem.model.Teacher;
//import com.example.resultmanagementsystem.model.User;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//import java.util.Set;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class AuthenticationService {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JWTService jwtService;
//    private final EmailService emailService;
//    private final AuthenticationManager authenticationManager;
//    private final TeacherRepository teacherRepository;
//
//    public AuthenticationResponse register(RegisterRequest request) {
//        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
//        if (existingUser.isPresent()) {
//            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
//        }
//
//        // Generate a verification token
//        String verificationToken = UUID.randomUUID().toString();
//
//        // Create a new user with verification token
//        User user = User.builder()
//                .firstname(request.getFirstname())
//                .lastname(request.getLastname())
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .role(request.getRole() != null ? Role.valueOf(request.getRole()) : Role.STUDENT)
//
//                .verificationToken(verificationToken)
//                .isVerified(false) // Default to false
//                .build();
//
//        // Save the user
//        userRepository.save(user);
//
//        // Send verification email
//        emailService.sendVerificationEmail(user.getEmail(), verificationToken);
//
//        return AuthenticationResponse.builder()
//                .message("User registered. Check your email for verification.")
//                .build();
//    }
//
//    public String verifyEmail(String token) {
//        User user = userRepository.findByVerificationToken(token)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid verification token"));
//
//        user.setVerified(true);
//        user.setVerificationToken(null); // Remove the token after verification
//        userRepository.save(user);
//
//        return "Email verified successfully!";
//    }
//
////    public AuthenticationResponse authenticate(AuthenticationRequest request) {
////        // Find the user by email
////        var user = userRepository.findByEmail(request.getEmail())
////                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));
////
////        // Generate JWT token
////        var token = jwtService.generateToken(user);
////
////        // Return the token and role in the response
////        return AuthenticationResponse.builder()
////                .token(token)
////                .firstName(user.getFirstname())
////                .departmentId(user.getDepartmentId())
////                .role(user.getRole().name())  // Convert enum to string
////                .build();
////    }
//
//
//    public AuthenticationResponse authenticate(AuthenticationRequest request) {
//        // Search in the users collection
//        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
//
//        if (userOpt.isPresent()) {
//            User user = userOpt.get();
//            return generateAuthResponse(user);
//        }
//
//        // Search in the teachers collection (if not found in users)
//        Optional<Teacher> teacherOpt = teacherRepository.findByEmail(request.getEmail());
//
//        if (teacherOpt.isPresent()) {
//            Teacher teacher = teacherOpt.get();
//            return generateAuthResponse(teacher);
//        }
//
//        throw new UsernameNotFoundException("User or Teacher not found with email: " + request.getEmail());
//    }
//
//    // Helper method to generate the response
//    private AuthenticationResponse generateAuthResponse(Object userOrTeacher) {
//        String token;
//        String firstName;
//        String email;
//        String lastName;
//        String role;
//        Set departmentId;
//
//        if (userOrTeacher instanceof User user) {
//            token = jwtService.generateToken(user);
//            firstName = user.getFirstname();
//            lastName = user.getLastname();
//            email = user.getEmail();
//            role = user.getRole().name();
//            departmentId = user.getDepartmentId();
//        } else if (userOrTeacher instanceof Teacher teacher) {
////            token = jwtService.generateToken(teacher);
//            firstName = teacher.getFirstname();
//            lastName = teacher.getLastname();
//            email = teacher.getEmail();
//            role = teacher.getRole().name();
//            departmentId = teacher.getDepartmentId();
//        } else {
//            throw new IllegalArgumentException("Invalid user type");
//        }
//
//        return AuthenticationResponse.builder()
////                .token(token)
//                .firstName(firstName)
//                .lastName(lastName)
//                .email(email)
//                .role(role)
//                .departmentId(departmentId)
//                .build();
//    }
//
//
//
//
//
//    public AuthenticationResponse registerStudent(RegisterRequest request, String studentId) {
//        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
//        if (existingUser.isPresent()) {
//            // User already exists, just make sure it's linked to the student
//            User user = existingUser.get();
//            user.setStudentId(studentId);
//            userRepository.save(user);
//
//            return AuthenticationResponse.builder()
//                    .message("User already exists and linked to student.")
//                    .build();
//        }
//
//        // Generate a verification token
//        String verificationToken = UUID.randomUUID().toString();
//
//        // Create a new user with verification token
//        User user = User.builder()
//                .firstname(request.getFirstname())
//                .lastname(request.getLastname())
//                .email(request.getEmail())
//                .password(request.getPassword()) // Assume password is already encoded in StudentService
//                .role(Role.STUDENT) // Always STUDENT for students
//                .studentId(studentId) // Set the studentId reference
//                .verificationToken(verificationToken)
//                .isVerified(true) // Set to true if you don't want email verification for students
//                .departmentId(Set.of(request.getDepartmentId())) // Set department
//                .build();
//
//        // Save the user
//        userRepository.save(user);
//
//        // Only send verification email if needed
//        if (!user.isVerified()) {
//            emailService.sendVerificationEmail(user.getEmail(), verificationToken);
//        }
//
//        return AuthenticationResponse.builder()
//                .message("Student registered for authentication.")
//                .build();
//    }
//
//    // Modify the authenticate method to search in student collection as well
//    public AuthenticationResponse authenticate(AuthenticationRequest request) {
//        // Search in the users collection
//        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
//
//        if (userOpt.isPresent()) {
//            User user = userOpt.get();
//            return generateAuthResponse(user);
//        }
//
//        // Search in the teachers collection
//        Optional<Teacher> teacherOpt = teacherRepository.findByEmail(request.getEmail());
//
//        if (teacherOpt.isPresent()) {
//            Teacher teacher = teacherOpt.get();
//            return generateAuthResponse(teacher);
//        }
//
//        // Search in the students collection
//        Optional<Student> studentOpt = studentRepository.findByEmail(request.getEmail());
//
//        if (studentOpt.isPresent()) {
//            Student student = studentOpt.get();
//
//            // Find associated user account
//            Optional<User> studentUserOpt = userRepository.findByStudentId(student.getStudentId());
//
//            if (studentUserOpt.isPresent()) {
//                return generateAuthResponse(studentUserOpt.get());
//            } else {
//                // Create user on-the-fly if it doesn't exist
//                RegisterRequest registerRequest = RegisterRequest.builder()
//                        .firstname(student.getName().split(" ")[0])
//                        .lastname(student.getName().contains(" ") ?
//                                student.getName().substring(student.getName().indexOf(" ") + 1) : "")
//                        .email(student.getEmail())
//                        .password(student.getPassword())
//                        .role("STUDENT")
//                        .build();
//
//                registerStudent(registerRequest, student.getStudentId());
//
//                // Now try to get the user again
//                Optional<User> newUser = userRepository.findByStudentId(student.getStudentId());
//                if (newUser.isPresent()) {
//                    return generateAuthResponse(newUser.get());
//                }
//            }
//        }
//
//        throw new UsernameNotFoundException("User, Teacher, or Student not found with email: " + request.getEmail());
//    }
//
//    // ... existing methods with updated generateAuthResponse
//    private AuthenticationResponse generateAuthResponse(Object userOrTeacher) {
//        String token;
//        String firstName;
//        String email;
//        String lastName;
//        String role;
//        Set departmentId;
//        String studentId = null;
//
//        if (userOrTeacher instanceof User user) {
//            token = jwtService.generateToken(user);
//            firstName = user.getFirstname();
//            lastName = user.getLastname();
//            email = user.getEmail();
//            role = user.getRole().name();
//            departmentId = user.getDepartmentId();
//            studentId = user.getStudentId(); // Add studentId if available
//        } else if (userOrTeacher instanceof Teacher teacher) {
//            token = jwtService.generateToken((UserDetails) teacher);
//            firstName = teacher.getFirstname();
//            lastName = teacher.getLastname();
//            email = teacher.getEmail();
//            role = teacher.getRole().name();
//            departmentId = teacher.getDepartmentId();
//        } else if (userOrTeacher instanceof Student student) {
//            // This assumes Student implements UserDetails or you have a way to generate a token
//            token = jwtService.generateToken(convertStudentToUserDetails(student));
//            firstName = student.getName().split(" ")[0];
//            lastName = student.getName().contains(" ") ?
//                    student.getName().substring(student.getName().indexOf(" ") + 1) : "";
//            email = student.getEmail();
//            role = "STUDENT";
//            departmentId = Set.of(student.getDepartmentId());
//            studentId = student.getStudentId();
//        } else {
//            throw new IllegalArgumentException("Invalid user type");
//        }
//
//        return AuthenticationResponse.builder()
//                .token(token)
//                .firstName(firstName)
//                .lastName(lastName)
//                .email(email)
//                .role(role)
//                .departmentId(departmentId)
//                .studentId(studentId) // Include studentId in response if available
//                .build();
//    }
//
//    // Helper method to convert Student to UserDetails
//    private UserDetails convertStudentToUserDetails(Student student) {
//        // Implementation depends on your Student model
//        // Here's a simple example using SimpleUserDetails
//        return User.builder()
//                .firstname(student.getName().split(" ")[0])
//                .lastname(student.getName().contains(" ") ?
//                        student.getName().substring(student.getName().indexOf(" ") + 1) : "")
//                .email(student.getEmail())
//                .password(student.getPassword())
//                .role(Role.STUDENT)
//                .build();
//    }
//}

package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.Repository.StudentRepository;
import com.example.resultmanagementsystem.Dto.Repository.TeacherRepository;
import com.example.resultmanagementsystem.Dto.Repository.UserRepository;
import com.example.resultmanagementsystem.Dto.userdto.AuthenticationRequest;
import com.example.resultmanagementsystem.Dto.userdto.AuthenticationResponse;
import com.example.resultmanagementsystem.Dto.userdto.RegisterRequest;
import com.example.resultmanagementsystem.config.JWTService;
import com.example.resultmanagementsystem.model.Role.Role;
import com.example.resultmanagementsystem.model.Student;
import com.example.resultmanagementsystem.model.Teacher;
import com.example.resultmanagementsystem.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public AuthenticationResponse register(RegisterRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }

        // Generate a verification token
        String verificationToken = UUID.randomUUID().toString();

        // Create a new user with verification token
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? Role.valueOf(request.getRole()) : Role.STUDENT)
                .verificationToken(verificationToken)
                .isVerified(false) // Default to false
                .build();

        // Set department if provided
        if (request.getDepartmentId() != null) {
            user.setDepartmentId(Set.of(request.getDepartmentId()));
        }

        // Save the user
        userRepository.save(user);

        // Send verification email
        emailService.sendVerificationEmail(user.getEmail(), verificationToken);

        return AuthenticationResponse.builder()
                .message("User registered. Check your email for verification.")
                .build();
    }

    public String verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification token"));

        user.setVerified(true);
        user.setVerificationToken(null); // Remove the token after verification
        userRepository.save(user);

        return "Email verified successfully!";
    }

    public AuthenticationResponse registerStudent(RegisterRequest request, String studentId) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            // User already exists, just make sure it's linked to the student
            User user = existingUser.get();
            user.setStudentId(studentId);
            userRepository.save(user);

            return AuthenticationResponse.builder()
                    .message("User already exists and linked to student.")
                    .build();
        }

        // Generate a verification token
        String verificationToken = UUID.randomUUID().toString();

        // Create a new user with verification token
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password("") // Encode the password
                .role(Role.STUDENT) // Always STUDENT for students
                .studentId(studentId) // Set the studentId reference
                .verificationToken(verificationToken)
                .isVerified(true) // Set to true if you don't want email verification for students
                .build();

        // Set department if provided
        if (request.getDepartmentId() != null) {
            user.setDepartmentId(Set.of(request.getDepartmentId()));
        }

        // Save the user
        userRepository.save(user);

        // Only send verification email if needed
        if (!user.isVerified()) {
            emailService.sendVerificationEmail(user.getEmail(), verificationToken);
        }

        return AuthenticationResponse.builder()
                .message("Student registered for authentication.")
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Search in the users collection
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return generateAuthResponse(user);
        }

        // Search in the teachers collection
        Optional<Teacher> teacherOpt = teacherRepository.findByEmail(request.getEmail());

        if (teacherOpt.isPresent()) {
            Teacher teacher = teacherOpt.get();
            return generateAuthResponse(teacher);
        }

        // Search in the students collection
        Optional<Student> studentOpt = studentRepository.findByEmail(request.getEmail());

        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();

            // Find associated user account
            Optional<User> studentUserOpt = userRepository.findByStudentId(student.getStudentId());

            if (studentUserOpt.isPresent()) {
                return generateAuthResponse(studentUserOpt.get());
            } else {
                // Create user on-the-fly if it doesn't exist
                RegisterRequest registerRequest = RegisterRequest.builder()
                        .firstname(student.getName().split(" ")[0])
                        .lastname(student.getName().contains(" ") ?
                                student.getName().substring(student.getName().indexOf(" ") + 1) : "")
                        .email(student.getEmail())
                        .password(student.getPassword())
                        .role("STUDENT")
                        .departmentId(student.getDepartmentId())
                        .build();

                registerStudent(registerRequest, student.getStudentId());

                // Now try to get the user again
                Optional<User> newUser = userRepository.findByStudentId(student.getStudentId());
                if (newUser.isPresent()) {
                    return generateAuthResponse(newUser.get());
                }
            }
        }

        throw new UsernameNotFoundException("User, Teacher, or Student not found with email: " + request.getEmail());
    }

    private AuthenticationResponse generateAuthResponse(Object userOrTeacher) {
        String token;
        String firstName;
        String email;
        String lastName;
        String role;
        Set<?> departmentId;
        String studentId = null;

        if (userOrTeacher instanceof User user) {
            token = jwtService.generateToken(user);
            firstName = user.getFirstname();
            lastName = user.getLastname();
            email = user.getEmail();
            role = user.getRole().name();
            departmentId = user.getDepartmentId();
            studentId = user.getStudentId(); // Add studentId if available
        } else if (userOrTeacher instanceof Teacher teacher) {
//            token = jwtService.generateToken(teacher);
            firstName = teacher.getFirstname();
            lastName = teacher.getLastname();
            email = teacher.getEmail();
            role = teacher.getRole().name();
            departmentId = teacher.getDepartmentId();
        } else if (userOrTeacher instanceof Student student) {
            // This assumes Student implements UserDetails or you have a way to generate a token
            token = jwtService.generateToken(convertStudentToUserDetails(student));
            firstName = student.getName().split(" ")[0];
            lastName = student.getName().contains(" ") ?
                    student.getName().substring(student.getName().indexOf(" ") + 1) : "";
            email = student.getEmail();
            role = "STUDENT";
            departmentId = Set.of(student.getDepartmentId());
            studentId = student.getStudentId();
        } else {
            throw new IllegalArgumentException("Invalid user type");
        }

        return AuthenticationResponse.builder()
//                .token(token)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .role(role)
                .departmentId((Set<String>) departmentId)
                .studentId(studentId) // Include studentId in response if available
                .build();
    }

    // Helper method to convert Student to UserDetails
    private UserDetails convertStudentToUserDetails(Student student) {
        // Implementation depends on your Student model
        return User.builder()
                .firstname(student.getName().split(" ")[0])
                .lastname(student.getName().contains(" ") ?
                        student.getName().substring(student.getName().indexOf(" ") + 1) : "")
                .email(student.getEmail())
                .password(student.getPassword())
                .role(Role.STUDENT)
                .build();
    }
}