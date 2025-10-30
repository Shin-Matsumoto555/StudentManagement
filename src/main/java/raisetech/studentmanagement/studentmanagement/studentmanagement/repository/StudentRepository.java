// src/main/java/raisetech/studentmanagement/repository/StudentRepository.java

package raisetech.studentmanagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import raisetech.studentmanagement.model.Student; // Student.java のインポート

@Mapper
public interface StudentRepository {

  // Read (参照) - 全件取得のメソッド
  // 👈 Student.java に合わせたフィールド名で SELECT します
  @Select("SELECT seq_id, student_uuid, name, furigana_name, nickname, email, address, age, gender FROM students")
  List<Student> findAll();

  // Read (参照) - UUID指定で1件取得のメソッド
  // 👈 UUIDは主キーなので、これで取得します
  @Select("SELECT seq_id, student_uuid, name, furigana_name, nickname, email, address, age, gender FROM students WHERE student_uuid = #{uuid}")
  Student findByUuid(String uuid);
}