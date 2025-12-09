package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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
  @Select("SELECT * FROM students")
  List<Student> search();

  /**
   * 受講生の検索を行います。
   *
   * @param studentUuid 受講生ID
   * @return 受講生
   */
  @Select("SELECT * FROM students WHERE student_uuid = #{studentUuid}")
  Student searchStudent(String studentUuid);

  /**
   * 受講生のコース情報の全件検索を行います。
   *
   * @return 受講生のコース情報（全件）
   */
  @Select("SELECT * FROM student_courses")
  List<StudentCourse> searchStudentCourseList();

  /**
   * 受講生IDに紐づく受講生コース情報を検索します。
   *
   * @param studentUuid 受講生ID
   * @return 受講生IDに紐づく受講生コース情報
   */
  @Select("SELECT * FROM student_courses WHERE student_uuid = #{studentUuid}")
  List<StudentCourse> searchStudentCourse(String studentUuid);

  /**
   * 受講生を新規登録します。 IDに関しては自動採番を行う。
   *
   * @param student 受講生
   */
  @Insert(
      "INSERT INTO students(student_uuid, name, furigana_name, nickname, email, address, age, gender, remark, is_deleted) "
          +
          "VALUES(#{studentUuid}, #{name}, #{furiganaName}, #{nickname}, #{email}, #{address}, #{age}, #{gender}, #{remark}, #{deleted})"
  )
  @Options(useGeneratedKeys = true, keyProperty = "student_uuid")
  void registerStudent(Student student);

  /**
   * 受講生コース情報を新規登録します。 IDに関しては自動採番を行う。
   *
   * @param studentCourse 受講生コース情報
   */
  @Insert(
      "INSERT INTO student_courses(uuid, student_uuid, course_name, start_date, end_date) " +
          "VALUES(#{uuid}, #{studentUuid}, #{courseName}, #{startDate}, #{endDate})")
  @Options(useGeneratedKeys = true, keyProperty = "uuid")
  void registerStudentCourse(StudentCourse studentCourse);

  /**
   * 受講生を更新します。
   *
   * @param student 受講生
   */
  @Update(
      "UPDATE students SET name = #{name}, furigana_name = #{furiganaName}, nickname = #{nickname}, "
          +
          "email = #{email}, address = #{address}, age = #{age}, gender = #{gender}, remark = #{remark}, "
          +
          "is_deleted = #{deleted} WHERE student_uuid = #{studentUuid}"
  )
  void updateStudent(Student student);

  /**
   * 受講生コース情報のコース名を更新します。
   *
   * @param studentCourse 受講生コース情報
   */
  @Update(
      "UPDATE student_courses SET course_name = #{courseName} WHERE uuid = #{uuid}"
  )
  void updateStudentCourse(StudentCourse studentCourse);
}
