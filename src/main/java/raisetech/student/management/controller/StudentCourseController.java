package raisetech.student.management.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import raisetech.student.management.data.StudentCourses;
import raisetech.student.management.service.StudentCourseService;

@RestController
public class StudentCourseController {

  private final StudentCourseService studentCourseService;

  public StudentCourseController(StudentCourseService studentCourseService) {
    this.studentCourseService = studentCourseService;
  }

  // Read (参照) - 全件取得のAPIエンドポイント
  @GetMapping("/studentcourses")
  public List<StudentCourses> getAllStudents() {
    // Service経由でデータを取得
    return studentCourseService.findAllStudents();
  }

  // Read (参照) - IDを指定して1件取得のAPIエンドポイント
  @GetMapping("/studentcourses/{uuid}")
  public StudentCourses getStudentById(@PathVariable String uuid) {
    return studentCourseService.findById(uuid);
  }
}