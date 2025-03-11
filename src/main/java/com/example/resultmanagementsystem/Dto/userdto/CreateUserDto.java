//package com.example.resultmanagementsystem.Dto.userdto;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.mongodb.core.index.Indexed;
//import org.springframework.data.mongodb.core.mapping.Document;
//
//import java.time.LocalDateTime;
//import java.util.Set;
//
//@Data
//@Slf4j
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Document("user-created")
//
//
//public class CreateUserDto {
//
//    @org.springframework.data.annotation.Id
//    private String id;
//    @Indexed
//    private String userName;
//
//    private String telephone;
//
//    private String password;
//    @Indexed
//    private String email;
//
//    private LocalDateTime dateTime;
//
//    private Set<Role> roles;
//}