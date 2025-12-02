package raisetech.student.management.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourses;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentService;

@RestController
public class StudentController {

  private final StudentService service;
  private final StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    List<Student> students = service.searchStudentList();
    List<StudentCourses> studentCourses = service.searchStudentCourseList();
    return converter.convertStudentDetails(students, studentCourses);
  }

  // Uuidから個人検索
  @GetMapping("/student/{studentUuid}")
  public StudentDetail getStudent(@PathVariable String studentUuid) {
    StudentDetail studentDetail = service.searchStudent(studentUuid);
    return service.searchStudent(studentUuid);
  }

  @GetMapping("/studentCourseList")
  public List<StudentCourses> getStudentCourseList() {
    return service.searchStudentCourseList();
  }

  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudentCourses(List.of(new StudentCourses()));
    model.addAttribute("studentDetail", studentDetail);
    return "registerStudent";
  }

  // 新規受講生情報を登録する処理を実装する。
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(@RequestBody StudentDetail studentDetail) {
    // Service に渡して DB に登録
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }
// ============================================================
// 🔽 編集画面表示
// ============================================================
  // 先生のコードはこう
  // @GetMapping("/student/{id")
  // public String getStudent(@PathVariable String id, Model model) {
  //   StudentDetail studentDetail = service.searchStudent(id);
  //   model.addAttribute("studentDetail", studentDetail);
  //   return "updateStudent";
  // }

  @GetMapping("/editStudent")
  public String editStudent(@RequestParam("uuid") String uuid, Model model) {
    // 全件取得（WHERE なし想定）
    List<Student> students = service.searchStudentList();
    List<StudentCourses> studentCourses = service.searchStudentCourseList();
    // 対象 Student 抽出
    Student target = students.stream()
        .filter(s -> uuid.equals(s.getStudentUuid()))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Student not found"));
    // 対象のコース抽出
    List<StudentCourses> targetCourses = studentCourses.stream()
        .filter(c -> uuid.equals(c.getStudentUuid()))
        .collect(Collectors.toList());

    // StudentDetail に詰める
    StudentDetail detail = new StudentDetail();
    detail.setStudent(target);
    detail.setStudentCourses(targetCourses);
    // 画面に渡す
    model.addAttribute("studentDetail", detail);
    return "updateStudent"; // ← 更新画面
  }

  // ============================================================
// 🔽 更新処理
// ============================================================
  @PostMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }
}