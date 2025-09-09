package raisetech.StudentManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
=======
>>>>>>> f4d31b5 (feat: Controllerを分離し、三層アーキテクチャの基本構造とMyBatisの準備を完了)

@SpringBootApplication
public class StudentManagementApplication {

  public static void main(String[] args) {
    SpringApplication.run(StudentManagementApplication.class, args);
  }

<<<<<<< HEAD
  @GetMapping("/hello")
  public String hello() {
    return "Hello, World!";
  }
}
=======
}
>>>>>>> f4d31b5 (feat: Controllerを分離し、三層アーキテクチャの基本構造とMyBatisの準備を完了)
