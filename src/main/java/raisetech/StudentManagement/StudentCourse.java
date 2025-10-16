package raisetech.studentmanagement;

import java.time.LocalDateTime;
import lombok.Data;

@Data  // 👈 これ1行でGetter/Setter/toStringなど全部自動生成！
public class StudentCourse {
  private int seqId;
  private String studentUuid;
  private String courseName;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
}