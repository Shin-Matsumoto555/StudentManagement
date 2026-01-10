package raisetech.student.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.ApplicationStatus;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    // 事前準備
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    List<ApplicationStatus> statusList = new ArrayList<>();
    Mockito.when(repository.search()).thenReturn(studentList);
    Mockito.when(repository.searchStudentCourseList()).thenReturn(studentCourseList);
    Mockito.when(repository.searchApplicationStatusList()).thenReturn(statusList);
    // 実行
    sut.searchStudentList();
    // 検証
    Mockito.verify(repository, times(1)).search();
    Mockito.verify(repository, times(1)).searchStudentCourseList();
    Mockito.verify(converter, times(1))
        .convertStudentDetails(studentList, studentCourseList, statusList);
    // 後処理
    // ここでDBを元に戻す

  }

  @Test
  void 受講生詳細の検索_該当する学生が存在する場合_学生とコースが返る() {
    // 事前準備
    String studentUuid = "test-uuid";
    Student student = new Student();
    student.setStudentUuid(studentUuid);
    List<StudentCourse> courseList = new ArrayList<>();
    Mockito.when(repository.searchStudent(studentUuid)).thenReturn(student);
    Mockito.when(repository.searchStudentCourse(studentUuid)).thenReturn(courseList);
    // 実行
    StudentDetail actual = sut.searchStudent(studentUuid);
    // 検証
    assertEquals(student, actual.getStudent());
    assertEquals(courseList, actual.getStudentCourseList());
  }

  @Test
  void 受講生詳細の検索_学生が存在しない場合_例外が発生する() {
    // 事前準備
    String studentUuid = "not-exist";
    Mockito.when(repository.searchStudent(studentUuid)).thenReturn(null);
    // 実行 + 検証
    assertThrows(RuntimeException.class, () -> sut.searchStudent(studentUuid));
  }

  @Test
  void 受講生コース初期化_受講生IDと日時が正しく設定されること() {
    // 準備
    String studentUuid = "test-uuid";
    StudentCourse studentCourse = new StudentCourse();
    // 実行
    sut.initStudentCourse(studentCourse, studentUuid);
    // 検証
    assertEquals(studentUuid, studentCourse.getStudentUuid());
    assertNotNull(studentCourse.getCourseUuid());
    assertEquals(LocalDateTime.now().getHour(),
        studentCourse.getStartDate().getHour());
    assertEquals(LocalDateTime.now().plusYears(1).getYear(),
        studentCourse.getEndDate().getYear()
    );
  }

  @Test
  void 受講生登録_repositoryの登録処理が呼ばれる() {
    // 事前準備
    Student student = new Student();
    StudentCourse course = new StudentCourse();
    List<StudentCourse> courseList = new ArrayList<>();
    courseList.add(course);

    // ↓ここから追加（ApplicationStatusの準備）
    ApplicationStatus status = new ApplicationStatus();
    List<ApplicationStatus> statusList = new ArrayList<>();
    statusList.add(status);

    StudentDetail studentDetail = new StudentDetail(student, courseList, statusList);

    // 実行
    StudentDetail actual = sut.registerStudent(studentDetail);
    // 検証
    Mockito.verify(repository, times(1)).registerStudent(student);
    Mockito.verify(repository, times(1)).registerStudentCourse(course);
    // ↓ここから追加（申込状況登録の検証）
    // 修正後(verify の引数を any() に差し替え。住所の一致（@...）を無視させます。)
    Mockito.verify(repository, times(1)).registerApplicationStatus(any(ApplicationStatus.class));

    assertEquals(studentDetail, actual);
  }

  @Test
  void 受講生更新_repositoryの更新処理が呼ばれる() {
    // 事前準備
    Student student = new Student();
    StudentCourse course = new StudentCourse();
    List<StudentCourse> courseList = new ArrayList<>();
    courseList.add(course);

    // 申込状況も空ではなく、具体的なデータとして用意する
    ApplicationStatus status = new ApplicationStatus();
    List<ApplicationStatus> statusList = new ArrayList<>();
    statusList.add(status);

    StudentDetail studentDetail = new StudentDetail(student, courseList, statusList);

    // ★【変更箇所1】Serviceがupdate側に進むように「データがある状況」を作る
    Mockito.when(repository.searchApplicationStatus(Mockito.any()))
        .thenReturn(List.of(new ApplicationStatus()));

    // 実行
    sut.updateStudent(studentDetail);
    // 検証
    Mockito.verify(repository, times(1)).updateStudent(student);
    Mockito.verify(repository, times(1)).updateStudentCourse(course);
    // ★【変更箇所2】registerではなく、正しく「update」が1回呼ばれることを検証する
    Mockito.verify(repository, Mockito.times(1)).updateApplicationStatus(Mockito.any());
  }

  @Test
  void 受講生一覧検索_条件指定あり_repositoryの検索処理が呼ばれる() {
    // 準備：検索条件となるオブジェクトを用意
    Student student = new Student();
    StudentCourse course = new StudentCourse();
    ApplicationStatus status = new ApplicationStatus();

    // Repositoryが返すダミーの生徒リスト
    List<Student> studentList = new ArrayList<>();
    studentList.add(student);

    // Mockの振る舞い設定
    Mockito.when(repository.searchByConditions(student, course, status)).thenReturn(studentList);
    Mockito.when(repository.searchStudentCourseList()).thenReturn(new ArrayList<>());
    Mockito.when(repository.searchApplicationStatusList()).thenReturn(new ArrayList<>());

    // 実行
    sut.searchStudentList(student, course, status);

    // 検証：条件を保持したままリポジトリの検索メソッドが呼ばれたか
    Mockito.verify(repository, times(1)).searchByConditions(student, course, status);
  }

}