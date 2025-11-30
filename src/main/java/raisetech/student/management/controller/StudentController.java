package raisetech.student.management.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourses;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentService;

@Controller
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentList")
  public String getStudentList(Model model) {
    List<Student> students = service.searchStudentList();
    List<StudentCourses> studentCourses = service.searchStudentCourseList();

    model.addAttribute("studentList", converter.convertStudentDetails(students, studentCourses));
    return "studentList";
  }

  @GetMapping("/studentCourseList")
  public List<StudentCourses> getStudentCourseList() {
    return service.searchStudentCourseList();
  }

  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudentCourses(Arrays.asList(new StudentCourses()));
    model.addAttribute("studentDetail", studentDetail);
    return "registerStudent";
  }

  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "registerStudent";
    }
    // 新規受講生情報を登録する処理を実装する。
    // ＜いらない？＞StudentDetail → Student に変換
    // ＜いらない？＞Student student = converter.convertToStudent(studentDetail);

    // Service に渡して DB に登録
    service.registerStudent(studentDetail);

    return "redirect:/studentList"; // 登録後、一覧ページに戻る
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
  public String updateStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "updateStudent";
    }

    service.updateStudent(studentDetail);

    return "redirect:/studentList";
  }
}