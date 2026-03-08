# School Management System - Complete API Documentation

## Table of Contents

1. [Authentication](#1-authentication)
2. [Teacher Authentication](#2-teacher-authentication)
3. [Dashboard](#3-dashboard)
4. [Students](#4-students)
5. [Teachers](#5-teachers)
6. [Departments](#6-departments)
7. [Courses](#7-courses)
8. [Results](#8-results)
9. [Assignments](#9-assignments)
10. [Submissions](#10-submissions)
11. [Fees](#11-fees)
12. [Fee Categories](#12-fee-categories)
13. [Complaints](#13-complaints)
14. [Notifications](#14-notifications)
15. [Timetables](#15-timetables)
16. [Substitute Requests](#16-substitute-requests)
17. [Calendar](#17-calendar)
18. [Admissions](#18-admissions)
19. [Enrollments](#19-enrollments)
20. [Attendance](#20-attendance)
21. [Parent/Guardian](#21-parentguardian)
22. [Exams](#22-exams)
23. [Report Cards](#23-report-cards)
24. [Gradebook](#24-gradebook)
25. [Content Management](#25-content-management)
26. [Discussions](#26-discussions)
27. [Learning Paths](#27-learning-paths)
28. [Staff](#28-staff)
29. [Leave Management](#29-leave-management)
30. [Payroll](#30-payroll)
31. [Library](#31-library)
32. [Transport](#32-transport)
33. [Hostel](#33-hostel)
34. [Inventory](#34-inventory)
35. [Analytics](#35-analytics)
36. [Audit Logging](#36-audit-logging)
37. [Announcements](#37-announcements)
38. [Messaging](#38-messaging)
39. [Class and Section Management](#39-class-and-section-management)

---

## 1. Authentication

**Base URL:** /api/v1/auth

### 1.1 Register User
- **Method:** POST
- **URL:** /api/v1/auth/register
- **Description:** Registers a new user account.
- **Request Body:** RegisterRequest (firstname, lastname, email, password, role)
- **Response:** 200 OK - AuthenticationResponse (token)

### 1.2 Authenticate (Login)
- **Method:** POST
- **URL:** /api/v1/auth/authenticate
- **Description:** Authenticates a user and returns a JWT token.
- **Request Body:** AuthenticationRequest (email, password)
- **Response:** 200 OK - AuthenticationResponse (token)

### 1.3 Verify Email (Deprecated)
- **Method:** GET
- **URL:** /api/v1/auth/verify-email
- **Description:** Verifies user email with a token. Deprecated.
- **Query Parameters:** token (String, required)
- **Response:** 200 OK - Confirmation string

---

## 2. Teacher Authentication

**Base URL:** /api/teachers/auth

### 2.1 Register Teacher
- **Method:** POST
- **URL:** /api/teachers/auth/register
- **Description:** Registers a new teacher account.
- **Request Body:** Teacher (name, email, password, departmentId)
- **Response:** 200 OK - Teacher object

### 2.2 Teacher Login
- **Method:** POST
- **URL:** /api/teachers/auth/login
- **Description:** Authenticates a teacher and returns a token.
- **Query Parameters:** email (String, required), password (String, required)
- **Response:** 200 OK - Authentication response

### 2.3 Verify Teacher Email
- **Method:** GET
- **URL:** /api/teachers/auth/verify
- **Description:** Verifies teacher email using a token.
- **Query Parameters:** token (String, required)
- **Response:** 200 OK - Confirmation string

---

## 3. Dashboard

**Base URL:** /api/dashboard

### 3.1 Get Dashboard Summary
- **Method:** GET
- **URL:** /api/dashboard/summary
- **Description:** Retrieves the dashboard summary including timetable information.
- **Response:** 200 OK - TimetableDashboard object

---

## 4. Students

**Base URL:** /api/students

### 4.1 Create Student
- **Method:** POST
- **URL:** /api/students/createStudent
- **Description:** Creates a new student record.
- **Request Body:** Student (name, email, departmentId, registrationNumber)
- **Response:** 201 Created - Student object

### 4.2 Get Student by ID
- **Method:** GET
- **URL:** /api/students/{studentId}
- **Description:** Retrieves a student by their unique identifier.
- **Path Parameters:** studentId (String, required)
- **Response:** 200 OK - Student object

### 4.3 Get Students by Department
- **Method:** GET
- **URL:** /api/students/department/{departmentId}
- **Description:** Retrieves all students in a specific department.
- **Path Parameters:** departmentId (String, required)
- **Response:** 200 OK - List of Student objects

### 4.4 Get All Students
- **Method:** GET
- **URL:** /api/students
- **Description:** Retrieves all students in the system.
- **Response:** 200 OK - List of Student objects

### 4.5 Delete Student
- **Method:** DELETE
- **URL:** /api/students/{studentId}
- **Description:** Deletes a student record.
- **Path Parameters:** studentId (String, required)
- **Response:** 200 OK

### 4.6 Get Student Name
- **Method:** GET
- **URL:** /api/students/{studentId}/name
- **Description:** Retrieves only the name of a student.
- **Path Parameters:** studentId (String, required)
- **Response:** 200 OK - String (student name)

### 4.7 Get Student Email
- **Method:** GET
- **URL:** /api/students/{studentId}/email
- **Description:** Retrieves only the email of a student.
- **Path Parameters:** studentId (String, required)
- **Response:** 200 OK - String (student email)

### 4.8 Get Student by Email
- **Method:** GET
- **URL:** /api/students/email
- **Description:** Retrieves a student by their email address.
- **Query Parameters:** email (String, required)
- **Response:** 200 OK - Student object

---

## 5. Teachers

**Base URL:** /api/teachers

### 5.1 Get Teacher by Email
- **Method:** GET
- **URL:** /api/teachers/getTeacherByemail/{email}
- **Description:** Retrieves a teacher by their email address.
- **Path Parameters:** email (String, required)
- **Response:** 200 OK - Teacher object

### 5.2 Create Teacher
- **Method:** POST
- **URL:** /api/teachers/createTeacher
- **Description:** Creates a new teacher record.
- **Request Body:** Teacher (name, email, departmentId)
- **Response:** 201 Created - Teacher object

### 5.3 Get Teacher by ID
- **Method:** GET
- **URL:** /api/teachers/getTeacherById/{id}
- **Description:** Retrieves a teacher by their unique identifier.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - Teacher object

### 5.4 Get All Teachers
- **Method:** GET
- **URL:** /api/teachers/getAllTeachers
- **Description:** Retrieves all teachers in the system.
- **Response:** 200 OK - List of Teacher objects

### 5.5 Update Teacher
- **Method:** PUT
- **URL:** /api/teachers/updateTeacher/{id}
- **Description:** Updates an existing teacher record.
- **Path Parameters:** id (String, required)
- **Request Body:** TeacherDTO (name, email, departmentId)
- **Response:** 200 OK - Teacher object

### 5.6 Delete Teacher
- **Method:** DELETE
- **URL:** /api/teachers/deleteTeacher/{id}
- **Description:** Deletes a teacher record.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK

---

## 6. Departments

**Base URL:** /api/departments

### 6.1 Create Department
- **Method:** POST
- **URL:** /api/departments/create
- **Description:** Creates a new department.
- **Request Body:** Department (name, description)
- **Response:** 201 Created - Department object

### 6.2 Get All Departments
- **Method:** GET
- **URL:** /api/departments/all
- **Description:** Retrieves all departments.
- **Response:** 200 OK - List of Department objects

### 6.3 Update Department
- **Method:** PUT
- **URL:** /api/departments/update/{id}
- **Description:** Updates an existing department.
- **Path Parameters:** id (String, required)
- **Request Body:** Department object
- **Response:** 200 OK - Department object

### 6.4 Delete Department
- **Method:** DELETE
- **URL:** /api/departments/delete/{id}
- **Description:** Deletes a department.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK

---

## 7. Courses

**Base URL:** /api/courses

### 7.1 Get All Courses
- **Method:** GET
- **URL:** /api/courses
- **Description:** Retrieves all courses.
- **Response:** 200 OK - List of Course objects

### 7.2 Get Course by ID
- **Method:** GET
- **URL:** /api/courses/{id}
- **Description:** Retrieves a course by its unique identifier.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - Course object

### 7.3 Get Courses by Department
- **Method:** GET
- **URL:** /api/courses/getAllCoursesBydepartmentId/{departmentId}
- **Description:** Retrieves all courses for a specific department.
- **Path Parameters:** departmentId (String, required)
- **Response:** 200 OK - List of Course objects

### 7.4 Create Course
- **Method:** POST
- **URL:** /api/courses/createCourse
- **Description:** Creates a new course.
- **Request Body:** Course (courseName, courseCode, departmentId, creditHours)
- **Response:** 201 Created - Course object

### 7.5 Update Course
- **Method:** PUT
- **URL:** /api/courses/{id}
- **Description:** Updates an existing course.
- **Path Parameters:** id (String, required)
- **Request Body:** Course object
- **Response:** 200 OK - Course object

### 7.6 Delete Course
- **Method:** DELETE
- **URL:** /api/courses/{id}
- **Description:** Deletes a course.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK

---

## 8. Results

**Base URL:** /api/results

### 8.1 Get Results by Email
- **Method:** GET
- **URL:** /api/results/student/email
- **Description:** Retrieves results for a student by email, semester, and academic year.
- **Query Parameters:** email (String, required), semester (String, required), year (String, required)
- **Response:** 200 OK - List of Result objects

### 8.2 Create Result
- **Method:** POST
- **URL:** /api/results
- **Description:** Creates a new result record.
- **Request Body:** ResultDTO (studentId, courseId, ca, exams, semester, academicYear)
- **Response:** 201 Created - Result object

### 8.3 Get Result by ID
- **Method:** GET
- **URL:** /api/results/getResultById{id}
- **Description:** Retrieves a result by its unique identifier.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - Result object

### 8.4 Get Results by Student
- **Method:** GET
- **URL:** /api/results/student/getResultByStudentId{studentId}
- **Description:** Retrieves all results for a specific student.
- **Path Parameters:** studentId (String, required)
- **Response:** 200 OK - List of Result objects

### 8.5 Get Results by Course
- **Method:** GET
- **URL:** /api/results/course/getResultByCourseId{courseId}
- **Description:** Retrieves all results for a specific course.
- **Path Parameters:** courseId (String, required)
- **Response:** 200 OK - List of Result objects

### 8.6 Calculate GPA
- **Method:** GET
- **URL:** /api/results/student/{studentId}/gpa
- **Description:** Calculates the GPA for a student in a specific semester and year.
- **Path Parameters:** studentId (String, required)
- **Query Parameters:** semester (String, required), year (String, required)
- **Response:** 200 OK - GPA value (number)

### 8.7 Update Result
- **Method:** PUT
- **URL:** /api/results/{id}
- **Description:** Updates an existing result record.
- **Path Parameters:** id (String, required)
- **Request Body:** Object with ca (number) and exams (number)
- **Response:** 200 OK - Result object

### 8.8 Delete Result
- **Method:** DELETE
- **URL:** /api/results/{id}
- **Description:** Deletes a result record.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK

---

## 9. Assignments

**Base URL:** /api/assignments

### 9.1 Create Assignment
- **Method:** POST
- **URL:** /api/assignments/teacher/{teacherId}
- **Description:** Creates a new assignment for a teacher.
- **Path Parameters:** teacherId (String, required)
- **Request Body:** Assignment (title, description, courseId, dueDate, totalMarks)
- **Response:** 201 Created - Assignment object

### 9.2 Get Assignments for Student
- **Method:** GET
- **URL:** /api/assignments/student/{studentId}
- **Description:** Retrieves all assignments for a specific student.
- **Path Parameters:** studentId (String, required)
- **Response:** 200 OK - List of Assignment objects

### 9.3 Get Assignments for Teacher
- **Method:** GET
- **URL:** /api/assignments/teacher/{teacherId}
- **Description:** Retrieves all assignments created by a specific teacher.
- **Path Parameters:** teacherId (String, required)
- **Response:** 200 OK - List of Assignment objects

### 9.4 Get Assignment by ID
- **Method:** GET
- **URL:** /api/assignments/{assignmentId}
- **Description:** Retrieves an assignment by its unique identifier.
- **Path Parameters:** assignmentId (String, required)
- **Response:** 200 OK - Assignment object

### 9.5 Update Assignment
- **Method:** PUT
- **URL:** /api/assignments/{assignmentId}/teacher/{teacherId}
- **Description:** Updates an existing assignment.
- **Path Parameters:** assignmentId (String, required), teacherId (String, required)
- **Request Body:** Assignment object
- **Response:** 200 OK - Assignment object

### 9.6 Delete Assignment
- **Method:** DELETE
- **URL:** /api/assignments/{assignmentId}/teacher/{teacherId}
- **Description:** Deletes an assignment.
- **Path Parameters:** assignmentId (String, required), teacherId (String, required)
- **Response:** 200 OK

---

## 10. Submissions

**Base URL:** /api/submissions

### 10.1 Submit Assignment
- **Method:** POST
- **URL:** /api/submissions/student/{studentId}
- **Description:** Submits an assignment for a student.
- **Path Parameters:** studentId (String, required)
- **Request Body:** Submission (assignmentId, content, attachments)
- **Response:** 201 Created - Submission object

### 10.2 Get Department Submissions
- **Method:** GET
- **URL:** /api/submissions/department/teacher/{teacherId}
- **Description:** Retrieves all submissions in a teacher's department.
- **Path Parameters:** teacherId (String, required)
- **Response:** 200 OK - List of Submission objects

### 10.3 Get Ungraded Submissions
- **Method:** GET
- **URL:** /api/submissions/department/teacher/{teacherId}/ungraded
- **Description:** Retrieves ungraded submissions in a teacher's department.
- **Path Parameters:** teacherId (String, required)
- **Response:** 200 OK - List of Submission objects

### 10.4 Get Graded Submissions
- **Method:** GET
- **URL:** /api/submissions/department/teacher/{teacherId}/graded
- **Description:** Retrieves graded submissions in a teacher's department.
- **Path Parameters:** teacherId (String, required)
- **Response:** 200 OK - List of Submission objects

### 10.5 Get Submissions for Assignment
- **Method:** GET
- **URL:** /api/submissions/assignment/{assignmentId}/teacher/{teacherId}
- **Description:** Retrieves all submissions for a specific assignment.
- **Path Parameters:** assignmentId (String, required), teacherId (String, required)
- **Response:** 200 OK - List of Submission objects

### 10.6 Get Student Submissions
- **Method:** GET
- **URL:** /api/submissions/student/{studentId}
- **Description:** Retrieves all submissions by a specific student.
- **Path Parameters:** studentId (String, required)
- **Response:** 200 OK - List of Submission objects

### 10.7 Grade Submission
- **Method:** PUT
- **URL:** /api/submissions/{submissionId}/grade/teacher/{teacherId}
- **Description:** Grades a student submission.
- **Path Parameters:** submissionId (String, required), teacherId (String, required)
- **Query Parameters:** grade (Double, required), feedback (String, optional)
- **Response:** 200 OK - Submission object

### 10.8 Batch Grade Submissions
- **Method:** PUT
- **URL:** /api/submissions/batch-grade/teacher/{teacherId}
- **Description:** Grades multiple submissions at once.
- **Path Parameters:** teacherId (String, required)
- **Request Body:** List of grading objects
- **Response:** 200 OK - List of Submission objects

### 10.9 Get Specific Submission
- **Method:** GET
- **URL:** /api/submissions/assignment/{assignmentId}/student/{studentId}
- **Description:** Retrieves a specific student's submission for an assignment.
- **Path Parameters:** assignmentId (String, required), studentId (String, required)
- **Response:** 200 OK - Submission object

---

## 11. Fees

**Base URL:** /api/fees

### 11.1 Get Fee Balance
- **Method:** GET
- **URL:** /api/fees/balance
- **Description:** Gets the fee balance for a student.
- **Query Parameters:** studentId (String, required), academicYear (String, required), semester (String, required)
- **Response:** 200 OK - Fee balance details

### 11.2 Get Payment History
- **Method:** GET
- **URL:** /api/fees/payment-history
- **Description:** Gets payment history for a student.
- **Query Parameters:** studentId (String, required), academicYear (String, required)
- **Response:** 200 OK - List of payment records

### 11.3 Create Fee
- **Method:** POST
- **URL:** /api/fees/create
- **Description:** Creates a new fee record.
- **Request Body:** CreateFeeRequest (studentId, feeType, amount, academicYear, semester, dueDate)
- **Response:** 201 Created - Fee object

### 11.4 Generate Fee Record
- **Method:** POST
- **URL:** /api/fees/generate
- **Description:** Generates a fee record for a student.
- **Query Parameters:** studentId (String, required), academicYear (String, required), semester (String, required)
- **Response:** 201 Created - Fee object

### 11.5 Get All Student Fees
- **Method:** GET
- **URL:** /api/fees/student/{studentId}
- **Description:** Gets all fee records for a student.
- **Path Parameters:** studentId (String, required)
- **Response:** 200 OK - List of Fee objects

### 11.6 Get Specific Fee Record
- **Method:** GET
- **URL:** /api/fees/record/student/{studentId}/year/{academicYear}/semester/{semester}
- **Description:** Gets a specific fee record for a student by year and semester.
- **Path Parameters:** studentId (String, required), academicYear (String, required), semester (String, required)
- **Response:** 200 OK - Fee object

### 11.7 Get Fee Breakdown
- **Method:** GET
- **URL:** /api/fees/breakdown/student/{studentId}/year/{academicYear}
- **Description:** Gets the fee breakdown for a student in an academic year.
- **Path Parameters:** studentId (String, required), academicYear (String, required)
- **Response:** 200 OK - Fee breakdown details

### 11.8 Process Payment
- **Method:** POST
- **URL:** /api/fees/payment
- **Description:** Processes a fee payment.
- **Request Body:** PaymentRequest (feeId, amount, paymentMethod, transactionReference)
- **Response:** 201 Created - Payment object

### 11.9 Confirm Payment
- **Method:** POST
- **URL:** /api/fees/payment/{paymentId}/confirm
- **Description:** Confirms a pending payment.
- **Path Parameters:** paymentId (String, required)
- **Request Body:** ConfirmPaymentRequest (transactionId, status)
- **Response:** 200 OK - Payment object

### 11.10 Get Student Payment History
- **Method:** GET
- **URL:** /api/fees/payments/student/{studentId}
- **Description:** Gets all payments made by a student.
- **Path Parameters:** studentId (String, required)
- **Response:** 200 OK - List of Payment objects

### 11.11 Get Fee Payments
- **Method:** GET
- **URL:** /api/fees/payments/fee/{feeId}
- **Description:** Gets all payments for a specific fee record.
- **Path Parameters:** feeId (String, required)
- **Response:** 200 OK - List of Payment objects

### 11.12 Check Outstanding Fees
- **Method:** GET
- **URL:** /api/fees/status/student/{studentId}/outstanding
- **Description:** Checks if a student has outstanding fees.
- **Path Parameters:** studentId (String, required)
- **Response:** 200 OK - Boolean

### 11.13 Get Fee Summary
- **Method:** GET
- **URL:** /api/fees/summary/student/{studentId}
- **Description:** Gets a comprehensive fee summary for a student.
- **Path Parameters:** studentId (String, required)
- **Response:** 200 OK - Fee summary object

### 11.14 Check Mandatory Fees Paid
- **Method:** GET
- **URL:** /api/fees/status/student/{studentId}/year/{academicYear}/mandatory-paid
- **Description:** Checks if all mandatory fees are paid for a student in an academic year.
- **Path Parameters:** studentId (String, required), academicYear (String, required)
- **Response:** 200 OK - Boolean

---

## 12. Fee Categories

**Base URL:** /api/fee-categories

### 12.1 Create Fee Category
- **Method:** POST
- **URL:** /api/fee-categories
- **Description:** Creates a new fee category.
- **Request Body:** FeeCategory (name, description, amount, academicYear, departmentId, mandatory)
- **Response:** 201 Created - FeeCategory object

### 12.2 Get All Fee Categories
- **Method:** GET
- **URL:** /api/fee-categories
- **Description:** Retrieves all fee categories with pagination.
- **Query Parameters:** page (int, default 0), size (int, default 10), sortBy (String, default "name"), sortDir (String, default "asc")
- **Response:** 200 OK - Page of FeeCategory objects

### 12.3 Get Fee Category by ID
- **Method:** GET
- **URL:** /api/fee-categories/{categoryId}
- **Description:** Retrieves a fee category by ID.
- **Path Parameters:** categoryId (String, required)
- **Response:** 200 OK - FeeCategory object

### 12.4 Update Fee Category
- **Method:** PUT
- **URL:** /api/fee-categories/{categoryId}
- **Description:** Updates an existing fee category.
- **Path Parameters:** categoryId (String, required)
- **Request Body:** FeeCategory object
- **Response:** 200 OK - FeeCategory object

### 12.5 Delete Fee Category
- **Method:** DELETE
- **URL:** /api/fee-categories/{categoryId}
- **Description:** Deletes a fee category.
- **Path Parameters:** categoryId (String, required)
- **Response:** 200 OK

### 12.6 Get Fee Categories by Year
- **Method:** GET
- **URL:** /api/fee-categories/year/{academicYear}
- **Description:** Gets all fee categories for a specific academic year.
- **Path Parameters:** academicYear (String, required)
- **Response:** 200 OK - List of FeeCategory objects

### 12.7 Get Fee Categories by Department and Year
- **Method:** GET
- **URL:** /api/fee-categories/department/{departmentId}/year/{academicYear}
- **Description:** Gets fee categories for a department in an academic year.
- **Path Parameters:** departmentId (String, required), academicYear (String, required)
- **Response:** 200 OK - List of FeeCategory objects

### 12.8 Get Mandatory Fee Categories
- **Method:** GET
- **URL:** /api/fee-categories/mandatory/year/{academicYear}
- **Description:** Gets all mandatory fee categories for an academic year.
- **Path Parameters:** academicYear (String, required)
- **Response:** 200 OK - List of FeeCategory objects

### 12.9 Get Optional Fee Categories
- **Method:** GET
- **URL:** /api/fee-categories/optional/year/{academicYear}
- **Description:** Gets all optional fee categories for an academic year.
- **Path Parameters:** academicYear (String, required)
- **Response:** 200 OK - List of FeeCategory objects

### 12.10 Calculate Mandatory Total
- **Method:** GET
- **URL:** /api/fee-categories/mandatory-total/department/{departmentId}/year/{academicYear}
- **Description:** Calculates total mandatory fees for a department in an academic year.
- **Path Parameters:** departmentId (String, required), academicYear (String, required)
- **Response:** 200 OK - Total amount (number)

### 12.11 Calculate Optional Total
- **Method:** GET
- **URL:** /api/fee-categories/optional-total/department/{departmentId}/year/{academicYear}
- **Description:** Calculates total optional fees for a department in an academic year.
- **Path Parameters:** departmentId (String, required), academicYear (String, required)
- **Response:** 200 OK - Total amount (number)

### 12.12 Get Breakdown Summary
- **Method:** GET
- **URL:** /api/fee-categories/summary/department/{departmentId}/year/{academicYear}
- **Description:** Gets fee breakdown summary for a department in an academic year.
- **Path Parameters:** departmentId (String, required), academicYear (String, required)
- **Response:** 200 OK - Breakdown summary object

### 12.13 Create Bulk Fee Categories
- **Method:** POST
- **URL:** /api/fee-categories/bulk
- **Description:** Creates multiple fee categories at once.
- **Request Body:** List of FeeCategory objects
- **Response:** 201 Created - List of FeeCategory objects

### 12.14 Bulk Update Amounts
- **Method:** PUT
- **URL:** /api/fee-categories/bulk-update-amounts/year/{academicYear}
- **Description:** Updates amounts for all fee categories in an academic year.
- **Path Parameters:** academicYear (String, required)
- **Request Body:** Map of category updates
- **Response:** 200 OK - List of FeeCategory objects

### 12.15 Setup Fee Structure
- **Method:** POST
- **URL:** /api/fee-categories/setup-fee-structure
- **Description:** Sets up the fee structure for an academic year.
- **Query Parameters:** academicYear (String, required)
- **Response:** 201 Created - List of FeeCategory objects

### 12.16 Clone Fee Structure
- **Method:** POST
- **URL:** /api/fee-categories/clone-structure
- **Description:** Clones fee structure from one year to another with optional inflation rate.
- **Query Parameters:** fromYear (String, required), toYear (String, required), inflationRate (Double, optional)
- **Response:** 201 Created - List of FeeCategory objects

### 12.17 Archive Fee Categories
- **Method:** PUT
- **URL:** /api/fee-categories/archive/year/{academicYear}
- **Description:** Archives all fee categories for an academic year.
- **Path Parameters:** academicYear (String, required)
- **Response:** 200 OK

### 12.18 Search Fee Categories
- **Method:** GET
- **URL:** /api/fee-categories/search
- **Description:** Searches fee categories by query string.
- **Query Parameters:** query (String, required), year (String, optional)
- **Response:** 200 OK - List of FeeCategory objects

### 12.19 Filter Fee Categories
- **Method:** POST
- **URL:** /api/fee-categories/filter
- **Description:** Filters fee categories using multiple criteria.
- **Request Body:** Map of filter criteria
- **Response:** 200 OK - List of FeeCategory objects

---

## 13. Complaints

**Base URL:** /api/complaints

### 13.1 Create Complaint
- **Method:** POST
- **URL:** /api/complaints
- **Description:** Creates a new complaint.
- **Request Body:** ComplaintDTO (studentId, courseId, type, description)
- **Response:** 201 Created - Complaint object

### 13.2 Get Complaint by ID
- **Method:** GET
- **URL:** /api/complaints/{id}
- **Description:** Retrieves a complaint by its ID.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - Complaint object

### 13.3 Get Detailed Complaint
- **Method:** GET
- **URL:** /api/complaints/{id}/details
- **Description:** Retrieves detailed complaint information.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - Detailed complaint object

### 13.4 Get Complaints by Student
- **Method:** GET
- **URL:** /api/complaints/student/{studentId}
- **Description:** Retrieves all complaints filed by a student.
- **Path Parameters:** studentId (String, required)
- **Response:** 200 OK - List of Complaint objects

### 13.5 Get Complaints by Course
- **Method:** GET
- **URL:** /api/complaints/course/{courseId}
- **Description:** Retrieves all complaints for a course.
- **Path Parameters:** courseId (String, required)
- **Response:** 200 OK - List of Complaint objects

### 13.6 Get Complaints by Status
- **Method:** GET
- **URL:** /api/complaints/status/{status}
- **Description:** Retrieves complaints filtered by status with pagination.
- **Path Parameters:** status (String, required)
- **Query Parameters:** page (int, default 0), size (int, default 10)
- **Response:** 200 OK - Page of Complaint objects

### 13.7 Get Complaints by Teacher
- **Method:** GET
- **URL:** /api/complaints/teacher/{teacherId}
- **Description:** Retrieves all complaints assigned to a teacher.
- **Path Parameters:** teacherId (String, required)
- **Response:** 200 OK - List of Complaint objects

### 13.8 Get Complaints by Teacher (Paginated)
- **Method:** GET
- **URL:** /api/complaints/teacher/{teacherId}/paginated
- **Description:** Retrieves complaints assigned to a teacher with pagination.
- **Path Parameters:** teacherId (String, required)
- **Query Parameters:** page (int, default 0), size (int, default 10)
- **Response:** 200 OK - Page of Complaint objects

### 13.9 Get Unresolved Complaints by Teacher
- **Method:** GET
- **URL:** /api/complaints/teacher/{teacherId}/unresolved
- **Description:** Retrieves unresolved complaints assigned to a teacher.
- **Path Parameters:** teacherId (String, required)
- **Response:** 200 OK - List of Complaint objects

### 13.10 Update Complaint Status
- **Method:** PUT
- **URL:** /api/complaints/{id}/status
- **Description:** Updates the status of a complaint.
- **Path Parameters:** id (String, required)
- **Query Parameters:** status (String, required), response (String, optional)
- **Response:** 200 OK - Complaint object

### 13.11 Assign Complaint
- **Method:** PUT
- **URL:** /api/complaints/{id}/assign
- **Description:** Assigns a complaint to a teacher or staff member.
- **Path Parameters:** id (String, required)
- **Request Body:** ComplaintAssignmentDTO (assignedTo, assignedBy)
- **Response:** 200 OK - Complaint object

### 13.12 Delete Complaint
- **Method:** DELETE
- **URL:** /api/complaints/{id}
- **Description:** Deletes a complaint.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK

### 13.13 Get Complaint Dashboard
- **Method:** GET
- **URL:** /api/complaints/dashboard
- **Description:** Retrieves complaint dashboard statistics.
- **Response:** 200 OK - Dashboard statistics object

---

## 14. Notifications

**Base URL:** /api/notifications

### 14.1 Get Teacher Notifications
- **Method:** GET
- **URL:** /api/notifications/teacher
- **Description:** Retrieves notifications for a specific teacher.
- **Query Parameters:** teacherId (String, required)
- **Response:** 200 OK - List of Notification objects

### 14.2 Get Student Notifications
- **Method:** GET
- **URL:** /api/notifications/student/{studentId}
- **Description:** Retrieves notifications for a specific student.
- **Path Parameters:** studentId (String, required)
- **Response:** 200 OK - List of Notification objects

### 14.3 Create Exam Notification
- **Method:** POST
- **URL:** /api/notifications/exam
- **Description:** Creates an exam notification.
- **Query Parameters:** courseId (String, required), examTitle (String, required), examDate (String, required), venue (String, required)
- **Response:** 201 Created - Notification object

### 14.4 Get All Notifications
- **Method:** GET
- **URL:** /api/notifications
- **Description:** Retrieves all notifications.
- **Response:** 200 OK - List of Notification objects

### 14.5 Get Notification by ID
- **Method:** GET
- **URL:** /api/notifications/{id}
- **Description:** Retrieves a notification by its ID.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - Notification object

### 14.6 Get Notifications by Recipient
- **Method:** GET
- **URL:** /api/notifications/recipient/{recipientId}
- **Description:** Retrieves all notifications for a recipient.
- **Path Parameters:** recipientId (String, required)
- **Response:** 200 OK - List of Notification objects

### 14.7 Get Unread Notifications
- **Method:** GET
- **URL:** /api/notifications/recipient/{recipientId}/unread
- **Description:** Retrieves unread notifications for a recipient.
- **Path Parameters:** recipientId (String, required)
- **Response:** 200 OK - List of Notification objects

### 14.8 Create Notification
- **Method:** POST
- **URL:** /api/notifications
- **Description:** Creates a new notification.
- **Request Body:** Notification (recipientId, type, title, message, priority)
- **Response:** 201 Created - Notification object

### 14.9 Mark Notification as Read
- **Method:** PATCH
- **URL:** /api/notifications/{id}/read
- **Description:** Marks a notification as read.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - Notification object

### 14.10 Create Schedule Change Notification
- **Method:** POST
- **URL:** /api/notifications/schedule-change
- **Description:** Creates a notification for a schedule change.
- **Request Body:** Schedule change details
- **Response:** 201 Created - Notification object

### 14.11 Create Substitution Notification
- **Method:** POST
- **URL:** /api/notifications/substitution
- **Description:** Creates a notification for a teacher substitution.
- **Request Body:** Substitution details
- **Response:** 201 Created - Notification object

### 14.12 Trigger Daily Reminders
- **Method:** POST
- **URL:** /api/notifications/daily-reminders/trigger
- **Description:** Triggers sending of daily reminder notifications.
- **Response:** 200 OK

### 14.13 Process Pending Notifications
- **Method:** POST
- **URL:** /api/notifications/pending/process
- **Description:** Processes all pending notifications.
- **Response:** 200 OK

### 14.14 Resend Notification
- **Method:** POST
- **URL:** /api/notifications/{id}/resend
- **Description:** Resends a notification.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - Notification object

### 14.15 Get Unread Count
- **Method:** GET
- **URL:** /api/notifications/recipient/{recipientId}/unread/count
- **Description:** Gets the count of unread notifications for a recipient.
- **Path Parameters:** recipientId (String, required)
- **Response:** 200 OK - Count (number)

### 14.16 Mark All as Read
- **Method:** PATCH
- **URL:** /api/notifications/recipient/{recipientId}/read-all
- **Description:** Marks all notifications as read for a recipient.
- **Path Parameters:** recipientId (String, required)
- **Response:** 200 OK

### 14.17 Get Notifications by Type
- **Method:** GET
- **URL:** /api/notifications/type/{type}
- **Description:** Retrieves notifications filtered by type (placeholder).
- **Path Parameters:** type (String, required)
- **Response:** 200 OK - List of Notification objects

### 14.18 Get Notifications by Priority
- **Method:** GET
- **URL:** /api/notifications/priority/{priority}
- **Description:** Retrieves notifications filtered by priority (placeholder).
- **Path Parameters:** priority (String, required)
- **Response:** 200 OK - List of Notification objects

---

## 15. Timetables

**Base URL:** /api/timetables

### 15.1 Get Substituted Timetables
- **Method:** GET
- **URL:** /api/timetables/substituted
- **Description:** Retrieves all timetable entries with substitute teachers.
- **Response:** 200 OK - List of Timetable objects

### 15.2 Get by Substitute Teacher
- **Method:** GET
- **URL:** /api/timetables/substitute-teacher/{substituteTeacherId}
- **Description:** Retrieves timetable entries for a specific substitute teacher.
- **Path Parameters:** substituteTeacherId (String, required)
- **Response:** 200 OK - List of Timetable objects

### 15.3 Remove Substitute
- **Method:** PATCH
- **URL:** /api/timetables/{id}/remove-substitute
- **Description:** Removes the substitute teacher from a timetable entry.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - Timetable object

### 15.4 Get All Timetables
- **Method:** GET
- **URL:** /api/timetables
- **Description:** Retrieves all timetable entries.
- **Response:** 200 OK - List of Timetable objects

### 15.5 Get Timetable by ID
- **Method:** GET
- **URL:** /api/timetables/{id}
- **Description:** Retrieves a timetable entry by ID.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - Timetable object

### 15.6 Get by Department
- **Method:** GET
- **URL:** /api/timetables/department/{departmentId}
- **Description:** Retrieves timetable entries for a department.
- **Path Parameters:** departmentId (String, required)
- **Response:** 200 OK - List of Timetable objects

### 15.7 Get by Teacher
- **Method:** GET
- **URL:** /api/timetables/teacher/{teacherId}
- **Description:** Retrieves timetable entries for a teacher.
- **Path Parameters:** teacherId (String, required)
- **Response:** 200 OK - List of Timetable objects

### 15.8 Get by Semester
- **Method:** GET
- **URL:** /api/timetables/semester/{semester}
- **Description:** Retrieves timetable entries for a semester.
- **Path Parameters:** semester (String, required)
- **Response:** 200 OK - List of Timetable objects

### 15.9 Get by Department and Semester
- **Method:** GET
- **URL:** /api/timetables/department/{departmentId}/semester/{semester}
- **Description:** Retrieves timetable entries for a department in a semester.
- **Path Parameters:** departmentId (String, required), semester (String, required)
- **Response:** 200 OK - List of Timetable objects

### 15.10 Get by Day
- **Method:** GET
- **URL:** /api/timetables/day/{dayOfWeek}
- **Description:** Retrieves timetable entries for a specific day.
- **Path Parameters:** dayOfWeek (String, required)
- **Response:** 200 OK - List of Timetable objects

### 15.11 Get by Department and Day
- **Method:** GET
- **URL:** /api/timetables/department/{departmentId}/day/{dayOfWeek}
- **Description:** Retrieves timetable entries for a department on a specific day.
- **Path Parameters:** departmentId (String, required), dayOfWeek (String, required)
- **Response:** 200 OK - List of Timetable objects

### 15.12 Get by Teacher and Day
- **Method:** GET
- **URL:** /api/timetables/teacher/{teacherId}/day/{dayOfWeek}
- **Description:** Retrieves timetable entries for a teacher on a specific day.
- **Path Parameters:** teacherId (String, required), dayOfWeek (String, required)
- **Response:** 200 OK - List of Timetable objects

### 15.13 Get by Academic Year
- **Method:** GET
- **URL:** /api/timetables/academic-year/{academicYear}
- **Description:** Retrieves timetable entries for an academic year.
- **Path Parameters:** academicYear (String, required)
- **Response:** 200 OK - List of Timetable objects

### 15.14 Get by Status
- **Method:** GET
- **URL:** /api/timetables/status/{status}
- **Description:** Retrieves timetable entries filtered by status.
- **Path Parameters:** status (String, required)
- **Response:** 200 OK - List of Timetable objects

### 15.15 Get by Department, Semester and Section
- **Method:** GET
- **URL:** /api/timetables/department/{departmentId}/semester/{semester}/section/{section}
- **Description:** Retrieves timetable entries for a specific class section.
- **Path Parameters:** departmentId (String, required), semester (String, required), section (String, required)
- **Response:** 200 OK - List of Timetable objects

### 15.16 Create Timetable
- **Method:** POST
- **URL:** /api/timetables
- **Description:** Creates a new timetable entry.
- **Request Body:** Timetable (departmentId, courseId, teacherId, dayOfWeek, startTime, endTime, room, semester, section, academicYear)
- **Response:** 201 Created - Timetable object

### 15.17 Update Timetable
- **Method:** PUT
- **URL:** /api/timetables/{id}
- **Description:** Updates an existing timetable entry.
- **Path Parameters:** id (String, required)
- **Request Body:** Timetable object
- **Response:** 200 OK - Timetable object

### 15.18 Delete Timetable
- **Method:** DELETE
- **URL:** /api/timetables/{id}
- **Description:** Deletes a timetable entry.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK

### 15.19 Deactivate Timetable
- **Method:** PATCH
- **URL:** /api/timetables/{id}/deactivate
- **Description:** Deactivates a timetable entry.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - Timetable object

---

## 16. Substitute Requests

**Base URL:** /api/substitute-requests

### 16.1 Get All Substitute Requests
- **Method:** GET
- **URL:** /api/substitute-requests
- **Description:** Retrieves all substitute teacher requests.
- **Response:** 200 OK - List of SubstituteRequest objects

### 16.2 Get Substitute Request by ID
- **Method:** GET
- **URL:** /api/substitute-requests/{id}
- **Description:** Retrieves a substitute request by ID.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - SubstituteRequest object

### 16.3 Get by Teacher
- **Method:** GET
- **URL:** /api/substitute-requests/teacher/{teacherId}
- **Description:** Retrieves substitute requests for a specific teacher.
- **Path Parameters:** teacherId (String, required)
- **Response:** 200 OK - List of SubstituteRequest objects

### 16.4 Get by Status
- **Method:** GET
- **URL:** /api/substitute-requests/status/{status}
- **Description:** Retrieves substitute requests filtered by status.
- **Path Parameters:** status (String, required)
- **Response:** 200 OK - List of SubstituteRequest objects

### 16.5 Create Substitute Request
- **Method:** POST
- **URL:** /api/substitute-requests
- **Description:** Creates a new substitute teacher request.
- **Request Body:** SubstituteRequest (teacherId, timetableId, reason, suggestedSubstituteId)
- **Response:** 201 Created - SubstituteRequest object

### 16.6 Approve Request
- **Method:** PATCH
- **URL:** /api/substitute-requests/{id}/approve
- **Description:** Approves a substitute teacher request.
- **Path Parameters:** id (String, required)
- **Query Parameters:** approvedBy (String, required)
- **Response:** 200 OK - SubstituteRequest object

### 16.7 Reject Request
- **Method:** PATCH
- **URL:** /api/substitute-requests/{id}/reject
- **Description:** Rejects a substitute teacher request.
- **Path Parameters:** id (String, required)
- **Query Parameters:** rejectedBy (String, required), comments (String, optional)
- **Response:** 200 OK - SubstituteRequest object

### 16.8 Complete Substitution
- **Method:** PATCH
- **URL:** /api/substitute-requests/{id}/complete
- **Description:** Marks a substitution as completed.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - SubstituteRequest object

---

## 17. Calendar

**Base URL:** /api/calendar

### 17.1 Create Event
- **Method:** POST
- **URL:** /api/calendar/create_events
- **Description:** Creates a new calendar event.
- **Request Body:** CalendarEvent (title, description, startDate, endDate, departmentId, eventType)
- **Response:** 201 Created - CalendarEvent object

### 17.2 Update Event
- **Method:** PUT
- **URL:** /api/calendar/events/{eventId}
- **Description:** Updates an existing calendar event.
- **Path Parameters:** eventId (String, required)
- **Request Body:** CalendarEvent object
- **Response:** 200 OK - CalendarEvent object

### 17.3 Delete Event
- **Method:** DELETE
- **URL:** /api/calendar/events/{eventId}
- **Description:** Deletes a calendar event.
- **Path Parameters:** eventId (String, required)
- **Response:** 200 OK

### 17.4 Get Events by Department
- **Method:** GET
- **URL:** /api/calendar/events/department/{departmentId}
- **Description:** Retrieves calendar events for a department.
- **Path Parameters:** departmentId (String, required)
- **Response:** 200 OK - List of CalendarEvent objects

### 17.5 Get Events by Date Range
- **Method:** GET
- **URL:** /api/calendar/events
- **Description:** Retrieves calendar events within a date range.
- **Query Parameters:** startDate (String, required), endDate (String, required), departmentId (String, optional)
- **Response:** 200 OK - List of CalendarEvent objects

### 17.6 Get Student Events
- **Method:** GET
- **URL:** /api/calendar/events/student/{studentId}
- **Description:** Retrieves calendar events relevant to a student.
- **Path Parameters:** studentId (String, required)
- **Response:** 200 OK - List of CalendarEvent objects

### 17.7 Send Event Notification
- **Method:** POST
- **URL:** /api/calendar/events/{eventId}/notify
- **Description:** Sends a notification about an event.
- **Path Parameters:** eventId (String, required)
- **Response:** 200 OK

---

## 18. Admissions

**Base URL:** /api/admissions

### 18.1 Submit Application
- **Method:** POST
- **URL:** /api/admissions
- **Description:** Submits a new admission application.
- **Request Body:** AdmissionApplicationDTO (studentName, email, departmentId, academicYear, documents)
- **Response:** 201 Created - AdmissionApplication object

### 18.2 Get Application by ID
- **Method:** GET
- **URL:** /api/admissions/{id}
- **Description:** Retrieves an admission application by ID.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - AdmissionApplication object

### 18.3 Get Applications by Status
- **Method:** GET
- **URL:** /api/admissions/status/{status}
- **Description:** Retrieves applications filtered by status with pagination.
- **Path Parameters:** status (String, required)
- **Query Parameters:** page (int, default 0), size (int, default 10)
- **Response:** 200 OK - Page of AdmissionApplication objects

### 18.4 Get Applications by Department
- **Method:** GET
- **URL:** /api/admissions/department/{departmentId}
- **Description:** Retrieves applications for a specific department.
- **Path Parameters:** departmentId (String, required)
- **Response:** 200 OK - List of AdmissionApplication objects

### 18.5 Get Applications by Academic Year
- **Method:** GET
- **URL:** /api/admissions/academic-year/{year}
- **Description:** Retrieves applications for an academic year.
- **Path Parameters:** year (String, required)
- **Response:** 200 OK - List of AdmissionApplication objects

### 18.6 Get Application by Email
- **Method:** GET
- **URL:** /api/admissions/email
- **Description:** Retrieves an application by email.
- **Query Parameters:** email (String, required)
- **Response:** 200 OK - AdmissionApplication object

### 18.7 Review Application
- **Method:** PUT
- **URL:** /api/admissions/{id}/review
- **Description:** Reviews an admission application.
- **Path Parameters:** id (String, required)
- **Request Body:** AdmissionReviewDTO (status, reviewComments, reviewedBy)
- **Response:** 200 OK - AdmissionApplication object

### 18.8 Get Admission Statistics
- **Method:** GET
- **URL:** /api/admissions/statistics/{academicYear}
- **Description:** Gets admission statistics for an academic year.
- **Path Parameters:** academicYear (String, required)
- **Response:** 200 OK - Statistics object

---

## 19. Enrollments

**Base URL:** /api/enrollments

### 19.1 Enroll Student
- **Method:** POST
- **URL:** /api/enrollments
- **Description:** Enrolls a student.
- **Request Body:** EnrollmentDTO (studentId, departmentId, classId, academicYear)
- **Response:** 201 Created - Enrollment object

### 19.2 Get Enrollment by ID
- **Method:** GET
- **URL:** /api/enrollments/{id}
- **Description:** Retrieves an enrollment by ID.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - Enrollment object

### 19.3 Get Enrollments by Student
- **Method:** GET
- **URL:** /api/enrollments/student/{studentId}
- **Description:** Retrieves all enrollments for a student.
- **Path Parameters:** studentId (String, required)
- **Response:** 200 OK - List of Enrollment objects

### 19.4 Get Enrollments by Department
- **Method:** GET
- **URL:** /api/enrollments/department/{departmentId}
- **Description:** Retrieves enrollments for a department with pagination.
- **Path Parameters:** departmentId (String, required)
- **Query Parameters:** page (int, default 0), size (int, default 10)
- **Response:** 200 OK - Page of Enrollment objects

### 19.5 Get Enrollments by Class
- **Method:** GET
- **URL:** /api/enrollments/class/{classId}
- **Description:** Retrieves enrollments for a class.
- **Path Parameters:** classId (String, required)
- **Response:** 200 OK - List of Enrollment objects

### 19.6 Update Enrollment Status
- **Method:** PATCH
- **URL:** /api/enrollments/{id}/status
- **Description:** Updates the status of an enrollment.
- **Path Parameters:** id (String, required)
- **Query Parameters:** status (String, required)
- **Response:** 200 OK - Enrollment object

### 19.7 Get Enrollment Statistics
- **Method:** GET
- **URL:** /api/enrollments/statistics/{academicYear}
- **Description:** Gets enrollment statistics for an academic year.
- **Path Parameters:** academicYear (String, required)
- **Response:** 200 OK - Statistics object

### 19.8 Get Active Enrollments
- **Method:** GET
- **URL:** /api/enrollments/active/{academicYear}
- **Description:** Retrieves active enrollments for an academic year with pagination.
- **Path Parameters:** academicYear (String, required)
- **Query Parameters:** page (int, default 0), size (int, default 10)
- **Response:** 200 OK - Page of Enrollment objects

---

## 20. Attendance

**Base URL:** /api/attendance

### 20.1 Mark Attendance
- **Method:** POST
- **URL:** /api/attendance
- **Description:** Marks attendance for a student.
- **Query Parameters:** markedBy (String, required)
- **Request Body:** AttendanceDTO (studentId, courseId, classId, date, status)
- **Response:** 201 Created - Attendance object

### 20.2 Mark Bulk Attendance
- **Method:** POST
- **URL:** /api/attendance/bulk
- **Description:** Marks attendance for multiple students at once.
- **Query Parameters:** markedBy (String, required)
- **Request Body:** BulkAttendanceDTO (courseId, classId, date, records)
- **Response:** 201 Created - List of Attendance objects

### 20.3 Get Attendance by Student and Course
- **Method:** GET
- **URL:** /api/attendance/student/{studentId}/course/{courseId}
- **Description:** Retrieves attendance records for a student in a course.
- **Path Parameters:** studentId (String, required), courseId (String, required)
- **Response:** 200 OK - List of Attendance objects

### 20.4 Get Attendance by Class and Date
- **Method:** GET
- **URL:** /api/attendance/class/{classId}/date/{date}
- **Description:** Retrieves attendance for a class on a specific date.
- **Path Parameters:** classId (String, required), date (String, required - ISO date)
- **Response:** 200 OK - List of Attendance objects

### 20.5 Get Attendance by Course and Date
- **Method:** GET
- **URL:** /api/attendance/course/{courseId}/date/{date}
- **Description:** Retrieves attendance for a course on a specific date.
- **Path Parameters:** courseId (String, required), date (String, required - ISO date)
- **Response:** 200 OK - List of Attendance objects

### 20.6 Get Attendance Summary
- **Method:** GET
- **URL:** /api/attendance/student/{studentId}/summary
- **Description:** Retrieves attendance summary for a student.
- **Path Parameters:** studentId (String, required)
- **Query Parameters:** academicYear (String, required), semester (String, required)
- **Response:** 200 OK - Attendance summary object

### 20.7 Get Attendance Report
- **Method:** GET
- **URL:** /api/attendance/student/{studentId}/report/{courseId}
- **Description:** Retrieves a detailed attendance report for a student in a course.
- **Path Parameters:** studentId (String, required), courseId (String, required)
- **Response:** 200 OK - Attendance report object

### 20.8 Get Low Attendance
- **Method:** GET
- **URL:** /api/attendance/low-attendance
- **Description:** Retrieves students with low attendance.
- **Query Parameters:** threshold (Double, required)
- **Response:** 200 OK - List of low attendance records

### 20.9 Get Attendance by Date Range
- **Method:** GET
- **URL:** /api/attendance/student/{studentId}/range
- **Description:** Retrieves attendance for a student within a date range.
- **Path Parameters:** studentId (String, required)
- **Query Parameters:** courseId (String, required), startDate (String, required), endDate (String, required)
- **Response:** 200 OK - List of Attendance objects

---

## 21. Parent/Guardian

**Base URL:** /api/parent

### 21.1 Create Parent/Guardian
- **Method:** POST
- **URL:** /api/parent
- **Description:** Creates a new parent/guardian record.
- **Request Body:** ParentGuardianDTO (name, email, phone, relationship, userId)
- **Response:** 201 Created - ParentGuardian object

### 21.2 Get Parent by ID
- **Method:** GET
- **URL:** /api/parent/{id}
- **Description:** Retrieves a parent/guardian by ID.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - ParentGuardian object

### 21.3 Get Parent by Email
- **Method:** GET
- **URL:** /api/parent/email
- **Description:** Retrieves a parent/guardian by email.
- **Query Parameters:** email (String, required)
- **Response:** 200 OK - ParentGuardian object

### 21.4 Get Parent by User ID
- **Method:** GET
- **URL:** /api/parent/user/{userId}
- **Description:** Retrieves a parent/guardian by user ID.
- **Path Parameters:** userId (String, required)
- **Response:** 200 OK - ParentGuardian object

### 21.5 Get Parents by Student
- **Method:** GET
- **URL:** /api/parent/student/{studentId}
- **Description:** Retrieves all parents/guardians linked to a student.
- **Path Parameters:** studentId (String, required)
- **Response:** 200 OK - List of ParentGuardian objects

### 21.6 Update Parent
- **Method:** PUT
- **URL:** /api/parent/{id}
- **Description:** Updates a parent/guardian record.
- **Path Parameters:** id (String, required)
- **Request Body:** ParentGuardianDTO object
- **Response:** 200 OK - ParentGuardian object

### 21.7 Link Student
- **Method:** POST
- **URL:** /api/parent/{parentId}/link/{studentId}
- **Description:** Links a student to a parent/guardian.
- **Path Parameters:** parentId (String, required), studentId (String, required)
- **Response:** 200 OK - ParentGuardian object

### 21.8 Unlink Student
- **Method:** DELETE
- **URL:** /api/parent/{parentId}/unlink/{studentId}
- **Description:** Unlinks a student from a parent/guardian.
- **Path Parameters:** parentId (String, required), studentId (String, required)
- **Response:** 200 OK - ParentGuardian object

### 21.9 Get Parent Dashboard
- **Method:** GET
- **URL:** /api/parent/{parentId}/dashboard
- **Description:** Retrieves the dashboard view for a parent/guardian.
- **Path Parameters:** parentId (String, required)
- **Response:** 200 OK - Dashboard object

### 21.10 Get All Parents
- **Method:** GET
- **URL:** /api/parent
- **Description:** Retrieves all parents/guardians with pagination.
- **Query Parameters:** page (int, default 0), size (int, default 10)
- **Response:** 200 OK - Page of ParentGuardian objects

---

## 22. Exams

**Base URL:** /api/exams

### 22.1 Create Exam
- **Method:** POST
- **URL:** /api/exams
- **Description:** Creates a new exam.
- **Request Body:** ExamDTO (title, courseId, departmentId, teacherId, examType, totalMarks, duration, instructions)
- **Response:** 201 Created - Exam object

### 22.2 Get Exam by ID
- **Method:** GET
- **URL:** /api/exams/{examId}
- **Description:** Retrieves an exam by ID.
- **Path Parameters:** examId (String, required)
- **Response:** 200 OK - Exam object

### 22.3 Get Exams by Course
- **Method:** GET
- **URL:** /api/exams/course/{courseId}
- **Description:** Retrieves all exams for a course.
- **Path Parameters:** courseId (String, required)
- **Response:** 200 OK - List of Exam objects

### 22.4 Get Exams by Department
- **Method:** GET
- **URL:** /api/exams/department/{departmentId}
- **Description:** Retrieves all exams for a department.
- **Path Parameters:** departmentId (String, required)
- **Response:** 200 OK - List of Exam objects

### 22.5 Get Exams by Teacher
- **Method:** GET
- **URL:** /api/exams/teacher/{teacherId}
- **Description:** Retrieves all exams created by a teacher.
- **Path Parameters:** teacherId (String, required)
- **Response:** 200 OK - List of Exam objects

### 22.6 Update Exam Status
- **Method:** PATCH
- **URL:** /api/exams/{examId}/status
- **Description:** Updates the status of an exam.
- **Path Parameters:** examId (String, required)
- **Query Parameters:** status (ExamStatus, required - DRAFT/SCHEDULED/IN_PROGRESS/COMPLETED/CANCELLED)
- **Response:** 200 OK - Exam object

### 22.7 Get Upcoming Exams
- **Method:** GET
- **URL:** /api/exams/upcoming
- **Description:** Retrieves upcoming exams.
- **Response:** 200 OK - List of Exam objects

### 22.8 Add Question to Question Bank
- **Method:** POST
- **URL:** /api/exams/questions
- **Description:** Adds a question to the question bank.
- **Request Body:** QuestionBankDTO (courseId, question, options, correctAnswer, difficulty, marks)
- **Response:** 201 Created - QuestionBank object

### 22.9 Get Questions by Course
- **Method:** GET
- **URL:** /api/exams/questions/course/{courseId}
- **Description:** Retrieves all questions for a course.
- **Path Parameters:** courseId (String, required)
- **Response:** 200 OK - List of QuestionBank objects

### 22.10 Generate Random Questions
- **Method:** GET
- **URL:** /api/exams/questions/generate
- **Description:** Generates random questions for an exam.
- **Query Parameters:** courseId (String, required), count (int, required), difficulty (String, optional)
- **Response:** 200 OK - List of QuestionBank objects

### 22.11 Submit Exam Result
- **Method:** POST
- **URL:** /api/exams/results
- **Description:** Submits an exam result.
- **Request Body:** ExamResultDTO (examId, studentId, answers, score, submittedAt)
- **Response:** 201 Created - ExamResult object

### 22.12 Get Exam Results by Exam
- **Method:** GET
- **URL:** /api/exams/results/exam/{examId}
- **Description:** Retrieves all results for a specific exam.
- **Path Parameters:** examId (String, required)
- **Response:** 200 OK - List of ExamResult objects

### 22.13 Get Exam Results by Student
- **Method:** GET
- **URL:** /api/exams/results/student/{studentId}
- **Description:** Retrieves all exam results for a student.
- **Path Parameters:** studentId (String, required)
- **Response:** 200 OK - List of ExamResult objects

### 22.14 Create Exam Schedule
- **Method:** POST
- **URL:** /api/exams/schedule
- **Description:** Creates an exam schedule.
- **Request Body:** ExamScheduleDTO (examId, date, startTime, endTime, venue, departmentId)
- **Response:** 201 Created - ExamSchedule object

### 22.15 Get Exam Schedule by Department
- **Method:** GET
- **URL:** /api/exams/schedule/department/{departmentId}
- **Description:** Retrieves exam schedules for a department.
- **Path Parameters:** departmentId (String, required)
- **Response:** 200 OK - List of ExamSchedule objects

### 22.16 Get Exam Schedule by Date
- **Method:** GET
- **URL:** /api/exams/schedule/date/{date}
- **Description:** Retrieves exam schedules for a specific date.
- **Path Parameters:** date (String, required - ISO date)
- **Response:** 200 OK - List of ExamSchedule objects

---

## 23. Report Cards

**Base URL:** /api/report-cards

### 23.1 Generate Report Card
- **Method:** POST
- **URL:** /api/report-cards/generate
- **Description:** Generates a report card for a student.
- **Request Body:** ReportCardRequestDTO (studentId, academicYear, semester, departmentId)
- **Response:** 201 Created - ReportCard object

### 23.2 Get Report Card by ID
- **Method:** GET
- **URL:** /api/report-cards/{id}
- **Description:** Retrieves a report card by ID.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - ReportCard object

### 23.3 Get Report Cards by Student
- **Method:** GET
- **URL:** /api/report-cards/student/{studentId}
- **Description:** Retrieves all report cards for a student.
- **Path Parameters:** studentId (String, required)
- **Response:** 200 OK - List of ReportCard objects

### 23.4 Get Report Cards by Department
- **Method:** GET
- **URL:** /api/report-cards/department/{departmentId}
- **Description:** Retrieves report cards for a department filtered by year and semester.
- **Path Parameters:** departmentId (String, required)
- **Query Parameters:** academicYear (String, required), semester (String, required)
- **Response:** 200 OK - List of ReportCard objects

### 23.5 Publish Report Card
- **Method:** PATCH
- **URL:** /api/report-cards/{id}/publish
- **Description:** Publishes a report card making it visible to students/parents.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - ReportCard object

### 23.6 Generate Bulk Report Cards
- **Method:** POST
- **URL:** /api/report-cards/generate/bulk
- **Description:** Generates report cards for all students in a department.
- **Query Parameters:** departmentId (String, required), academicYear (String, required), semester (String, required)
- **Response:** 201 Created - List of ReportCard objects

### 23.7 Generate Transcript
- **Method:** POST
- **URL:** /api/report-cards/transcript/generate
- **Description:** Generates an academic transcript for a student.
- **Request Body:** TranscriptRequestDTO (studentId, includeAllSemesters)
- **Response:** 201 Created - Transcript object

### 23.8 Get Transcript
- **Method:** GET
- **URL:** /api/report-cards/transcript/{id}
- **Description:** Retrieves a transcript by ID.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - Transcript object

### 23.9 Verify Transcript
- **Method:** GET
- **URL:** /api/report-cards/transcript/verify/{verificationCode}
- **Description:** Verifies the authenticity of a transcript.
- **Path Parameters:** verificationCode (String, required)
- **Response:** 200 OK - Verification result

---

## 24. Gradebook

**Base URL:** /api/gradebook

### 24.1 Create Grade Category
- **Method:** POST
- **URL:** /api/gradebook/categories
- **Description:** Creates a new grade category.
- **Request Body:** GradeCategoryDTO (name, weight, courseId, departmentId)
- **Response:** 201 Created - GradeCategory object

### 24.2 Get Categories by Course
- **Method:** GET
- **URL:** /api/gradebook/categories/course/{courseId}
- **Description:** Retrieves grade categories for a course.
- **Path Parameters:** courseId (String, required)
- **Response:** 200 OK - List of GradeCategory objects

### 24.3 Get Categories by Department
- **Method:** GET
- **URL:** /api/gradebook/categories/department/{departmentId}
- **Description:** Retrieves grade categories for a department.
- **Path Parameters:** departmentId (String, required)
- **Response:** 200 OK - List of GradeCategory objects

### 24.4 Update Grade Category
- **Method:** PUT
- **URL:** /api/gradebook/categories/{id}
- **Description:** Updates a grade category.
- **Path Parameters:** id (String, required)
- **Request Body:** GradeCategoryDTO object
- **Response:** 200 OK - GradeCategory object

### 24.5 Delete Grade Category
- **Method:** DELETE
- **URL:** /api/gradebook/categories/{id}
- **Description:** Deletes a grade category.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK

### 24.6 Create Grade Scale
- **Method:** POST
- **URL:** /api/gradebook/scales
- **Description:** Creates a new grading scale.
- **Request Body:** GradeScaleDTO (name, departmentId, ranges)
- **Response:** 201 Created - GradeScale object

### 24.7 Get Scales by Department
- **Method:** GET
- **URL:** /api/gradebook/scales/department/{departmentId}
- **Description:** Retrieves grading scales for a department.
- **Path Parameters:** departmentId (String, required)
- **Response:** 200 OK - List of GradeScale objects

### 24.8 Get Default Scale
- **Method:** GET
- **URL:** /api/gradebook/scales/default
- **Description:** Retrieves the default grading scale.
- **Response:** 200 OK - GradeScale object

### 24.9 Submit Grade Appeal
- **Method:** POST
- **URL:** /api/gradebook/appeals
- **Description:** Submits a grade appeal.
- **Request Body:** GradeAppealDTO (studentId, courseId, reason, expectedGrade)
- **Response:** 201 Created - GradeAppeal object

### 24.10 Get Appeals by Student
- **Method:** GET
- **URL:** /api/gradebook/appeals/student/{studentId}
- **Description:** Retrieves grade appeals for a student.
- **Path Parameters:** studentId (String, required)
- **Response:** 200 OK - List of GradeAppeal objects

### 24.11 Get Appeals by Course
- **Method:** GET
- **URL:** /api/gradebook/appeals/course/{courseId}
- **Description:** Retrieves grade appeals for a course.
- **Path Parameters:** courseId (String, required)
- **Response:** 200 OK - List of GradeAppeal objects

### 24.12 Get Pending Appeals
- **Method:** GET
- **URL:** /api/gradebook/appeals/pending
- **Description:** Retrieves all pending grade appeals with pagination.
- **Query Parameters:** page (int, default 0), size (int, default 10)
- **Response:** 200 OK - Page of GradeAppeal objects

### 24.13 Review Appeal
- **Method:** PUT
- **URL:** /api/gradebook/appeals/{id}/review
- **Description:** Reviews a grade appeal.
- **Path Parameters:** id (String, required)
- **Request Body:** GradeAppealReviewDTO (status, reviewComments, newGrade, reviewedBy)
- **Response:** 200 OK - GradeAppeal object

### 24.14 Calculate Weighted Grade
- **Method:** GET
- **URL:** /api/gradebook/weighted-grade/student/{studentId}/course/{courseId}
- **Description:** Calculates the weighted grade for a student in a course.
- **Path Parameters:** studentId (String, required), courseId (String, required)
- **Response:** 200 OK - Weighted grade (number)

---

## 25. Content Management

**Base URL:** /api/content

### 25.1 Create Course Module
- **Method:** POST
- **URL:** /api/content/modules
- **Description:** Creates a new course module.
- **Request Body:** CourseModuleDTO (courseId, title, description, orderIndex)
- **Response:** 201 Created - CourseModule object

### 25.2 Get Modules by Course
- **Method:** GET
- **URL:** /api/content/modules/course/{courseId}
- **Description:** Retrieves all modules for a course.
- **Path Parameters:** courseId (String, required)
- **Response:** 200 OK - List of CourseModule objects

### 25.3 Update Module
- **Method:** PUT
- **URL:** /api/content/modules/{id}
- **Description:** Updates a course module.
- **Path Parameters:** id (String, required)
- **Request Body:** CourseModuleDTO object
- **Response:** 200 OK - CourseModule object

### 25.4 Delete Module
- **Method:** DELETE
- **URL:** /api/content/modules/{id}
- **Description:** Deletes a course module.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK

### 25.5 Reorder Modules
- **Method:** PUT
- **URL:** /api/content/modules/reorder/{courseId}
- **Description:** Reorders modules for a course.
- **Path Parameters:** courseId (String, required)
- **Request Body:** List of module IDs (ordered)
- **Response:** 200 OK - List of CourseModule objects

### 25.6 Create Lesson
- **Method:** POST
- **URL:** /api/content/lessons
- **Description:** Creates a new lesson.
- **Request Body:** LessonDTO (moduleId, courseId, title, content, contentType, duration, orderIndex)
- **Response:** 201 Created - Lesson object

### 25.7 Get Lessons by Module
- **Method:** GET
- **URL:** /api/content/lessons/module/{moduleId}
- **Description:** Retrieves all lessons for a module.
- **Path Parameters:** moduleId (String, required)
- **Response:** 200 OK - List of Lesson objects

### 25.8 Get Lessons by Course
- **Method:** GET
- **URL:** /api/content/lessons/course/{courseId}
- **Description:** Retrieves all lessons for a course.
- **Path Parameters:** courseId (String, required)
- **Response:** 200 OK - List of Lesson objects

### 25.9 Update Lesson
- **Method:** PUT
- **URL:** /api/content/lessons/{id}
- **Description:** Updates a lesson.
- **Path Parameters:** id (String, required)
- **Request Body:** LessonDTO object
- **Response:** 200 OK - Lesson object

### 25.10 Delete Lesson
- **Method:** DELETE
- **URL:** /api/content/lessons/{id}
- **Description:** Deletes a lesson.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK

### 25.11 Track Learning Progress
- **Method:** POST
- **URL:** /api/content/progress
- **Description:** Tracks a student's learning progress for a lesson.
- **Query Parameters:** studentId (String, required), lessonId (String, required)
- **Request Body:** LearningProgressDTO (completed, timeSpent, notes)
- **Response:** 200 OK - LearningProgress object

### 25.12 Get Student Progress by Course
- **Method:** GET
- **URL:** /api/content/progress/student/{studentId}/course/{courseId}
- **Description:** Retrieves a student's learning progress for a course.
- **Path Parameters:** studentId (String, required), courseId (String, required)
- **Response:** 200 OK - List of LearningProgress objects

### 25.13 Get Completion Percentage
- **Method:** GET
- **URL:** /api/content/progress/completion/{studentId}/{courseId}
- **Description:** Gets the completion percentage for a student in a course.
- **Path Parameters:** studentId (String, required), courseId (String, required)
- **Response:** 200 OK - Completion percentage (number)

---

## 26. Discussions

**Base URL:** /api/discussions

### 26.1 Create Forum
- **Method:** POST
- **URL:** /api/discussions/forums
- **Description:** Creates a new discussion forum.
- **Request Body:** DiscussionForumDTO (courseId, title, description, createdBy)
- **Response:** 201 Created - DiscussionForum object

### 26.2 Get Forums by Course
- **Method:** GET
- **URL:** /api/discussions/forums/course/{courseId}
- **Description:** Retrieves all discussion forums for a course.
- **Path Parameters:** courseId (String, required)
- **Response:** 200 OK - List of DiscussionForum objects

### 26.3 Create Post
- **Method:** POST
- **URL:** /api/discussions/posts
- **Description:** Creates a new discussion post.
- **Request Body:** DiscussionPostDTO (forumId, authorId, content, parentPostId)
- **Response:** 201 Created - DiscussionPost object

### 26.4 Get Posts by Forum
- **Method:** GET
- **URL:** /api/discussions/posts/forum/{forumId}
- **Description:** Retrieves posts for a forum with pagination.
- **Path Parameters:** forumId (String, required)
- **Query Parameters:** page (int, default 0), size (int, default 10)
- **Response:** 200 OK - Page of DiscussionPost objects

### 26.5 Get Replies
- **Method:** GET
- **URL:** /api/discussions/posts/{postId}/replies
- **Description:** Retrieves replies to a specific post.
- **Path Parameters:** postId (String, required)
- **Response:** 200 OK - List of DiscussionPost objects

### 26.6 Like Post
- **Method:** POST
- **URL:** /api/discussions/posts/{postId}/like
- **Description:** Likes a discussion post.
- **Path Parameters:** postId (String, required)
- **Query Parameters:** userId (String, required)
- **Response:** 200 OK - DiscussionPost object

### 26.7 Unlike Post
- **Method:** DELETE
- **URL:** /api/discussions/posts/{postId}/unlike
- **Description:** Removes a like from a discussion post.
- **Path Parameters:** postId (String, required)
- **Query Parameters:** userId (String, required)
- **Response:** 200 OK - DiscussionPost object

### 26.8 Toggle Pin Post
- **Method:** PATCH
- **URL:** /api/discussions/posts/{postId}/pin
- **Description:** Toggles the pinned status of a post.
- **Path Parameters:** postId (String, required)
- **Response:** 200 OK - DiscussionPost object

### 26.9 Edit Post
- **Method:** PUT
- **URL:** /api/discussions/posts/{postId}
- **Description:** Edits a discussion post.
- **Path Parameters:** postId (String, required)
- **Request Body:** Object with content (String) and userId (String)
- **Response:** 200 OK - DiscussionPost object

### 26.10 Delete Post
- **Method:** DELETE
- **URL:** /api/discussions/posts/{postId}
- **Description:** Deletes a discussion post.
- **Path Parameters:** postId (String, required)
- **Response:** 200 OK

---

## 27. Learning Paths

**Base URL:** /api/learning-paths

### 27.1 Create Learning Path
- **Method:** POST
- **URL:** /api/learning-paths
- **Description:** Creates a new learning path with specified courses, prerequisites, difficulty level, and tags. Created in unpublished state.
- **Request Body:** LearningPathDTO (title, description, courseIds, prerequisites, difficultyLevel, tags, departmentId)
- **Response:** 201 Created - LearningPath object

### 27.2 Get Learning Path by ID
- **Method:** GET
- **URL:** /api/learning-paths/{id}
- **Description:** Retrieves a specific learning path by its unique identifier.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - LearningPath object

### 27.3 Get Published Learning Paths
- **Method:** GET
- **URL:** /api/learning-paths/published
- **Description:** Retrieves a paginated list of all published learning paths.
- **Query Parameters:** page (int, default 0), size (int, default 10)
- **Response:** 200 OK - Page of LearningPath objects

### 27.4 Get Learning Paths by Department
- **Method:** GET
- **URL:** /api/learning-paths/department/{departmentId}
- **Description:** Retrieves all learning paths for a department.
- **Path Parameters:** departmentId (String, required)
- **Response:** 200 OK - List of LearningPath objects

### 27.5 Enroll Student in Learning Path
- **Method:** POST
- **URL:** /api/learning-paths/{pathId}/enroll/{studentId}
- **Description:** Enrolls a student in a learning path and creates a progress tracking record.
- **Path Parameters:** pathId (String, required), studentId (String, required)
- **Response:** 201 Created - LearningPathProgress object

### 27.6 Update Student Progress
- **Method:** PUT
- **URL:** /api/learning-paths/{pathId}/progress/{studentId}
- **Description:** Updates a student's progress by marking a course as completed.
- **Path Parameters:** pathId (String, required), studentId (String, required)
- **Query Parameters:** completedCourseId (String, required)
- **Response:** 200 OK - LearningPathProgress object

### 27.7 Get Student Progress
- **Method:** GET
- **URL:** /api/learning-paths/progress/{studentId}/{pathId}
- **Description:** Retrieves the progress of a student on a specific learning path.
- **Path Parameters:** studentId (String, required), pathId (String, required)
- **Response:** 200 OK - LearningPathProgressDTO object

### 27.8 Get All Paths Progress for Student
- **Method:** GET
- **URL:** /api/learning-paths/progress/student/{studentId}
- **Description:** Retrieves progress for all learning paths a student is enrolled in.
- **Path Parameters:** studentId (String, required)
- **Response:** 200 OK - List of LearningPathProgressDTO objects

### 27.9 Publish Learning Path
- **Method:** PATCH
- **URL:** /api/learning-paths/{id}/publish
- **Description:** Publishes a learning path, making it available for enrollment.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - LearningPath object

### 27.10 Update Learning Path
- **Method:** PUT
- **URL:** /api/learning-paths/{id}
- **Description:** Updates an existing learning path.
- **Path Parameters:** id (String, required)
- **Request Body:** LearningPathDTO object
- **Response:** 200 OK - LearningPath object

---

## 28. Staff

**Base URL:** /api/staff

### 28.1 Create Staff
- **Method:** POST
- **URL:** /api/staff
- **Description:** Creates a new staff member record.
- **Request Body:** StaffDTO (name, email, departmentId, staffType, designation, salary)
- **Response:** 201 Created - Staff object

### 28.2 Get Staff by ID
- **Method:** GET
- **URL:** /api/staff/{id}
- **Description:** Retrieves a staff member by ID.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - Staff object

### 28.3 Get Staff by Email
- **Method:** GET
- **URL:** /api/staff/email
- **Description:** Retrieves a staff member by email.
- **Query Parameters:** email (String, required)
- **Response:** 200 OK - Staff object

### 28.4 Get All Staff
- **Method:** GET
- **URL:** /api/staff
- **Description:** Retrieves all staff members with pagination.
- **Query Parameters:** page (int, default 0), size (int, default 10)
- **Response:** 200 OK - Page of Staff objects

### 28.5 Get Staff by Department
- **Method:** GET
- **URL:** /api/staff/department/{departmentId}
- **Description:** Retrieves staff members in a department.
- **Path Parameters:** departmentId (String, required)
- **Response:** 200 OK - List of Staff objects

### 28.6 Get Staff by Type
- **Method:** GET
- **URL:** /api/staff/type/{staffType}
- **Description:** Retrieves staff members by type.
- **Path Parameters:** staffType (String, required)
- **Response:** 200 OK - List of Staff objects

### 28.7 Update Staff
- **Method:** PUT
- **URL:** /api/staff/{id}
- **Description:** Updates a staff member record.
- **Path Parameters:** id (String, required)
- **Request Body:** StaffDTO object
- **Response:** 200 OK - Staff object

### 28.8 Update Employment Status
- **Method:** PATCH
- **URL:** /api/staff/{id}/status
- **Description:** Updates the employment status of a staff member.
- **Path Parameters:** id (String, required)
- **Query Parameters:** status (EmploymentStatus, required)
- **Response:** 200 OK - Staff object

### 28.9 Delete Staff
- **Method:** DELETE
- **URL:** /api/staff/{id}
- **Description:** Deletes a staff member record.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK

---

## 29. Leave Management

**Base URL:** /api/leave

### 29.1 Apply for Leave
- **Method:** POST
- **URL:** /api/leave
- **Description:** Submits a leave request.
- **Query Parameters:** staffId (String, required)
- **Request Body:** LeaveRequestDTO (leaveType, startDate, endDate, reason)
- **Response:** 201 Created - LeaveRequest object

### 29.2 Get Leave Request by ID
- **Method:** GET
- **URL:** /api/leave/{id}
- **Description:** Retrieves a leave request by ID.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - LeaveRequest object

### 29.3 Get Leave Requests by Staff
- **Method:** GET
- **URL:** /api/leave/staff/{staffId}
- **Description:** Retrieves all leave requests for a staff member.
- **Path Parameters:** staffId (String, required)
- **Response:** 200 OK - List of LeaveRequest objects

### 29.4 Get Pending Leave Requests
- **Method:** GET
- **URL:** /api/leave/pending
- **Description:** Retrieves all pending leave requests with pagination.
- **Query Parameters:** page (int, default 0), size (int, default 10)
- **Response:** 200 OK - Page of LeaveRequest objects

### 29.5 Review Leave Request
- **Method:** PUT
- **URL:** /api/leave/{id}/review
- **Description:** Reviews a leave request (approve/reject).
- **Path Parameters:** id (String, required)
- **Request Body:** LeaveReviewDTO (status, reviewComments, reviewedBy)
- **Response:** 200 OK - LeaveRequest object

### 29.6 Cancel Leave Request
- **Method:** PATCH
- **URL:** /api/leave/{id}/cancel
- **Description:** Cancels a leave request.
- **Path Parameters:** id (String, required)
- **Query Parameters:** staffId (String, required)
- **Response:** 200 OK - LeaveRequest object

### 29.7 Get Leave Balance
- **Method:** GET
- **URL:** /api/leave/balance/{staffId}
- **Description:** Retrieves the leave balance for a staff member.
- **Path Parameters:** staffId (String, required)
- **Query Parameters:** academicYear (String, required)
- **Response:** 200 OK - Leave balance object

### 29.8 Initialize Leave Balance
- **Method:** POST
- **URL:** /api/leave/balance/initialize
- **Description:** Initializes leave balance for a staff member.
- **Query Parameters:** staffId (String, required), academicYear (String, required)
- **Response:** 201 Created - Leave balance object

---

## 30. Payroll

**Base URL:** /api/payroll

### 30.1 Generate Payroll
- **Method:** POST
- **URL:** /api/payroll/generate
- **Description:** Generates payroll for a staff member.
- **Request Body:** PayrollGenerateDTO (staffId, month, year, allowances, deductions)
- **Response:** 201 Created - Payroll object

### 30.2 Generate Bulk Payroll
- **Method:** POST
- **URL:** /api/payroll/generate/bulk
- **Description:** Generates payroll for all staff for a given month.
- **Query Parameters:** month (int, required), year (int, required)
- **Response:** 201 Created - List of Payroll objects

### 30.3 Get Payroll by ID
- **Method:** GET
- **URL:** /api/payroll/{id}
- **Description:** Retrieves a payroll record by ID.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - Payroll object

### 30.4 Get Payroll by Staff
- **Method:** GET
- **URL:** /api/payroll/staff/{staffId}
- **Description:** Retrieves all payroll records for a staff member.
- **Path Parameters:** staffId (String, required)
- **Response:** 200 OK - List of Payroll objects

### 30.5 Get Monthly Payroll
- **Method:** GET
- **URL:** /api/payroll/monthly
- **Description:** Retrieves payroll records for a specific month with pagination.
- **Query Parameters:** month (int, required), year (int, required), page (int, default 0), size (int, default 10)
- **Response:** 200 OK - Page of Payroll objects

### 30.6 Process Payment
- **Method:** PATCH
- **URL:** /api/payroll/{id}/process
- **Description:** Processes a payroll payment.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - Payroll object

### 30.7 Get Payslip
- **Method:** GET
- **URL:** /api/payroll/{id}/payslip
- **Description:** Retrieves the payslip for a payroll record.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - Payslip object

### 30.8 Create Salary Structure
- **Method:** POST
- **URL:** /api/payroll/salary-structure
- **Description:** Creates a new salary structure.
- **Request Body:** SalaryStructureDTO (designation, baseSalary, allowances, deductions)
- **Response:** 201 Created - SalaryStructure object

### 30.9 Get All Salary Structures
- **Method:** GET
- **URL:** /api/payroll/salary-structure
- **Description:** Retrieves all salary structures.
- **Response:** 200 OK - List of SalaryStructure objects

### 30.10 Get Salary Structure by Designation
- **Method:** GET
- **URL:** /api/payroll/salary-structure/{designation}
- **Description:** Retrieves a salary structure by designation.
- **Path Parameters:** designation (String, required)
- **Response:** 200 OK - SalaryStructure object

---

## 31. Library

**Base URL:** /api/library

### 31.1 Add Book
- **Method:** POST
- **URL:** /api/library/admin/books
- **Description:** Adds a new book to the library.
- **Request Body:** LibraryBookDTO (title, author, isbn, category, totalCopies, publisher, publishYear)
- **Response:** 201 Created - LibraryBook object

### 31.2 Get Book by ID
- **Method:** GET
- **URL:** /api/library/books/{id}
- **Description:** Retrieves a book by ID.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - LibraryBook object

### 31.3 Search Books
- **Method:** GET
- **URL:** /api/library/books/search
- **Description:** Searches books by query string.
- **Query Parameters:** query (String, required), page (int, default 0), size (int, default 10)
- **Response:** 200 OK - Page of LibraryBook objects

### 31.4 Get Books by Category
- **Method:** GET
- **URL:** /api/library/books/category/{category}
- **Description:** Retrieves books by category with pagination.
- **Path Parameters:** category (String, required)
- **Query Parameters:** page (int, default 0), size (int, default 10)
- **Response:** 200 OK - Page of LibraryBook objects

### 31.5 Update Book
- **Method:** PUT
- **URL:** /api/library/admin/books/{id}
- **Description:** Updates a book record.
- **Path Parameters:** id (String, required)
- **Request Body:** LibraryBookDTO object
- **Response:** 200 OK - LibraryBook object

### 31.6 Issue Book
- **Method:** POST
- **URL:** /api/library/issue
- **Description:** Issues a book to a borrower.
- **Query Parameters:** issuedBy (String, required)
- **Request Body:** BookIssueDTO (bookId, borrowerId, borrowerType, dueDate)
- **Response:** 201 Created - BookIssue object

### 31.7 Return Book
- **Method:** PATCH
- **URL:** /api/library/return/{issueId}
- **Description:** Processes a book return.
- **Path Parameters:** issueId (String, required)
- **Query Parameters:** returnedTo (String, required)
- **Request Body:** BookReturnDTO (condition, notes)
- **Response:** 200 OK - BookIssue object

### 31.8 Get Issues by Borrower
- **Method:** GET
- **URL:** /api/library/issues/borrower/{borrowerId}
- **Description:** Retrieves all book issues for a borrower.
- **Path Parameters:** borrowerId (String, required)
- **Response:** 200 OK - List of BookIssue objects

### 31.9 Get Overdue Books
- **Method:** GET
- **URL:** /api/library/issues/overdue
- **Description:** Retrieves all overdue book issues.
- **Response:** 200 OK - List of BookIssue objects

### 31.10 Calculate Fine
- **Method:** GET
- **URL:** /api/library/issues/{issueId}/fine
- **Description:** Calculates the fine for an overdue book.
- **Path Parameters:** issueId (String, required)
- **Response:** 200 OK - Fine amount (number)

### 31.11 Reserve Book
- **Method:** POST
- **URL:** /api/library/reserve
- **Description:** Reserves a book.
- **Request Body:** BookReservationDTO (bookId, reservedBy, reservedByType)
- **Response:** 201 Created - BookReservation object

### 31.12 Get Reservations for Book
- **Method:** GET
- **URL:** /api/library/reservations/book/{bookId}
- **Description:** Retrieves all reservations for a book.
- **Path Parameters:** bookId (String, required)
- **Response:** 200 OK - List of BookReservation objects

### 31.13 Cancel Reservation
- **Method:** DELETE
- **URL:** /api/library/reservations/{id}
- **Description:** Cancels a book reservation.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK

### 31.14 Get Library Statistics
- **Method:** GET
- **URL:** /api/library/statistics
- **Description:** Retrieves library statistics.
- **Response:** 200 OK - Library statistics object

---

## 32. Transport

**Base URL:** /api/transport

### 32.1 Add Vehicle
- **Method:** POST
- **URL:** /api/transport/admin/vehicles
- **Description:** Adds a new vehicle to the transport fleet.
- **Request Body:** VehicleDTO (vehicleNumber, type, capacity, driverName, driverContact)
- **Response:** 201 Created - Vehicle object

### 32.2 Get Vehicle by ID
- **Method:** GET
- **URL:** /api/transport/vehicles/{id}
- **Description:** Retrieves a vehicle by ID.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - Vehicle object

### 32.3 Get All Vehicles
- **Method:** GET
- **URL:** /api/transport/vehicles
- **Description:** Retrieves all vehicles with pagination.
- **Query Parameters:** page (int, default 0), size (int, default 10)
- **Response:** 200 OK - Page of Vehicle objects

### 32.4 Update Vehicle
- **Method:** PUT
- **URL:** /api/transport/admin/vehicles/{id}
- **Description:** Updates a vehicle record.
- **Path Parameters:** id (String, required)
- **Request Body:** VehicleDTO object
- **Response:** 200 OK - Vehicle object

### 32.5 Update Vehicle Status
- **Method:** PATCH
- **URL:** /api/transport/admin/vehicles/{id}/status
- **Description:** Updates the status of a vehicle.
- **Path Parameters:** id (String, required)
- **Request Body/Query:** Status value
- **Response:** 200 OK - Vehicle object

### 32.6 Create Transport Route
- **Method:** POST
- **URL:** /api/transport/admin/routes
- **Description:** Creates a new transport route.
- **Request Body:** TransportRouteDTO (routeName, startPoint, endPoint, distance, vehicleId)
- **Response:** 201 Created - TransportRoute object

### 32.7 Get Route by ID
- **Method:** GET
- **URL:** /api/transport/routes/{id}
- **Description:** Retrieves a transport route by ID.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - TransportRoute object

### 32.8 Get Active Routes
- **Method:** GET
- **URL:** /api/transport/routes/active
- **Description:** Retrieves all active transport routes.
- **Response:** 200 OK - List of TransportRoute objects

### 32.9 Update Route
- **Method:** PUT
- **URL:** /api/transport/admin/routes/{id}
- **Description:** Updates a transport route.
- **Path Parameters:** id (String, required)
- **Request Body:** TransportRouteDTO object
- **Response:** 200 OK - TransportRoute object

### 32.10 Add Stop to Route
- **Method:** POST
- **URL:** /api/transport/admin/routes/{routeId}/stops
- **Description:** Adds a stop to a transport route.
- **Path Parameters:** routeId (String, required)
- **Request Body:** RouteStop (stopName, pickupTime, dropTime, orderIndex)
- **Response:** 200 OK - TransportRoute object

### 32.11 Remove Stop from Route
- **Method:** DELETE
- **URL:** /api/transport/admin/routes/{routeId}/stops/{stopName}
- **Description:** Removes a stop from a transport route.
- **Path Parameters:** routeId (String, required), stopName (String, required)
- **Response:** 200 OK - TransportRoute object

### 32.12 Assign Student to Transport
- **Method:** POST
- **URL:** /api/transport/assignments
- **Description:** Assigns a student to a transport route.
- **Request Body:** TransportAssignmentDTO (studentId, routeId, stopName, academicYear)
- **Response:** 201 Created - TransportAssignment object

### 32.13 Get Assignments by Student
- **Method:** GET
- **URL:** /api/transport/assignments/student/{studentId}
- **Description:** Retrieves transport assignments for a student.
- **Path Parameters:** studentId (String, required)
- **Response:** 200 OK - List of TransportAssignment objects

### 32.14 Get Assignments by Route
- **Method:** GET
- **URL:** /api/transport/assignments/route/{routeId}
- **Description:** Retrieves transport assignments for a route.
- **Path Parameters:** routeId (String, required)
- **Response:** 200 OK - List of TransportAssignment objects

### 32.15 Remove Transport Assignment
- **Method:** DELETE
- **URL:** /api/transport/assignments/{id}
- **Description:** Removes a transport assignment.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK

### 32.16 Get Transport Statistics
- **Method:** GET
- **URL:** /api/transport/statistics
- **Description:** Retrieves transport statistics.
- **Response:** 200 OK - Transport statistics object

---

## 33. Hostel

**Base URL:** /api/hostel

### 33.1 Create Hostel
- **Method:** POST
- **URL:** /api/hostel/admin/hostels
- **Description:** Creates a new hostel.
- **Request Body:** HostelDTO (name, type, capacity, warden, location)
- **Response:** 201 Created - Hostel object

### 33.2 Get Hostel by ID
- **Method:** GET
- **URL:** /api/hostel/hostels/{id}
- **Description:** Retrieves a hostel by ID.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - Hostel object

### 33.3 Get All Hostels
- **Method:** GET
- **URL:** /api/hostel/hostels
- **Description:** Retrieves all hostels.
- **Response:** 200 OK - List of Hostel objects

### 33.4 Update Hostel
- **Method:** PUT
- **URL:** /api/hostel/admin/hostels/{id}
- **Description:** Updates a hostel.
- **Path Parameters:** id (String, required)
- **Request Body:** HostelDTO object
- **Response:** 200 OK - Hostel object

### 33.5 Add Room
- **Method:** POST
- **URL:** /api/hostel/admin/rooms
- **Description:** Adds a new room to a hostel.
- **Request Body:** HostelRoomDTO (hostelId, roomNumber, capacity, type, floor)
- **Response:** 201 Created - HostelRoom object

### 33.6 Get Rooms by Hostel
- **Method:** GET
- **URL:** /api/hostel/rooms/hostel/{hostelId}
- **Description:** Retrieves all rooms in a hostel.
- **Path Parameters:** hostelId (String, required)
- **Response:** 200 OK - List of HostelRoom objects

### 33.7 Get Available Rooms
- **Method:** GET
- **URL:** /api/hostel/rooms/available
- **Description:** Retrieves all available rooms.
- **Response:** 200 OK - List of HostelRoom objects

### 33.8 Update Room
- **Method:** PUT
- **URL:** /api/hostel/admin/rooms/{id}
- **Description:** Updates a room.
- **Path Parameters:** id (String, required)
- **Request Body:** HostelRoomDTO object
- **Response:** 200 OK - HostelRoom object

### 33.9 Allocate Student
- **Method:** POST
- **URL:** /api/hostel/allocations
- **Description:** Allocates a student to a hostel room.
- **Request Body:** HostelAllocationDTO (studentId, hostelId, roomId, academicYear)
- **Response:** 201 Created - HostelAllocation object

### 33.10 Get Allocation by Student
- **Method:** GET
- **URL:** /api/hostel/allocations/student/{studentId}
- **Description:** Retrieves hostel allocation for a student.
- **Path Parameters:** studentId (String, required)
- **Response:** 200 OK - HostelAllocation object

### 33.11 Get Allocations by Hostel
- **Method:** GET
- **URL:** /api/hostel/allocations/hostel/{hostelId}
- **Description:** Retrieves all allocations for a hostel.
- **Path Parameters:** hostelId (String, required)
- **Response:** 200 OK - List of HostelAllocation objects

### 33.12 Vacate Student
- **Method:** PATCH
- **URL:** /api/hostel/allocations/{id}/vacate
- **Description:** Vacates a student from their room.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - HostelAllocation object

### 33.13 Transfer Student
- **Method:** PATCH
- **URL:** /api/hostel/allocations/{id}/transfer
- **Description:** Transfers a student to a different room.
- **Path Parameters:** id (String, required)
- **Request Body:** HostelAllocationDTO (new room details)
- **Response:** 200 OK - HostelAllocation object

### 33.14 Create Maintenance Request
- **Method:** POST
- **URL:** /api/hostel/maintenance
- **Description:** Creates a maintenance request for a hostel.
- **Request Body:** MaintenanceRequestDTO (hostelId, roomId, description, priority, reportedBy)
- **Response:** 201 Created - MaintenanceRequest object

### 33.15 Get Maintenance by Hostel
- **Method:** GET
- **URL:** /api/hostel/maintenance/hostel/{hostelId}
- **Description:** Retrieves maintenance requests for a hostel.
- **Path Parameters:** hostelId (String, required)
- **Response:** 200 OK - List of MaintenanceRequest objects

### 33.16 Update Maintenance Status
- **Method:** PATCH
- **URL:** /api/hostel/maintenance/{id}/status
- **Description:** Updates the status of a maintenance request.
- **Path Parameters:** id (String, required)
- **Request Body/Query:** Status value
- **Response:** 200 OK - MaintenanceRequest object

### 33.17 Get Hostel Statistics
- **Method:** GET
- **URL:** /api/hostel/statistics
- **Description:** Retrieves hostel statistics.
- **Response:** 200 OK - Hostel statistics object

---

## 34. Inventory

**Base URL:** /api/inventory

### 34.1 Add Asset
- **Method:** POST
- **URL:** /api/inventory/assets
- **Description:** Adds a new asset.
- **Request Body:** AssetDTO (name, category, departmentId, purchaseDate, purchasePrice, condition, location)
- **Response:** 201 Created - Asset object

### 34.2 Get Asset by ID
- **Method:** GET
- **URL:** /api/inventory/assets/{id}
- **Description:** Retrieves an asset by ID.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - Asset object

### 34.3 Get All Assets
- **Method:** GET
- **URL:** /api/inventory/assets
- **Description:** Retrieves all assets with pagination.
- **Query Parameters:** page (int, default 0), size (int, default 10)
- **Response:** 200 OK - Page of Asset objects

### 34.4 Get Assets by Department
- **Method:** GET
- **URL:** /api/inventory/assets/department/{departmentId}
- **Description:** Retrieves assets for a department.
- **Path Parameters:** departmentId (String, required)
- **Response:** 200 OK - List of Asset objects

### 34.5 Get Assets by Category
- **Method:** GET
- **URL:** /api/inventory/assets/category/{category}
- **Description:** Retrieves assets by category.
- **Path Parameters:** category (String, required)
- **Response:** 200 OK - List of Asset objects

### 34.6 Update Asset
- **Method:** PUT
- **URL:** /api/inventory/assets/{id}
- **Description:** Updates an asset.
- **Path Parameters:** id (String, required)
- **Request Body:** AssetDTO object
- **Response:** 200 OK - Asset object

### 34.7 Dispose Asset
- **Method:** PATCH
- **URL:** /api/inventory/assets/{id}/dispose
- **Description:** Marks an asset as disposed.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - Asset object

### 34.8 Add Inventory Item
- **Method:** POST
- **URL:** /api/inventory/items
- **Description:** Adds a new inventory item.
- **Request Body:** InventoryItemDTO (name, category, departmentId, quantity, minimumStock, unitPrice)
- **Response:** 201 Created - InventoryItem object

### 34.9 Get Item by ID
- **Method:** GET
- **URL:** /api/inventory/items/{id}
- **Description:** Retrieves an inventory item by ID.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - InventoryItem object

### 34.10 Get All Items
- **Method:** GET
- **URL:** /api/inventory/items
- **Description:** Retrieves all inventory items with pagination.
- **Query Parameters:** page (int, default 0), size (int, default 10)
- **Response:** 200 OK - Page of InventoryItem objects

### 34.11 Get Items by Department
- **Method:** GET
- **URL:** /api/inventory/items/department/{departmentId}
- **Description:** Retrieves inventory items for a department.
- **Path Parameters:** departmentId (String, required)
- **Response:** 200 OK - List of InventoryItem objects

### 34.12 Get Low Stock Items
- **Method:** GET
- **URL:** /api/inventory/items/low-stock
- **Description:** Retrieves items with stock below minimum threshold.
- **Response:** 200 OK - List of InventoryItem objects

### 34.13 Update Item
- **Method:** PUT
- **URL:** /api/inventory/items/{id}
- **Description:** Updates an inventory item.
- **Path Parameters:** id (String, required)
- **Request Body:** InventoryItemDTO object
- **Response:** 200 OK - InventoryItem object

### 34.14 Record Transaction
- **Method:** POST
- **URL:** /api/inventory/transactions
- **Description:** Records an inventory transaction (issue/receive/return).
- **Request Body:** InventoryTransactionDTO (itemId, transactionType, quantity, departmentId, performedBy, notes)
- **Response:** 201 Created - InventoryTransaction object

### 34.15 Get Transactions by Item
- **Method:** GET
- **URL:** /api/inventory/transactions/item/{itemId}
- **Description:** Retrieves transactions for an inventory item.
- **Path Parameters:** itemId (String, required)
- **Response:** 200 OK - List of InventoryTransaction objects

### 34.16 Get Transactions by Department
- **Method:** GET
- **URL:** /api/inventory/transactions/department/{departmentId}
- **Description:** Retrieves inventory transactions for a department.
- **Path Parameters:** departmentId (String, required)
- **Response:** 200 OK - List of InventoryTransaction objects

### 34.17 Get Inventory Statistics
- **Method:** GET
- **URL:** /api/inventory/statistics
- **Description:** Retrieves inventory statistics.
- **Response:** 200 OK - Inventory statistics object

---

## 35. Analytics

**Base URL:** /api/analytics

### 35.1 Get Student Performance
- **Method:** GET
- **URL:** /api/analytics/student/{studentId}/performance
- **Description:** Retrieves performance analytics for a student.
- **Path Parameters:** studentId (String, required)
- **Response:** 200 OK - Student performance analytics object

### 35.2 Get Department Analytics
- **Method:** GET
- **URL:** /api/analytics/department/{departmentId}
- **Description:** Retrieves analytics for a department.
- **Path Parameters:** departmentId (String, required)
- **Query Parameters:** academicYear (String, required)
- **Response:** 200 OK - Department analytics object

### 35.3 Get Fee Analytics
- **Method:** GET
- **URL:** /api/analytics/fees
- **Description:** Retrieves fee collection analytics.
- **Query Parameters:** academicYear (String, required)
- **Response:** 200 OK - Fee analytics object

### 35.4 Get Attendance Trend
- **Method:** GET
- **URL:** /api/analytics/attendance-trend
- **Description:** Retrieves attendance trend data.
- **Query Parameters:** departmentId (String, required), academicYear (String, required), semester (String, required)
- **Response:** 200 OK - Attendance trend data

### 35.5 Get Enrollment Trend
- **Method:** GET
- **URL:** /api/analytics/enrollment-trend
- **Description:** Retrieves enrollment trend data.
- **Query Parameters:** academicYear (String, required)
- **Response:** 200 OK - Enrollment trend data

### 35.6 Get Exam Analysis
- **Method:** GET
- **URL:** /api/analytics/exam/{examId}/analysis
- **Description:** Retrieves analysis for a specific exam.
- **Path Parameters:** examId (String, required)
- **Response:** 200 OK - Exam analysis object

### 35.7 Get Dashboard Statistics
- **Method:** GET
- **URL:** /api/analytics/dashboard
- **Description:** Retrieves overall dashboard statistics.
- **Response:** 200 OK - Dashboard statistics object

### 35.8 Generate Report
- **Method:** POST
- **URL:** /api/analytics/reports/generate
- **Description:** Generates a custom analytics report.
- **Request Body:** AnalyticsRequestDTO (reportType, parameters, dateRange)
- **Response:** 201 Created - Report object

### 35.9 Get Report by ID
- **Method:** GET
- **URL:** /api/analytics/reports/{id}
- **Description:** Retrieves a generated report by ID.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - Report object

### 35.10 Get Reports by Type
- **Method:** GET
- **URL:** /api/analytics/reports/type/{reportType}
- **Description:** Retrieves reports filtered by type with pagination.
- **Path Parameters:** reportType (String, required)
- **Query Parameters:** page (int, default 0), size (int, default 10)
- **Response:** 200 OK - Page of Report objects

---

## 36. Audit Logging

**Base URL:** /api/audit

### 36.1 Get Audit Logs by Entity
- **Method:** GET
- **URL:** /api/audit/entity/{entityType}/{entityId}
- **Description:** Retrieves audit logs for a specific entity type and ID.
- **Path Parameters:** entityType (String, required), entityId (String, required)
- **Response:** 200 OK - List of AuditLogDTO objects

### 36.2 Get Audit Logs by User
- **Method:** GET
- **URL:** /api/audit/user/{performedBy}
- **Description:** Retrieves audit logs for actions performed by a specific user.
- **Path Parameters:** performedBy (String, required)
- **Query Parameters:** page (int, default 0), size (int, default 10)
- **Response:** 200 OK - Page of AuditLogDTO objects

### 36.3 Get Audit Logs by Action
- **Method:** GET
- **URL:** /api/audit/action/{action}
- **Description:** Retrieves audit logs filtered by action type.
- **Path Parameters:** action (String, required - CREATE/READ/UPDATE/DELETE/LOGIN/LOGOUT/EXPORT/IMPORT/STATUS_CHANGE)
- **Query Parameters:** page (int, default 0), size (int, default 10)
- **Response:** 200 OK - Page of AuditLogDTO objects

### 36.4 Get Audit Logs by Date Range
- **Method:** GET
- **URL:** /api/audit/date-range
- **Description:** Retrieves audit logs within a date range.
- **Query Parameters:** startDate (LocalDateTime, required - ISO format), endDate (LocalDateTime, required - ISO format), page (int, default 0), size (int, default 10)
- **Response:** 200 OK - Page of AuditLogDTO objects

### 36.5 Search Audit Logs
- **Method:** POST
- **URL:** /api/audit/search
- **Description:** Searches audit logs using multiple criteria.
- **Request Body:** AuditSearchDTO (entityType, entityId, performedBy, action, startDate, endDate)
- **Query Parameters:** page (int, default 0), size (int, default 10)
- **Response:** 200 OK - Page of AuditLogDTO objects

### 36.6 Get Recent Activity
- **Method:** GET
- **URL:** /api/audit/recent
- **Description:** Retrieves the most recent audit log entries.
- **Query Parameters:** limit (int, default 20)
- **Response:** 200 OK - List of AuditLogDTO objects

---

## 37. Announcements

**Base URL:** /api/announcements

### 37.1 Create Announcement
- **Method:** POST
- **URL:** /api/announcements
- **Description:** Creates a new announcement.
- **Request Body:** AnnouncementDTO (title, content, type, priority, targetAudience, departmentId, pinned, expiryDate)
- **Response:** 201 Created - Announcement object

### 37.2 Get Announcement by ID
- **Method:** GET
- **URL:** /api/announcements/{id}
- **Description:** Retrieves an announcement by ID.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - Announcement object

### 37.3 Get Active Announcements
- **Method:** GET
- **URL:** /api/announcements/active
- **Description:** Retrieves all active announcements with pagination.
- **Query Parameters:** page (int, default 0), size (int, default 10)
- **Response:** 200 OK - Page of Announcement objects

### 37.4 Get Announcements by Type
- **Method:** GET
- **URL:** /api/announcements/type/{type}
- **Description:** Retrieves announcements by type (GENERAL/ACADEMIC/ADMINISTRATIVE/EMERGENCY/EVENT).
- **Path Parameters:** type (String, required)
- **Query Parameters:** page (int, default 0), size (int, default 10)
- **Response:** 200 OK - Page of Announcement objects

### 37.5 Get Announcements by Audience
- **Method:** GET
- **URL:** /api/announcements/audience/{audience}
- **Description:** Retrieves announcements by target audience (ALL/STUDENTS/TEACHERS/PARENTS/STAFF/DEPARTMENT).
- **Path Parameters:** audience (String, required)
- **Query Parameters:** page (int, default 0), size (int, default 10)
- **Response:** 200 OK - Page of Announcement objects

### 37.6 Get Pinned Announcements
- **Method:** GET
- **URL:** /api/announcements/pinned
- **Description:** Retrieves all active pinned announcements.
- **Response:** 200 OK - List of Announcement objects

### 37.7 Update Announcement
- **Method:** PUT
- **URL:** /api/announcements/{id}
- **Description:** Updates an existing announcement.
- **Path Parameters:** id (String, required)
- **Request Body:** AnnouncementDTO object
- **Response:** 200 OK - Announcement object

### 37.8 Deactivate Announcement
- **Method:** PATCH
- **URL:** /api/announcements/{id}/deactivate
- **Description:** Deactivates an announcement.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - Announcement object

---

## 38. Messaging

**Base URL:** /api/chat

### 38.1 Send Message
- **Method:** POST
- **URL:** /api/chat
- **Description:** Sends a new message from one user to another.
- **Request Body:** MessageDTO (senderId, receiverId, subject, content, conversationId)
- **Response:** 201 Created - Message object

### 38.2 Get Inbox
- **Method:** GET
- **URL:** /api/chat/inbox/{userId}
- **Description:** Retrieves inbox messages for a user.
- **Path Parameters:** userId (String, required)
- **Query Parameters:** page (int, default 0), size (int, default 10)
- **Response:** 200 OK - Page of Message objects

### 38.3 Get Sent Messages
- **Method:** GET
- **URL:** /api/chat/sent/{userId}
- **Description:** Retrieves sent messages for a user.
- **Path Parameters:** userId (String, required)
- **Query Parameters:** page (int, default 0), size (int, default 10)
- **Response:** 200 OK - Page of Message objects

### 38.4 Get Conversation
- **Method:** GET
- **URL:** /api/chat/conversation/{conversationId}
- **Description:** Retrieves messages in a conversation.
- **Path Parameters:** conversationId (String, required)
- **Query Parameters:** page (int, default 0), size (int, default 50)
- **Response:** 200 OK - Page of Message objects

### 38.5 Mark Message as Read
- **Method:** PATCH
- **URL:** /api/chat/{messageId}/read
- **Description:** Marks a message as read.
- **Path Parameters:** messageId (String, required)
- **Response:** 200 OK - Message object

### 38.6 Mark All Messages as Read
- **Method:** PATCH
- **URL:** /api/chat/read-all/{userId}
- **Description:** Marks all unread messages as read for a user.
- **Path Parameters:** userId (String, required)
- **Response:** 200 OK - Confirmation message

### 38.7 Get Unread Message Count
- **Method:** GET
- **URL:** /api/chat/unread-count/{userId}
- **Description:** Gets the count of unread messages for a user.
- **Path Parameters:** userId (String, required)
- **Response:** 200 OK - Object with unreadCount (number)

### 38.8 Delete Message
- **Method:** DELETE
- **URL:** /api/chat/{messageId}/{userId}
- **Description:** Soft-deletes a message for a user. Permanently deleted when both parties have deleted it.
- **Path Parameters:** messageId (String, required), userId (String, required)
- **Response:** 200 OK - Confirmation message

### 38.9 Get User Conversations
- **Method:** GET
- **URL:** /api/chat/conversations/{userId}
- **Description:** Retrieves all conversations for a user with last message and unread count.
- **Path Parameters:** userId (String, required)
- **Response:** 200 OK - List of ConversationDTO objects

---

## 39. Class and Section Management

**Base URL:** /api/classes

### 39.1 Create Class Section
- **Method:** POST
- **URL:** /api/classes
- **Description:** Creates a new class section.
- **Request Body:** ClassSectionDTO (className, section, departmentId, academicYear, classTeacherId, maxCapacity)
- **Response:** 201 Created - ClassSection object

### 39.2 Get Class by ID
- **Method:** GET
- **URL:** /api/classes/{id}
- **Description:** Retrieves a class section by ID.
- **Path Parameters:** id (String, required)
- **Response:** 200 OK - ClassSection object

### 39.3 Get Classes by Department
- **Method:** GET
- **URL:** /api/classes/department/{departmentId}
- **Description:** Retrieves classes for a department and academic year.
- **Path Parameters:** departmentId (String, required)
- **Query Parameters:** academicYear (String, required)
- **Response:** 200 OK - List of ClassSection objects

### 39.4 Get Classes by Teacher
- **Method:** GET
- **URL:** /api/classes/teacher/{teacherId}
- **Description:** Retrieves classes assigned to a teacher.
- **Path Parameters:** teacherId (String, required)
- **Response:** 200 OK - List of ClassSection objects

### 39.5 Update Class Section
- **Method:** PUT
- **URL:** /api/classes/{id}
- **Description:** Updates a class section.
- **Path Parameters:** id (String, required)
- **Request Body:** ClassSectionDTO object
- **Response:** 200 OK - ClassSection object

### 39.6 Add Student to Class
- **Method:** POST
- **URL:** /api/classes/{classId}/students/{studentId}
- **Description:** Adds a student to a class. Fails if class is at capacity or student already enrolled.
- **Path Parameters:** classId (String, required), studentId (String, required)
- **Response:** 200 OK - ClassSection object

### 39.7 Remove Student from Class
- **Method:** DELETE
- **URL:** /api/classes/{classId}/students/{studentId}
- **Description:** Removes a student from a class.
- **Path Parameters:** classId (String, required), studentId (String, required)
- **Response:** 200 OK - ClassSection object

### 39.8 Add Course to Class
- **Method:** POST
- **URL:** /api/classes/{classId}/courses/{courseId}
- **Description:** Assigns a course to a class.
- **Path Parameters:** classId (String, required), courseId (String, required)
- **Response:** 200 OK - ClassSection object

### 39.9 Remove Course from Class
- **Method:** DELETE
- **URL:** /api/classes/{classId}/courses/{courseId}
- **Description:** Removes a course from a class.
- **Path Parameters:** classId (String, required), courseId (String, required)
- **Response:** 200 OK - ClassSection object

### 39.10 Get Class Students
- **Method:** GET
- **URL:** /api/classes/{classId}/students
- **Description:** Retrieves all students in a class.
- **Path Parameters:** classId (String, required)
- **Response:** 200 OK - List of Student objects

### 39.11 Promote Student
- **Method:** POST
- **URL:** /api/classes/promote
- **Description:** Promotes a student from one class to another.
- **Request Body:** ClassPromotionDTO (studentId, fromClassId, toClassId, fromAcademicYear, toAcademicYear)
- **Response:** 201 Created - ClassPromotion object

### 39.12 Bulk Promote Students
- **Method:** POST
- **URL:** /api/classes/promote/bulk
- **Description:** Promotes multiple students at once.
- **Query Parameters:** fromClassId (String, required), toClassId (String, required), toAcademicYear (String, required)
- **Request Body:** List of student IDs
- **Response:** 201 Created - List of ClassPromotion objects

### 39.13 Get Promotion History
- **Method:** GET
- **URL:** /api/classes/promotions/student/{studentId}
- **Description:** Retrieves the promotion history for a student.
- **Path Parameters:** studentId (String, required)
- **Response:** 200 OK - List of ClassPromotion objects

### 39.14 Get Class Statistics
- **Method:** GET
- **URL:** /api/classes/{classId}/statistics
- **Description:** Retrieves statistics for a class including capacity, strength, available slots, and course count.
- **Path Parameters:** classId (String, required)
- **Response:** 200 OK - ClassStatsDTO object
