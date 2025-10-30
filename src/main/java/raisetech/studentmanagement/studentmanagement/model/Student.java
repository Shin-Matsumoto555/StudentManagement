package raisetech.studentmanagement.model;

import lombok.Data;

@Data
public class Student {

  private Integer seqId;          // int (auto_increment)
  private String studentUuid;     // char(36)
  private String name;            // varchar(50)
  private String furiganaName;    // varchar(50)
  private String nickname;        // varchar(50)
  private String email;           // varchar(50)
  private String address;         // varchar(50)
  private Integer age;            // int
  private String gender;          // varchar(50)
}