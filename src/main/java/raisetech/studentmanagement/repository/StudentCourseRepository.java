// src/main/java/raisetech/studentmanagement/repository/StudentCourseRepository.java

package raisetech.studentmanagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import raisetech.studentmanagement.StudentCourse;

@Mapper
public interface StudentCourseRepository {

  // Read (参照) - 全件取得のメソッド
  @Select("SELECT * FROM student_courses")
  List<StudentCourse> findAll();

  // Read (参照) - ID指定で1件取得のメソッド
  @Select("SELECT * FROM student_courses WHERE id = #{id}")
  StudentCourse findById(int id);
}