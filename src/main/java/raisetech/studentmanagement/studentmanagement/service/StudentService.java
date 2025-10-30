package raisetech.studentmanagement.service;

import org.springframework.stereotype.Service;
import raisetech.studentmanagement.repository.StudentRepository;
import raisetech.studentmanagement.model.Student; // 👈 Studentクラスのインポートを追加

import java.util.List;

@Service
public class StudentService {

  private final StudentRepository studentRepository;

  public StudentService(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  // 👈 復元: StudentServiceに必要なメソッドを追加
  public List<Student> findAll() {
    // Repositoryを呼び出す最小限の実装
    return studentRepository.findAll();
  }

  public Student findByUuid(String uuid) {
    // Repositoryを呼び出す最小限の実装
    return studentRepository.findByUuid(uuid);
  }
}