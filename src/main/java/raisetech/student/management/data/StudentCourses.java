package raisetech.student.management.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourses {

  private String uuid;
  private String studentUuid;
  private String courseName;
  private java.time.LocalDateTime startDate;
  private java.time.LocalDateTime endDate;
}