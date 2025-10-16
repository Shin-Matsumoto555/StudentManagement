package raisetech.studentmanagement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {

  private String seqId;
  private String studentUuid;
  private String name;
  private String furiganaName;
  private String nickname;
  private String email;
  private String address;
  private  int age;
  private String gender;

}
