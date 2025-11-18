package raisetech.student.management;

import org.springframework.stereotype.Service;
import raisetech.student.management.StudentCourse;

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

  public StudentCourse findById(String uuid) {
    return studentCourseRepository.findById(uuid);
  }
}