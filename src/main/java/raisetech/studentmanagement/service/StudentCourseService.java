package raisetech.studentmanagement.service;

import org.springframework.stereotype.Service;
import raisetech.studentmanagement.StudentCourse;
import raisetech.studentmanagement.repository.StudentCourseRepository;

import java.util.List;

@Service
public class StudentCourseService {

  private final StudentCourseRepository studentCourseRepository;

  public StudentCourseService(StudentCourseRepository studentCourseRepository) {
    this.studentCourseRepository = studentCourseRepository;
  }

  public List<StudentCourse> findAllStudents() {
    // 後でRepository（MyBatis）の実装を呼び出す
    return studentCourseRepository.findAll();
  }

  public StudentCourse findById(int id) {
    return studentCourseRepository.findById(id);
  }
}