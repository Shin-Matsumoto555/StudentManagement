package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourses;

/**
 * 　受講生情報を扱うリポジトリ。 　全件検索や単一条件での検索、コース情報の検索が行えるクラスです。
 */

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student> search();

  @Select("SELECT * FROM students WHERE student_uuid = #{studentUuid}")
  Student searchStudent(String studentUuid);

  @Select("SELECT * FROM student_courses")
  List<StudentCourses> searchStudentCoursesList();

  @Select("SELECT * FROM student_courses WHERE student_uuid = #{studentUuid}")
  List<StudentCourses> searchStudentCourses(String studentUuid);

  // 追加：新規登録
  @Insert(
      "INSERT INTO students(student_uuid, name, furigana_name, nickname, email, address, age, gender, remark, is_deleted) "
          +
          "VALUES(#{studentUuid}, #{name}, #{furiganaName}, #{nickname}, #{email}, #{address}, #{age}, #{gender}, #{remark}, #{deleted})"
  )
  @Options(useGeneratedKeys = true, keyProperty = "student_uuid")
  void registerStudent(Student student);

  @Insert(
      "INSERT INTO student_courses(uuid, student_uuid, course_name, start_date, end_date) " +
          "VALUES(#{uuid}, #{studentUuid}, #{courseName}, #{startDate}, #{endDate})")
  @Options(useGeneratedKeys = true, keyProperty = "uuid")
  void registerStudentCourses(StudentCourses studentCourses);

  // 追加：受講生の更新
  @Update(
      "UPDATE students SET name = #{name}, furigana_name = #{furiganaName}, nickname = #{nickname}, "
          +
          "email = #{email}, address = #{address}, age = #{age}, gender = #{gender}, remark = #{remark}, "
          +
          "is_deleted = #{deleted} WHERE student_uuid = #{studentUuid}"
  )
  void updateStudent(Student student);

  // 追加：コースの更新
  @Update(
      "UPDATE student_courses SET course_name = #{courseName} WHERE uuid = #{uuid}"
  )
  void updateStudentCourses(StudentCourses studentCourses);
}
