package com.example.student_management_system.analytics.service;

import com.example.student_management_system.analytics.dto.CourseStatsDTO;
import com.example.student_management_system.analytics.dto.DashboardResponseDTO;
import com.example.student_management_system.analytics.dto.StudentRiskDTO;
import com.example.student_management_system.analytics.util.RiskAnalysisUtil;
import com.example.student_management_system.attendance.repository.AttendanceRepository;
import com.example.student_management_system.enrollment.repository.EnrollmentRepository;
import com.example.student_management_system.marks.repository.MarksRepository;
import com.example.student_management_system.student.entity.Student;
import com.example.student_management_system.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final StudentRepository studentRepository;

    private final AttendanceRepository attendanceRepository;

    private final MarksRepository marksRepository;

    private final EnrollmentRepository enrollmentRepository;

    @Override
    public DashboardResponseDTO getDashboardAnalytics() {

        long totalStudents =
                studentRepository.getTotalStudents();

        Double averageAttendance = 0.0;

        Double averageMarks =
                marksRepository.getAverageMarks();

        List<CourseStatsDTO> topCourses =
                enrollmentRepository
                        .getTopCourses(PageRequest.of(0, 5))
                        .stream()
                        .map(obj -> new CourseStatsDTO(
                                (String) obj[0],
                                (Long) obj[1]
                        ))
                        .toList();

        return new DashboardResponseDTO(
                totalStudents,
                averageAttendance != null
                        ? averageAttendance
                        : 0,

                averageMarks != null
                        ? averageMarks
                        : 0,

                topCourses
        );
    }

    @Override
    public List<StudentRiskDTO> getStudentRisks() {

        List<Student> students =
                studentRepository.findAll();

        return students.stream()
                .map(student -> {

                    Double attendance = 0.0;

                    Double marks =
                            marksRepository
                                    .getAverageMarksByStudent(
                                            student.getId()
                                    );

                    double attendanceValue =
                            attendance != null
                                    ? attendance
                                    : 0;

                    double marksValue =
                            marks != null
                                    ? marks
                                    : 0;

                    return new StudentRiskDTO(
                            student.getId(),
                            student.getName(),
                            attendanceValue,
                            marksValue,
                            RiskAnalysisUtil.calculateRisk(
                                    attendanceValue,
                                    marksValue
                            )
                    );
                })
                .toList();
    }
}

