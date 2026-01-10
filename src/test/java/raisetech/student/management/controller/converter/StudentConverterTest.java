package raisetech.student.management.controller.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;

class StudentConverterTest {

  // テスト対象（System Under Test）
  private StudentConverter sut;

  /**
   * 各テストの前にStudentConverterを初期化する。 すべてのテストで同じインスタンスを使うため、ここで生成する。
   */
  @BeforeEach
  void before() {
    sut = new StudentConverter();
  }

  /**
   * convertStudentDetailsのテスト
   * <p>
   * 概要： - 複数のコースを受講している受講生も正しくマッピングされることを確認 - 受講生に紐づかないStudentCourseは結果に含まれないことを確認
   * <p>
   * テストデータ準備： - Studentリストに2人の受講生を追加 - StudentCourseリストに3件の紐づくコースと1件の紐づかないコースを追加
   */
  @Test
  void convertStudentDetails_複数コースも正しくマッピングされ紐づかないコースは除外されること() {
    // 受講生データ準備
    Student student1 = new Student();
    student1.setStudentUuid("uuid-1");
    student1.setName("Alice");

    Student student2 = new Student();
    student2.setStudentUuid("uuid-2");
    student2.setName("Bob");

    // コースデータ準備
    StudentCourse course1 = new StudentCourse();
    course1.setStudentUuid("uuid-1");
    course1.setCourseName("Math");

    StudentCourse course2 = new StudentCourse();
    course2.setStudentUuid("uuid-1");
    course2.setCourseName("Science");

    StudentCourse course3 = new StudentCourse();
    course3.setStudentUuid("uuid-2");
    course3.setCourseName("English");

    StudentCourse course4 = new StudentCourse();
    course4.setStudentUuid("uuid-3");
    course4.setCourseName("History"); // 紐づかないコース

    // テスト対象メソッド呼び出し
    List<StudentDetail> details = sut.convertStudentDetails(
        List.of(student1, student2),
        List.of(course1, course2, course3, course4),
        List.of()
    );

    // 検証
    assertThat(details).hasSize(2);

    StudentDetail detail1 = details.get(0);
    assertThat(detail1.getStudentCourseList()).hasSize(2)
        .extracting("courseName")
        .containsExactlyInAnyOrder("Math", "Science");

    StudentDetail detail2 = details.get(1);
    assertThat(detail2.getStudentCourseList()).hasSize(1)
        .extracting("courseName")
        .containsExactly("English");
  }

  /**
   * convertToStudentのテスト
   * <p>
   * 概要： - StudentDetailからStudentに正しく変換できることを確認 - 自動生成されたUUIDがセットされること - 必要なフィールドがコピーされること
   * <p>
   * テストデータ準備： - StudentDetailにStudent情報をセット
   */
  @Test
  void convertToStudent_StudentDetailからStudentに変換できること() {
    // StudentDetailの準備
    StudentDetail detail = new StudentDetail();
    Student original = new Student();
    original.setName("Charlie");
    original.setFuriganaName("チャーリー");
    original.setEmail("charlie@example.com");
    original.setAddress("Tokyo");
    detail.setStudent(original);

    // テスト対象メソッド呼び出し
    Student converted = sut.convertToStudent(detail);

    // 検証
    assertThat(converted.getStudentUuid()).isNotNull();
    assertThat(converted.getName()).isEqualTo("Charlie");
    assertThat(converted.getFuriganaName()).isEqualTo("チャーリー");
    assertThat(converted.getEmail()).isEqualTo("charlie@example.com");
    assertThat(converted.getAddress()).isEqualTo("Tokyo");
    assertThat(converted.getIsDeleted()).isFalse();
  }
}