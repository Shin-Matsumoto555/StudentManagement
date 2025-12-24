package raisetech.student.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.controller.converter.StudentConverter;
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
    Mockito.when(repository.search()).thenReturn(studentList);
    Mockito.when(repository.searchStudentCourseList()).thenReturn(studentCourseList);
    // 実行
    List<StudentDetail> actual = sut.searchStudentList();
    // 検証
    Mockito.verify(repository, times(1)).search();
    Mockito.verify(repository, times(1)).searchStudentCourseList();
    Mockito.verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);
    // 後処理
    // ここでDBを元に戻す

  }

  @Test
  void 受講生詳細検索_該当する学生が存在する場合_学生とコースが返る() {
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
  void 受講生詳細検索_学生が存在しない場合_例外が発生する() {
    // 事前準備
    String studentUuid = "not-exist";
    Mockito.when(repository.searchStudent(studentUuid)).thenReturn(null);
    // 実行 + 検証
    assertThrows(RuntimeException.class, () -> sut.searchStudent(studentUuid));
  }

  @Test
  void 受講生登録_repositoryの登録処理が呼ばれる() {
    // 事前準備
    Student student = new Student();
    StudentCourse course = new StudentCourse();
    List<StudentCourse> courseList = new ArrayList<>();
    courseList.add(course);
    StudentDetail studentDetail = new StudentDetail(student, courseList);
    // 実行
    StudentDetail actual = sut.registerStudent(studentDetail);
    // 検証
    Mockito.verify(repository, times(1)).registerStudent(student);
    Mockito.verify(repository, times(1)).registerStudentCourse(course);
    assertEquals(studentDetail, actual);
  }

  @Test
  void 受講生更新_repositoryの更新処理が呼ばれる() {
    // 事前準備
    Student student = new Student();
    StudentCourse course = new StudentCourse();
    List<StudentCourse> courseList = new ArrayList<>();
    courseList.add(course);
    StudentDetail studentDetail = new StudentDetail(student, courseList);
    // 実行
    sut.updateStudent(studentDetail);
    // 検証
    Mockito.verify(repository, times(1)).updateStudent(student);
    Mockito.verify(repository, times(1)).updateStudentCourse(course);
  }

}