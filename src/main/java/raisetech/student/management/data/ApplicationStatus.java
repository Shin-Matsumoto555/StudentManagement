package raisetech.student.management.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationStatus {

  private String statusUuid;
  private String courseUuid;
  private String status;
  private Boolean isDeleted;
}