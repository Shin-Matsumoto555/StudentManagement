package raisetech.student.management.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourses;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;

@Service
public class StudentService {

  private final StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    // 検索処理を行う
    repository.search();
    // 例：絞り込みをする、年齢が３０代の人のみを抽出する。
    // 例：抽出したリストをコントローラーに返す。
    return repository.search();
  }

  public List<StudentCourses> searchStudentCourseList() {
    // 例：絞り込み検索で"Java Course"のコース情報のみを抽出する。
    // 例：抽出したリストをコントローラーに返す。
    return repository.searchStudentCoursesList();
  }

  public StudentDetail searchStudent(String studentUuid) {
    Student student = repository.searchStudent(studentUuid);
    List<StudentCourses> studentCourses = repository.searchStudentCourses(student.getStudentUuid());
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentCourses(studentCourses);
    return studentDetail;
  }

  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    // Student オブジェクトを変数に入れる
    Student student = studentDetail.getStudent();
    // UUID を生成してセット
    student.setStudentUuid(UUID.randomUUID().toString());
    // ここで DB に保存する処理
    repository.registerStudent(studentDetail.getStudent());
    // コース情報登録も行う
    for (StudentCourses studentCourse : studentDetail.getStudentCourses()) {
      // 各コース行の UUID を生成
      studentCourse.setUuid(UUID.randomUUID().toString());

      studentCourse.setStudentUuid(studentDetail.getStudent().getStudentUuid());
      studentCourse.setStartDate(LocalDateTime.now());
      studentCourse.setEndDate(LocalDateTime.now().plusYears(1));
      repository.registerStudentCourses(studentCourse);
    }
    return studentDetail;
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {

    // Student の更新
    repository.updateStudent(studentDetail.getStudent());

    // コースの更新（今は uuid が存在する前提の最小構成）
    for (StudentCourses course : studentDetail.getStudentCourses()) {
      repository.updateStudentCourses(course);
    }
  }
}
