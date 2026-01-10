package raisetech.student.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして受け付けるControllerです。
 */
@Validated
@RestController
public class StudentController {

  private final StudentService service;

  /**
   * 　コンストラクタ
   *
   * @param service 受講生サービス
   */
  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生詳細の一覧検索です。 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講生詳細一覧（全件）
   */
  @Operation(summary = "一覧検索", description = "受講生の一覧を検索します。")
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return service.searchStudentList();
  }

  /**
   * 受講生詳細の検索です。 IDに紐づく任意の受講生の情報を取得します。
   *
   * @param studentUuid 受講生ID
   * @return 受講生
   */
  @GetMapping("/student/{studentUuid}")
  public StudentDetail getStudent(@PathVariable @Size(min = 1, max = 36) String studentUuid) {
    return service.searchStudent(studentUuid);
  }

  /**
   * 【課題44】受講生の条件検索です。 受講生、コース、申込状況の条件に合致する情報を取得します。
   *
   * @return 条件に合致した受講生詳細一覧
   */
  @Operation(summary = "条件検索", description = "条件を指定して受講生を検索します。")
  @GetMapping("/studentSearch")
  public List<StudentDetail> searchStudentList(
      @ModelAttribute Student student,
      @ModelAttribute StudentCourse studentCourse,
      @ModelAttribute raisetech.student.management.data.ApplicationStatus applicationStatus) {
    return service.searchStudentList(student, studentCourse, applicationStatus);
  }

  /**
   * 受講生詳細の登録を行います。
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  // 新規受講生情報を登録する処理を実装する。
  @Operation(summary = "受講生登録", description = "受講生を登録します。")
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(
      @Valid @RequestBody StudentDetail studentDetail) {
    // Service に渡して DB に登録
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生詳細の更新を行います。 キャンセルフラグの更新もここで行います（論理削除）
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@Valid @RequestBody StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }

  /**
   * 申込状況（ステータス）の更新を行います。 「仮申込」「本申込」「受講中」「受講終了」などの状態を変更します。
   *
   * @param applicationStatus 申込状況情報
   * @return 実行結果
   */
  @Operation(summary = "申込状況更新", description = "コースの申込状況（ステータス）を更新します。")
  @PutMapping("/updateApplicationStatus")
  public ResponseEntity<String> updateApplicationStatus(
      @RequestBody raisetech.student.management.data.ApplicationStatus applicationStatus) {
    service.updateApplicationStatus(applicationStatus);
    return ResponseEntity.ok("申込状況の更新が成功しました。");
  }

}