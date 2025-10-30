package raisetech.studentmanagement.model;

import lombok.Data;
import java.sql.Timestamp; // timestamp型に対応

@Data
public class StudentCourse {

  private Integer seqId;          // int (auto_increment)
  private String studentUuid;     // char(36)
  private String courseName;      // varchar(50)
  private Timestamp startDate;    // timestamp
  private Timestamp endDate;      // timestamp
}