package raisetech.StudentManagement;

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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }
}
