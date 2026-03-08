package com.example.resultmanagementsystem.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${app.base-url:http://localhost:15002}")
    private String baseUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("School Management System API")
                        .version("2.0.0")
                        .description("""
                                Comprehensive School Management System (SMS) and Learning Management System (LMS) API.

                                ## Modules
                                - **Authentication** - JWT-based auth with role-based access control
                                - **Academic** - Students, Teachers, Courses, Departments, Results, Gradebook
                                - **Admissions** - Application, Review, Enrollment workflows
                                - **Attendance** - Daily tracking, bulk marking, summaries
                                - **Examinations** - Exam creation, question banks, scheduling, results
                                - **Assignments** - Creation, submission, grading
                                - **Report Cards & Transcripts** - Generation, publishing, verification
                                - **Learning Content** - Modules, lessons, progress tracking
                                - **Discussion Forums** - Course discussions, threaded posts
                                - **Learning Paths** - Structured learning sequences
                                - **Fee Management** - Billing, payments, mobile money integration
                                - **Complaints** - Submission, assignment, resolution tracking
                                - **Notifications** - Email, push, SMS notifications
                                - **Timetable** - Schedule management, substitute teachers
                                - **Calendar** - Events and scheduling
                                - **Parent Portal** - Multi-child dashboard, communication
                                - **HR & Staff** - Employee management, leave tracking
                                - **Payroll** - Salary structures, payslip generation
                                - **Library** - Books, issues, reservations, fines
                                - **Transport** - Routes, vehicles, student assignments
                                - **Hostel** - Rooms, allocations, maintenance
                                - **Inventory** - Assets, stock management, transactions
                                - **Class Management** - Sections, promotions, course assignment
                                - **Analytics** - Performance trends, dashboards, reports
                                - **Audit** - Activity logging, compliance tracking
                                - **Communication** - Announcements, messaging, conversations
                                """)
                        .contact(new Contact()
                                .name("School Management System")
                                .email("admin@schoolms.com"))
                        .license(new License()
                                .name("Proprietary")
                                .url("https://schoolms.com/license")))
                .servers(List.of(
                        new Server().url(baseUrl).description("Current Server")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .bearerFormat("JWT")
                                        .scheme("bearer")
                                        .description("Enter your JWT token")));
    }
}
