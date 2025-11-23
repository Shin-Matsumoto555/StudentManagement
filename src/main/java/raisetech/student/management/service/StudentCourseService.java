package raisetech.student.management.service;

import org.springframework.stereotype.Service;

import java.util.List;
import raisetech.student.management.data.StudentCourses;
import raisetech.student.management.repository.StudentCourseRepository;

@Service
public class StudentCourseService {

  private final StudentCourseRepository studentCourseRepository;

  public StudentCourseService(StudentCourseRepository studentCourseRepository) {
    this.studentCourseRepository = studentCourseRepository;
  }

  public List<StudentCourses> findAllStudents() {
    // 後でRepository（MyBatis）の実装を呼び出す
    return studentCourseRepository.findAll();
  }

  public StudentCourses findById(String uuid) {
    return studentCourseRepository.findById(uuid);
  }
}