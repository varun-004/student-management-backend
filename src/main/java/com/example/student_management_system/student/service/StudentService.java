package com.example.student_management_system.student.service;

import com.example.student_management_system.student.dto.StudentDTO;
import com.example.student_management_system.ResponseDTO.StudentResponseDTO;
import com.example.student_management_system.student.entity.Student;
import com.example.student_management_system.student.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


@Service
public class StudentService {

    private final StudentRepository repo;



    public StudentService(StudentRepository repo) {
        this.repo = repo;
    }

    public List<StudentResponseDTO> getAllStudents() {
        return repo.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public StudentResponseDTO saveStudent(StudentDTO dto) {

        Student student = new Student();
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());

        Student saved = repo.save(student);
        return convertToDTO(saved);
    }

    public void deleteStudent(Long id) {
        Student existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        repo.delete(existing);
    }

    public StudentResponseDTO getStudentById(Long id) {

        Student student = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return convertToDTO(student);
    }

    public StudentResponseDTO updateStudent(Long id, StudentDTO dto) {

        Student existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());

        Student updated = repo.save(existing);
        return convertToDTO(updated);
    }


    public StudentResponseDTO convertToDTO(Student student) {
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        return dto;
    }

    public Page<StudentResponseDTO> getStudents(Pageable pageable) {

        return repo.findAll(pageable)
                .map(this::convertToDTO);
    }

    public List<StudentResponseDTO> searchByName(String name) {
        return repo.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public Page<StudentResponseDTO> searchStudents(String name, Pageable pageable) {

        return repo.findByNameContainingIgnoreCase(name, pageable)
                .map(this::convertToDTO);
    }


    public StudentResponseDTO getStudentByEmail(
            String email
    ) {

        Student student =
                repo.findByEmail(email)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Student not found"
                                )
                        );

        return convertToDTO(student);
    }

}