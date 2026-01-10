package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

@MybatisTest
@Transactional
@Rollback
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  // 1. 受講生の全件検索（既存）
  @Test
  void 受講生の全件検索が行えること() {
    List<Student> actual = sut.search();
    assertThat(actual.size()).isEqualTo(5);
  }

  // 2. 受講生の登録（既存）
  @Test
  void 受講生の登録が行えること() {
    Student student = new Student();
    student.setStudentUuid(UUID.randomUUID().toString());
    student.setName("Shinnosuke Matsumoto");
    student.setFuriganaName("シンノスケ マツモト");
    student.setNickname("Shin");
    student.setEmail("shinnosuke.m@example.com");
    student.setAddress("Kinshi, Sumida-ku, Tokyo");
    student.setAge(35);
    student.setGender("Male");
    student.setRemark("Studying hard to become a Java Full Stack Engineer.");
    student.setIsDeleted(false);

    sut.registerStudent(student);

    List<Student> actual = sut.search();
    assertThat(actual.size()).isEqualTo(6);
  }

  // 3. 受講生1件検索
  @Test
  void 受講生IDを指定して受講生が1件検索できること() {
    Student target = sut.search().get(0);
    Student actual = sut.searchStudent(target.getStudentUuid());

    assertThat(actual).isNotNull();
    assertThat(actual.getName()).isEqualTo(target.getName());
  }

  // 4. 受講生コース情報の全件検索
  @Test
  void 受講生コース情報の全件検索が行えること() {
    List<StudentCourse> actual = sut.searchStudentCourseList();
    assertThat(actual.size()).isEqualTo(5);
  }

  // 5. 受講生IDに紐づく受講生コース情報の検索
  @Test
  void 受講生IDに紐づく受講生コース情報が検索できること() {
    Student student = sut.search().get(0);
    List<StudentCourse> actual = sut.searchStudentCourse(student.getStudentUuid());

    assertThat(actual).isNotNull();
  }

  // 6. 受講生の更新
  @Test
  void 受講生情報が更新できること() {
    Student target = sut.search().get(0);
    target.setName("更新 太郎");
    sut.updateStudent(target);

    Student actual = sut.searchStudent(target.getStudentUuid());
    assertThat(actual.getName()).isEqualTo("更新 太郎");
  }

  // 7. 受講生コース情報の更新
  @Test
  void 受講生コース情報のコース名が更新できること() {
    StudentCourse target = sut.searchStudentCourseList().get(0);
    target.setCourseName("Updated Course Name");
    sut.updateStudentCourse(target);

    List<StudentCourse> courses = sut.searchStudentCourseList();
    StudentCourse actual = courses.stream()
        .filter(c -> c.getCourseUuid().equals(target.getCourseUuid()))
        .findFirst().orElseThrow();

    assertThat(actual.getCourseName()).isEqualTo("Updated Course Name");
  }

  // 8. 受講生コース情報の登録
  @Test
  void 受講生コース情報の登録が行えること() {
    StudentCourse course = new StudentCourse();
    course.setCourseUuid(UUID.randomUUID().toString());
    course.setStudentUuid(sut.search().get(0).getStudentUuid());
    course.setCourseName("New Course");
    course.setStartDate(LocalDateTime.now());
    course.setEndDate(LocalDateTime.now().plusYears(1));

    // ★ ここに以下の1行を追加！ ★
    course.setIsDeleted(false);
    
    sut.registerStudentCourse(course);

    List<StudentCourse> actual = sut.searchStudentCourse(course.getStudentUuid());
    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual)
        .extracting(StudentCourse::getCourseName)
        .contains("New Course");
  }

  // 9. 受講生の条件検索
  @Test
  void 受講生の条件検索が行えること() {
    // 検索条件をすべてnull（実質全件検索と同じSQLパス）で実行
    List<Student> actual = sut.searchByConditions(new Student(), new StudentCourse(),
        new raisetech.student.management.data.ApplicationStatus());

    // 既存のデータが5件ある前提
    assertThat(actual.size()).isEqualTo(5);
  }

}