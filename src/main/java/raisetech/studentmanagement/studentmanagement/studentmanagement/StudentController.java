package raisetech.studentmanagement;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.studentmanagement.service.StudentService;
import raisetech.studentmanagement.model.Student;

import java.util.List;

@RestController
public class StudentController {

  private final StudentService studentService;

  // StudentServiceをインジェクション
  public StudentController(StudentService studentService) {
    this.studentService = studentService;
  }

  // /students のリクエストを処理し、StudentServiceからデータを取得して返す
  @GetMapping("/students")
  public List<Student> findAll() {
    return studentService.findAll();
  }
}