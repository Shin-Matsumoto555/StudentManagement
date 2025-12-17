package raisetech.student.management.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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

  @Min(0)
  @Max(150)
  private Integer age;

  private String gender;

  private String remark;

  private boolean isDeleted;
}
