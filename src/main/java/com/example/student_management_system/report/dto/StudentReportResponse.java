git add src/main/java/com/example/student_management_system/analytics/service/AnalyticsServiceImpl.java
git add src/main/java/com/example/student_management_system/attendance/repository/AttendanceRepository.java
git add src/main/java/com/example/student_management_system/marks/repository/MarksRepository.javapackage com.example.student_management_system.report.dto;

import com.example.student_management_system.marks.dto.MarksResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentReportResponse {

    private Long studentId;

    private String studentName;

    private String email;

    private Double attendancePercentage;

    private Double averageMarks;

    private Double gpa;

    private List<MarksResponse> marks;
}