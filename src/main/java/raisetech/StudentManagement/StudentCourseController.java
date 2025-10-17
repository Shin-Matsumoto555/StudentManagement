package raisetech.studentmanagement;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.studentmanagement.service.StudentCourseService;

import java.util.List;

@RestController
public class StudentCourseController {

  private final StudentCourseService studentCourseService;

  public StudentCourseController(StudentCourseService studentCourseService) {
    this.studentCourseService = studentCourseService;
  }

  // Read (参照) - 全件取得のAPIエンドポイント
  @GetMapping("/students")
  public List<StudentCourse> getAllStudents() {
    // Service経由でデータを取得
    return studentCourseService.findAllStudents();
  }
}