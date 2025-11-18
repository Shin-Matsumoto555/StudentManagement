// src/main/java/raisetech/studentmanagement/repository/StudentCourseRepository.java

package raisetech.student.management;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import raisetech.student.management.StudentCourse;

@Mapper
public interface StudentCourseRepository {

  // Read (参照) - 全件取得のメソッド
  // 👈 SELECT * から明示的なカラムリストに修正
  @Select("SELECT uuid, student_uuid, course_name, start_date, end_date FROM student_courses")
  List<StudentCourse> findAll();

  // Read (参照) - ID指定で1件取得のメソッド
  // 👈 SELECT * から明示的なカラムリストに修正
  @Select("SELECT uuid, student_uuid, course_name, start_date, end_date FROM student_courses WHERE uuid = #{uuid}")
  StudentCourse findById(String uuid);
}