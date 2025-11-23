package raisetech.student.management.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourses;
import raisetech.student.management.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    // 検索処理を行う
    repository.search();

    // 絞り込みをする、年齢が３０代の人のみを抽出する。
    // 抽出したリストをコントローラーに返す。

    return repository.search();
  }

  public List<StudentCourses> searchStudentCourseList() {
    // 絞り込み検索で"Java Course"のコース情報のみを抽出する。
    // 抽出したリストをコントローラーに返す。
    return repository.searchStudentCourses();
  }
}
