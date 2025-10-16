package raisetech.studentmanagement;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

  @Autowired
  private StudentRepository repository;

  @Autowired
  private StudentCourseRepository studentCourseRepository; // 👈 ← これを追加！

  private String name = "Shin";
  private int age = 35;

  public static void main(String[] args) {
    SpringApplication.run(StudentManagementApplication.class, args);
  }

  @GetMapping("/name")
  public String getName() {
    return name;
  }

  @GetMapping("/age")
  public String getAge() {
    return age + " years old";
  }

  @GetMapping("/studentlist")
  public List<Student> getStudentList() {
    return repository.search();
  }

  @PostMapping("/name")
  public void setName(String name) {
    this.name = name;
  }

  // 既存のエンドポイント（/name とか /studentlist）はそのまま

  @GetMapping("/studentcourses") // 👈 ← 新しいURLを追加
  public List<StudentCourse> getStudentCourses() {
    return studentCourseRepository.findAll(); // 全件取得
  }
}