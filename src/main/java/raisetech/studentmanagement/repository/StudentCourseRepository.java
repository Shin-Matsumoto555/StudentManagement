package raisetech.studentmanagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import raisetech.studentmanagement.StudentCourse;

@Mapper
public interface StudentCourseRepository {

  // student_courses テーブルの全件を取得
  @Select("SELECT * FROM student_courses")
  List<StudentCourse> findAll();
}