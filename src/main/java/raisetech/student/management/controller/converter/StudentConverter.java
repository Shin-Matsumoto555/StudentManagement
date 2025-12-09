package raisetech.student.management.controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;

/**
 * 受講生と受講生コース情報を受王政詳細に変換するコンバーターです。
 */
@Component
public class StudentConverter {

  /**
   * 受講生に紐づく受講生コース情報をマッピングする。 受講生コース情報は受講生に対して複数存在するので、ループを回して受講生詳細情報を組み立てる。
   *
   * @param studentList       受講生一覧
   * @param studentCourseList 受講生コース情報のリスト
   * @return 受講生詳細情報のリスト
   */
  // 既存メソッド（Student + Courses → StudentDetail）
  public List<StudentDetail> convertStudentDetails(List<Student> studentList,
      List<StudentCourse> studentCourseList) {
    List<StudentDetail> studentDetails = new ArrayList<>();
    studentList.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);

      List<StudentCourse> convertStudentCourseList = studentCourseList.stream()
          .filter(studentCourse -> student.getStudentUuid().equals(studentCourse.getStudentUuid()))
          .collect(Collectors.toList());

      studentDetail.setStudentCourseList(convertStudentCourseList);
      studentDetails.add(studentDetail);
    });
    return studentDetails;
  }

  // 追加：StudentDetail → Student
  public Student convertToStudent(StudentDetail detail) {
    Student student = new Student();
    student.setStudentUuid(UUID.randomUUID().toString()); // 自動ID生成
    student.setName(detail.getStudent().getName());
    student.setFuriganaName(detail.getStudent().getFuriganaName());
    student.setNickname(detail.getStudent().getNickname());
    student.setEmail(detail.getStudent().getEmail());
    student.setAddress(detail.getStudent().getAddress());
    student.setAge(detail.getStudent().getAge());
    student.setGender(detail.getStudent().getGender());
    student.setRemark(detail.getStudent().getRemark());
    student.setDeleted(false);
    return student;
  }
}
