package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生コース情報")
@Getter
@Setter
public class StudentCourse {

  private String courseUuid;
  private String studentUuid;
  private String courseName;
  private java.time.LocalDateTime startDate;
  private java.time.LocalDateTime endDate;
  private Boolean isDeleted;
}