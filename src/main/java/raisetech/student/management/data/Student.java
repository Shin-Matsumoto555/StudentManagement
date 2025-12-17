package raisetech.student.management.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {

  private String studentUuid;

  @NotBlank
  private String name;

  @NotBlank
  private String furiganaName;

  private String nickname;

  @NotBlank
  @Email
  private String email;

  @NotBlank
  private String address;

  @Pattern(regexp = "\\d+$")
  private int age;

  private String gender;

  private String remark;

  private boolean isDeleted;
}
