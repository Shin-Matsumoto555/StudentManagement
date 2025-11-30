package raisetech.student.management.controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourses;
import raisetech.student.management.domain.StudentDetail;

@Component
public class StudentConverter {

  // 既存メソッド（Student + Courses → StudentDetail）
  public List<StudentDetail> convertStudentDetails(List<Student> students,
      List<StudentCourses> studentCourses) {
    List<StudentDetail> studentDetails = new ArrayList<>();
    students.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);

      List<StudentCourses> convertStudentCourses = studentCourses.stream()
          .filter(studentCourse -> student.getStudentUuid().equals(studentCourse.getStudentUuid()))
          .collect(Collectors.toList());

      studentDetail.setStudentCourses(convertStudentCourses);
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
