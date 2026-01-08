package raisetech.student.management.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.ApplicationStatus;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;

/**
 * 受講生情報を取り扱うサービスです。 受講生の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

  private final StudentRepository repository;
  private final StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受講生コース情報を登録する際の初期情報を設定する。
   *
   * @param studentCourse 受講生コース情報
   * @param studentUuid   受講生ID
   */
  void initStudentCourse(StudentCourse studentCourse, String studentUuid) {
    LocalDateTime now = LocalDateTime.now();

    studentCourse.setCourseUuid(UUID.randomUUID().toString());      // 各コース行の UUID を生成
    studentCourse.setStudentUuid(studentUuid);
    studentCourse.setStartDate(now);
    studentCourse.setEndDate(now.plusYears(1));
  }

  /**
   * 申込状況情報を登録する際の初期情報を設定する。
   *
   * @param applicationStatus 申込状況情報
   * @param courseUuid        受講生コースID
   */
  void initApplicationStatus(ApplicationStatus applicationStatus, String courseUuid) {
    applicationStatus.setStatusUuid(UUID.randomUUID().toString()); // 状況IDを生成
    applicationStatus.setCourseUuid(courseUuid);                   // どのコースの状況か紐付け
    applicationStatus.setStatus("仮申込");                          // 初期ステータスを「仮申込」に設定
  }

  /**
   * 受講生詳細の一覧検索です。 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講生詳細一覧（全件）
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.search();
    List<StudentCourse> studentCourseList = repository.searchStudentCourseList();

    // ↓ 全件検索の時も「申込状況」を取ってくるようにします。
    List<ApplicationStatus> applicationStatusList = repository.searchApplicationStatusList();

    return converter.convertStudentDetails(studentList, studentCourseList, applicationStatusList);
  }

  /**
   * 受講生詳細検索です。 IDに紐づく受講生情報を取得したあと、その受講生に紐づく受講生コース情報を取得して設定します。
   *
   * @param studentUuid 受講生ID
   * @return 受講生詳細
   */
  public StudentDetail searchStudent(String studentUuid) {
    Student student = repository.searchStudent(studentUuid);

    // 存在しない学生を検索した場合の例外処理
    if (student == null) {
      throw new RuntimeException("該当の学生が存在しません: " + studentUuid);
    }

    List<StudentCourse> studentCourseList = repository.searchStudentCourse(
        student.getStudentUuid());
    // ↓ 講義44により追加。コースIDを使って「申込状況」をDBから取ってきます。
    List<ApplicationStatus> applicationStatusList = studentCourseList.stream()
        .flatMap(course -> repository.searchApplicationStatus(course.getCourseUuid()).stream())
        .toList();

    // 最後、戻り値の StudentDetail に applicationStatusList も渡すように書き換えます。
    return new StudentDetail(student, studentCourseList, applicationStatusList);
  }

  /**
   * 条件を指定して受講生詳細の一覧を検索します。 受講生情報、コース情報、申込状況のいずれかの条件に合致する受講生を取得し、紐づく情報を設定します。
   *
   * @param student           受講生情報の検索条件
   * @param studentCourse     受講生コース情報の検索条件
   * @param applicationStatus 申込状況の検索条件
   * @return 受講生詳細一覧
   */
  public List<StudentDetail> searchStudentList(Student student, StudentCourse studentCourse,
      ApplicationStatus applicationStatus) {
    // 1. Repositoryに新設した searchByConditions を呼び、条件に合う「生徒」だけを特定する
    List<Student> studentList = repository.searchByConditions(student, studentCourse,
        applicationStatus);

    // 2. あとは既存の「がっちゃんこ」の仕組み（Converter）をそのまま使って詳細を組み立てる
    List<StudentCourse> studentCourseList = repository.searchStudentCourseList();
    List<ApplicationStatus> applicationStatusList = repository.searchApplicationStatusList();

    return converter.convertStudentDetails(studentList, studentCourseList, applicationStatusList);
  }

  /**
   * 受講生詳細の登録を行います。 受講生と受講生コース情報を個別に登録し、受講生コース情報には受講生情報を紐づける値とコース開始日・終了日を設定します。
   *
   * @param studentDetail 受講生詳細
   * @return 登録情報を付与した受講生情報
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();    // Student オブジェクトを変数に入れる（準備1）
    student.setStudentUuid(UUID.randomUUID().toString());    // UUID を生成してセット（準備2）

    repository.registerStudent(student);    // ここから DB に保存する処理（やりたいこと）

    studentDetail.getStudentCourseList().forEach(studentCourse -> {      // コース情報登録も行う
      initStudentCourse(studentCourse, student.getStudentUuid());
      repository.registerStudentCourse(studentCourse);

      // 課題44により追加。引数の studentDetail から status を取り出す
      ApplicationStatus status = studentDetail.getApplicationStatusList().get(0);

      initApplicationStatus(status, studentCourse.getCourseUuid());
      repository.registerApplicationStatus(status);
    });
    return studentDetail;
  }

  /**
   * 受講生詳細の更新を行います。 受講生と受講生コース情報をそれぞれ更新します。
   *
   * @param studentDetail 受講生詳細
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent()); // Student の更新
    studentDetail.getStudentCourseList()
        .forEach(repository::updateStudentCourse); // コースの更新（今は uuid が存在する前提の最小構成）
    // repository:: ではなく this:: に書き換える！
    studentDetail.getApplicationStatusList().forEach(this::updateApplicationStatus);
  }
  
  /**
   * 申込状況の更新を行います。 DBに対象のコースIDのステータスが存在しない場合は、新規登録(INSERT)を行います。
   *
   * @param applicationStatus 申込状況
   */
  @Transactional
  public void updateApplicationStatus(ApplicationStatus applicationStatus) {
    // 1. まず現在のステータスをDBから探す
    List<ApplicationStatus> statusList = repository.searchApplicationStatus(
        applicationStatus.getCourseUuid());

    if (statusList.isEmpty()) {
      // 2. DBにまだレコードがない場合は、新規登録(INSERT)
      applicationStatus.setStatusUuid(UUID.randomUUID().toString()); // UUIDを新しく作る
      repository.registerApplicationStatus(applicationStatus);
    } else {
      // 3. すでにDBにレコードがある場合は、更新(UPDATE)
      repository.updateApplicationStatus(applicationStatus);
    }
  }
}
