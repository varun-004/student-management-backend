package com.example.student_management_system.config;

import com.example.student_management_system.attendance.entity.Attendance;
import com.example.student_management_system.attendance.repository.AttendanceRepository;
import com.example.student_management_system.course.entity.Course;
import com.example.student_management_system.course.repository.CourseRepository;
import com.example.student_management_system.enrollment.entity.Enrollment;
import com.example.student_management_system.enrollment.repository.EnrollmentRepository;
import com.example.student_management_system.entity.Role;
import com.example.student_management_system.entity.User;
import com.example.student_management_system.marks.repository.MarksRepository;
import com.example.student_management_system.repository.UserRepository;
import com.example.student_management_system.student.entity.Student;
import com.example.student_management_system.student.repository.StudentRepository;
import com.example.student_management_system.teacher.entity.Teacher;
import com.example.student_management_system.teacher.repository.TeacherRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import com.example.student_management_system.marks.entity.Marks;
import com.example.student_management_system.marks.repository.MarksRepository;


@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AttendanceRepository attendanceRepository;
    private final MarksRepository marksRepository;

    @Override
    public void run(String... args) throws Exception {

        log.info("====================================");
        log.info("Starting Database Seeder...");
        log.info("====================================");

        // Step 2

        seedTeachers();
        seedCourses();
        seedStudents();
        seedEnrollments();
        seedAttendance();
        seedMarks();


        log.info("====================================");
        log.info("Database Seeder Finished");
        log.info("====================================");
    }

    private void seedTeachers() {

        if (teacherRepository.count() >= 10) {
            log.info("Teachers already exist. Skipping...");
            return;
        }

        String[] firstNames = {
                "Rahul", "Anil", "Kiran", "Suresh", "Vijay",
                "Priya", "Sneha", "Pooja", "Divya", "Neha"
        };

        String[] lastNames = {
                "Sharma", "Patel", "Kumar", "Rao", "Reddy",
                "Singh", "Gupta", "Joshi", "Verma", "Nair"
        };

        String[] departments = {
                "Computer Science",
                "Information Science",
                "Electronics",
                "Mechanical",
                "Civil"
        };

        String[] designations = {
                "Assistant Professor",
                "Associate Professor",
                "Professor"
        };

        for (int i = 1; i <= 10; i++) {

            String email = "teacher" + i + "@sms.com";

            if (userRepository.findByEmail(email).isPresent()) {
                continue;
            }

            User user = new User();
            user.setName(firstNames[i - 1] + " " + lastNames[i - 1]);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode("Teacher@123"));
            user.setRole(Role.TEACHER);

            user = userRepository.save(user);

            Teacher teacher = new Teacher();
            teacher.setEmployeeId("EMP" + String.format("%03d", i));
            teacher.setFirstName(firstNames[i - 1]);
            teacher.setLastName(lastNames[i - 1]);
            teacher.setDepartment(departments[i % departments.length]);
            teacher.setDesignation(designations[i % designations.length]);
            teacher.setUser(user);

            teacherRepository.save(teacher);
        }

        log.info("10 Teachers Seeded Successfully");
    }

    private void seedCourses() {

        if (courseRepository.count() >= 10) {
            log.info("Courses already exist. Skipping...");
            return;
        }

        List<Teacher> teachers = teacherRepository.findAll();

        if (teachers.isEmpty()) {
            log.warn("No teachers found. Cannot seed courses.");
            return;
        }

        String[] courseCodes = {
                "CS101",
                "CS102",
                "CS103",
                "CS104",
                "CS105",
                "CS106",
                "CS107",
                "CS108",
                "CS109",
                "CS110"
        };

        String[] courseNames = {
                "Java Programming",
                "Spring Boot",
                "React JS",
                "Database Systems",
                "Cloud Computing",
                "Operating Systems",
                "Computer Networks",
                "Software Engineering",
                "Data Structures",
                "Machine Learning"
        };

        for (int i = 0; i < 10; i++) {

            if (courseRepository.existsByCourseCode(courseCodes[i])) {
                continue;
            }

            Course course = new Course();

            course.setCourseCode(courseCodes[i]);
            course.setCourseName(courseNames[i]);
            course.setDescription(courseNames[i] + " Course");
            course.setCredits(4);

            course.setTeacher(
                    teachers.get(i % teachers.size())
            );

            courseRepository.save(course);
        }

        log.info("10 Courses Seeded Successfully");
    }

    private void seedStudents() {

        if (studentRepository.count() >= 50) {
            log.info("Students already exist. Skipping...");
            return;
        }

        String[] names = {
                "Aarav","Vivaan","Aditya","Arjun","Krishna",
                "Rohan","Karthik","Nikhil","Varun","Akash",
                "Rahul","Sanjay","Harish","Manoj","Vikas",
                "Sneha","Pooja","Divya","Ananya","Priya",
                "Neha","Kavya","Aishwarya","Nandini","Megha",
                "Ritika","Sahana","Deepa","Bhavana","Swathi",
                "Abhishek","Yash","Tejas","Darshan","Lokesh",
                "Manasa","Keerthi","Preethi","Chaitra","Ashwini",
                "Shreya","Rakesh","Praveen","Vinay","Suraj",
                "Mahesh","Girish","Dinesh","Siddarth","Ramesh"
        };

        for (int i = 1; i <= 50; i++) {

            String email = "student" + i + "@sms.com";

            if (studentRepository.existsByEmail(email)) {
                continue;
            }

            User user = new User();
            user.setName(names[i - 1]);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode("Student@123"));
            user.setRole(Role.STUDENT);

            user = userRepository.save(user);

            Student student = new Student();

            student.setName(names[i - 1]);
            student.setEmail(email);

            studentRepository.save(student);
        }

        log.info("50 Students Seeded Successfully");
    }

    private void seedEnrollments() {

        if (enrollmentRepository.count() > 0) {
            log.info("Enrollments already exist. Skipping...");
            return;
        }

        List<Student> students = studentRepository.findAll();
        List<Course> courses = courseRepository.findAll();

        Random random = new Random();

        for (Student student : students) {

            Set<Integer> selectedCourses = new HashSet<>();

            while (selectedCourses.size() < 3) {
                selectedCourses.add(random.nextInt(courses.size()));
            }

            for (Integer index : selectedCourses) {

                Enrollment enrollment = new Enrollment();

                enrollment.setStudent(student);
                enrollment.setCourse(courses.get(index));

                enrollmentRepository.save(enrollment);
            }
        }

        log.info("{} Enrollments Seeded Successfully",
                enrollmentRepository.count());
    }
    private void seedAttendance() {

        if (attendanceRepository.count() > 0) {
            log.info("Attendance already exists. Skipping...");
            return;
        }

        List<Enrollment> enrollments = enrollmentRepository.findAll();

        Random random = new Random();

        LocalDate startDate = LocalDate.now().minusDays(10);

        for (Enrollment enrollment : enrollments) {

            for (int i = 0; i < 10; i++) {

                Attendance attendance = new Attendance();

                attendance.setStudent(enrollment.getStudent());

                attendance.setCourse(enrollment.getCourse());

                attendance.setAttendanceDate(startDate.plusDays(i));

                // 85% Present
                attendance.setPresent(random.nextInt(100) < 85);

                attendanceRepository.save(attendance);
            }
        }

        log.info("{} Attendance Records Seeded Successfully",
                attendanceRepository.count());
    }

    private void seedMarks() {

        if (marksRepository.count() > 0) {
            log.info("Marks already exist. Skipping...");
            return;
        }

        List<Enrollment> enrollments = enrollmentRepository.findAll();

        Random random = new Random();

        for (Enrollment enrollment : enrollments) {

            double score = 40 + (60 * random.nextDouble());

            String grade;

            if (score >= 90)
                grade = "A+";
            else if (score >= 80)
                grade = "A";
            else if (score >= 70)
                grade = "B";
            else if (score >= 60)
                grade = "C";
            else if (score >= 50)
                grade = "D";
            else
                grade = "F";

            Marks marks = new Marks();

            marks.setStudent(enrollment.getStudent());
            marks.setCourse(enrollment.getCourse());

            marks.setSubject(enrollment.getCourse().getCourseName());

            marks.setScore(Math.round(score * 100.0) / 100.0);

            marks.setGrade(grade);

            marksRepository.save(marks);
        }

        log.info("{} Marks Records Seeded Successfully",
                marksRepository.count());
    }
}