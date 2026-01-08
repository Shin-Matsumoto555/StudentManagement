package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import raisetech.student.management.data.ApplicationStatus;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

/**
 * 受講生テーブルと受講生コース情報テーブルと紐づくRepositoryです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索を行います。
   *
   * @return 受講生一覧（全件）
   */
  List<Student> search();

  /**
   * 受講生の検索を行います。
   *
   * @param studentUuid 受講生ID
   * @return 受講生
   */
  Student searchStudent(String studentUuid);

  /**
   * 条件を指定して受講生の検索を行います。
   *
   * @param student 受講生情報
   * @param course  受講生コース情報
   * @param status  申込状況
   * @return 条件に合致した受講生一覧
   */
  List<Student> searchByConditions(
      @Param("student") Student student,
      @Param("course") StudentCourse course,
      @Param("status") ApplicationStatus status
  );

  /**
   * 受講生のコース情報の全件検索を行います。
   *
   * @return 受講生のコース情報（全件）
   */
  List<StudentCourse> searchStudentCourseList();

  /**
   * 受講生IDに紐づく受講生コース情報を検索します。
   *
   * @param studentUuid 受講生ID
   * @return 受講生IDに紐づく受講生コース情報
   */
  List<StudentCourse> searchStudentCourse(String studentUuid);

  /**
   * 受講生を新規登録します。 IDに関しては自動採番を行う。
   *
   * @param student 受講生
   */
  void registerStudent(Student student);

  /**
   * 受講生コース情報を新規登録します。 IDに関しては自動採番を行う。
   *
   * @param studentCourse 受講生コース情報
   */
  void registerStudentCourse(StudentCourse studentCourse);

  /**
   * 受講生を更新します。
   *
   * @param student 受講生
   */
  void updateStudent(Student student);

  /**
   * 受講生コース情報のコース名を更新します。
   *
   * @param studentCourse 受講生コース情報
   */
  void updateStudentCourse(StudentCourse studentCourse);

  /**
   * 申込状況の全件検索を行います。
   *
   * @return 申込状況（全件）
   */
  List<ApplicationStatus> searchApplicationStatusList(); // 課題44

  /**
   * 受講生コースIDに紐づく申込状況を検索します。
   *
   * @param courseUuid 受講生コースID
   * @return コースIDに紐づく申込状況
   */
  List<ApplicationStatus> searchApplicationStatus(String courseUuid); // 課題44

  /**
   * 申込状況を新規登録します。
   *
   * @param applicationStatus 申込状況
   */
  void registerApplicationStatus(ApplicationStatus applicationStatus);

  /**
   * 申込状況を更新します。
   *
   * @param applicationStatus 申込状況情報
   */
  void updateApplicationStatus(ApplicationStatus applicationStatus);

}
