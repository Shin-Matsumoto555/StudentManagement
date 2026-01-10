package raisetech.student.management.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.student.management.data.ApplicationStatus;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper; // 指摘により追加

  @MockBean
  private StudentService service;

  @Test
  void 受講生詳細の一覧検索が実行できて空のリストが返ってくること() throws Exception {
    when(service.searchStudentList()).thenReturn(List.of(new StudentDetail()));
    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk())
        .andExpect(content().json(
            "[{\"student\":null,\"studentCourseList\":null,\"applicationStatusList\":null}]"));
    verify(service, times(1)).searchStudentList();
  }

  @Test
  void studentListが複数件返ること() throws Exception {
    when(service.searchStudentList()).thenReturn(List.of(new StudentDetail(), new StudentDetail()));

    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk())
        .andExpect(content().json("""
            [
              {"student":null, "studentCourseList":null, "applicationStatusList":null},
              {"student":null, "studentCourseList":null, "applicationStatusList":null}
            ]
            """));
    verify(service, times(1)).searchStudentList();
  }

  @Test
  void 受講生IDを指定して詳細を取得できること() throws Exception {
    StudentDetail detail = new StudentDetail();
    when(service.searchStudent("test-uuid")).thenReturn(detail);

    mockMvc.perform(get("/student/test-uuid"))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudent("test-uuid");
  }

  @Test
  void 存在しないUUIDで受講生詳細取得すると例外が返ること() throws Exception {
    when(service.searchStudent("nonexistent")).thenThrow(new RuntimeException("Not found"));

    mockMvc.perform(get("/student/nonexistent"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void 空のUUIDで受講生詳細取得するとバリデーションエラーになること() throws Exception {
    mockMvc.perform(get("/student/{uuid}", ""))
        .andExpect(status().isBadRequest());
  }

  @Test
  void 受講生一覧検索で条件を指定した時に適切な検索結果が返ること() throws Exception {
    StudentDetail detail = new StudentDetail();
    when(service.searchStudentList(any(Student.class), any(StudentCourse.class),
        any(ApplicationStatus.class)))
        .thenReturn(List.of(detail));

    mockMvc.perform(get("/studentSearch")
            .param("name", "Shin")
            .param("courseName", "Java")
            .param("status", "仮申込"))
        .andExpect(status().isOk())
        .andExpect(content().json("""
            [
              {"student":null, "studentCourseList":null, "applicationStatusList":null}
            ]
            """));

    verify(service, times(1)).searchStudentList(any(Student.class), any(StudentCourse.class),
        any(ApplicationStatus.class));
  }

  @Test
  void registerStudentで正常に登録できること() throws Exception {
    StudentDetail detail = new StudentDetail();
    Student student = new Student();
    student.setStudentUuid("new-uuid");
    student.setName("Shin");
    student.setFuriganaName("シン");
    student.setEmail("test@example.com");
    student.setAddress("Tokyo");
    detail.setStudent(student);
    detail.setApplicationStatusList(List.of(new ApplicationStatus())); // ←ここを追加

    when(service.registerStudent(any(StudentDetail.class))).thenReturn(detail);

    mockMvc.perform(post("/registerStudent")
            .contentType("application/json")
            .content("""
                {
                  "student": {
                    "studentUuid": "new-uuid",
                    "name": "Shin",
                    "furiganaName": "シン",
                    "email": "test@example.com",
                    "address": "Tokyo"
                  }
                }
                """))
        .andExpect(status().isOk())
        .andExpect(content().json("""
                {
                  "student": {
                    "studentUuid": "new-uuid",
                    "name": "Shin",
                    "furiganaName": "シン",
                    "email": "test@example.com",
                    "address": "Tokyo"
                  },
                  "studentCourseList": null,
                  "applicationStatusList": [{}]
                }
            """));

    verify(service, times(1)).registerStudent(any(StudentDetail.class));
  }

  @Test
  void registerStudentで正常に登録できること2() throws Exception {
    Student student = new Student();
    student.setStudentUuid("new-uuid");
    student.setName("Shin");
    student.setFuriganaName("シン");
    student.setEmail("test@example.com");
    student.setAddress("Tokyo");

    StudentDetail detail = new StudentDetail();
    detail.setStudent(student);
    detail.setApplicationStatusList(List.of(new ApplicationStatus()));

    when(service.registerStudent(any(StudentDetail.class))).thenReturn(detail);

    mockMvc.perform(post("/registerStudent")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(detail)))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(detail)));

    verify(service, times(1)).registerStudent(any(StudentDetail.class));
  }

  @Test
  void registerStudentで必須項目が空だと400エラーになること() throws Exception {
    mockMvc.perform(post("/registerStudent")
            .contentType("application/json")
            .content("""
                {
                  "student": {
                    "studentUuid": "",
                    "name": "",
                    "furiganaName": "シン",
                    "email": "test@example.com",
                    "address": "Tokyo"
                  }
                }
                """))
        .andExpect(status().isBadRequest());
  }


  @Test
  void 受講生詳細を更新できること() throws Exception {
    mockMvc.perform(
            put("/updateStudent")
                .contentType("application/json")
                .content("""
                    {
                      "student": {
                        "studentUuid": "test-uuid",
                        "name": "Shin",
                        "furiganaName": "シン",
                        "email": "test@example.com",
                        "address": "Tokyo"
                      }
                    }
                    """))
        .andExpect(status().isOk())
        .andExpect(content().string("更新処理が成功しました。"));

    verify(service, times(1)).updateStudent(any(StudentDetail.class));
  }

  @Test
  void 受講生詳細の受講生で適切な値を入力した時に入力チェックに異常が発生しないこと() {
    Student student = new Student();
    student.setStudentUuid("テストです");
    student.setName("Shin");
    student.setFuriganaName("シン");
    student.setEmail("test@example.com");
    student.setAddress("somewhere in Japan");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生詳細の受講生で必須項目に空欄があった時に入力チェックにかかること() {
    Student student = new Student();
    student.setStudentUuid("test-uuid");
    student.setName("");
    student.setFuriganaName("シン");
    student.setEmail("test@example.com");
    student.setAddress("somewhere in Japan");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message").containsOnly("入力必須項目に空欄があります。");
  }
}